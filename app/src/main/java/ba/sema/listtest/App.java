package ba.sema.listtest;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class App extends Application
{
    public static final String TAG = App.class.getSimpleName();
    private static App appInstance;
    private RequestQueue volleyRequestQueue;

    @Override
    public void onCreate()
    {
        super.onCreate();
        appInstance = this;
    }

    public static synchronized App getInstance()
    {
        return appInstance;
    }

    public RequestQueue getRequestQueue()
    {
        if (volleyRequestQueue == null)
        {
            volleyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return volleyRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request)
    {
        request.setTag(TAG);
        getRequestQueue().add(request);
    }

    public void cancelPendingRequests(Object tag)
    {
        if (volleyRequestQueue != null)
        {
            volleyRequestQueue.cancelAll(tag);
        }
    }
}
