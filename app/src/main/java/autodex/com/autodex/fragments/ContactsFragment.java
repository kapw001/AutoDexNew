package autodex.com.autodex.fragments;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import autodex.com.autodex.MessageEvent;
import autodex.com.autodex.R;
import autodex.com.autodex.UtilFunctionalitys;
import autodex.com.autodex.Utility;
import autodex.com.autodex.activitys.HomeActivity;
import autodex.com.autodex.activitys.LoginActivity;
import autodex.com.autodex.adapter.ContactsAdapter;
import autodex.com.autodex.interfacecallback.WebResponse;
import autodex.com.autodex.model.Contact;
import autodex.com.autodex.webrequest.WebUrl;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import volleywebrequest.com.mysqlitelibrary.ContactAttribute;
import volleywebrequest.com.mysqlitelibrary.ContactCategory;
import volleywebrequest.com.mysqlitelibrary.GetContactListResponse;
import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

import static android.content.ContentValues.TAG;
import static autodex.com.autodex.R.id.recyclerView;
import static autodex.com.autodex.sessionmanagement.SessionManager.KEY_TOKEN;

/**
 * Created by yasar on 11/9/17.
 */

public class ContactsFragment extends BaseFragment implements ContactsAdapter.ItemListener {
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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        contactList = new ArrayList<>();
        contactsAdapter = new ContactsAdapter(getActivity(), contactList, this);
        mRecyclerView.setAdapter(contactsAdapter);
//        mIndexLayoutManager = (IndexLayoutManager) view.findViewById(R.id.index_layout);
//        mIndexLayoutManager.attach(mRecyclerView);

        errormsg = (TextView) view.findViewById(R.id.errormsg);


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        callDB();

        Log.e(TAG, "onResume: Contact Fragment called");
    }

    @Override
    public void onResponse(JSONObject response) {
        super.onResponse(response);


    }

    public void callDB() {
        Log.e(TAG, "onCreateView: " + sessionManager.getKeySqlliteloading());


        Observable.fromCallable(new Callable<List<GetContactListResponse>>() {

            @Override
            public List<GetContactListResponse> call() throws Exception {
                List<GetContactListResponse> l1 = databaseHandler.getAllDatas();
                List<GetContactListResponse> lNew = new ArrayList<>();

                for (GetContactListResponse response : l1
                        ) {

                    String name = response.getFirstName() + response.getMiddleName() + response.getLastName();
                    response.setWholeName(name);
                    lNew.add(response);
                }

                UtilFunctionalitys.shortListAlphabet2(lNew);

                return lNew;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<GetContactListResponse>>() {
            @Override
            public void accept(List<GetContactListResponse> list) throws Exception {

                if (list.size() > 0) {

//            HashSet hs = new HashSet();
//            hs.addAll(l1);
//            l1.clear();
//            l1.addAll(hs);
                    contactList.clear();
                    contactList.addAll(list);
                    Log.e(TAG, "onResponse: size  " + list.size());


                    contactsAdapter.updateList(list);
//            for (GetContactListResponse getContactListResponse : l1
//                    ) {
//                Log.e(TAG, "onResponse: " + getContactListResponse.getFirstName());
//            }
                    errormsg.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    errormsg.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                }

            }
        });


//        if (sessionManager.getKeySqlliteloading()) {


//        } else {
//            callRequest();
//        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        Log.e(TAG, "onMessageEvent: ");
//        Toast.makeText(getActivity(), event.message, Toast.LENGTH_SHORT).show();
        callDB();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    @Override
    public void searchFilter(final String s) {


        contactsAdapter.getFilter().filter(s);


//        if (contactList.size() > 0) {
//
        List<GetContactListResponse> l = UtilFunctionalitys.getSearchList(s, contactList);
//
//            UtilFunctionalitys.shortListAlphabet(l);
//
//            contactsAdapter.updateList(l);
//
//        } else {
//            Log.e(TAG, "searchFilter: There is no contact to search ");
//        }
    }


    @Override
    public void onItemClick(GetContactListResponse item) {

        databaseHandler.updateData(item);
        updateFav(item);

    }

    private void updateFav(GetContactListResponse item) {

        final GetContactListResponse res = item;
        List<ContactAttribute> CAList = databaseHandler.getAllContactAttributeDatas(String.valueOf(res.getId()));
        List<ContactCategory> CCList = databaseHandler.getAllContactCategoryDatas(String.valueOf(res.getId()));
        res.setContactAttributes(CAList);
        res.setContactCategories(CCList);
        Utility.showProgress(getContext(), "Loading");
        Gson gson = new Gson();
        String jsonObject = gson.toJson(res);
        try {
            JSONObject jsonObject1 = new JSONObject(jsonObject);
            final String token = sessionManager.getDetails().get(KEY_TOKEN);
            webPostRequest.postJSONPutDataGetNumberStringJson(WebUrl.ADDCONTACT, jsonObject1, token, new WebResponse() {
                @Override
                public void onResponse(JSONObject response) {
                    Utility.hideProgress();
                    Log.e(TAG, "onResponse: fave updated  " + response);
                }

                @Override
                public void onResponse(String response) {
                    Log.e(TAG, "onResponse: fave updated  " + response);
                    Utility.hideProgress();
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "onResponse: fave updated  " + error.getMessage());
                    Utility.hideProgress();
                }

                @Override
                public void callRequest() {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "onClick: JSON  " + gson.toJson(res).toString());
    }


}