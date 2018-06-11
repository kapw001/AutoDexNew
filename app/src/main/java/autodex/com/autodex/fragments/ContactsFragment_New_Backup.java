package autodex.com.autodex.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import java.util.List;

import autodex.com.autodex.R;
import autodex.com.autodex.UtilFunctionalitys;
import autodex.com.autodex.Utility;
import autodex.com.autodex.adapter.ContactsAdapter;
import autodex.com.autodex.model.Contact;

import static android.content.ContentValues.TAG;

/**
 * Created by yasar on 11/9/17.
 */

public class ContactsFragment_New_Backup extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {
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


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)
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
        Uri CONTACT_URI = ContactsContract.Contacts.CONTENT_URI;
//        CursorLoader cursorLoader = new CursorLoader(getActivity(), CONTACT_URI, null,
//                null, null, null);

//        String[] projectionFields = new String[]{ContactsContract.Contacts._ID,
//                ContactsContract.Contacts.DISPLAY_NAME,
//                ContactsContract.Contacts.PHOTO_URI};
        // Construct the loader
        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                CONTACT_URI, // URI
                null, // projection fields
                null, // the selection criteria
                null, // the selection args
                null // the sort order
        );

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor1) {

        List<Contact> cList = new ArrayList<>();

        while (cursor1.moveToNext()) {
            String name = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String contactId = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID));
            Contact contact = new Contact();
            contact.setName(name);
            contact.setId(contactId);
            try {
                String imageUri = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                Uri uri = Uri.parse(imageUri);
                contact.setUri(uri.toString());
            } catch (NullPointerException e) {
                Log.e(TAG, "Contact image not found  " + e.getMessage());
            }

            cList.add(contact);


        }
//        UtilFunctionalitys.shortListAlphabet(cList);

        contactList.addAll(cList);

//        contactsAdapter.updateList(cList);
//        Utility.hideProgress();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    @Override
    public void searchFilter(final String s) {

        if (contactList.size() > 0) {

//            List<Contact> l = UtilFunctionalitys.getSearchList(s, contactList);

//            UtilFunctionalitys.shortListAlphabet(l);
//
//            contactsAdapter.updateList(l);

        } else {
            Log.e(TAG, "searchFilter: There is no contact to search ");
        }
    }


}