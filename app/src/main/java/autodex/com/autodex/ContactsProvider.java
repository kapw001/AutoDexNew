package autodex.com.autodex;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import autodex.com.autodex.model.Contact;

import static android.content.ContentValues.TAG;

/**
 * Created by Alexandr Timoshenko <thick.tav@gmail.com> on 11/13/15.
 */
public class ContactsProvider {
    public static List<Contact> load(Context pContext) {
        List<Contact> lContacts = new ArrayList<>();
        Cursor c = null;
        try {
            c = pContext.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            if (c != null && c.getCount() > 0) {
                while (c.moveToNext()) {
                    String lName = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));

                    Uri uri = getPhotoUri(pContext, Long.parseLong(contactId));
//                    Log.e(TAG, "load: " + contactId + " " + uri);
                    lContacts.add(new Contact(lName, uri.toString()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.sort(lContacts, new Comparator<Contact>() {
            @Override
            public int compare(Contact lhs, Contact rhs) {
                return lhs.getName().compareToIgnoreCase(rhs.getName());
            }
        });
        return lContacts;
    }

    public static String fetchContactIdFromPhoneNumber(Context pContext, String phoneNumber) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber));
        Cursor cursor = pContext.getContentResolver().query(uri,
                new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID},
                null, null, null);

        String contactId = "";

        if (cursor.moveToFirst()) {
            do {
                contactId = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.PhoneLookup._ID));
            } while (cursor.moveToNext());
        }

        return contactId;
    }

    public static Uri getPhotoUri(Context pContext, long contactId) {
        ContentResolver contentResolver = pContext.getContentResolver();

        try {
            Cursor cursor = contentResolver
                    .query(ContactsContract.Data.CONTENT_URI,
                            null,
                            ContactsContract.Data.CONTACT_ID
                                    + "="
                                    + contactId
                                    + " AND "

                                    + ContactsContract.Data.MIMETYPE
                                    + "='"
                                    + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
                                    + "'", null, null);

            if (cursor != null) {
                Uri person = ContentUris.withAppendedId(
                        ContactsContract.Contacts.CONTENT_URI, contactId);
                Uri uri = Uri.withAppendedPath(person,
                        ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
                cursor.close();
                return uri;

            } else {
                cursor.close();
                return null; // error in cursor process
            }

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }


    }
}
