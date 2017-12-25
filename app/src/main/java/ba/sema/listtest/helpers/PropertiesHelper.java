package ba.sema.listtest.helpers;


import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesHelper
{
    public static String getProperty(String key, Context context)
    {
        Properties properties = new Properties();
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = null;

        try
        {
            inputStream = assetManager.open("app.properties");
            properties.load(inputStream);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }

        return properties.getProperty(key);
    }
}
