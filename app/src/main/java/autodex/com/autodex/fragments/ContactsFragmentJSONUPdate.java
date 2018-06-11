package autodex.com.autodex.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.google.gson.Gson;
import com.tunebrains.rcsections.IndexLayoutManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import autodex.com.autodex.MessageEvent;
import autodex.com.autodex.R;
import autodex.com.autodex.UtilFunctionalitys;
import autodex.com.autodex.Utility;
import autodex.com.autodex.adapter.ContactsAdapter;
import autodex.com.autodex.webrequest.WebUrl;
import volleywebrequest.com.mysqlitelibrary.ContactAttribute;
import volleywebrequest.com.mysqlitelibrary.ContactCategory;
import volleywebrequest.com.mysqlitelibrary.GetContactListResponse;

import static android.content.ContentValues.TAG;
import static autodex.com.autodex.sessionmanagement.SessionManager.KEY_TOKEN;

/**
 * Created by yasar on 11/9/17.
 */

public class ContactsFragmentJSONUPdate extends BaseFragment implements ContactsAdapter.ItemListener {
    private RecyclerView mRecyclerView;
    private IndexLayoutManager mIndexLayoutManager;
    private List<GetContactListResponse> contactList = new ArrayList<>();
    private ContactsAdapter contactsAdapter;
    private TextView errormsg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_contacts, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        errormsg = (TextView) view.findViewById(R.id.errormsg);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        contactList = new ArrayList<>();
        contactsAdapter = new ContactsAdapter(getActivity(), contactList,this);
        mRecyclerView.setAdapter(contactsAdapter);

//        VerticalRecyclerViewFastScroller fastScroller = (VerticalRecyclerViewFastScroller) view.findViewById(R.id.fast_scroller);
//
//        // Connect the recycler to the scroller (to let the scroller scroll the list)
//        fastScroller.setRecyclerView(mRecyclerView);
//
//        // Connect the scroller to the recycler (to let the recycler scroll the scroller's handle)
//        mRecyclerView.setOnScrollListener(fastScroller.getOnScrollListener());

        mIndexLayoutManager = (IndexLayoutManager) view.findViewById(R.id.index_layout);
        mIndexLayoutManager.attach(mRecyclerView);
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
//            // Do something for lollipop and above versions
//        } else{
//            // do something for phones running an SDK before lollipop
//        }

        // Check if we're running on Android 5.0 or higher
        callDB();


        return view;
    }

    @Override
    public void onResponse(JSONObject response) {
        super.onResponse(response);


    }

    public void callDB() {
        Log.e(TAG, "onCreateView: " + sessionManager.getKeySqlliteloading());

        if (sessionManager.getKeySqlliteloading()) {
            List<GetContactListResponse> l1 = databaseHandler.getAllDatas();
            if (l1.size() > 0) {

                HashSet hs = new HashSet();
                hs.addAll(l1);
                l1.clear();
                l1.addAll(hs);
                contactList.clear();
                contactList.addAll(l1);
                Log.e(TAG, "onResponse: size  " + l1.size());
                UtilFunctionalitys.shortListAlphabet(l1);


                contactsAdapter.updateList(l1);
                for (GetContactListResponse getContactListResponse : l1
                        ) {
                    Log.e(TAG, "onResponse: " + getContactListResponse.getFirstName());
                }
                errormsg.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            } else {
                errormsg.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }

        } else {
            callRequest();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        Log.e(TAG, "onMessageEvent: ");

        if (event.message.equalsIgnoreCase("servercall")) {
            callRequestBackground();
        } else {
            callDB();
        }

        Toast.makeText(getActivity(), event.message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

//    @Override
//    public void onStop() {
//        EventBus.getDefault().unregister(this);
//        super.onStop();
//    }

    @Override
    public void onResponse(String response) {
        super.onResponse(response);

        Gson gson = new Gson();

        Log.e(TAG, "onResponse: res  " + response.toString());

        databaseHandler.deleteAllData();
        databaseHandler.deleteAllContactCategoryData();
        databaseHandler.deleteAllContactAttributeData();

        GetContactListResponse[] list = gson.fromJson(response.toString(), GetContactListResponse[].class);

        List<GetContactListResponse> l = Arrays.asList(list);

        for (GetContactListResponse getContactListResponse : l
                ) {

            List<ContactAttribute> cAList = getContactListResponse.getContactAttributes();
            List<ContactCategory> cCList = getContactListResponse.getContactCategories();

            String forignId = String.valueOf(getContactListResponse.getId());

            for (int i = 0; i < cAList.size(); i++) {
                cAList.get(i).setForignID(forignId);
            }

            for (int i = 0; i < cCList.size(); i++) {
                cCList.get(i).setForignID(forignId);
            }


            databaseHandler.addContactAttributeData(cAList);
            databaseHandler.addContactCategoryData(cCList);

        }
//
//        databaseHandler.deleteAllData();
        databaseHandler.addData(l);

        List<GetContactListResponse> l1 = databaseHandler.getAllDatas();
        if (l1.size() > 0) {

            HashSet hs = new HashSet();
            hs.addAll(l1);
            l1.clear();
            l1.addAll(hs);
            contactList.clear();
            contactList.addAll(l1);
            Log.e(TAG, "onResponse: size  " + l1.size());
            UtilFunctionalitys.shortListAlphabet(l1);


            contactsAdapter.updateList(l1);
            for (GetContactListResponse getContactListResponse : l1
                    ) {
                Log.e(TAG, "onResponse: " + getContactListResponse.getFirstName());
            }
            errormsg.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            errormsg.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
        sessionManager.setKeySqlliteloading(true);

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        super.onErrorResponse(error);
        Log.e(TAG, "onErrorResponse: " + error.getMessage());
        Utility.showDialog(getActivity(), "Error", "Server error");

    }

    @Override
    public void callRequest() {
        super.callRequest();
        String token = sessionManager.getDetails().get(KEY_TOKEN);
        Log.e(TAG, "callRequest: " + token);
        webPostRequest.postGETData(WebUrl.GETCONTACTLIST, token, this);
    }

    public void callRequestBackground() {
        String token = sessionManager.getDetails().get(KEY_TOKEN);
        Log.e(TAG, "callRequest: " + token);
        webPostRequest.postGETData(WebUrl.GETCONTACTLIST, token, this);
    }

    @Override
    public void searchFilter(final String s) {

        if (contactList.size() > 0) {

            List<GetContactListResponse> l = UtilFunctionalitys.getSearchList(s, contactList);

            UtilFunctionalitys.shortListAlphabet(l);

            contactsAdapter.updateList(l);

        } else {
            Log.e(TAG, "searchFilter: There is no contact to search ");
        }
    }


    @Override
    public void onItemClick(GetContactListResponse item) {

    }
}