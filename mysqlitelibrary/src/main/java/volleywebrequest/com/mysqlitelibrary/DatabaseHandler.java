package volleywebrequest.com.mysqlitelibrary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by yasar on 6/10/17.
 */

public class DatabaseHandler {

    private FeedReaderDbHelper feedReaderDbHelper;

    private static DatabaseHandler databaseHandler;

    private SQLiteDatabase db;

    private DatabaseHandler(Context context) {
        feedReaderDbHelper = new FeedReaderDbHelper(context);
        getWritableDatabase();
    }

    public static DatabaseHandler getInstace(Context context) {
        if (databaseHandler == null) {
            databaseHandler = new DatabaseHandler(context);
        }

        return databaseHandler;
    }


    public void getWritableDatabase() {
        db = feedReaderDbHelper.getWritableDatabase();
    }

    public void closeDatabase() {
        db.close();
    }

    public static String checkISNull(Object s) {

        if (s != null) {
            return s.toString();
        } else {
            return "";
        }
    }

    public void addData(List<GetContactListResponse> list) {

        Log.e(TAG, "addData: called ");

        if (db.isOpen()) {
            // Create a new map of values, where column names are the keys

            db.beginTransaction();
            for (int i = 0; i < list.size(); i++) {

                try {

                    GetContactListResponse contact = list.get(i);
                    ContentValues values = new ContentValues();
                    values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_ID, contact.getId());
                    values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_createdByUser, checkISNull(contact.getCreatedByUser()));
                    values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_modifiedByUser, checkISNull(contact.getModifiedByUser()));
                    values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_createDate, checkISNull(contact.getCreateDate()));
                    values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_modifiedDate, checkISNull(contact.getModifiedDate()));
                    values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_profileTag, contact.getProfileTag());
                    values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_firstName, contact.getFirstName());
                    values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_middleName, contact.getMiddleName());
                    values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_lastName, contact.getLastName());
                    values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_contactUserId, contact.getContactUserId());
                    try {
                        values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_favorite, contact.getFavorite().toString());
                    } catch (NullPointerException e) {
                        values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_favorite, "false");
                    }

                    values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_dob, contact.getDob());
                    values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_phoneNumber, contact.getPhoneNumber());
                    values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_useEmail, contact.getUseEmail());
                    values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_address1, contact.getAddress1());
                    values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_address2, contact.getAddress2());
                    values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_city, contact.getCity());
                    values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_state, contact.getState());
                    values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_zip, contact.getZip());
                    values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_LOCALCONTACTID, contact.getLocalContactID());
                    values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_profileImage, contact.getProfileImage());


// Insert the new row, returning the primary key value of the new row
                    long newRowId = db.insert(FeedReaderContract.ContactListFeedEntry.TABLE_NAME, null, values);
                    Log.e(TAG, "addData: newRowId " + newRowId);
                } catch (NullPointerException e) {
                    Log.e(TAG, "addData: " + e.getMessage());
                }
            }

            db.setTransactionSuccessful();
            db.endTransaction();
        } else {
            Log.e(TAG, "database not open ");
        }


    }


    public void addContactAttributeData(List<ContactAttribute> list) {

        if (db.isOpen()) {
            // Create a new map of values, where column names are the keys

            db.beginTransaction();
            for (int i = 0; i < list.size(); i++) {
                ContactAttribute contact = list.get(i);
                ContentValues values = new ContentValues();
                values.put(FeedReaderContract.ContactAttributeFeedEntry.COLUMN_NAME_forignID, contact.getForignID());
                values.put(FeedReaderContract.ContactAttributeFeedEntry.COLUMN_NAME_ID, contact.getId());
                values.put(FeedReaderContract.ContactAttributeFeedEntry.COLUMN_NAME_createdByUser, checkISNull(contact.getCreatedByUser()));
                values.put(FeedReaderContract.ContactAttributeFeedEntry.COLUMN_NAME_modifiedByUser, checkISNull(contact.getModifiedByUser()));
                values.put(FeedReaderContract.ContactAttributeFeedEntry.COLUMN_NAME_createDate, checkISNull(contact.getCreateDate()));
                values.put(FeedReaderContract.ContactAttributeFeedEntry.COLUMN_NAME_modifiedDate, checkISNull(contact.getModifiedDate()));
                values.put(FeedReaderContract.ContactAttributeFeedEntry.COLUMN_NAME_name, contact.getName());
                values.put(FeedReaderContract.ContactAttributeFeedEntry.COLUMN_NAME_value, contact.getValue());


// Insert the new row, returning the primary key value of the new row
                long newRowId = db.insert(FeedReaderContract.ContactAttributeFeedEntry.TABLE_NAME, null, values);
                Log.e(TAG, "addData: newRowId " + newRowId);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
        } else {
            Log.e(TAG, "database not open ");
        }


    }

    public void addContactCategoryData(List<ContactCategory> list) {

        if (db.isOpen()) {
            // Create a new map of values, where column names are the keys

            db.beginTransaction();
            for (int i = 0; i < list.size(); i++) {
                ContactCategory contact = list.get(i);
                ContentValues values = new ContentValues();
                values.put(FeedReaderContract.ContactCategoryFeedEntry.COLUMN_NAME_forignID, contact.getForignID());
                values.put(FeedReaderContract.ContactCategoryFeedEntry.COLUMN_NAME_ID, contact.getId());
                values.put(FeedReaderContract.ContactCategoryFeedEntry.COLUMN_NAME_createdByUser, checkISNull(contact.getCreatedByUser()));
                values.put(FeedReaderContract.ContactCategoryFeedEntry.COLUMN_NAME_modifiedByUser, checkISNull(contact.getModifiedByUser()));
                values.put(FeedReaderContract.ContactCategoryFeedEntry.COLUMN_NAME_createDate, checkISNull(contact.getCreateDate()));
                values.put(FeedReaderContract.ContactCategoryFeedEntry.COLUMN_NAME_modifiedDate, checkISNull(contact.getModifiedDate()));
                values.put(FeedReaderContract.ContactCategoryFeedEntry.COLUMN_NAME_category, contact.getCategory());


// Insert the new row, returning the primary key value of the new row
                long newRowId = db.insert(FeedReaderContract.ContactCategoryFeedEntry.TABLE_NAME, null, values);
                Log.e(TAG, "addData: newRowId " + newRowId);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
        } else {
            Log.e(TAG, "database not open ");
        }


    }

    public void updateContactCategoryData(List<ContactCategory> list) {

        for (int i = 0; i < list.size(); i++) {
            ContactCategory contact = list.get(i);
            ContentValues values = new ContentValues();
            values.put(FeedReaderContract.ContactCategoryFeedEntry.COLUMN_NAME_forignID, contact.getForignID());
            values.put(FeedReaderContract.ContactCategoryFeedEntry.COLUMN_NAME_ID, contact.getId());
            values.put(FeedReaderContract.ContactCategoryFeedEntry.COLUMN_NAME_createdByUser, checkISNull(contact.getCreatedByUser()));
            values.put(FeedReaderContract.ContactCategoryFeedEntry.COLUMN_NAME_modifiedByUser, checkISNull(contact.getModifiedByUser()));
            values.put(FeedReaderContract.ContactCategoryFeedEntry.COLUMN_NAME_createDate, checkISNull(contact.getCreateDate()));
            values.put(FeedReaderContract.ContactCategoryFeedEntry.COLUMN_NAME_modifiedDate, checkISNull(contact.getModifiedDate()));
            values.put(FeedReaderContract.ContactCategoryFeedEntry.COLUMN_NAME_category, contact.getCategory());

            // updating row
            long updateId = db.update(FeedReaderContract.ContactCategoryFeedEntry.TABLE_NAME, values, FeedReaderContract.ContactCategoryFeedEntry.COLUMN_NAME_ID + " = ?",
                    new String[]{String.valueOf(contact.getId())});

            Log.e(TAG, "updateContactCategoryData : " + updateId);
        }

    }


    public List<ContactCategory> getAllContactCategoryDatas() {
        List<ContactCategory> contactList = new ArrayList<ContactCategory>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + FeedReaderContract.ContactCategoryFeedEntry.TABLE_NAME;

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ContactCategory contact = new ContactCategory();
                contact.setForignID(cursor.getString(0));
                contact.setId(cursor.getInt(1));
                contact.setCreatedByUser(cursor.getString(2));
                contact.setModifiedByUser(cursor.getString(3));
                contact.setCreateDate(cursor.getString(4));
                contact.setModifiedDate(cursor.getString(5));
                contact.setCategory(cursor.getString(6));
                // Adding contact to list
                contactList.add(contact);
//                Log.e(TAG, "getAllDatas: "+contact.toString() );
            } while (cursor.moveToNext());
        }

        cursor.close();

        // return contact list
        return contactList;
    }


    public void updateContactAttributeData(List<ContactAttribute> list) {

        for (int i = 0; i < list.size(); i++) {
            ContactAttribute contact = list.get(i);
            ContentValues values = new ContentValues();
//            values.put(FeedReaderContract.ContactAttributeFeedEntry.COLUMN_NAME_forignID, contact.getForignID());
            values.put(FeedReaderContract.ContactAttributeFeedEntry.COLUMN_NAME_ID, contact.getId());
            values.put(FeedReaderContract.ContactAttributeFeedEntry.COLUMN_NAME_createdByUser, checkISNull(contact.getCreatedByUser()));
            values.put(FeedReaderContract.ContactAttributeFeedEntry.COLUMN_NAME_modifiedByUser, checkISNull(contact.getModifiedByUser()));
            values.put(FeedReaderContract.ContactAttributeFeedEntry.COLUMN_NAME_createDate, checkISNull(contact.getCreateDate()));
            values.put(FeedReaderContract.ContactAttributeFeedEntry.COLUMN_NAME_modifiedDate, checkISNull(contact.getModifiedDate()));
            values.put(FeedReaderContract.ContactAttributeFeedEntry.COLUMN_NAME_name, contact.getName());
            values.put(FeedReaderContract.ContactAttributeFeedEntry.COLUMN_NAME_value, contact.getValue());

            // updating row
            long updateId = db.update(FeedReaderContract.ContactAttributeFeedEntry.TABLE_NAME, values, FeedReaderContract.ContactAttributeFeedEntry.COLUMN_NAME_ID + " = ?",
                    new String[]{String.valueOf(contact.getId())});

            Log.e(TAG, "updateContactAttributeData : " + updateId);

        }
    }

    public List<ContactAttribute> getAllContactAttributeDatas() {
        List<ContactAttribute> contactList = new ArrayList<ContactAttribute>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + FeedReaderContract.ContactAttributeFeedEntry.TABLE_NAME;

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ContactAttribute contact = new ContactAttribute();
                contact.setForignID(cursor.getString(1));
                contact.setId(cursor.getInt(2));
                contact.setCreatedByUser(cursor.getString(3));
                contact.setModifiedByUser(cursor.getString(4));
                contact.setCreateDate(cursor.getString(5));
                contact.setModifiedDate(cursor.getString(6));
                contact.setName(cursor.getString(7));
                contact.setValue(cursor.getString(8));
                // Adding contact to list
                contactList.add(contact);
//                Log.e(TAG, "getAllDatas: "+contact.toString() );
            } while (cursor.moveToNext());
        }

        cursor.close();

        // return contact list
        return contactList;
    }

    public List<ContactAttribute> getAllContactAttributeDatas(String id) {
        List<ContactAttribute> contactList = new ArrayList<ContactAttribute>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + FeedReaderContract.ContactAttributeFeedEntry.TABLE_NAME + " WHERE " + FeedReaderContract.ContactAttributeFeedEntry.COLUMN_NAME_forignID + "=" + id;

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ContactAttribute contact = new ContactAttribute();
                contact.setForignID(cursor.getString(0));
                contact.setId(cursor.getInt(1));
                contact.setCreatedByUser(cursor.getString(2));
                contact.setModifiedByUser(cursor.getString(3));
                contact.setCreateDate(cursor.getString(4));
                contact.setModifiedDate(cursor.getString(5));
                contact.setName(cursor.getString(6));
                contact.setValue(cursor.getString(7));
                // Adding contact to list
                contactList.add(contact);
//                Log.e(TAG, "getAllDatas: "+contact.toString() );
            } while (cursor.moveToNext());
        }

        cursor.close();

        // return contact list
        return contactList;
    }

    public List<ContactCategory> getAllContactCategoryDatas(String id) {
        List<ContactCategory> contactList = new ArrayList<ContactCategory>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + FeedReaderContract.ContactCategoryFeedEntry.TABLE_NAME + " WHERE " + FeedReaderContract.ContactCategoryFeedEntry.COLUMN_NAME_forignID + "=" + id;

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ContactCategory contact = new ContactCategory();
                contact.setForignID(cursor.getString(0));
                contact.setId(cursor.getInt(1));
                contact.setCreatedByUser(cursor.getString(2));
                contact.setModifiedByUser(cursor.getString(3));
                contact.setCreateDate(cursor.getString(4));
                contact.setModifiedDate(cursor.getString(5));
                contact.setCategory(cursor.getString(6));
                // Adding contact to list
                contactList.add(contact);
//                Log.e(TAG, "getAllDatas: "+contact.toString() );
            } while (cursor.moveToNext());
        }

        cursor.close();

        // return contact list
        return contactList;
    }


    public void updateData(GetContactListResponse contact) {

//        try {
        ContentValues values = new ContentValues();
//        values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_ID, contact.getId());
        values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_createdByUser, checkISNull(contact.getCreatedByUser()));
        values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_modifiedByUser, checkISNull(contact.getModifiedByUser()));
        values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_createDate, checkISNull(contact.getCreateDate()));
        values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_modifiedDate, checkISNull(contact.getModifiedDate()));
        values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_profileTag, contact.getProfileTag());
        values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_firstName, contact.getFirstName());
        values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_middleName, contact.getMiddleName());
        values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_lastName, contact.getLastName());
        values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_contactUserId, contact.getContactUserId());
        values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_favorite, contact.getFavorite().toString());
        values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_dob, contact.getDob());
        values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_phoneNumber, contact.getPhoneNumber());
        values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_useEmail, contact.getUseEmail());
        values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_address1, contact.getAddress1());
        values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_address2, contact.getAddress2());
        values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_city, contact.getCity());
        values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_state, contact.getState());
        values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_zip, contact.getZip());
//        values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_LOCALCONTACTID, contact.getLocalContactID());
        values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_profileImage, contact.getProfileImage());

        // updating row
        long updateId = db.update(FeedReaderContract.ContactListFeedEntry.TABLE_NAME, values, FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_ID + " = ?",
                new String[]{String.valueOf(contact.getId())});
        Log.e(TAG, "updateData:  updating row   " + updateId + " passed  id " + contact.getId());

//        } catch (NullPointerException e) {
//
//        }


    }

    public void updateDataFavourite(Boolean contact, int id) {


        ContentValues data = new ContentValues();
        data.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_favorite, contact.toString());
        long updateId = db.update(FeedReaderContract.ContactListFeedEntry.TABLE_NAME, data, FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_ID + " = " + id, null);

        Log.e(TAG, "updateDataFavourite: id " + id);

//        try {
//        ContentValues values = new ContentValues();
//
//        values.put(FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_favorite, contact);
//
//
//        // updating row
//        long updateId = db.update(FeedReaderContract.ContactListFeedEntry.TABLE_NAME, values, FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_ID + " = ?",
//                new String[]{String.valueOf(id)});
        Log.e(TAG, "updateData:  updating row   " + updateId);

//        } catch (NullPointerException e) {
//
//        }


    }

    public List<GetContactListResponse> getAllDatas() {
        List<GetContactListResponse> contactList = new ArrayList<GetContactListResponse>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + FeedReaderContract.ContactListFeedEntry.TABLE_NAME;

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                GetContactListResponse contact = new GetContactListResponse();
                contact.setId(cursor.getInt(1));
                contact.setCreatedByUser(cursor.getString(2));
                contact.setModifiedByUser(cursor.getString(3));
                contact.setCreateDate(cursor.getString(4));
                contact.setModifiedDate(cursor.getString(5));
                contact.setProfileTag(cursor.getString(6));
                contact.setFirstName(cursor.getString(7));
                contact.setMiddleName(cursor.getString(8));
                contact.setLastName(cursor.getString(9));
                contact.setContactUserId(cursor.getInt(10));
                contact.setFavorite(Boolean.parseBoolean(cursor.getString(11)));
                contact.setDob(cursor.getString(12));
                contact.setPhoneNumber(cursor.getString(13));
                contact.setUseEmail(Boolean.parseBoolean(cursor.getString(14)));
                contact.setAddress1(cursor.getString(15));
                contact.setAddress2(cursor.getString(16));
                contact.setCity(cursor.getString(17));
                contact.setState(cursor.getString(18));
                contact.setZip(cursor.getString(19));
                contact.setLocalContactID(cursor.getString(20));
                contact.setProfileImage(cursor.getString(21));
                // Adding contact to list
                contactList.add(contact);
//                Log.e(TAG, "getAllDatas: "+contact.toString() );
            } while (cursor.moveToNext());
        }

        cursor.close();

        // return contact list
        return contactList;
    }


    public List<GetContactListResponse> getAllDatas(String id) {
        List<GetContactListResponse> contactList = new ArrayList<GetContactListResponse>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + FeedReaderContract.ContactListFeedEntry.TABLE_NAME + " WHERE " + FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_ID + "=" + id;

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                GetContactListResponse contact = new GetContactListResponse();
                contact.setId(cursor.getInt(1));
                contact.setCreatedByUser(cursor.getString(2));
                contact.setModifiedByUser(cursor.getString(3));
                contact.setCreateDate(cursor.getString(4));
                contact.setModifiedDate(cursor.getString(5));
                contact.setProfileTag(cursor.getString(6));
                contact.setFirstName(cursor.getString(7));
                contact.setMiddleName(cursor.getString(8));
                contact.setLastName(cursor.getString(9));
                contact.setContactUserId(cursor.getInt(10));
                contact.setFavorite(Boolean.parseBoolean(cursor.getString(11)));
                contact.setDob(cursor.getString(12));
                contact.setPhoneNumber(cursor.getString(13));
                contact.setUseEmail(Boolean.parseBoolean(cursor.getString(14)));
                contact.setAddress1(cursor.getString(15));
                contact.setAddress2(cursor.getString(16));
                contact.setCity(cursor.getString(17));
                contact.setState(cursor.getString(18));
                contact.setZip(cursor.getString(19));
                contact.setProfileImage(cursor.getString(20));
                // Adding contact to list
                contactList.add(contact);
//                Log.e(TAG, "getAllDatas: "+contact.toString() );
            } while (cursor.moveToNext());
        }

        cursor.close();

        // return contact list
        return contactList;
    }

    public List<GetContactListResponse> getAllFavouritesDatas(String isFav) {
        List<GetContactListResponse> contactList = new ArrayList<GetContactListResponse>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + FeedReaderContract.ContactListFeedEntry.TABLE_NAME + " WHERE " + FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_favorite + "=true";

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                GetContactListResponse contact = new GetContactListResponse();
                contact.setId(cursor.getInt(1));
                contact.setCreatedByUser(cursor.getString(2));
                contact.setModifiedByUser(cursor.getString(3));
                contact.setCreateDate(cursor.getString(4));
                contact.setModifiedDate(cursor.getString(5));
                contact.setProfileTag(cursor.getString(6));
                contact.setFirstName(cursor.getString(7));
                contact.setMiddleName(cursor.getString(8));
                contact.setLastName(cursor.getString(9));
                contact.setContactUserId(cursor.getInt(10));
                contact.setFavorite(Boolean.parseBoolean(cursor.getString(11)));
                contact.setDob(cursor.getString(12));
                contact.setPhoneNumber(cursor.getString(13));
                contact.setUseEmail(Boolean.parseBoolean(cursor.getString(14)));
                contact.setAddress1(cursor.getString(15));
                contact.setAddress2(cursor.getString(16));
                contact.setCity(cursor.getString(17));
                contact.setState(cursor.getString(18));
                contact.setZip(cursor.getString(19));
                contact.setProfileImage(cursor.getString(20));
                // Adding contact to list
                contactList.add(contact);
//                Log.e(TAG, "getAllDatas: "+contact.toString() );
            } while (cursor.moveToNext());
        }

        cursor.close();

        // return contact list
        return contactList;
    }

    public int getDataCount() {
        String countQuery = "SELECT  * FROM " + FeedReaderContract.ContactListFeedEntry.TABLE_NAME;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public void deleteData(GetContactListResponse contact) {

        db.delete(FeedReaderContract.ContactListFeedEntry.TABLE_NAME, FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_ID + " = ?",
                new String[]{String.valueOf(contact.getId())});

    }

    public void deleteAllData() {
        db.delete(FeedReaderContract.ContactListFeedEntry.TABLE_NAME, null, null);
    }

    public void deleteAllContactAttributeData() {
        db.delete(FeedReaderContract.ContactAttributeFeedEntry.TABLE_NAME, null, null);
    }

    public void deleteAllContactCategoryData() {
        db.delete(FeedReaderContract.ContactCategoryFeedEntry.TABLE_NAME, null, null);
    }

    public void deleteAllDataRow(String id) {
        long isd = db.delete(FeedReaderContract.ContactListFeedEntry.TABLE_NAME, FeedReaderContract.ContactListFeedEntry.COLUMN_NAME_ID + " = ?", new String[]{id});

        Log.e(TAG, "deleteAllDataRow: " + isd);
    }

    public void deleteAllContactAttributeDataRow(String id) {
        long idd = db.delete(FeedReaderContract.ContactAttributeFeedEntry.TABLE_NAME, FeedReaderContract.ContactAttributeFeedEntry.COLUMN_NAME_forignID + " = ?", new String[]{id});
        Log.e(TAG, "deleteAllContactAttributeDataRow: " + idd);
    }

    public void deleteAllContactCategoryDataRow(String id) {
        long iddd = db.delete(FeedReaderContract.ContactCategoryFeedEntry.TABLE_NAME, FeedReaderContract.ContactCategoryFeedEntry.COLUMN_NAME_forignID + " = ?", new String[]{id});

        Log.e(TAG, "deleteAllContactCategoryDataRow: " + iddd);
    }

}
