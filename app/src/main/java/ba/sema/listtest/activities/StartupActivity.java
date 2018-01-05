package ba.sema.listtest.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ba.sema.listtest.SharedPreferencesManager;


public class StartupActivity extends AppCompatActivity
{
    private SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent activityIntent;

        // Provjera da li je korisnik već prijavljen:
        sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());
        boolean userLoggedIn = sharedPreferencesManager.userLoggedIn();

        if (userLoggedIn)
        {
            activityIntent = new Intent(this, MainActivity.class);
        }
        else
        {
            activityIntent = new Intent(this, LoginActivity.class);
        }
        startActivity(activityIntent);

        finish();
    }
}
