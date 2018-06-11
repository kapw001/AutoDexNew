package autodex.com.autodex.webrequest;


import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.request.JsonRequest;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by yasar on 24/10/17.
 */

public class JSONArraytoString extends JsonRequest<String> {

    /**
     * Creates a new request.
     * @param method the HTTP method to use
     * @param url URL to fetch the JSON from
     * @param jsonRequest A {@link JSONObject} to post with the request. Null is allowed and
     *   indicates no parameters will be posted along with request.
     * @param listener Listener to receive the JSON response
     * @param errorListener Error listener, or null to ignore errors.
     */
    public JSONArraytoString(int method, String url, JSONArray jsonRequest,
                             Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener,
                errorListener);

    }

    /**
     * Constructor which defaults to <code>GET</code> if <code>jsonRequest</code> is
     * <code>null</code>, <code>POST</code> otherwise.
     *
     * @see #JSONArraytoString(String, JSONArray, Response.Listener, Response.ErrorListener)
     */
    public JSONArraytoString(String url, JSONArray jsonRequest, Response.Listener<String> listener,
                             Response.ErrorListener errorListener) {
        this(jsonRequest == null ? Method.GET : Method.POST, url, jsonRequest,
                listener, errorListener);
    }


    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }
}