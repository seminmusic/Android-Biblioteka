package ba.sema.biblioteka.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;


public class SharedPreferencesManager
{
    private static final String PREF_NAME = "ba.sema.listtest.shared.prefs";
    private static final int PREF_MODE = MODE_PRIVATE;

    private static final String KEY_USER_LOGGED_IN = "user_logged_in";
    public static final String KEY_USER_BASE64_AUTH = "user_base64_auth";

    private SharedPreferences sharedPreferences;
    private Editor editor;
    private Context context;

    // Constructor:
    public SharedPreferencesManager(Context context)
    {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, PREF_MODE);
        this.editor = sharedPreferences.edit();
    }

    public void saveLoginData(String base64Auth)
    {
        editor.putBoolean(KEY_USER_LOGGED_IN, true);
        editor.putString(KEY_USER_BASE64_AUTH, base64Auth);
        // Commit changes:
        editor.commit();
    }

    public void clearLoginData()
    {
        editor.clear();
        editor.commit();
    }

    public HashMap<String, String> getLoginData()
    {
        HashMap<String, String> loginData = new HashMap<String, String>();
        loginData.put(KEY_USER_BASE64_AUTH, sharedPreferences.getString(KEY_USER_BASE64_AUTH, null));
        return loginData;
    }

    public boolean userLoggedIn()
    {
        return sharedPreferences.getBoolean(KEY_USER_LOGGED_IN, false);
    }
}
