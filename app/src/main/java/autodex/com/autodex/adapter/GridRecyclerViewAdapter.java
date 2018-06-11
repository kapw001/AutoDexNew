package autodex.com.autodex.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import autodex.com.autodex.R;
import autodex.com.autodex.UtilFunctionalitys;
import autodex.com.autodex.activitys.ContactDetailsActivity;
import autodex.com.autodex.activitys.UpdateContactActivity;
import autodex.com.autodex.model.Contact;
import volleywebrequest.com.mysqlitelibrary.GetContactListResponse;

/**
 * Created by yasar on 13/9/17.
 */

public class GridRecyclerViewAdapter extends RecyclerView.Adapter<GridRecyclerViewAdapter.ViewHolder> implements Filterable {

    private List<GetContactListResponse> list;
    private List<GetContactListResponse> filteredList;
    private Context mContext;
    protected ItemListener mListener;
    private Activity activity;

    public GridRecyclerViewAdapter(Activity activity, Context context, List<GetContactListResponse> list, ItemListener itemListener) {

        this.list = list;
        this.filteredList = list;
        this.activity = activity;
        mContext = context;
        mListener = itemListener;
    }

    public void updateList(List<GetContactListResponse> mContacts) {
        this.list = new ArrayList<>();
        this.list = new ArrayList<>();
        this.filteredList.addAll(mContacts);
        this.list.addAll(mContacts);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();

                int count = filteredList.size();


                if (constraint != null && constraint.length() > 0) {

                    List<GetContactListResponse> mNlist = new ArrayList<>();

                    for (GetContactListResponse response : filteredList
                            ) {

                        if (response.getFirstName().toLowerCase().contains(constraint.toString().toLowerCase())

                                || response.getLastName().toLowerCase().contains(constraint.toString().toLowerCase())

                                || response.getMiddleName().toLowerCase().contains(constraint.toString().toLowerCase())

                                ) {

                            mNlist.add(response);


                        }
                    }

                    results.count = mNlist.size();
                    results.values = mNlist;


                } else {

                    results.count = count;
                    results.values = filteredList;


                }


                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                list = (List<GetContactListResponse>) results.values;
                notifyDataSetChanged();

            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView, number;
        public ImageView imageView, favorite, menuoption;
        public RelativeLayout relativeLayout;
        GetContactListResponse item;

        public ViewHolder(View v) {

            super(v);

//            v.setOnClickListener(this);
            textView = (TextView) v.findViewById(R.id.textView);
            number = (TextView) v.findViewById(R.id.number);
            imageView = (ImageView) v.findViewById(R.id.imageView);
            menuoption = (ImageView) v.findViewById(R.id.menuoption);
            favorite = (ImageView) v.findViewById(R.id.favorite);
            relativeLayout = (RelativeLayout) v.findViewById(R.id.relativeLayout);


        }

        public void setData(final GetContactListResponse item) {
            this.item = item;

            textView.setText(item.getFirstName() + " " + item.getLastName());
//            imageView.setImageResource(item.getImgId());
//            Picasso.with(mContext).load(item.getImgId()).fit().into(imageView);
            RequestOptions cropOptions = new RequestOptions().centerCrop();
            try {

                if (item.getProfileImage().length() > 0) {
                    Glide.with(mContext).load(item.getProfileImage()).apply(cropOptions).into(imageView);
                } else {
                    imageView.setImageResource(R.drawable.profile);
                }
            } catch (NullPointerException e) {
                imageView.setImageResource(R.drawable.profile);
            }

            number.setText(item.getPhoneNumber());

            if (item.getFavorite()) {
                favorite.setColorFilter(ContextCompat.getColor(mContext, R.color.red), PorterDuff.Mode.SRC_IN);
            } else {
                favorite.setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            }


            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(view.getContext(), "Ok", Toast.LENGTH_SHORT).show();
                    if (item.getFavorite()) {
                        item.setFavorite(false);
//                        favorite.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.red));
                    } else {
                        item.setFavorite(true);
//                        favorite.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.colorPrimary));
                    }
                    if (mListener != null) {
                        mListener.onItemClick(item);
                    }
                    notifyItemChanged(getPosition());

                }
            });

//            relativeLayout.setBackgroundColor(Color.parseColor(item.color));

        }


    }

    @Override
    public GridRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.row_favourites, parent, false);

//        GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) view.getLayoutParams();
//        lp.height = parent.getMeasuredHeight() / 4;
//        view.setLayoutParams(lp);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final GetContactListResponse contact = list.get(position);
        holder.setData(contact);

        holder.menuoption.setVisibility(View.GONE);
//        holder.menuoption.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PopupMenu popup = new PopupMenu(mContext, v);
//                popup.getMenuInflater().inflate(R.menu.favrowmenu, popup.getMenu());
//
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    public boolean onMenuItemClick(MenuItem item) {
//                        if (item.getItemId() == R.id.show) {
//                            Intent intent = new Intent(mContext, ContactDetailsActivity.class);
//                            intent.putExtra("contact", (Serializable) contact);
//                            mContext.startActivity(intent);
//                        } else if (item.getItemId() == R.id.show) {
//                            UtilFunctionalitys.share(mContext, "", "");
//                        }
//                        return true;
//                    }
//                });
//
//                popup.show();
//            }
//        });

        Display display = activity.getWindowManager().getDefaultDisplay();
        ;

        int width = display.getWidth(); // ((display.getWidth()*20)/100)
        int height = ((display.getHeight() * 30) / 120);//display.getHeight();//

        holder.itemView.getLayoutParams().height = height;

//        holder.imageView.setImageResource(R.drawable.cute);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ContactDetailsActivity.class);
//                intent.putExtra("contacturi", uri.toString());
                intent.putExtra("contact", (Serializable) contact);
                intent.putExtra("isFromFav", true);
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {

//        return list.size();
        return (list == null) ? 0 : list.size();
    }

    public interface ItemListener {
        void onItemClick(GetContactListResponse item);
    }
}