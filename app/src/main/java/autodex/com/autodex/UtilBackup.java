package autodex.com.autodex;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yasar on 28/10/17.
 */

public class UtilBackup {
    private static final String TAG = "Util";

    public static List<Map<Object, Object>> readContacts(Context context) {

        List<Map<Object, Object>> list = new ArrayList<>();


        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                null, null, null);
//        Cursor cur = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null,
//                ContactsContract.Contacts._ID + " = ?", new String[]{String.valueOf(contactId)}, null);
        if (cur != null && cur.getCount() > 0) {

            while (cur.moveToNext()) {
                Map<Object, Object> contactMap = new HashMap<>();
                contactMap.put("profileTag", "personal");
                String id = cur.getString(cur
                        .getColumnIndex(ContactsContract.Contacts._ID));
                String whereName = ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = ?";
                String[] whereNameParams = new String[]{ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE, id};
                Cursor nameCur = cr.query(ContactsContract.Data.CONTENT_URI, null, whereName, whereNameParams, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
                while (nameCur.moveToNext()) {
                    String given = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
                    String family = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
                    String middle = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME));
                    String display = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME));
                    Log.e(TAG, "readContacts: " + given + "   " + middle + "  " + family);
                    contactMap.put("firstName", given);
                    contactMap.put("lastName", family);
                    contactMap.put("middleName", middle);
                    contactMap.put("displayname", display);
                    contactMap.put("contactUserId", 0);
                    contactMap.put("favorite", false);

                }
                nameCur.close();
//                String image_uri = cur
//                        .getString(cur
//                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
//                contactMap.put("uri", image_uri);
                List<Map<Object, Object>> contactAttributes = new ArrayList<>();
                if (Integer
                        .parseInt(cur.getString(cur
                                .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                    + " = ?", new String[]{id}, null);

                    while (pCur.moveToNext()) {
                        int phoneType = pCur.getInt(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                        //String isStarred 		= pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.STARRED));
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        //you will get all phone numbers according to it's type as below switch case.
                        //Logs.e will print the phone number along with the name in DDMS. you can use these details where ever you want.
                        switch (phoneType) {
                            case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:

                                contactMap.put("phoneNumber", phoneNo);
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                Map<Object, Object> homeNumber = new HashMap<>();
                                homeNumber.put("name", "home-phone");
                                homeNumber.put("value", phoneNo);
                                contactAttributes.add(homeNumber);
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                Map<Object, Object> workNumber = new HashMap<>();
                                workNumber.put("name", "work-phone");
                                workNumber.put("value", phoneNo);
                                contactAttributes.add(workNumber);
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE:
                                Map<Object, Object> workNumber1 = new HashMap<>();
                                workNumber1.put("name", "work-mobile-phone");
                                workNumber1.put("value", phoneNo);
                                contactAttributes.add(workNumber1);
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                                Map<Object, Object> workNumber2 = new HashMap<>();
                                workNumber2.put("name", "other-phone");
                                workNumber2.put("value", phoneNo);
                                contactAttributes.add(workNumber2);
                                break;
                            default:
                                break;
                        }
                    }
                    pCur.close();

                }

                Cursor emailCur = cr.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID
                                + " = ?", new String[]{id}, null);
                while (emailCur.moveToNext()) {
                    String emailContact = emailCur
                            .getString(emailCur
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    //                    emailType = emailCur
                    //                            .getString(emailCur
                    //                                    .getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
                    //                    sb.append("\nEmail:" + emailContact + "Email type:" + emailType);
                    //                    System.out.println("Email " + emailContact
                    //                            + " Email Type : " + emailType);

//                    contactMap.put("email", emailContact);

                    if (emailContact.length() > 0) {
                        contactMap.put("useEmail", true);
                    } else {
                        contactMap.put("useEmail", false);
                    }

                    Map<Object, Object> email = new HashMap<>();
                    email.put("name", "email");
                    email.put("value", emailContact);
                    contactAttributes.add(email);
                    //
                }
                //
                emailCur.close();

                contactMap.put("contactAttributes", contactAttributes);
                // iterate through all Contact's Birthdays and print in log
                Cursor cursor = getContactsBirthdays(context);
                int bDayColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE);
                while (cursor.moveToNext()) {
                    String bDay = cursor.getString(bDayColumn);
                    contactMap.put("dob", bDay);
                }
                cursor.close();

                Map<Object, Object> category = new HashMap<>();
                category.put("category", "personal");

                List<Map<Object, Object>> contactCategories = new ArrayList<>();
                contactCategories.add(category);
                contactMap.put("contactCategories", contactCategories);

                list.add(contactMap);
            }


            cur.close();

        }
        return list;
    }

    private static Cursor getContactsBirthdays(Context context) {
        Uri uri = ContactsContract.Data.CONTENT_URI;
        String[] projection = new String[]{
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Event.CONTACT_ID,
                ContactsContract.CommonDataKinds.Event.START_DATE
        };

        String where =
                ContactsContract.Data.MIMETYPE + "= ? AND " +
                        ContactsContract.CommonDataKinds.Event.TYPE + "=" +
                        ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY;
        String[] selectionArgs = new String[]{
                ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE
        };
        String sortOrder = null;
        return context.getContentResolver().query(uri, projection, where, selectionArgs, sortOrder);
    }
}