package autodex.com.autodex.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.android.volley.error.VolleyError;

import org.json.JSONObject;

import autodex.com.autodex.Utility;
import autodex.com.autodex.activitys.BaseActivity;
import autodex.com.autodex.database.AppDatabase;
import autodex.com.autodex.interfacecallback.IShowCount;
import autodex.com.autodex.interfacecallback.WebResponse;
import autodex.com.autodex.sessionmanagement.SessionManager;
import autodex.com.autodex.webrequest.WebPostRequest;
import volleywebrequest.com.mysqlitelibrary.DatabaseHandler;

/**
 * Created by yasar on 11/9/17.
 */

public abstract class BaseFragment extends Fragment implements WebResponse {

    public DatabaseHandler databaseHandler;
    public WebPostRequest webPostRequest;
    public SessionManager sessionManager;
    public AppDatabase appDatabase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHandler = DatabaseHandler.getInstace(getActivity());
        webPostRequest = WebPostRequest.getInstance(getActivity());
        sessionManager = SessionManager.getInstance(getActivity());
        appDatabase = AppDatabase.getAppDatabase(getActivity());
    }

    @Override
    public void onResponse(JSONObject response) {
        Utility.hideProgress();

    }

    @Override
    public void onResponse(String response) {
        Utility.hideProgress();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Utility.hideProgress();
    }

    @Override
    public void callRequest() {
        Utility.showProgress(getActivity(), "Loading");
    }

    abstract void searchFilter(final String s);

    public IShowCount iShowCount;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        iShowCount = (IShowCount) activity;
    }

}
