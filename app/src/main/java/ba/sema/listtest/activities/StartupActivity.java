package ba.sema.listtest.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class StartupActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent activityIntent;

        boolean userLoggedIn = false;  // Provjeriti Shared Preferences
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
