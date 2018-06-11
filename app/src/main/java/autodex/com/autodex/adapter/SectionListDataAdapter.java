package autodex.com.autodex.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import autodex.com.autodex.R;
import autodex.com.autodex.activitys.ContactDetailsActivity;
import autodex.com.autodex.model.Contact;
import autodex.com.autodex.model.SingleItemModel;
import volleywebrequest.com.mysqlitelibrary.GetContactListResponse;

/**
 * Created by yasar on 27/9/17.
 */

public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder> {

    private List<GetContactListResponse> itemsList;
    private Context mContext;

    public SectionListDataAdapter(Context context, List<GetContactListResponse> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_single_cardgroup, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {

        final GetContactListResponse contact = itemsList.get(i);

        holder.tvTitle.setText(contact.getFirstName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ContactDetailsActivity.class);
//                intent.putExtra("contacturi", uri.toString());
                intent.putExtra("contact", (Serializable) contact);
                intent.putExtra("isFromFav", false);
                mContext.startActivity(intent);
            }
        });


       /* Glide.with(mContext)
                .load(feedItem.getImageURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.bg)
                .into(feedListRowHolder.thumbView);*/
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView tvTitle;

        protected ImageView itemImage;


        public SingleItemRowHolder(View view) {
            super(view);

            this.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);

//
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//
//                    Toast.makeText(v.getContext(), tvTitle.getText(), Toast.LENGTH_SHORT).show();
//
//                }
//            });


        }

    }

}