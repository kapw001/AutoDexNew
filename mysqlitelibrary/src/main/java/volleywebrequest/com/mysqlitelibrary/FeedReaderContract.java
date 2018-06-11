package volleywebrequest.com.mysqlitelibrary;

import android.provider.BaseColumns;

/**
 * Created by yasar on 6/10/17.
 */

public final class FeedReaderContract {
    private FeedReaderContract() {
    }

    /* Inner class that defines the table contents */
    public static class ContactListFeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "ContactList";
        public static final String COLUMN_NAME_DEFAULTID = "did";
        public static final String COLUMN_NAME_LOCALCONTACTID = "localid";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_createdByUser = "createdByUser";
        public static final String COLUMN_NAME_modifiedByUser = "modifiedByUser";
        public static final String COLUMN_NAME_createDate = "createDate";
        public static final String COLUMN_NAME_modifiedDate = "modifiedDate";
        public static final String COLUMN_NAME_profileTag = "profileTag";
        public static final String COLUMN_NAME_firstName = "firstName";
        public static final String COLUMN_NAME_middleName = "middleName";
        public static final String COLUMN_NAME_lastName = "lastName";
        public static final String COLUMN_NAME_contactUserId = "contactUserId";
        public static final String COLUMN_NAME_favorite = "favorite";
        public static final String COLUMN_NAME_dob = "dob";
        public static final String COLUMN_NAME_phoneNumber = "phoneNumber";
        public static final String COLUMN_NAME_useEmail = "useEmail";
        public static final String COLUMN_NAME_address1 = "address1";
        public static final String COLUMN_NAME_address2 = "address2";
        public static final String COLUMN_NAME_city = "city";
        public static final String COLUMN_NAME_state = "state";
        public static final String COLUMN_NAME_zip = "zip";
        public static final String COLUMN_NAME_profileImage = "profileImage";


    }

    public static final String SQL_CREATE_ContactListENTRIES =
            "CREATE TABLE " + ContactListFeedEntry.TABLE_NAME + " (" +
                    ContactListFeedEntry.COLUMN_NAME_DEFAULTID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    ContactListFeedEntry.COLUMN_NAME_ID + " INTEGER," +
                    ContactListFeedEntry.COLUMN_NAME_createdByUser + " TEXT," +
                    ContactListFeedEntry.COLUMN_NAME_modifiedByUser + " TEXT," +
                    ContactListFeedEntry.COLUMN_NAME_createDate + " TEXT," +
                    ContactListFeedEntry.COLUMN_NAME_modifiedDate + " TEXT," +
                    ContactListFeedEntry.COLUMN_NAME_profileTag + " TEXT," +
                    ContactListFeedEntry.COLUMN_NAME_firstName + " TEXT," +
                    ContactListFeedEntry.COLUMN_NAME_middleName + " TEXT," +
                    ContactListFeedEntry.COLUMN_NAME_lastName + " TEXT," +
                    ContactListFeedEntry.COLUMN_NAME_contactUserId + " INTEGER," +
                    ContactListFeedEntry.COLUMN_NAME_favorite + " TEXT," +
                    ContactListFeedEntry.COLUMN_NAME_dob + " TEXT," +
                    ContactListFeedEntry.COLUMN_NAME_phoneNumber + " INTEGER," +
                    ContactListFeedEntry.COLUMN_NAME_useEmail + " TEXT," +
                    ContactListFeedEntry.COLUMN_NAME_address1 + " TEXT," +
                    ContactListFeedEntry.COLUMN_NAME_address2 + " TEXT," +
                    ContactListFeedEntry.COLUMN_NAME_city + " TEXT," +
                    ContactListFeedEntry.COLUMN_NAME_state + " TEXT," +
                    ContactListFeedEntry.COLUMN_NAME_zip + " INTEGER," +
                    ContactListFeedEntry.COLUMN_NAME_LOCALCONTACTID + " INTEGER," +
                    ContactListFeedEntry.COLUMN_NAME_profileImage + " TEXT)";

    public static final String SQL_DELETE_ContactListENTRIES =
            "DROP TABLE IF EXISTS " + ContactListFeedEntry.TABLE_NAME;


    /* Inner class that defines the table contents */
    public static class ContactAttributeFeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "ContactAttribute";
        public static final String COLUMN_NAME_forignID = "forignid";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_createdByUser = "createdByUser";
        public static final String COLUMN_NAME_modifiedByUser = "modifiedByUser";
        public static final String COLUMN_NAME_createDate = "createDate";
        public static final String COLUMN_NAME_modifiedDate = "modifiedDate";
        public static final String COLUMN_NAME_name = "name";
        public static final String COLUMN_NAME_value = "value";
    }

    public static final String SQL_CREATE_ContactAttributeENTRIES =
            "CREATE TABLE " + ContactAttributeFeedEntry.TABLE_NAME + " (" +
                    ContactAttributeFeedEntry.COLUMN_NAME_forignID + " INTEGER," +
                    ContactAttributeFeedEntry.COLUMN_NAME_ID + " INTEGER," +
                    ContactAttributeFeedEntry.COLUMN_NAME_createdByUser + " TEXT," +
                    ContactAttributeFeedEntry.COLUMN_NAME_modifiedByUser + " TEXT," +
                    ContactAttributeFeedEntry.COLUMN_NAME_createDate + " TEXT," +
                    ContactAttributeFeedEntry.COLUMN_NAME_modifiedDate + " TEXT," +
                    ContactAttributeFeedEntry.COLUMN_NAME_name + " TEXT," +
                    ContactAttributeFeedEntry.COLUMN_NAME_value + " TEXT)";

    public static final String SQL_DELETE_ContactAttributeENTRIES =
            "DROP TABLE IF EXISTS " + ContactAttributeFeedEntry.TABLE_NAME;


    /* Inner class that defines the table contents */
    public static class ContactCategoryFeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "ContactCategory";
        public static final String COLUMN_NAME_forignID = "forignid";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_createdByUser = "createdByUser";
        public static final String COLUMN_NAME_modifiedByUser = "modifiedByUser";
        public static final String COLUMN_NAME_createDate = "createDate";
        public static final String COLUMN_NAME_modifiedDate = "modifiedDate";
        public static final String COLUMN_NAME_category = "category";
    }

    public static final String SQL_CREATE_ContactCategoryENTRIES =
            "CREATE TABLE " + ContactCategoryFeedEntry.TABLE_NAME + " (" +
                    ContactCategoryFeedEntry.COLUMN_NAME_forignID + " INTEGER," +
                    ContactCategoryFeedEntry.COLUMN_NAME_ID + " INTEGER," +
                    ContactCategoryFeedEntry.COLUMN_NAME_createdByUser + " TEXT," +
                    ContactCategoryFeedEntry.COLUMN_NAME_modifiedByUser + " TEXT," +
                    ContactCategoryFeedEntry.COLUMN_NAME_createDate + " TEXT," +
                    ContactCategoryFeedEntry.COLUMN_NAME_modifiedDate + " TEXT," +
                    ContactCategoryFeedEntry.COLUMN_NAME_category + " TEXT)";

    public static final String SQL_DELETE_ContactCategoryENTRIES =
            "DROP TABLE IF EXISTS " + ContactCategoryFeedEntry.TABLE_NAME;
}
