package autodex.com.autodex;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import autodex.com.autodex.activitys.HomeActivity;
import autodex.com.autodex.locationservice.TestJobService;
import volleywebrequest.com.mysqlitelibrary.ContactAttribute;
import volleywebrequest.com.mysqlitelibrary.ContactCategory;
import volleywebrequest.com.mysqlitelibrary.GetContactListResponse;

/**
 * Created by yasar on 28/10/17.
 */

public class Util {
    private static final String TAG = "Util";

    public static String formatToYesterdayOrToday(String date) throws ParseException {
        Date dateTime = new SimpleDateFormat("EEE hh:mm a MMM d, yyyy").parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        Calendar today = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        DateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
//        DateFormat d = new SimpleDateFormat("MM-dd-yy");

        if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            return "Today, " + timeFormatter.format(dateTime);
        } else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
            return "Yesterday, " + timeFormatter.format(dateTime);
        } else {
            return date;
        }
    }


//    public static String formateddate(String date) {
//        DateTime dateTime = DateTimeFormat.forPattern("dd-MMM-yyyy").parseDateTime(date);
//        DateTime today = new DateTime();
//        DateTime yesterday = today.minusDays(1);
//        DateTime twodaysago = today.minusDays(2);
//        DateTime tomorrow= today.minusDays(-1);
//
//        if (dateTime.toLocalDate().equals(today.toLocalDate())) {
//            return "Today ";
//        } else if (dateTime.toLocalDate().equals(yesterday.toLocalDate())) {
//            return "Yesterday ";
//        } else if (dateTime.toLocalDate().equals(twodaysago.toLocalDate())) {
//            return "2 days ago ";
//        } else if (dateTime.toLocalDate().equals(tomorrow.toLocalDate())) {
//            return "Tomorrow ";
//        } else {
//            return date;
//        }
//    }


    public static void sendNotification(Context context, String notificationDetails) {
        // Create an explicit content Intent that starts the main Activity.
        Intent notificationIntent = new Intent(context, MainActivity.class);

        notificationIntent.putExtra("from_notification", true);

        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(HomeActivity.class);

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        // Define the notification settings.
        builder.setSmallIcon(R.mipmap.ic_logo)
                // In a real app, you may want to use a library like Volley
                // to decode the Bitmap.
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.ic_logo))
                .setColor(Color.RED)
                .setContentTitle("Location update")
                .setContentText(notificationDetails)
                .setContentIntent(notificationPendingIntent);

        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        // Get an instance of the Notification manager
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Issue the notification
        mNotificationManager.notify(0, builder.build());
    }


    public static void scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, TestJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(1 * 1000); // wait at least
        builder.setOverrideDeadline(2 * 1000); // maximum delay
        //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
        //builder.setRequiresDeviceIdle(true); // device should be idle
        //builder.setRequiresCharging(false); // we don't care if the device is charging or not
        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.schedule(builder.build());
    }

    public static String toTitleCase(String givenString) {
//        String[] arr = givenString.split(" ");
//        StringBuffer sb = new StringBuffer();
//
//        for (int i = 0; i < arr.length; i++) {
//            sb.append(Character.toUpperCase(arr[i].charAt(0)))
//                    .append(arr[i].substring(1)).append(" ");
//        }
//        return sb.toString().trim();

        String c = (givenString != null) ? givenString.trim() : "";
        String[] words = c.split(" ");
        String result = "";
        for (String w : words) {
            result += (w.length() > 1 ? w.substring(0, 1).toUpperCase(Locale.US) + w.substring(1, w.length()).toLowerCase(Locale.US) : w) + " ";
        }
        return result.trim();
    }


    public static List<GetContactListResponse> readContacts(Context context) {

        List<GetContactListResponse> list = new ArrayList<>();


        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                null, null, null);
        if (cur != null && cur.getCount() > 0) {

            while (cur.moveToNext()) {
                GetContactListResponse contactMap = new GetContactListResponse();
                contactMap.setProfileTag("uncategorized");
                String id = cur.getString(cur
                        .getColumnIndex(ContactsContract.Contacts._ID));
                String whereName = ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = ?";
                String[] whereNameParams = new String[]{ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE, id};
                Cursor nameCur = cr.query(ContactsContract.Data.CONTENT_URI, null, whereName, whereNameParams, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
                contactMap.setLocalContactID(id);
//                contactMap.setFirstName(".");
//                contactMap.setLastName(".");
//                contactMap.setMiddleName(".");

                boolean isHaveNotFirstAndLastName = false;

                if (nameCur.getCount() > 0) {
//                    isHaveNotFirstAndLastName = true;
                } else {
                    Log.e(TAG, "readContacts: called  Log.e(TAG, \"readContacts: called \" +isHaveNotFirstAndLastName); " + isHaveNotFirstAndLastName);
                    isHaveNotFirstAndLastName = true;
                }

                while (nameCur.moveToNext()) {
                    String given = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
                    String family = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
                    String middle = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME));
                    String display = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME));
                    Log.e(TAG, "readContacts: " + given + "   " + middle + "  " + family + "  " + display);

//                    if ((given != null && given.length() > 0) || (family != null && family.length() > 0)) {

                    contactMap.setFirstName(given == null ? "" : (given.length() > 0 ? given : ""));
                    contactMap.setLastName(family == null ? "" : (family.length() > 0 ? family : ""));
                    contactMap.setMiddleName(middle == null ? "" : (middle.length() > 0 ? middle : ""));
                    contactMap.setContactUserId(0);
                    contactMap.setFavorite(false);
                    if (display != null && display.length() > 0) {
                        isHaveNotFirstAndLastName = false;
                    } else {
                        Log.e(TAG, "readContacts: called  Log.e(TAG, \"readContacts: called \" +isHaveNotFirstAndLastName); " + isHaveNotFirstAndLastName);
                        isHaveNotFirstAndLastName = true;
                    }

                }
                nameCur.close();

                if (isHaveNotFirstAndLastName) {
                    Log.e(TAG, "readContacts: called  Log.e(TAG, \"readContacts: called \" +isHaveNotFirstAndLastName); " + isHaveNotFirstAndLastName);
                    continue;
                }

//                String image_uri = cur
//                        .getString(cur
//                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
//                contactMap.put("uri", image_uri);
                List<ContactAttribute> contactAttributes = new ArrayList<>();
                if (Integer
                        .parseInt(cur.getString(cur
                                .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                    + " = ?", new String[]{id}, null);

                    boolean isHasNotPhoneNumber = false;


                    while (pCur.moveToNext()) {
                        int phoneType = pCur.getInt(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                        //String isStarred 		= pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.STARRED));
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactMap.setPhoneNumber(phoneNo);
                        if (!(phoneNo.length() > 0)) {
                            isHasNotPhoneNumber = true;
                        }

//                        Log.e(TAG, "readContacts:Type    " + phoneType);

                        //you will get all phone numbers according to it's type as below switch case.
                        //Logs.e will print the phone number along with the name in DDMS. you can use these details where ever you want.
                        switch (phoneType) {

                            case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
//                                Log.e(TAG, "readContacts: Mobile " + contactMap.getFirstName() + " " + phoneNo);
                                if (!(phoneNo.length() > 0)) {
                                    isHasNotPhoneNumber = true;
                                }
                                contactMap.setPhoneNumber(phoneNo);

                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
//                                Log.e(TAG, "readContacts:Home " + contactMap.getFirstName() + " " + phoneNo);
                                ContactAttribute contactAttribute = new ContactAttribute();
                                contactAttribute.setName("home-phone");
                                contactAttribute.setValue(phoneNo);
                                contactAttributes.add(contactAttribute);
                                if (!(phoneNo.length() > 0)) {
                                    isHasNotPhoneNumber = true;
                                }

                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
//                                Log.e(TAG, "readContacts:Work " + contactMap.getFirstName() + " " + phoneNo);
                                ContactAttribute contactAttribute1 = new ContactAttribute();
                                contactAttribute1.setName("work-phone");
                                contactAttribute1.setValue(phoneNo);
                                contactAttributes.add(contactAttribute1);
                                if (!(phoneNo.length() > 0)) {
                                    isHasNotPhoneNumber = true;
                                }

                                break;

                            default:
                                break;
                        }
                    }
                    pCur.close();

                    if (isHasNotPhoneNumber) {
                        continue;
                    }

                } else {

                    Log.e(TAG, "readContacts: called ");

                    continue;
//                    contactMap.setPhoneNumber("");
//                    ContactAttribute contactAttribute = new ContactAttribute();
//                    contactAttribute.setName("home-phone");
//                    contactAttribute.setValue("");
//                    contactAttributes.add(contactAttribute);
//                    ContactAttribute contactAttribute1 = new ContactAttribute();
//                    contactAttribute1.setName("work-phone");
//                    contactAttribute1.setValue("");
//                    contactAttributes.add(contactAttribute1);
                }

                Cursor emailCur = cr.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID
                                + " = ?", new String[]{id}, null);
                contactMap.setUseEmail(false);
                while (emailCur.moveToNext()) {
                    String emailContact = emailCur
                            .getString(emailCur
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));


                    if (emailContact.length() > 0) {
                        contactMap.setUseEmail(true);
                    } else {
                        contactMap.setUseEmail(false);
                    }


                    ContactAttribute contactAttribute3 = new ContactAttribute();
                    contactAttribute3.setName("email");
                    contactAttribute3.setValue(emailContact);
                    contactAttributes.add(contactAttribute3);
                    //
                }
                //
                emailCur.close();

                contactMap.setContactAttributes(contactAttributes);
                // iterate through all Contact's Birthdays and print in log
                Cursor cursor = getContactsBirthdays(context);
                int bDayColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE);
                while (cursor.moveToNext()) {
                    String bDay = cursor.getString(bDayColumn);
                    contactMap.setDob(bDay);
                }
                cursor.close();

                ContactCategory category = new ContactCategory();
                category.setCategory("uncategorized");

                List<ContactCategory> contactCategories = new ArrayList<>();
                contactCategories.add(category);
                contactMap.setContactCategories(contactCategories);

//                Log.e(TAG, "readContacts: json " + new Gson().toJson(contactMap));

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