package ba.sema.biblioteka.helpers;

import java.io.IOException;
import java.io.InputStream;

import ba.sema.biblioteka.App;


public class JSONHelper
{
    public static String loadJSONFromAsset(String fileName)
    {
        String json;
        try
        {
            InputStream is = App.getContext().getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            int no_bytes_read = is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }
        catch (IOException ex)
        {
            return null;
        }
        return json;
    }
}
