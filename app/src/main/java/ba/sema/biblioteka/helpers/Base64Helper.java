package ba.sema.biblioteka.helpers;

import android.util.Base64;

import java.io.UnsupportedEncodingException;


public class Base64Helper
{
    public static String encodeToString(String input)
    {
        String encoded = null;
        try
        {
            encoded = Base64.encodeToString(input.getBytes("UTF-8"), Base64.NO_WRAP);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return encoded;
    }
}
