package autodex.com.autodex.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tunebrains.rcsections.IndexLayoutManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import autodex.com.autodex.ContactsProvider;
import autodex.com.autodex.R;
import autodex.com.autodex.Utility;
import autodex.com.autodex.adapter.ContactsAdapter;
import autodex.com.autodex.model.Contact;

import static android.content.ContentValues.TAG;

/**
 * Created by yasar on 11/9/17.
 */

public class ContactsFragment_new_old extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private RecyclerView mRecyclerView;
    private IndexLayoutManager mIndexLayoutManager;
    private List<Contact> contactList = new ArrayList<>();
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
        Utility.showProgress(getActivity(), "Loading...");
        getLoaderManager().restartLoader(1, null, this);
//        new Load().execute(new ArrayList<Contact>());

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

//        Uri CONTACT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
//        Uri CONTACT_URI = ContactsContract.Contacts.CONTENT_URI;
//        CursorLoader cursorLoader = new CursorLoader(getActivity(), CONTACT_URI, null,
//                null, null, null);

        String[] projectionFields = new String[]{ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_URI};
        // Construct the loader
        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, // URI
                null, // projection fields
                null, // the selection criteria
                null, // the selection args
                null // the sort order
        );

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor1) {

        List<Contact> contactList = new ArrayList<>();

        while (cursor1.moveToNext()) {
            String name = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            String phonenumber = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            String contactId = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID));
            Log.e(TAG, "onLoadFinished: ID " + contactId);
//            String[] projectionF = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
////            Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projectionF,
////                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
//            String phoneNumber = null;
//            int i = 0;
//            while (phones.moveToNext()) {
//                //                int type = phones.getInt(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
//                phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//
//                Log.e(TAG, "onLoadFinished: phones " + i);
//                i++;
////                switch (type) {
////                    case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
////                        // do something with the Home number here...
////
////                        break;
////                    case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
////                        // do something with the Mobile number here...
////                        break;
////                    case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
////                        // do something with the Work number here...
////                        break;
////                }
//            }
//            phones.close();

//            String phoneNumber = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//            Log.e(TAG, "onLoadFinished: phoneNumber    " + phonenumber);
            Contact contact = new Contact();
            contact.setName(name);
            contact.setId(contactId);
            contact.setPhoneNumber(phonenumber);
//            try {
//
//                String imageUri = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
//
//                Log.e(TAG, "onLoadFinished: uri " + imageUri);
//
//                Uri uri = Uri.parse(imageUri);
//                contact.setUri(uri);
//
//
//            } catch (NullPointerException e) {
//                Log.e(TAG, "onLoadFinished: " + e.getMessage());
//            }

            contactList.add(contact);


        }

        Collections.sort(contactList, new Comparator<Contact>() {
            @Override
            public int compare(Contact contact, Contact t1) {
                return contact.getName().compareTo(t1.getName());
            }
        });

//        contactsAdapter.updateList(contactList);
        Utility.hideProgress();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

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
