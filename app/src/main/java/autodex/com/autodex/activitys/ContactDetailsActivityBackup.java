package autodex.com.autodex.activitys;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import autodex.com.autodex.R;
import autodex.com.autodex.UtilFunctionalitys;
import volleywebrequest.com.mysqlitelibrary.ContactAttribute;
import volleywebrequest.com.mysqlitelibrary.ContactCategory;
import volleywebrequest.com.mysqlitelibrary.GetContactListResponse;

public class ContactDetailsActivityBackup extends BaseActivity {

    private static final String TAG = "ContactDetailsActivity";

    private TextView mobile;
    private ImageView profileImg, phonecall, favorite;
    private Uri uri;
    private int idEDIT_CONTACT = 1;
    private String contactID;
    private Map<String, String> details = new LinkedHashMap<>();
    private GetContactListResponse contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        profileImg = (ImageView) findViewById(R.id.profile_img);
        phonecall = (ImageView) findViewById(R.id.phonecall);
        favorite = (ImageView) findViewById(R.id.favorite);
        mobile = (TextView) findViewById(R.id.mobile);

        contact = (GetContactListResponse) getIntent().getSerializableExtra("contact");


        Gson gson = new Gson();

        String jsonInString = gson.toJson(contact);

        List<ContactAttribute> list = databaseHandler.getAllContactAttributeDatas(String.valueOf(contact.getId()));
        List<ContactCategory> list1 = databaseHandler.getAllContactCategoryDatas(String.valueOf(contact.getId()));

        String jsonInString1 = gson.toJson(list);
        String jsonInString2 = gson.toJson(list1);


        Log.e(TAG, "onCreate: " + jsonInString);
        Log.e(TAG, "onCreate: " + jsonInString1);
        Log.e(TAG, "onCreate: " + jsonInString2);


//        Log.e(TAG, "onCreate: " + contact.toString());
//
//        Log.e(TAG, "onCreate: " + contact.getContactAttributes().toString());
//        Log.e(TAG, "onCreate: " + contact.getContactCategories().toString());

//        contactID = contact.getId();
//
//        phonecall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mobile.getText().length() > 0) {
//                    Uri number = Uri.parse("tel:" + mobile.getText());
//                    Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
//                    startActivity(callIntent);
//                } else {
//                    Toast.makeText(ContactDetailsActivity.this, "There is no dial number available.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
////        getSupportActionBar().setTitle(contact.getName());
//
//        String u = getIntent().getStringExtra("contacturi");
//
//        uri = Uri.parse(u);
//
//        try {
//            Uri uri = Uri.parse(contact.getUri());
//            profileImg.setImageURI(uri);
//        } catch (NullPointerException e) {
//            Log.e(TAG, "onCreate: " + e.getMessage());
//        }
//
//
//        readContacts(contactID);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.contactdetailsmenu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        } else if (item.getItemId() == R.id.edit) {

            final Uri uri = Uri.withAppendedPath(
                    ContactsContract.Contacts.CONTENT_URI, contactID);


            Intent intent = new Intent(this, CreateContactActivity.class);
            intent.putExtra("contacturi", uri.toString());
            intent.putExtra("contact", (Serializable) contact);
            intent.putExtra("edit", true);
            startActivity(intent);


//            Intent intent = new Intent(Intent.ACTION_EDIT, uri);
//            intent.putExtra("finishActivityOnSaveCompleted", true);
////            startActivity(intent);
//
//            startActivityForResult(intent, idEDIT_CONTACT);
        } else if (item.getItemId() == R.id.show) {
            UtilFunctionalitys.share(this, "", "");
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Intent intent = getIntent();
        finish();
        startActivity(intent);

    }

    public void readContacts(String contactId) {
        StringBuffer sb = new StringBuffer();
        sb.append("......Contact Details.....");
        ContentResolver cr = getContentResolver();
//        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
//                null, null, null);

        Cursor cur = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null,
                ContactsContract.Contacts._ID + " = ?", new String[]{String.valueOf(contactId)}, null);


//        Toast.makeText(this, "Cur " + cur.getCount(), Toast.LENGTH_SHORT).show();

        String phone = null;
        String emailContact = null;
        String emailType = null;
        String image_uri = "";
        Bitmap bitmap = null;
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur
                        .getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur
                        .getString(cur
                                .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                getSupportActionBar().setTitle(name);

//
                image_uri = cur
                        .getString(cur
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

                try {
                    Uri uri = Uri.parse(image_uri);
                    profileImg.setImageURI(uri);
                } catch (NullPointerException e) {
                    Log.e(TAG, "onCreate: " + e.getMessage());
                }

                if (Integer
                        .parseInt(cur.getString(cur
                                .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    System.out.println("name : " + name + ", ID : " + id);
                    sb.append("\n Contact Name:" + name);
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                    + " = ?", new String[]{id}, null);
                    pCur.moveToFirst();
//                    while (pCur.moveToNext()) {
                    phone = pCur
                            .getString(pCur
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    sb.append("\n Phone number:" + phone);
                    System.out.println("phone" + phone);
                    details.put("Phonenumber", phone);

                    mobile.setText(phone);


//                    }
                    pCur.close();

//                    Cursor emailCur = cr.query(
//                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
//                            null,
//                            ContactsContract.CommonDataKinds.Email.CONTACT_ID
//                                    + " = ?", new String[]{id}, null);
//                    while (emailCur.moveToNext()) {
//                        emailContact = emailCur
//                                .getString(emailCur
//                                        .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
//                        emailType = emailCur
//                                .getString(emailCur
//                                        .getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
//                        sb.append("\nEmail:" + emailContact + "Email type:" + emailType);
//                        System.out.println("Email " + emailContact
//                                + " Email Type : " + emailType);
//
//                    }
//
//                    emailCur.close();
                } else {
                    mobile.setText("");
                }

//                if (image_uri != null) {
//                    System.out.println(Uri.parse(image_uri));
//                    try {
//                        bitmap = MediaStore.Images.Media
//                                .getBitmap(this.getContentResolver(),
//                                        Uri.parse(image_uri));
//                        sb.append("\n Image in Bitmap:" + bitmap);
//                        System.out.println(bitmap);
//
//                    } catch (FileNotFoundException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//
//                }


//                sb.append("\n........................................");
            }

        } else {
            finish();
        }
    }


}
