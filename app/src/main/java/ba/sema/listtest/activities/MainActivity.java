package ba.sema.listtest.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ba.sema.listtest.App;
import ba.sema.listtest.R;
import ba.sema.listtest.helpers.EmisijeHelper;
import ba.sema.listtest.helpers.PropertiesHelper;
import ba.sema.listtest.helpers.SwipeListAdapter;
import ba.sema.listtest.models.Emisija;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener
{
    private String TAG = MainActivity.class.getSimpleName();
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listViewEmisije;
    private SwipeListAdapter adapter;
    private List<Emisija> listaEmisija;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewEmisije = (ListView) findViewById(R.id.lista_emisija);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.main_swipe_refresh_layout);

        listaEmisija = new ArrayList<>();
        adapter = new SwipeListAdapter(this, listaEmisija);
        listViewEmisije.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run()
                {
                    swipeRefreshLayout.setRefreshing(true);
                    dohvatiPodatke();
                }
        });
    }

    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh()
    {
        dohvatiPodatke();
    }

    /**
     * Fetching json by making http call
     */
    private void dohvatiPodatke()
    {
        // Showing refresh animation before making http call
        swipeRefreshLayout.setRefreshing(true);

        String datum = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String baseUrl = PropertiesHelper.getProperty("api.tv.url.base", getApplicationContext());
        String url = baseUrl + "?startDate=" + datum + "&endDate=" + datum;

        // Volley's json object request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.d(TAG, response.toString());
                        if (response.length() > 0)
                        {
                            EmisijeHelper.HandleEmisijeResponse(response, listaEmisija);
                            adapter.notifyDataSetChanged();
                        }
                        // Stopping swipe refresh
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e(TAG, "Server Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        // Stopping swipe refresh
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                // headers.put("Content-Type", "application/json; charset=UTF-8");
                headers.put("Authorization", PropertiesHelper.getProperty("api.tv.authorization", getApplicationContext()));
                return headers;
            }
        };

        // Adding request to request queue
        App.getInstance().addToRequestQueue(request);
    }
}
