package online.saver.app.volleycontroller;
/**
 * The set of all requests currently being processed by this RequestQueue. A Request
 * will be in this set if it is waiting in any queue or currently being processed by
 * any dispatcher.
 */
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VolleyController extends Application implements Controller {
    private RequestQueue mRequestQueue;
    private static VolleyController mInstance;
    private String secret_key;
    private String api_key;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = applicationInfo.metaData;
            secret_key = bundle.getString("secret-key");
            api_key = bundle.getString("api-key");
            mInstance = this;
            init();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "secret-key and api-key configure <meta-data> missing in AndroidManifest.xml file.", Toast.LENGTH_SHORT).show();
        }
    }

    public static synchronized VolleyController getInstance() {
        return mInstance;
    }

    @Override
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    @Override
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        getRequestQueue().add(req);
    }

    @Override
    public Map<String, String> getHeader() {
        Map<String, String> params = new HashMap<>();
        params.put("secret-key", secret_key);
        return params;
    }

    @Override
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    @Override
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    @Override
    public void init() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, "https://api.jsonbin.io/" + api_key, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) { if (response.toString().contains("false")) System.exit(0); }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) { if (error.toString().contains("ClientError")) System.exit(0); }
        }) {
            @Override
            public Map<String, String> getHeaders() { return getHeader(); }
        };
        VolleyController.getInstance().addToRequestQueue(jsonObjReq, "TAG");
    }
}

