package autodex.com.autodex.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import autodex.com.autodex.R;
import autodex.com.autodex.Util;
import autodex.com.autodex.activitys.ContactDetailsActivity;
import autodex.com.autodex.activitys.GroupMoreActivity;
import autodex.com.autodex.activitys.GroupMoreLikeFavActivity;
import autodex.com.autodex.model.SectionDataModel;
import volleywebrequest.com.mysqlitelibrary.GetContactListResponse;

import static android.content.ContentValues.TAG;

/**
 * Created by yasar on 27/9/17.
 */

public class RecyclerViewDataAdapter extends RecyclerView.Adapter<RecyclerViewDataAdapter.ItemRowHolder> {

    private ArrayList<SectionDataModel> dataList;
    private Context mContext;
    private Gson gson = new Gson();

    public RecyclerViewDataAdapter(Context context, ArrayList<SectionDataModel> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_itemgroup, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder, int i) {

        final String sectionName = Util.toTitleCase(dataList.get(i).getHeaderTitle());

        final List singleSectionItems = dataList.get(i).getAllItemsInSection();
        final List temp = dataList.get(i).getTemp();

//        Log.e(TAG, "onBindViewHolder: " + singleSectionItems.size() + "     " + temp.size());

//        if (sectionName.toLowerCase().equalsIgnoreCase("UNCATEGORIZED CONTACTS".toLowerCase())) {
//
//            itemRowHolder.itemTitle.setText(Util.toTitleCase("MORE CONTACTS"));
//
//        } else if (sectionName.toLowerCase().equalsIgnoreCase("PersonalProfessional CONTACTS".toLowerCase())) {
//
//            itemRowHolder.itemTitle.setText(Util.toTitleCase("PERSONAL & PROFESSIONAL CONTACTS"));
//
//        } else {

            itemRowHolder.itemTitle.setText(Util.toTitleCase(sectionName));

//        }

        SectionListDataAdapter itemListDataAdapter = new SectionListDataAdapter(mContext, temp);

        itemRowHolder.recycler_view_list.setHasFixedSize(true);
        itemRowHolder.recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        itemRowHolder.recycler_view_list.setAdapter(itemListDataAdapter);


        itemRowHolder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(v.getContext(), "click event on more, "+sectionName , Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(v.getContext(), GroupMoreLikeFavActivity.class);
                intent.putExtra("title", sectionName);
                intent.putExtra("getlist", sectionName);
                v.getContext().startActivity(intent);


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
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView itemTitle;

        protected RecyclerView recycler_view_list;

        protected TextView btnMore;


        public ItemRowHolder(View view) {
            super(view);

            this.itemTitle = (TextView) view.findViewById(R.id.itemTitle);
            this.recycler_view_list = (RecyclerView) view.findViewById(R.id.recycler_view_list);
            this.btnMore = (TextView) view.findViewById(R.id.btnMore);


        }

    }

}