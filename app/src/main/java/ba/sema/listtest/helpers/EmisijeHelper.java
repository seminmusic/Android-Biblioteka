package ba.sema.listtest.helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ba.sema.listtest.models.Emisija;


public class EmisijeHelper
{
    public static void HandleEmisijeResponse(JSONObject response, List<Emisija> listaEmisija)
    {
        JSONArray broadcasts = null;
        try
        {
            broadcasts = response.getJSONArray("broadcasts");
            if (broadcasts.length() > 0)
            {
                listaEmisija.clear();
                for (int i = 0; i < broadcasts.length(); i++)
                {
                    JSONObject b = broadcasts.getJSONObject(i);
                    String datumVrijeme = b.getString("date")+ " " + b.getString("start_time").substring(0, 5);
                    String naslov = b.getString("title");
                    listaEmisija.add(new Emisija(datumVrijeme, naslov));
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
