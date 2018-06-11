package autodex.com.autodex.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import autodex.com.autodex.ContactsProvider;
import autodex.com.autodex.R;
import autodex.com.autodex.activitys.ContactDetailsActivity;
import autodex.com.autodex.fragments.ContactsFragment;
import autodex.com.autodex.model.Contact;
import de.hdodenhof.circleimageview.CircleImageView;
import volleywebrequest.com.mysqlitelibrary.GetContactListResponse;

import static android.content.ContentValues.TAG;

/**
 * Created by yasar on 13/9/17.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> implements Filterable {

    private List<GetContactListResponse> mContacts = new ArrayList<>();
    private List<GetContactListResponse> mFilteredContacts = new ArrayList<>();
    private LinkedHashMap<String, Integer> mMapIndex;
    private ArrayList<String> mSectionList;
    private String[] mSections;
    private Context context;
    protected ItemListener mListener;

    public ContactsAdapter(Context pContext, List<GetContactListResponse> mContacts, ItemListener itemListener) {
//        mContacts = ContactsProvider.load(pContext);
        this.mContacts = mContacts;
        this.mFilteredContacts = mContacts;
        this.context = pContext;
        mListener = itemListener;
        fillSections();
    }


    public void updateList(List<GetContactListResponse> mContacts) {
        this.mContacts = new ArrayList<>();
        this.mFilteredContacts = new ArrayList<>();
        this.mFilteredContacts.addAll(mContacts);
        this.mContacts.addAll(mContacts);
        fillSections();

        notifyDataSetChanged();
    }

    public void updateListFiltered(List<GetContactListResponse> mContacts) {
        this.mContacts = new ArrayList<>();
        this.mContacts.addAll(mContacts);
        fillSections();

        notifyDataSetChanged();
    }

    private void fillSections() {
        mMapIndex = new LinkedHashMap<String, Integer>();

        for (int x = 0; x < mContacts.size(); x++) {
            String fruit = mContacts.get(x).getFirstName() != null ? mContacts.get(x).getFirstName() : "";
            if (fruit.length() > 1) {
                String ch = fruit.substring(0, 1);
                ch = ch.toUpperCase();
                if (!mMapIndex.containsKey(ch)) {
                    mMapIndex.put(ch, x);
                }
            }
        }
        Set<String> sectionLetters = mMapIndex.keySet();
        // create a list from the set to sort
        mSectionList = new ArrayList<String>(sectionLetters);
        Collections.sort(mSectionList);

        mSections = new String[mSectionList.size()];
        mSectionList.toArray(mSections);
    }

    @Override
    public ContactsAdapter.ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View content = LayoutInflater.from(context).inflate(R.layout.contact_item, null);
        return new ContactsAdapter.ContactViewHolder(content);
    }

    @Override
    public void onBindViewHolder(ContactsAdapter.ContactViewHolder holder, final int position) {
        final GetContactListResponse lContact = getItem(position);

        Log.e(TAG, "onBindViewHolder: " + lContact.getId());

        String section = getSection(lContact);
        try {
            holder.bind(lContact);
//            holder.bind(lContact, section, mMapIndex.get(section) == position);
        } catch (NullPointerException e) {
            Log.e(TAG, "onBindViewHolder: " + e.getStackTrace());
        }

        if (lContact.getFavorite()) {
            holder.fav.setColorFilter(ContextCompat.getColor(context, R.color.red));
        } else {
            holder.fav.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary));
        }


        //        Drawable d = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_favorites, null);
//        d = DrawableCompat.wrap(d);
//        DrawableCompat.setTint(d, Color.RED);
//        holder.fav.setImageDrawable(d);

        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                    Toast.makeText(view.getContext(), "Ok", Toast.LENGTH_SHORT).show();
                if (lContact.getFavorite()) {
                    lContact.setFavorite(false);
//                        favorite.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.red));
                } else {
                    lContact.setFavorite(true);
//                        favorite.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.colorPrimary));
                }
                if (mListener != null) {
                    mListener.onItemClick(lContact);
                }
                notifyItemChanged(position);

            }
        });

        holder.phoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (lContact.getPhoneNumber().length() > 0) {
                        Uri number = Uri.parse("tel:" + lContact.getPhoneNumber());
                        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                        context.startActivity(callIntent);
                    } else {
                        Toast.makeText(context, "There is no dial number available.", Toast.LENGTH_SHORT).show();
                    }
                } catch (NullPointerException e) {
                    Toast.makeText(context, "There is no dial number available.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        holder.msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lContact.getPhoneNumber() != null && lContact.getPhoneNumber().length() > 0) {
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.setData(Uri.parse("sms:" + lContact.getPhoneNumber()));
//                sendIntent.setData(Uri.parse("sms:"));
                    view.getContext().startActivity(sendIntent);
                } else {
                    Toast.makeText(context, "There is no mobile number to send sms ", Toast.LENGTH_SHORT).show();
                }

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                final Uri uri = ContactsContract.Contacts.getLookupUri(
//                        lContact.getId(),
//                        cursor.getString(ContactsQuery.LOOKUP_KEY));
//
//                final Uri uri = Uri.withAppendedPath(
//                        ContactsContract.Contacts.CONTENT_URI, lContact.getId());

//                Log.e(TAG, "onClick: " + uri.toString());
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
//                intent.putExtra("contacturi", uri.toString());
                intent.putExtra("contact", (Serializable) lContact);
                context.startActivity(intent);
            }
        });
    }

    private String getSection(GetContactListResponse pContact) {

        if (pContact.getFirstName() != null && pContact.getFirstName().length() > 0) {

            return pContact.getFirstName().substring(0, 1).toUpperCase();

        } else if (pContact.getLastName() != null && pContact.getLastName().length() > 0) {

            return pContact.getLastName().substring(0, 1).toUpperCase();

        } else {

            return "";

        }

//        String n = pContact.getFirstName() != null ? pContact.getFirstName() : pContact.getLastName();
//        return n.substring(0, 1).toUpperCase();

    }

    private GetContactListResponse getItem(int pPosition) {
        return mContacts.get(pPosition);
    }

    @Override
    public int getItemCount() {
//        return mContacts.size();

        return (mContacts == null) ? 0 : mContacts.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if (constraint != null && constraint.length() > 0) {

                    List<GetContactListResponse> mNlist = new ArrayList<>();

                    int count = mFilteredContacts.size();

                    for (int i = 0; i < count; i++) {
                        GetContactListResponse response = mFilteredContacts.get(i);

                        if (response.getFirstName().toLowerCase().contains(constraint.toString().toLowerCase())

                                || response.getLastName().toLowerCase().contains(constraint.toString().toLowerCase())

                                || response.getMiddleName().toLowerCase().contains(constraint.toString().toLowerCase())

                                ) {

                            mNlist.add(response);


                        }

                    }

                    results.count = count;
                    results.values = mNlist;

                } else {

                    results.count = mFilteredContacts.size();
                    results.values = mFilteredContacts;

                }


                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {


                mContacts = (List<GetContactListResponse>) results.values;

                notifyDataSetChanged();

//                List<GetContactListResponse> list = (List<GetContactListResponse>) results.values;
//
//                Log.e(TAG, "publishResults: " + list.size());
//
//                updateListFiltered(list);


            }
        };
    }


    public class ContactViewHolder extends RecyclerView.ViewHolder {
        private final TextView mName;
        private final TextView mSectionName;
        private final CircleImageView img_contact;
        private final ImageView phoneCall, fav, msg;

        public ContactViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.name);
            mSectionName = (TextView) itemView.findViewById(R.id.section_title);
            img_contact = (CircleImageView) itemView.findViewById(R.id.img_contact);
            phoneCall = (ImageView) itemView.findViewById(R.id.phonecall);
            fav = (ImageView) itemView.findViewById(R.id.favorite);
            msg = (ImageView) itemView.findViewById(R.id.msg);

        }

        public void bind(GetContactListResponse pItem) {

            Log.e(TAG, "bind: firstname " + pItem.getFirstName() + pItem.getLastName() + pItem.getMiddleName());

            mName.setText(pItem.getFirstName() + " " + pItem.getMiddleName() + " " + pItem.getLastName());
//            mSectionName.setText(pSection);
            mSectionName.setVisibility(View.GONE);

//            String s = getPhoneNumber(pItem.getId()) + "";

//            Log.e(TAG, "bind: phone Number " + s);


            if (pItem.getProfileImage().length() > 0) {
                Glide.with(context).load(Uri.parse(pItem.getProfileImage())).into(img_contact);
            } else {
                Glide.with(context).load(R.drawable.profile).into(img_contact);
            }

        }
    }


//    public InputStream openPhoto(Context context, long contactId) {
//        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
//        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
//        Cursor cursor = context.getContentResolver().query(photoUri,
//                new String[]{ContactsContract.Contacts.Photo.PHOTO}, null, null, null);
//        if (cursor == null) {
//            return null;
//        }
//        try {
//            if (cursor.moveToFirst()) {
//                byte[] data = cursor.getBlob(0);
//                if (data != null) {
//                    return new ByteArrayInputStream(data);
//                }
//            }
//        } finally {
//            cursor.close();
//            return null;
//        }
//
//    }
//
//    public Uri getPhotoUri(String id) {
//        try {
//            Cursor cur = this.context.getContentResolver().query(
//                    ContactsContract.Data.CONTENT_URI,
//                    null,
//                    ContactsContract.Data.CONTACT_ID + "=" + id + " AND "
//                            + ContactsContract.Data.MIMETYPE + "='"
//                            + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'", null,
//                    null);
//            if (cur != null) {
//                if (!cur.moveToFirst()) {
//                    return null; // no photo
//                }
//            } else {
//                return null; // error in cursor process
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//        Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long
//                .parseLong(id));
//        return Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
//    }
//
//    private String getPhoneNumber(String id) {
//
//        String phone = null;
//        Cursor cursor = context.getContentResolver().query(
//                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                null,
//                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
//                new String[]{id}, null);
//
//        while (cursor.moveToNext()) {
//            phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//        }
//
//        cursor.close();
//
//        return phone;
//    }

    public interface ItemListener {
        void onItemClick(GetContactListResponse item);
    }
}