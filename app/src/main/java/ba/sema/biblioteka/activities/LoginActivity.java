package ba.sema.biblioteka.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ba.sema.biblioteka.App;
import ba.sema.biblioteka.R;
import ba.sema.biblioteka.storage.SharedPreferencesManager;
import ba.sema.biblioteka.helpers.Base64Helper;
import ba.sema.biblioteka.helpers.PropertiesHelper;


public class LoginActivity extends AppCompatActivity
{
    private static final String TAG = LoginActivity.class.getSimpleName();

    private EditText inputUsername;
    private EditText inputPassword;
    private Button btnLogin;
    private UserLoginTask userLoginTask = null;
    private View loginFormView;
    private View progressView;
    private SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());

        inputUsername = (EditText) findViewById(R.id.login_username);
        inputPassword = (EditText) findViewById(R.id.login_password);
        btnLogin = (Button) findViewById(R.id.login_button);

        loginFormView = findViewById(R.id.login_form);
        progressView = findViewById(R.id.login_progress);

        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                login();
            }
        });
    }

    private void login()
    {
        if (userLoginTask != null)
        {
            return;
        }

        String username = inputUsername.getText().toString();
        String password = inputPassword.getText().toString();

        boolean cancelLogin = false;
        View focusView = null;
        String poruka = null;

        if (TextUtils.isEmpty(username))
        {
            // inputUsername.setError("Unesite korisničko ime");
            poruka = "Unesite korisničko ime";
            focusView = inputUsername;
            cancelLogin = true;
        }
        else if (TextUtils.isEmpty(password))
        {
            // inputPassword.setError("Unesite lozinku");
            poruka = "Unesite lozinku";
            focusView = inputPassword;
            cancelLogin = true;
        }

        if (cancelLogin)
        {
            // Error in login:
            Toast.makeText(getApplicationContext(), poruka, Toast.LENGTH_SHORT).show();
            focusView.requestFocus();
        }
        else
        {
            // Show progress spinner, and start background task to login:
            showProgress(true);
            userLoginTask = new UserLoginTask(username, password);
            userLoginTask.execute((Void) null);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show)
    {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in the progress spinner:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
        {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            loginFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        }
        // The ViewPropertyAnimator APIs are not available, so simply show and hide the relevant UI components:
        else
        {
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean>
    {
        private final String username;
        private final String password;
        private final String basicAuthBase64;

        public UserLoginTask(String username, String password)
        {
            this.username = username;
            this.password = password;
            this.basicAuthBase64 = Base64Helper.encodeToString(this.username + ":" + this.password);
        }

        @Override
        protected Boolean doInBackground(Void... voids)
        {
            // Authentication:
            String baseUrl = PropertiesHelper.getPropertyValue("api.tv.url.base");
            String url = baseUrl + "?startDate=2000-01-01";  // TODO: URL za autentifikaciju

            final boolean[] result = new boolean[1];
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.d(TAG, response.toString());
                        result[0] = true;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e(TAG, "Server Error: " + error.getMessage());
                        result[0] = false;
                    }
                }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    // headers.put("Content-Type", "application/json; charset=UTF-8");
                    headers.put("Authorization", "Basic " + basicAuthBase64);
                    return headers;
                }
            };
            // Adding request to request queue
            App.getInstance().addToRequestQueue(request);

            // Loading još malo:
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {

            }

            return result[0];
        }

        @Override
        protected void onPostExecute(final Boolean success)
        {
            userLoginTask = null;

            if (success)
            {
                progressView.setVisibility(View.GONE);
                loginFormView.setVisibility(View.GONE);

                // Login success - save login data and move to main activity:
                sharedPreferencesManager.saveLoginData(basicAuthBase64);
                //
                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
            else
            {
                // Login failure:
                showProgress(false);
                Toast.makeText(getApplicationContext(), "Greška prilikom prijave!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled()
        {
            userLoginTask = null;
            showProgress(false);
        }
    }
}
