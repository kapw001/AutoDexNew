package autodex.com.autodex.webrequest;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.ParseError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import autodex.com.autodex.interfacecallback.WebResponse;
import autodex.com.autodex.volleysingleton.VolleyController;

import static android.content.ContentValues.TAG;

/**
 * Created by yasar on 4/10/17.
 */

public class WebPostRequest {

    private Context context;

    private VolleyController volleyController;

    private static WebPostRequest webPostRequest;

    private WebPostRequest(Context context) {
        this.context = context;
        this.volleyController = VolleyController.getInstance(context);

    }

    public static synchronized WebPostRequest getInstance(Context context) {
        if (webPostRequest == null) {
            webPostRequest = new WebPostRequest(context);
        }

        return webPostRequest;
    }


    public void postData(String url, final Map<String, String> params, final String token, final WebResponse webResponse) {
        // Tag used to cancel the request
        String tag_string_req = "string_req";


        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                webResponse.onResponse(response);
                pDialog.hide();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                webResponse.onErrorResponse(error);
                pDialog.hide();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("token", token);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        strReq.setShouldCache(false);
// Adding request to request queue
        volleyController.addToRequestQueue(strReq, tag_string_req);
    }

    public void postGETData(String url, final String token, final WebResponse webResponse) {
        // Tag used to cancel the request
        String tag_string_req = "string_req";

        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                webResponse.onResponse(response);


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                webResponse.onErrorResponse(error);

            }
        })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("token", token);
                return headers;
            }
        };

        strReq.setShouldCache(false);
// Adding request to request queue
        volleyController.addToRequestQueue(strReq, tag_string_req);
    }

    public void postGETDataOTP(String url, final String token, final WebResponse webResponse) {
        // Tag used to cancel the request
        String tag_string_req = "string_req";

        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                webResponse.onResponse(response);


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                webResponse.onErrorResponse(error);

            }
        })

        {

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    JSONObject jsonResponse = new JSONObject(jsonString);
                    jsonResponse.put("headers", new JSONObject(response.headers));

                    return Response.success(jsonResponse.toString(),
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("token", token);
                return headers;
            }
        };

        strReq.setShouldCache(false);
// Adding request to request queue
        volleyController.addToRequestQueue(strReq, tag_string_req);
    }

    public void postJSONData(String url, final Map<Object, Object> params, final String token, final WebResponse webResponse) {
        // Tag used to cancel the request
        String tag_string_req = "string_req";

        Log.e(TAG, "postJSONData: " + params.toString());


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        webResponse.onResponse(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

                webResponse.onErrorResponse(error);
            }
        }) {

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    JSONObject jsonResponse = new JSONObject(jsonString);
                    jsonResponse.put("headers", new JSONObject(response.headers));

                    return Response.success(jsonResponse,
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("token", token);
                return headers;
            }

//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                return params;
//            }
        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjReq.setShouldCache(false);
// Adding request to request queue
        volleyController.addToRequestQueue(jsonObjReq, tag_string_req);
    }

    public void postJSONDataPUTMethod(String url, final Map<Object, Object> params, final String token, final WebResponse webResponse) {
        // Tag used to cancel the request
        String tag_string_req = "string_req";

        Log.e(TAG, "postJSONData: " + params.toString());


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                url, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        webResponse.onResponse(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

                webResponse.onErrorResponse(error);
            }
        }) {

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    JSONObject jsonResponse = new JSONObject(jsonString);
                    jsonResponse.put("headers", new JSONObject(response.headers));

                    return Response.success(jsonResponse,
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("token", token);
                return headers;
            }

//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                return params;
//            }
        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjReq.setShouldCache(false);
// Adding request to request queue
        volleyController.addToRequestQueue(jsonObjReq, tag_string_req);
    }


    public void postJSONDataGetNumber(String url, final Map<Object, Object> params, final String token, final WebResponse webResponse) {
        // Tag used to cancel the request
        String tag_string_req = "string_req";

        Log.e(TAG, "postJSONData: " + params.toString());


        JSONtoString jsonObjReq = new JSONtoString(Request.Method.POST,
                url, new JSONObject(params),
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());

                        webResponse.onResponse(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

                webResponse.onErrorResponse(error);
            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("token", token);
                return headers;
            }

        };

//        jsonObjReq..setRetryPolicy(new DefaultRetryPolicy(0,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
// Adding request to request queue
        jsonObjReq.setShouldCache(false);
        volleyController.addToRequestQueue(jsonObjReq, tag_string_req);
    }

    public void postJSONDataGetNumber1(String url, final Map<Object, Object> params, final String token, final WebResponse webResponse) {
        // Tag used to cancel the request
        String tag_string_req = "string_req";

        Log.e(TAG, "postJSONData: " + params.toString());


        JSONtoString jsonObjReq = new JSONtoString(Request.Method.POST,
                url, new JSONObject(params),
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());

                        webResponse.onResponse(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

                webResponse.onErrorResponse(error);
            }
        }) {

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    JSONObject jsonResponse = new JSONObject(jsonString);
                    jsonResponse.put("headers", new JSONObject(response.headers));

                    return Response.success(jsonResponse.toString(),
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("token", token);
                return headers;
            }

        };

//        jsonObjReq..setRetryPolicy(new DefaultRetryPolicy(0,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
// Adding request to request queue
        jsonObjReq.setShouldCache(false);
        volleyController.addToRequestQueue(jsonObjReq, tag_string_req);
    }


    public void postJSONPutDataGetNumber(String url, final Map<Object, Object> params, final String token, final WebResponse webResponse) {
        // Tag used to cancel the request
        String tag_string_req = "string_req";

        Log.e(TAG, "postJSONData: " + params.toString());


        JSONtoString jsonObjReq = new JSONtoString(Request.Method.PUT,
                url, new JSONObject(params),
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());

                        webResponse.onResponse(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

                webResponse.onErrorResponse(error);
            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("token", token);
                return headers;
            }

        };

        jsonObjReq.setShouldCache(false);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
// Adding request to request queue
        volleyController.addToRequestQueue(jsonObjReq, tag_string_req);
    }


    public void test() {

        StringRequest request = new StringRequest(Request.Method.PUT, "", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                JSONObject jsonObject = new JSONObject();

                Map<String, String> params = new HashMap<String, String>();
                params.put("message", jsonObject.toString());

                return params;
            }
        };

        volleyController.addToRequestQueue(request);
    }

    public void postJSONChangeNumber(String url, JSONObject params, final String token, final WebResponse webResponse) {
        // Tag used to cancel the request
        String tag_string_req = "string_req";

        Log.e(TAG, "postJSONData: " + params.toString());


        JSONtoString jsonObjReq = new JSONtoString(Request.Method.POST,
                url, params,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());

                        webResponse.onResponse(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

                webResponse.onErrorResponse(error);
            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("token", token);
                return headers;
            }

        };

        jsonObjReq.setShouldCache(false);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
// Adding request to request queue
        volleyController.addToRequestQueue(jsonObjReq, tag_string_req);
    }


    public void postJSONPutDataGetNumberStringJson(String url, JSONObject params, final String token, final WebResponse webResponse) {
        // Tag used to cancel the request
        String tag_string_req = "string_req";

        Log.e(TAG, "postJSONData: " + params.toString());


        JSONtoString jsonObjReq = new JSONtoString(Request.Method.PUT,
                url, params,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());

                        webResponse.onResponse(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

                webResponse.onErrorResponse(error);
            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("token", token);
                return headers;
            }

        };

        jsonObjReq.setShouldCache(false);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
// Adding request to request queue
        volleyController.addToRequestQueue(jsonObjReq, tag_string_req);
    }

    public void postJSONPutDataGetNumberStringJsonArray(String url, JSONArray params, final String token, final WebResponse webResponse) {
        // Tag used to cancel the request
        String tag_string_req = "string_req";

        Log.e(TAG, "postJSONData: " + params.toString());


        JSONArraytoString jsonObjReq = new JSONArraytoString(Request.Method.PUT,
                url, params,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());

                        webResponse.onResponse(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

                webResponse.onErrorResponse(error);
            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("token", token);
                return headers;
            }

        };

        jsonObjReq.setShouldCache(false);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
// Adding request to request queue
        volleyController.addToRequestQueue(jsonObjReq, tag_string_req);
    }


    public void postDelete(String url, final String token, final WebResponse webResponse) {
        // Tag used to cancel the request
        String tag_string_req = "string_req";


        JSONArraytoString jsonObjReq = new JSONArraytoString(Request.Method.DELETE,
                url, null,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());

                        webResponse.onResponse(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

                webResponse.onErrorResponse(error);
            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("token", token);
                return headers;
            }

        };

        jsonObjReq.setShouldCache(false);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
// Adding request to request queue
        volleyController.addToRequestQueue(jsonObjReq, tag_string_req);
    }


    public void fileUpload(String BASE_URL, final String imagePath, final String token, final WebResponse webResponse) {
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "onResponse: file upload response " + response);
                        webResponse.onResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error.getMessage());
                webResponse.onErrorResponse(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "multipart/form-data");
                headers.put("token", token);
                return headers;
            }

        };

        smr.setShouldCache(false);
        smr.addFile("file", imagePath);
        volleyController.addToRequestQueue(smr);
    }

}
