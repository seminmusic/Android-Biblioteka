package ba.sema.listtest;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


/**
 * Handles the initialization of the App, contains static method reusable for entire App
 */
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

    public static App getInstance()
    {
        return appInstance;
    }

    public static Context getContext()
    {
        return appInstance.getApplicationContext();
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
