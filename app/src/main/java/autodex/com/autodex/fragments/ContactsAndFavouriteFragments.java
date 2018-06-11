package autodex.com.autodex.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import autodex.com.autodex.R;
import autodex.com.autodex.activitys.HomeActivity;
import autodex.com.autodex.interfacecallback.IShowCount;
import autodex.com.autodex.viewpageadapter.ViewPagerAdapter;

import static android.content.ContentValues.TAG;

/**
 * Created by yasar on 11/9/17.
 */

public class ContactsAndFavouriteFragments extends BaseFragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.ic_tabfavourites,
            R.drawable.ic_tabusericon

    };

    private SearchView searchView;
    private ViewPagerAdapter adapter;
    private IShowCount iShowCount;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        iShowCount = (IShowCount) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_contactandfavourite, container, false);
        searchView = (SearchView) view.findViewById(R.id.search);
//        searchView.setBackgroundColor(Color.WHITE);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        tabLayout.addOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        int tabIconColor = ContextCompat.getColor(getContext(), R.color.white);
                        tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
//                        tab.getIcon().setAlpha(1);

                        Fragment fragment = adapter.getItem(viewPager.getCurrentItem());
                        if (fragment instanceof FavouritesFragment) {
                            ((FavouritesFragment) fragment).onResume();
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);
                        int tabIconColor = ContextCompat.getColor(getContext(), R.color.cardbackgroundcolor);
                        tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }
                }
        );


        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Log.e(TAG, "onFocusChange: " + b);
            }
        });

        searchView.setOnQueryTextListener(searchQueryListener);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
////                Log.e(TAG, "onQueryTextSubmit: " + query);
////
////                Fragment fragment = adapter.getItem(viewPager.getCurrentItem());
//                if (fragment instanceof ContactsFragment) {
//                    ((ContactsFragment) fragment).searchFilter(query);
//                }
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                Log.e(TAG, "onQueryTextChange: " + newText);
//                Fragment fragment = adapter.getItem(viewPager.getCurrentItem());
//                if (fragment instanceof ContactsFragment) {
//                    ((ContactsFragment) fragment).searchFilter(newText);
//                }
//                return true;
//            }
//        });


//        iShowCount.showNotificationCount(HomeActivity.count);

        return view;
    }

    private SearchView.OnQueryTextListener searchQueryListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            Log.e(TAG, "onQueryTextChange: " + newText);

            BaseFragment fragment = (BaseFragment) adapter.getItem(viewPager.getCurrentItem());
            fragment.searchFilter(newText);


//            Toast.makeText(getContext(), "" + newText, Toast.LENGTH_SHORT).show();

            return false;
        }
    };

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(0).getIcon().setColorFilter(ContextCompat.getColor(getContext(), R.color.cardbackgroundcolor), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(ContextCompat.getColor(getContext(), R.color.white), PorterDuff.Mode.SRC_IN);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new FavouritesFragment(), "Favourites");
//        adapter.addFragment(new ContactsFragment(), "Contacts");
        adapter.addFragment(new ContactsFragment(), "Contacts");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(2);
    }

    @Override
    void searchFilter(String s) {

    }

    public void callDB() {
        Fragment fragment = adapter.getItem(viewPager.getCurrentItem());
        if (fragment instanceof ContactsFragment) {
            ((ContactsFragment) fragment).callDB();
        }
    }

    public void refreshFavourite() {

    }
}
