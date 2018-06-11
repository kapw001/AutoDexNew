package autodex.com.autodex.activitys;

import android.content.Context;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.error.VolleyError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import autodex.com.autodex.R;
import autodex.com.autodex.Util;
import autodex.com.autodex.Utility;
import autodex.com.autodex.adapter.ContactsAdapter;
import autodex.com.autodex.adapter.GridRecyclerViewAdapter;
import autodex.com.autodex.interfacecallback.WebResponse;
import autodex.com.autodex.webrequest.WebUrl;
import volleywebrequest.com.mysqlitelibrary.ContactAttribute;
import volleywebrequest.com.mysqlitelibrary.ContactCategory;
import volleywebrequest.com.mysqlitelibrary.GetContactListResponse;

import static autodex.com.autodex.sessionmanagement.SessionManager.KEY_TOKEN;

public class GroupMoreLikeFavActivity extends BaseActivity implements GridRecyclerViewAdapter.ItemListener, ContactsAdapter.ItemListener, SearchView.OnQueryTextListener {

    private static final String TAG = "GroupMoreActivity";
    private RecyclerView mRecyclerView;
    private ContactsAdapter contactsAdapter;
    private List<GetContactListResponse> contactList = new ArrayList<>();
    private Gson gson = new Gson();
    private GridLayoutManager mLayoutManager;
    private AdView mAdView;

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grpup_more);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String title = getIntent().getStringExtra("title");

//        if (title.toLowerCase().equalsIgnoreCase("UNCATEGORIZED CONTACTS".toLowerCase())) {
//
//            title = "MORE CONTACTS";
//
//        }

        getSupportActionBar().setTitle(Util.toTitleCase(title));

        searchView = (SearchView) findViewById(R.id.search);

        searchView.setOnQueryTextListener(this);

//        String list = getIntent().getStringExtra("getlist");
//
//        loadData(list);

//        Type type = new TypeToken<List<GetContactListResponse>>() {
//        }.getType();
//        contactList = gson.fromJson(list, type);

//        contactList = (List<GetContactListResponse>) getIntent().getSerializableExtra("list");
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactList = new ArrayList<>();
        contactsAdapter = new ContactsAdapter(this, contactList, this);
        mRecyclerView.setAdapter(contactsAdapter);


//        Display display = getWindowManager().getDefaultDisplay();
//        DisplayMetrics outMetrics = new DisplayMetrics();
//        display.getMetrics(outMetrics);
//        float density = getResources().getDisplayMetrics().density;
//        float dpWidth = outMetrics.widthPixels / density;
//        int columns = Math.round(dpWidth / 300);
//        mLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
//        contactsAdapter = new GridRecyclerViewAdapter(this,this, contactList, this);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(contactsAdapter);
//        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
//        mRecyclerView.addItemDecoration(itemDecoration);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();

        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener(mAdView));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        contactsAdapter.getFilter().filter(newText);

        return false;
    }

    public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

        private int mItemOffset;

        public ItemOffsetDecoration(int itemOffset) {
            mItemOffset = itemOffset;
        }

        public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
            this(context.getResources().getDimensionPixelSize(itemOffsetId));
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume:group more  ");
        String list = getIntent().getStringExtra("getlist");

        loadData(list);
    }

    private void loadData(String groupname) {


        new LoadData().execute(groupname);


    }


    class LoadData extends AsyncTask<String, Void, List<GetContactListResponse>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utility.showProgress(GroupMoreLikeFavActivity.this, "Loading");
        }

        @Override
        protected List<GetContactListResponse> doInBackground(String... params) {

            Log.e(TAG, "loadData: " + Util.toTitleCase(params[0]));

            contactList.clear();
            List<GetContactListResponse> contactList1 = databaseHandler.getAllDatas();

//        List<GetContactListResponse> categoryContactList = new ArrayList<>();
//
//        for (int i = 0; i < contactList1.size(); i++) {
//
//            GetContactListResponse getContactListResponse = contactList1.get(i);
//            List<ContactCategory> cList = databaseHandler.getAllContactCategoryDatas(String.valueOf(getContactListResponse.getId()));
//            if (cList.size() > 0) {
//                getContactListResponse.setCategory(cList.get(0).getCategory());
//            }
//            categoryContactList.add(getContactListResponse);
//
//        }

            for (int i = 0; i < contactList1.size(); i++) {

                GetContactListResponse getContactListResponse = contactList1.get(i);


                if (params[0].toLowerCase().equalsIgnoreCase("Personal".toLowerCase() + " Contacts".toLowerCase()) || params[0].toLowerCase().equalsIgnoreCase("Professional".toLowerCase() + " Contacts".toLowerCase())) {


                    if (params[0] != null && getContactListResponse.getProfileTag() != null && params[0].toLowerCase().equalsIgnoreCase(getContactListResponse.getProfileTag().toLowerCase() + " Contacts".toLowerCase())) {
                        contactList.add(getContactListResponse);
                    }

                    if (getContactListResponse.getProfileTag() != null && "personalprofessional".toLowerCase().equalsIgnoreCase(getContactListResponse.getProfileTag().toLowerCase())) {
                        contactList.add(getContactListResponse);
                    } else if (getContactListResponse.getProfileTag() != null && "personal & professional".toLowerCase().equalsIgnoreCase(getContactListResponse.getProfileTag().toLowerCase())) {
                        contactList.add(getContactListResponse);
                    } else if (getContactListResponse.getProfileTag() != null && "personal and professional".toLowerCase().equalsIgnoreCase(getContactListResponse.getProfileTag().toLowerCase())) {
                        contactList.add(getContactListResponse);
                    } else if (getContactListResponse.getProfileTag() != null && "personal professional".toLowerCase().equalsIgnoreCase(getContactListResponse.getProfileTag().toLowerCase())) {
                        contactList.add(getContactListResponse);
                    }


                } else {

                    if (params[0] != null && getContactListResponse.getProfileTag() != null && params[0].toLowerCase().equalsIgnoreCase(getContactListResponse.getProfileTag().toLowerCase() + " Contacts".toLowerCase())) {
                        contactList.add(getContactListResponse);
                    }

                }

//            if (groupname != null && getContactListResponse.getProfileTag() != null && groupname.toLowerCase().equalsIgnoreCase(getContactListResponse.getProfileTag().toLowerCase() + " Contacts".toLowerCase())) {
//                contactList.add(getContactListResponse);
//            }

            }

            return contactList;
        }

        @Override
        protected void onPostExecute(List<GetContactListResponse> getContactListResponses) {
            super.onPostExecute(getContactListResponses);
            Utility.hideProgress();
            contactsAdapter.updateList(contactList);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(GetContactListResponse item) {
        final GetContactListResponse res = item;

        databaseHandler.updateData(res);

        List<ContactAttribute> CAList = databaseHandler.getAllContactAttributeDatas(String.valueOf(res.getId()));
        List<ContactCategory> CCList = databaseHandler.getAllContactCategoryDatas(String.valueOf(res.getId()));
        res.setContactAttributes(CAList);
        res.setContactCategories(CCList);
        Utility.showProgress(GroupMoreLikeFavActivity.this, "Loading");
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
    }
}
