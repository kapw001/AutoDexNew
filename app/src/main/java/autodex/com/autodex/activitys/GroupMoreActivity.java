package autodex.com.autodex.activitys;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import autodex.com.autodex.R;
import autodex.com.autodex.adapter.GroupMoreAdapter;
import volleywebrequest.com.mysqlitelibrary.ContactCategory;
import volleywebrequest.com.mysqlitelibrary.GetContactListResponse;

public class GroupMoreActivity extends BaseActivity {

    private static final String TAG = "GroupMoreActivity";
    private RecyclerView mRecyclerView;
    private GroupMoreAdapter contactsAdapter;
    private List<GetContactListResponse> contactList = new ArrayList<>();
    private Gson gson = new Gson();
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grpup_more);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String title = getIntent().getStringExtra("title");
        getSupportActionBar().setTitle(title);

        String list = getIntent().getStringExtra("getlist");

        loadData(list);

//        Type type = new TypeToken<List<GetContactListResponse>>() {
//        }.getType();
//        contactList = gson.fromJson(list, type);

//        contactList = (List<GetContactListResponse>) getIntent().getSerializableExtra("list");
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactsAdapter = new GroupMoreAdapter(this, contactList);
        mRecyclerView.setAdapter(contactsAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();

        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener(mAdView));
    }


    private void loadData(String groupname) {

        Log.e(TAG, "loadData: " + groupname);

        contactList.clear();
        List<GetContactListResponse> contactList1 = databaseHandler.getAllDatas();

        List<GetContactListResponse> categoryContactList = new ArrayList<>();

        for (int i = 0; i < contactList1.size(); i++) {

            GetContactListResponse getContactListResponse = contactList1.get(i);
            List<ContactCategory> cList = databaseHandler.getAllContactCategoryDatas(String.valueOf(getContactListResponse.getId()));
            getContactListResponse.setCategory(cList.get(0).getCategory());
            categoryContactList.add(getContactListResponse);

        }

        for (int i = 0; i < contactList1.size(); i++) {

            GetContactListResponse getContactListResponse = contactList1.get(i);

            if (groupname.toLowerCase().equalsIgnoreCase(getContactListResponse.getCategory().toLowerCase() + " Contacts".toLowerCase())) {
                contactList.add(getContactListResponse);
            }
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
