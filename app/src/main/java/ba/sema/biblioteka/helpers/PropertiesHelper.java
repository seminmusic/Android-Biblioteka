package ba.sema.biblioteka.helpers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import ba.sema.biblioteka.App;


public class PropertiesHelper
{
    private static Properties properties;
    static
    {
        try
        {
            properties = new Properties();
            properties.load(App.getContext().getAssets().open("app.properties"));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static String getPropertyValue(String key)
    {
        return properties.getProperty(key, null);
    }
}
