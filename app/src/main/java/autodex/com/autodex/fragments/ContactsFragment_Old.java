package autodex.com.autodex.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tunebrains.rcsections.IndexLayoutManager;

import java.util.ArrayList;
import java.util.List;

import autodex.com.autodex.ContactsProvider;
import autodex.com.autodex.R;
import autodex.com.autodex.Utility;
import autodex.com.autodex.adapter.ContactsAdapter;
import autodex.com.autodex.model.Contact;

/**
 * Created by yasar on 11/9/17.
 */

public class ContactsFragment_Old extends BaseFragment {
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private RecyclerView mRecyclerView;
    private IndexLayoutManager mIndexLayoutManager;
    private List<Contact> contactList=new ArrayList<>();
    private ContactsAdapter contactsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_contacts, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        contactList = new ArrayList<>();
//        contactsAdapter = new ContactsAdapter(getActivity(), contactList);
        mRecyclerView.setAdapter(contactsAdapter);

        mIndexLayoutManager = (IndexLayoutManager) view.findViewById(R.id.index_layout);
        mIndexLayoutManager.attach(mRecyclerView);
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
//            // Do something for lollipop and above versions
//        } else{
//            // do something for phones running an SDK before lollipop
//        }

        // Check if we're running on Android 5.0 or higher
        if (Build.VERSION.SDK_INT >= 21) {
            // Call some material design APIs here
            showContacts();
        } else {
            // Implement this feature without material design
            updateList();
        }


        return view;
    }

    private void showContacts() {
        if (getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            updateList();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateList();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    Toast.makeText(getContext(), "Please provide contact read permissions", Toast.LENGTH_SHORT).show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    public void updateList() {
//        List<Contact> list = ContactsProvider.load(getActivity());
//
//        contactsAdapter.updateList(list);

        new Load().execute(new ArrayList<Contact>());

    }

    @Override
    void searchFilter(String s) {

    }

    class Load extends AsyncTask<List<Contact>, List<Contact>, List<Contact>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Utility.showProgress(getActivity(), "Loading...");

        }

        @Override
        protected List<Contact> doInBackground(List<Contact>... lists) {

            List<Contact> list = ContactsProvider.load(getActivity());

            return list;
        }

        @Override
        protected void onPostExecute(List<Contact> list) {
//            super.onPostExecute(contacts);
//            contactsAdapter.updateList(list);
            Utility.hideProgress();
        }
    }


}
