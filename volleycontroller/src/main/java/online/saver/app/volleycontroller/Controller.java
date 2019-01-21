package online.saver.app.volleycontroller;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import java.util.Map;

public interface Controller {
    RequestQueue getRequestQueue();
    <T> void addToRequestQueue(Request<T> req, String tag);
    Map<String, String> getHeader();
    <T> void addToRequestQueue(Request<T> req);
    void cancelPendingRequests(Object tag);
    void init();
}
