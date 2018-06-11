package autodex.com.autodex.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import autodex.com.autodex.R;
import autodex.com.autodex.activitys.ContactDetailsActivity;
import autodex.com.autodex.model.Contact;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

/**
 * Created by yasar on 13/9/17.
 */

public class ContactsSearchAdapter extends RecyclerView.Adapter<ContactsSearchAdapter.ContactViewHolder> {
    private List<Contact> mContacts;
    private LinkedHashMap<String, Integer> mMapIndex;
    private ArrayList<String> mSectionList;
    private String[] mSections;
    private Context context;

    public ContactsSearchAdapter(Context pContext, List<Contact> mContacts) {
//        mContacts = ContactsProvider.load(pContext);
        this.mContacts = mContacts;
        this.context = pContext;

    }


    public void updateList(List<Contact> mContacts) {
        this.mContacts = new ArrayList<>();
        this.mContacts.addAll(mContacts);
        notifyDataSetChanged();
    }


    @Override
    public ContactsSearchAdapter.ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View content = LayoutInflater.from(context).inflate(R.layout.contact_item, null);
        return new ContactsSearchAdapter.ContactViewHolder(content);
    }

    @Override
    public void onBindViewHolder(ContactsSearchAdapter.ContactViewHolder holder, int position) {
        final Contact lContact = getItem(position);
        String section = getSection(lContact);
        try {
            holder.bind(lContact, section, mMapIndex.get(section) == position);


        } catch (NullPointerException e) {
            Log.e(TAG, "onBindViewHolder: " + e.getStackTrace());
        }

        holder.mSectionName.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                final Uri uri = ContactsContract.Contacts.getLookupUri(
//                        lContact.getId(),
//                        cursor.getString(ContactsQuery.LOOKUP_KEY));

                final Uri uri = Uri.withAppendedPath(
                        ContactsContract.Contacts.CONTENT_URI, lContact.getId());

                Log.e(TAG, "onClick: " + uri.toString());
//
//                Intent intent = new Intent(Intent.ACTION_EDIT, uri);
//
//                // Because of an issue in Android 4.0 (API level 14), clicking Done or Back in the
//                // People app doesn't return the user to your app; instead, it displays the People
//                // app's contact list. A workaround, introduced in Android 4.0.3 (API level 15) is
//                // to set a special flag in the extended data for the Intent you send to the People
//                // app. The issue is does not appear in versions prior to Android 4.0. You can use
//                // the flag with any version of the People app; if the workaround isn't needed,
//                // the flag is ignored.
//                intent.putExtra("finishActivityOnSaveCompleted", true);

                // Start the edit activity

                Intent intent = new Intent(context, ContactDetailsActivity.class);
                intent.putExtra("contacturi", uri.toString());
                intent.putExtra("contact", (Serializable) lContact);
                context.startActivity(intent);
            }
        });
    }

    private String getSection(Contact pContact) {
        return pContact.getName() != null ? pContact.getName().substring(0, 1).toUpperCase() : "";

    }

    private Contact getItem(int pPosition) {
        return mContacts.get(pPosition);
    }

    @Override
    public int getItemCount() {
//        return mContacts.size();

        return (mContacts == null) ? 0 : mContacts.size();
    }


    public class ContactViewHolder extends RecyclerView.ViewHolder {
        private final TextView mName;
        private final TextView mSectionName;
        private final CircleImageView img_contact;

        public ContactViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.name);
            mSectionName = (TextView) itemView.findViewById(R.id.section_title);
            img_contact = (CircleImageView) itemView.findViewById(R.id.img_contact);

        }

        public void bind(Contact pItem, String pSection, boolean bShowSection) {
            mName.setText(pItem.getName());
            mSectionName.setText(pSection);
            mSectionName.setVisibility(bShowSection ? View.VISIBLE : View.GONE);

            String s = getPhoneNumber(pItem.getId()) + "";

            Log.e(TAG, "bind: phone Number " + s);


            if (pItem.getUri() != null) {
                Glide.with(context).load(Uri.parse(pItem.getUri())).into(img_contact);
            } else {
                Glide.with(context).load(R.drawable.profile).into(img_contact);
            }


        }
    }


    public InputStream openPhoto(Context context, long contactId) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[]{ContactsContract.Contacts.Photo.PHOTO}, null, null, null);
        if (cursor == null) {
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                byte[] data = cursor.getBlob(0);
                if (data != null) {
                    return new ByteArrayInputStream(data);
                }
            }
        } finally {
            cursor.close();
            return null;
        }

    }

    public Uri getPhotoUri(String id) {
        try {
            Cursor cur = this.context.getContentResolver().query(
                    ContactsContract.Data.CONTENT_URI,
                    null,
                    ContactsContract.Data.CONTACT_ID + "=" + id + " AND "
                            + ContactsContract.Data.MIMETYPE + "='"
                            + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'", null,
                    null);
            if (cur != null) {
                if (!cur.moveToFirst()) {
                    return null; // no photo
                }
            } else {
                return null; // error in cursor process
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long
                .parseLong(id));
        return Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
    }

    private String getPhoneNumber(String id) {

        String phone = null;
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                new String[]{id}, null);

        while (cursor.moveToNext()) {
            phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }

        cursor.close();

        return phone;
    }
}