package autodex.com.autodex.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import autodex.com.autodex.R;
import autodex.com.autodex.activitys.ContactDetailsActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import volleywebrequest.com.mysqlitelibrary.GetContactListResponse;

/**
 * Created by yasar on 13/9/17.
 */

public class GroupMoreAdapter extends RecyclerView.Adapter<GroupMoreAdapter.ContactViewHolder> {
    private List<GetContactListResponse> mContacts;
    private Context mContext;

    public GroupMoreAdapter(Context pContext, List<GetContactListResponse> mContacts) {
//        mContacts = ContactsProvider.load(pContext);
        this.mContacts = mContacts;
        this.mContext = pContext;

    }


    public void updateList(List<GetContactListResponse> mContacts) {
        this.mContacts = new ArrayList<>();
        this.mContacts.addAll(mContacts);
        notifyDataSetChanged();
    }


    @Override
    public GroupMoreAdapter.ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View content = LayoutInflater.from(mContext).inflate(R.layout.moregroupcontact_item, null);
        return new GroupMoreAdapter.ContactViewHolder(content);
    }

    @Override
    public void onBindViewHolder(GroupMoreAdapter.ContactViewHolder holder, int position) {
        final GetContactListResponse lContact = getItem(position);
        holder.bind(lContact);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ContactDetailsActivity.class);
//                intent.putExtra("contacturi", uri.toString());
                intent.putExtra("contact", (Serializable) lContact);
                intent.putExtra("isFromFav", false);
                mContext.startActivity(intent);
            }
        });
    }


    private GetContactListResponse getItem(int pPosition) {
        return mContacts.get(pPosition);
    }

    @Override
    public int getItemCount() {
//        return mContacts.size();

        return (mContacts == null) ? 0 : mContacts.size();
    }


    public class ContactViewHolder extends RecyclerView.ViewHolder {
        private final TextView mName;
        private final CircleImageView img_contact;

        public ContactViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.name);
            img_contact = (CircleImageView) itemView.findViewById(R.id.img_contact);

        }

        public void bind(GetContactListResponse pItem) {
            mName.setText(pItem.getFirstName());
            Picasso.with(mContext).load(R.drawable.profile).centerCrop().into(img_contact);
//            if (pItem.getUri() != null) {
//                Picasso.with(mContext).load(pItem.getUri()).fit().into(img_contact);
//            } else {
//                Picasso.with(mContext).load(R.drawable.profile).fit().into(img_contact);
//            }
        }
    }


}