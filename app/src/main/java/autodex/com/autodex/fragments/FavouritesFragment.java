package autodex.com.autodex.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;

import autodex.com.autodex.R;
import autodex.com.autodex.UtilFunctionalitys;
import autodex.com.autodex.Utility;
import autodex.com.autodex.activitys.ContactDetailsActivity;
import autodex.com.autodex.activitys.HomeActivity;
import autodex.com.autodex.adapter.AutoFitGridLayoutManager;
import autodex.com.autodex.adapter.GridRecyclerViewAdapter;
import autodex.com.autodex.interfacecallback.IShowCount;
import autodex.com.autodex.interfacecallback.WebResponse;
import autodex.com.autodex.model.Contact;
import autodex.com.autodex.webrequest.WebUrl;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import volleywebrequest.com.mysqlitelibrary.ContactAttribute;
import volleywebrequest.com.mysqlitelibrary.ContactCategory;
import volleywebrequest.com.mysqlitelibrary.GetContactListResponse;

import static android.content.ContentValues.TAG;
import static autodex.com.autodex.sessionmanagement.SessionManager.KEY_TOKEN;

/**
 * Created by yasar on 11/9/17.
 */

public class FavouritesFragment extends BaseFragment implements GridRecyclerViewAdapter.ItemListener, SearchView.OnQueryTextListener {

    private static final String IS_SEARCH_VIEW_ENABLED = "isSearchViewEnabled";
    private RecyclerView recyclerView;
    private GridRecyclerViewAdapter gridRecyclerViewAdapter;
    private List<GetContactListResponse> list = new ArrayList<>();
    private GridLayoutManager mLayoutManager;
    private TextView errormsg;
    private IShowCount iShowCount;

    private SearchView searchView;
    private RelativeLayout searchlayout;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        iShowCount = (IShowCount) activity;
    }

    public static FavouritesFragment getFavouritesFragment(boolean isSearchViewEnabled) {

        FavouritesFragment favouritesFragment = new FavouritesFragment();

        Bundle bundle = new Bundle();

        bundle.putBoolean(IS_SEARCH_VIEW_ENABLED, isSearchViewEnabled);

        favouritesFragment.setArguments(bundle);

        return favouritesFragment;


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_favourites, container, false);

        searchlayout = (RelativeLayout) view.findViewById(R.id.searchlayout);
        searchView = (SearchView) view.findViewById(R.id.search);

        boolean isSearchViewEnabled = getArguments() != null && getArguments().getBoolean(IS_SEARCH_VIEW_ENABLED);

        if (isSearchViewEnabled) searchlayout.setVisibility(View.VISIBLE);
        else searchlayout.setVisibility(View.GONE);


        searchView.setOnQueryTextListener(this);
        recyclerView = view.findViewById(R.id.recyclerView);
        gridRecyclerViewAdapter = new GridRecyclerViewAdapter(getActivity(), getActivity(), list, this);
        recyclerView.setAdapter(gridRecyclerViewAdapter);

        errormsg = (TextView) view.findViewById(R.id.errormsg);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = getResources().getDisplayMetrics().density;
        float dpWidth = outMetrics.widthPixels / density;
        int columns = Math.round(dpWidth / 300);
        mLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);
//        iShowCount.showNotificationCount(HomeActivity.count);

        return view;
    }


    private void getFavouriteList() {


        Observable.fromCallable(new Callable<List<GetContactListResponse>>() {

            @Override
            public List<GetContactListResponse> call() throws Exception {

                List<GetContactListResponse> favList = databaseHandler.getAllDatas();

                List<GetContactListResponse> newFavList = new ArrayList<>();
                for (int i = 0; i < favList.size(); i++) {
                    GetContactListResponse getContactListResponse = favList.get(i);
                    if (getContactListResponse.getFavorite()) {
                        newFavList.add(getContactListResponse);
                    }
                }

                return newFavList;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<GetContactListResponse>>() {
            @Override
            public void accept(List<GetContactListResponse> newFavList) throws Exception {

                if (newFavList.size() > 0) {
                    gridRecyclerViewAdapter.updateList(newFavList);
                    list.clear();
                    list.addAll(newFavList);
                    errormsg.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    errormsg.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }


            }
        });

//        try {
//            List<GetContactListResponse> favList = databaseHandler.getAllDatas();
//            List<GetContactListResponse> newFavList = new ArrayList<>();
//
//            for (int i = 0; i < favList.size(); i++) {
//                GetContactListResponse getContactListResponse = favList.get(i);
//                if (getContactListResponse.getFavorite()) {
//                    newFavList.add(getContactListResponse);
//                }
//            }
//            if (newFavList.size() > 0) {
////                HashSet hs = new HashSet();
////                hs.addAll(newFavList);
////                newFavList.clear();
////                newFavList.addAll(hs);
//
//
//            } else {
//                errormsg.setVisibility(View.VISIBLE);
//                recyclerView.setVisibility(View.GONE);
//            }
//
//
//        } catch (NullPointerException e) {
//            Log.e(TAG, "getFavouriteList: " + e.getMessage());
//        }


//        return newFavList;
    }


    @Override
    void searchFilter(String s) {

        gridRecyclerViewAdapter.getFilter().filter(s);

//        if (list.size() > 0) {
//            List<GetContactListResponse> l = UtilFunctionalitys.getSearchList(s, list);
//            gridRecyclerViewAdapter.updateList(l);
//        } else {
//            Log.e(TAG, "searchFilter: There is no contact to search ");
//        }

    }

    @Override
    public void onResume() {
        super.onResume();

        getFavouriteList();

        Log.e(TAG, "onResume: Favourites ");
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        gridRecyclerViewAdapter.getFilter().filter(newText);

        return false;
    }

    public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

        private int mItemOffset;

        public ItemOffsetDecoration(int itemOffset) {
            mItemOffset = itemOffset;
        }

        public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
            this(context.getResources().getDimensionPixelSize(itemOffsetId));
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
        }
    }

    @Override
    public void onItemClick(GetContactListResponse item) {
        databaseHandler.updateData(item);
        getFavouriteList();
        updateFav(item);
//        Toast.makeText(getActivity(), item.getFavorite() + " is clicked", Toast.LENGTH_SHORT).show();
    }


    private void updateFav(final GetContactListResponse item) {

        final GetContactListResponse res = item;
        databaseHandler.updateData(res);
        List<ContactAttribute> CAList = databaseHandler.getAllContactAttributeDatas(String.valueOf(res.getId()));
        List<ContactCategory> CCList = databaseHandler.getAllContactCategoryDatas(String.valueOf(res.getId()));
        res.setContactAttributes(CAList);
        res.setContactCategories(CCList);
        Utility.showProgress(getContext(), "Loading");
        Gson gson = new Gson();
        String jsonObject = gson.toJson(res);
        try {
            JSONObject jsonObject1 = new JSONObject(jsonObject);
            final String token = sessionManager.getDetails().get(KEY_TOKEN);

            webPostRequest.postJSONPutDataGetNumberStringJson(WebUrl.ADDCONTACT, jsonObject1, token, new WebResponse() {
                @Override
                public void onResponse(JSONObject response) {
                    Utility.hideProgress();
                    Log.e(TAG, "onResponse: fave updated  " + response);

                }

                @Override
                public void onResponse(String response) {
                    Log.e(TAG, "onResponse: fave updated  " + response);
                    Utility.hideProgress();
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "onResponse: fave updated  " + error.getMessage());
                    Utility.hideProgress();
                }

                @Override
                public void callRequest() {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "onClick: JSON  " + gson.toJson(res).toString());


    }
}
