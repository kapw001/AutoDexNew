package autodex.com.autodex.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import autodex.com.autodex.R;
import autodex.com.autodex.Util;
import autodex.com.autodex.Utility;
import autodex.com.autodex.activitys.GroupMoreLikeFavActivity;
import autodex.com.autodex.activitys.HomeActivity;
import autodex.com.autodex.adapter.RecyclerViewDataAdapter;
import autodex.com.autodex.interfacecallback.IShowCount;
import autodex.com.autodex.model.Contact;
import autodex.com.autodex.model.SectionDataModel;
import autodex.com.autodex.model.SingleItemModel;
import volleywebrequest.com.mysqlitelibrary.ContactCategory;
import volleywebrequest.com.mysqlitelibrary.GetContactListResponse;

/**
 * Created by yasar on 11/9/17.
 */

public class GroupsFragment extends BaseFragment {


    private static final String TAG = "GroupsFragment";
    private RecyclerView my_recycler_view;
    private ArrayList<SectionDataModel> allSampleData;
    private IShowCount iShowCount;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        iShowCount = (IShowCount) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_groups, container, false);
        allSampleData = new ArrayList<SectionDataModel>();

        my_recycler_view = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        my_recycler_view.setHasFixedSize(true);

//        iShowCount.showNotificationCount(HomeActivity.count);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        createDummyData();
    }

    public void createDummyData() {


        new LoadData().execute();


    }


    class LoadData extends AsyncTask<Void, Void, ArrayList<SectionDataModel>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utility.showProgress(getActivity(), "Loading");
        }

        @Override
        protected ArrayList<SectionDataModel> doInBackground(Void... params) {

            allSampleData.clear();
            List<GetContactListResponse> contactList = databaseHandler.getAllDatas();

//        List<GetContactListResponse> categoryContactList = new ArrayList<>();
//
//        for (int i = 0; i < contactList.size(); i++) {
//
//            GetContactListResponse getContactListResponse = contactList.get(i);
//            List<ContactCategory> cList = databaseHandler.getAllContactCategoryDatas(String.valueOf(getContactListResponse.getId()));
//
//            if (cList.size() > 0) getContactListResponse.setCategory(cList.get(0).getCategory());
//
//            categoryContactList.add(getContactListResponse);
//
//        }
//
//        List<ContactCategory> categories = databaseHandler.getAllContactCategoryDatas();

            List<String> categoriesList = new ArrayList<>();

//            for (int i = 0; i < contactList.size(); i++) {
//
//                GetContactListResponse contactCategory = contactList.get(i);
//
//                if (contactCategory.getProfileTag().toUpperCase().equalsIgnoreCase("personalprofessional") || contactCategory.getProfileTag().toUpperCase().equalsIgnoreCase("personal & professional") || contactCategory.getProfileTag().toUpperCase().equalsIgnoreCase("personal and professional") || contactCategory.getProfileTag().toUpperCase().equalsIgnoreCase("personal professional")) {
//
//                    Log.e(TAG, "doInBackground: no need to add ");
//
//                } else {
//
//                    categoriesList.add(contactCategory.getProfileTag().toUpperCase());
//                }
//
//
//            }
//            Set<String> categorySet = new HashSet<>();
//            categorySet.addAll(categoriesList);
            categoriesList.clear();

            categoriesList.add("Uncategorized");
            categoriesList.add("Professional");
            categoriesList.add("Personal");
//            categoriesList.addAll(categorySet);

            Log.e(TAG, "doInBackground: " + categoriesList.size());
//
//
            Map<Object, List<GetContactListResponse>> mapList = new HashMap();

            for (int i = 0; i < categoriesList.size(); i++) {

                List<GetContactListResponse> l = new ArrayList<>();
                String s = categoriesList.get(i);

                Log.e(TAG, "doInBackground: group  " + s);

                for (int j = 0; j < contactList.size(); j++) {
                    GetContactListResponse getContactListResponse = contactList.get(j);


                    if (s.toLowerCase().equalsIgnoreCase("Personal".toLowerCase()) || s.toLowerCase().equalsIgnoreCase("Professional".toLowerCase())) {


                        if (s != null && getContactListResponse.getProfileTag() != null && s.toLowerCase().equalsIgnoreCase(getContactListResponse.getProfileTag().toLowerCase())) {
                            l.add(getContactListResponse);
                        }

                        if (getContactListResponse.getProfileTag() != null && "personalprofessional".toLowerCase().equalsIgnoreCase(getContactListResponse.getProfileTag().toLowerCase())) {
                            l.add(getContactListResponse);
                        } else if (getContactListResponse.getProfileTag() != null && "personal & professional".toLowerCase().equalsIgnoreCase(getContactListResponse.getProfileTag().toLowerCase())) {
                            l.add(getContactListResponse);
                        } else if (getContactListResponse.getProfileTag() != null && "personal and professional".toLowerCase().equalsIgnoreCase(getContactListResponse.getProfileTag().toLowerCase())) {
                            l.add(getContactListResponse);
                        } else if (getContactListResponse.getProfileTag() != null && "personal professional".toLowerCase().equalsIgnoreCase(getContactListResponse.getProfileTag().toLowerCase())) {
                            l.add(getContactListResponse);
                        }


                    } else {

                        if (s != null && getContactListResponse.getProfileTag() != null && s.toLowerCase().equalsIgnoreCase(getContactListResponse.getProfileTag().toLowerCase())) {
                            l.add(getContactListResponse);
                        }

                    }

                }

                mapList.put(s, l);

            }


            for (int i = categoriesList.size() - 1; i >= 0; i--) {

                SectionDataModel dm = new SectionDataModel();

                String ca = categoriesList.get(i);

                dm.setHeaderTitle(ca + " CONTACTS");

                List<GetContactListResponse> listResponses = mapList.get(ca);
                dm.setAllItemsInSection(listResponses);


                List<GetContactListResponse> temp = new ArrayList<>();
                temp.clear();
                temp.addAll(listResponses);

                if (temp.size() > 5) {
                    temp.subList(5, temp.size()).clear();
                }

                dm.setTemp(temp);


                allSampleData.add(dm);

            }

            return allSampleData;
        }

        @Override
        protected void onPostExecute(ArrayList<SectionDataModel> getContactListResponses) {
            super.onPostExecute(getContactListResponses);
            Utility.hideProgress();
            RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(getActivity(), getContactListResponses);

            my_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

            my_recycler_view.setAdapter(adapter);
        }
    }

    @Override
    void searchFilter(String s) {

    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        // TODO Add your menu entries here
//        menu.clear();
//        inflater.inflate(R.menu.sync, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.tick:
//                Toast.makeText(getActivity(), "Tick", Toast.LENGTH_SHORT).show();
//                break;
//
//
//        }
//        return true;
//
//    }


}
