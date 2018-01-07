package ba.sema.biblioteka.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ba.sema.biblioteka.App;
import ba.sema.biblioteka.R;
import ba.sema.biblioteka.helpers.StavkeHelper;
import ba.sema.biblioteka.models.Stavka;
import ba.sema.biblioteka.storage.SharedPreferencesManager;
import ba.sema.biblioteka.fragments.DatePickerFragment;
import ba.sema.biblioteka.helpers.DateHelper;
import ba.sema.biblioteka.helpers.EmisijeHelper;
import ba.sema.biblioteka.helpers.PropertiesHelper;
import ba.sema.biblioteka.adapters.SwipeListAdapter;
import ba.sema.biblioteka.models.Emisija;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, DatePickerFragment.OnDateSetListener
{
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final SimpleDateFormat enDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat bsDateFormat = new SimpleDateFormat("dd.MM.yyyy");

    private SharedPreferencesManager sharedPreferencesManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listViewEmisije;
    private SwipeListAdapter adapter;
    private List<Emisija> listaEmisija;
    private List<Stavka> listaStavki;
    private Toolbar toolbar;
    private DialogFragment datePicker;
    private Date datum;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());

        listViewEmisije = (ListView) findViewById(R.id.lista_emisija);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.main_swipe_refresh_layout);

        listaEmisija = new ArrayList<>();
        listaStavki = StavkeHelper.loadAndMapTestData();
        adapter = new SwipeListAdapter(this, listaEmisija);
        listViewEmisije.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this);

        datePicker = new DatePickerFragment();
        datum = new Date();  // Trenutni datum

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(DateHelper.nazivDanaSaDatumom(datum, bsDateFormat));
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);  // Placing toolbar in place of ActionBar
        getSupportActionBar().setIcon(R.mipmap.ic_biblioteka);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present:
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_open_calendar)
        {
            showDatePickerDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
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

        String datumString = enDateFormat.format(datum);
        String baseUrl = PropertiesHelper.getPropertyValue("api.tv.url.base");  // String baseUrl = BuildConfig.API_BASE_URL;
        String url = baseUrl + "?startDate=" + datumString + "&endDate=" + datumString;

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
                headers.put("Authorization", "Basic " + sharedPreferencesManager.getLoginData().get(SharedPreferencesManager.KEY_USER_BASE64_AUTH));
                return headers;
            }
        };

        // Adding request to request queue
        App.getInstance().addToRequestQueue(request);
    }

    private void showDatePickerDialog()
    {
        datePicker.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DATE, day);

        datum = calendar.getTime();
        toolbar.setTitle(DateHelper.nazivDanaSaDatumom(datum, bsDateFormat));
        Toast.makeText(getApplicationContext(), "Izabrani datum: " + bsDateFormat.format(datum), Toast.LENGTH_LONG).show();
        dohvatiPodatke();
    }
}
