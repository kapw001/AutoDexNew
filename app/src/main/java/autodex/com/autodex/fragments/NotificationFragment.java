package autodex.com.autodex.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import autodex.com.autodex.R;
import autodex.com.autodex.Utility;
import autodex.com.autodex.activitys.HomeActivity;
import autodex.com.autodex.adapter.GridRecyclerViewAdapter;
import autodex.com.autodex.adapter.NotificationAdapter;
import autodex.com.autodex.adapter.RecyclerItemTouchHelper;
import autodex.com.autodex.interfacecallback.IShowCount;
import autodex.com.autodex.interfacecallback.WebResponse;
import autodex.com.autodex.model.Contact;
import autodex.com.autodex.model.NotificationModel;
import autodex.com.autodex.webrequest.WebUrl;

/**
 * Created by yasar on 11/9/17.
 */

public class NotificationFragment extends BaseFragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    private static final String TAG = "NotificationFragment";
    private List<NotificationModel> list = new ArrayList<>();

    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;

    private RelativeLayout noti;
    private TextView mErrorMsg;

    private IShowCount iShowCount;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        iShowCount = (IShowCount) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_notification, container, false);


        mErrorMsg = (TextView) view.findViewById(R.id.errormsg);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        notificationAdapter = new NotificationAdapter(getActivity(), list);
        noti = (RelativeLayout) view.findViewById(R.id.noti);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(notificationAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        prepareNotificationData();

//        iShowCount.showNotificationCount(HomeActivity.count);


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
//        inflater.inflate(R.menu.notificationonly, menu);
//        Drawable drawable = menu.getItem(0).getIcon();
//        if (drawable != null) {
//            drawable.mutate();
//            drawable.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notification:
//                Toast.makeText(getActivity(), "Noti", Toast.LENGTH_SHORT).show();
                break;


        }
        return true;

    }


    private void prepareNotificationData() {
        list.clear();
//        NotificationModel n = new NotificationModel();
//        n.setImgId(R.drawable.sundarpichai);
//        n.setName("Sundar Pichai");
//        n.setnDate("");
//        n.setnType("First");
//        list.add(n);
//
//        n = new NotificationModel();
//        n.setImgId(R.drawable.cute);
//        n.setName("Cute");
//        n.setnDate("");
//        n.setnType("First");
//        list.add(n);
//
//
//        n = new NotificationModel();
//        n.setImgId(R.drawable.trump);
//        n.setName("It's Donald Trump's Birthday");
//        n.setnDate("12-6-2017");
//        n.setnType("Second");
//        list.add(n);
//
//        n = new NotificationModel();
//        n.setImgId(R.drawable.testimg);
//        n.setName("It's Bill Gates Birthday");
//        n.setnDate("12-6-2017");
//        n.setnType("Second");
//        list.add(n);


        Utility.showProgress(getContext(), "Loading");
        String token = sessionManager.getKeyToken();
        webPostRequest.postGETData(WebUrl.NOTIFICATION_LIST, token, new WebResponse() {
            @Override
            public void onResponse(JSONObject response) {
                Utility.hideProgress();
                Log.e(TAG, "onResponse: " + response);

            }

            @Override
            public void onResponse(String response) {
                Utility.hideProgress();
                Gson gson = new Gson();

                NotificationModel[] lNotificationModels = gson.fromJson(response, NotificationModel[].class);

                if (lNotificationModels.length > 0) {
                    list.addAll(Arrays.asList(lNotificationModels));
                    notificationAdapter.notifyDataSetChanged();
                    mErrorMsg.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    mErrorMsg.setVisibility(View.VISIBLE);
                    mErrorMsg.setTextColor(Color.BLACK);
                    recyclerView.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "There is no notification", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onErrorResponse(VolleyError error) {
                mErrorMsg.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                Utility.hideProgress();
                Utility.showDialog(getActivity(), "Error", "Server error");
                notificationAdapter.notifyDataSetChanged();
            }

            @Override
            public void callRequest() {
                Utility.hideProgress();
            }
        });


//        notificationAdapter.notifyDataSetChanged();
    }


    @Override
    void searchFilter(String s) {

    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof NotificationAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = list.get(viewHolder.getAdapterPosition()).getMessage();

            // backup of removed item for undo purpose
            final NotificationModel deletedItem = list.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
//            notificationAdapter.removeItem(viewHolder.getAdapterPosition());

            String token = sessionManager.getKeyToken();
            Utility.showProgress(getContext(), "Loading");
            webPostRequest.postDelete(WebUrl.NOTIFICATION_READ + "/" + deletedItem.getId(), token, new WebResponse() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e(TAG, "onResponse: Delete " + response);
                    Utility.hideProgress();
                }

                @Override
                public void onResponse(String response) {
                    Log.e(TAG, "onResponse: Delete " + response);
                    Utility.hideProgress();
                    notificationAdapter.removeItem(viewHolder.getAdapterPosition());
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    Utility.hideProgress();
                    Log.e(TAG, "onResponse: Delete " + error.getMessage());
                }

                @Override
                public void callRequest() {

                }
            });

            // showing snack bar with Undo option
//            Snackbar snackbar = Snackbar
//                    .make(noti, name + " removed from notification!", Snackbar.LENGTH_LONG);
//            snackbar.setAction("UNDO", new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    // undo is selected, restore the deleted item
//                    notificationAdapter.restoreItem(deletedItem, deletedIndex);
//                }
//            });
//            snackbar.setActionTextColor(Color.YELLOW);
//            snackbar.show();
        }
    }
}
