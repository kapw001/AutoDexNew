package autodex.com.autodex;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import autodex.com.autodex.activitys.SignUpActivity;
import autodex.com.autodex.model.Contact;
import volleywebrequest.com.mysqlitelibrary.GetContactListResponse;


/**
 * Created by yasar on 7/9/17.
 */

public class UtilFunctionalitys {
    private static final String TAG = "UtilFunctionalitys";

    public static void share(Context context, String Subject, String msg) {
        Log.e(TAG, "share: ");
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "AutoDex");
        share.putExtra(Intent.EXTRA_TEXT, msg + " This is shared from autodex");

        context.startActivity(Intent.createChooser(share, "Share link!"));

    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    public static Bitmap getBitmapdecode64(Context context, String imagebase64) {
        if (imagebase64 != null && imagebase64.length() > 0) {
            byte[] decodedString = Base64.decode(imagebase64, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } else {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.profile);
        }

    }

    public static List<GetContactListResponse> getSearchList(final String s, List<GetContactListResponse> contactList) {
        List<GetContactListResponse> list = new ArrayList<GetContactListResponse>();

        for (int i = 0; i < contactList.size(); i++) {
            GetContactListResponse contact = contactList.get(i);

            if (contact.getFirstName().toLowerCase().contains(s.toLowerCase()) || contact.getLastName().toLowerCase().contains(s.toLowerCase()) || contact.getMiddleName().toLowerCase().contains(s.toLowerCase())) {
                list.add(contact);

            }
        }

        return list;
    }


    public static void shortListAlphabet2(List<GetContactListResponse> list) {
        Collections.sort(list, new Comparator<GetContactListResponse>() {
            @Override
            public int compare(GetContactListResponse contact, GetContactListResponse t1) {

                if (contact.getWholeName() != null && t1.getWholeName() != null) {
                    return contact.getWholeName().toLowerCase().compareTo(t1.getWholeName().toLowerCase());
                }

                return -1;


            }
        });
    }

    public static void shortListAlphabet(List<GetContactListResponse> list) {
        Collections.sort(list, new Comparator<GetContactListResponse>() {
            @Override
            public int compare(GetContactListResponse contact, GetContactListResponse t1) {

                if (contact.getFirstName() != null && t1.getFirstName() != null) {
                    return contact.getFirstName().toLowerCase().compareTo(t1.getFirstName().toLowerCase());
                } else if (contact.getLastName() != null && t1.getLastName() != null) {
                    return contact.getLastName().toLowerCase().compareTo(t1.getLastName().toLowerCase());
                } else if (contact.getMiddleName() != null && t1.getMiddleName() != null) {
                    return contact.getMiddleName().toLowerCase().compareTo(t1.getMiddleName().toLowerCase());
                }

                return -1;


            }
        });
    }

    public static void shortListAlphabetAnother(List<GetContactListResponse> list) {
        Collections.sort(list, new Comparator<GetContactListResponse>() {
            @Override
            public int compare(GetContactListResponse contact, GetContactListResponse t1) {
                int c = -1;
                if (contact.getFirstName() != null && t1.getFirstName() != null) {
                    c = contact.getFirstName().toLowerCase().compareTo(t1.getFirstName().toLowerCase());
                }

                if (c == 0)
                    if (contact.getLastName() != null && t1.getLastName() != null) {
                        c = contact.getLastName().toLowerCase().compareTo(t1.getLastName().toLowerCase());
                    }
                if (c == 0)
                    if (contact.getMiddleName() != null && t1.getMiddleName() != null) {
                        c = contact.getMiddleName().toLowerCase().compareTo(t1.getMiddleName().toLowerCase());
                    }

                if (c == 0)
                    if (contact.getPhoneNumber() != null && t1.getPhoneNumber() != null) {
                        c = contact.getPhoneNumber().toLowerCase().compareTo(t1.getPhoneNumber().toLowerCase());
                    }


                return c;


            }
        });
    }


}
