package autodex.com.autodex.interfacecallback;


import com.android.volley.error.VolleyError;

import org.json.JSONObject;

/**
 * Created by yasar on 4/10/17.
 */

public interface WebResponse {

    void onResponse(JSONObject response);

    void onResponse(String response);

    void onErrorResponse(VolleyError error);

    void callRequest();
}
