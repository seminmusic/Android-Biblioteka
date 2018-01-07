package ba.sema.biblioteka.helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import ba.sema.biblioteka.models.Emisija;


public class EmisijeHelper
{
    private static final SimpleDateFormat enDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat bsDateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public static void HandleEmisijeResponse(JSONObject response, List<Emisija> listaEmisija)
    {
        listaEmisija.clear();
        JSONArray broadcasts = null;
        try
        {
            broadcasts = response.getJSONArray("broadcasts");
            if (broadcasts.length() > 0)
            {
                for (int i = 0; i < broadcasts.length(); i++)
                {
                    JSONObject b = broadcasts.getJSONObject(i);
                    String datum = bsDateFormat.format(enDateFormat.parse(b.getString("date")));
                    String datumVrijeme = datum + " - " + b.getString("start_time").substring(0, 5);
                    String naslov = b.getString("title");
                    listaEmisija.add(new Emisija(datumVrijeme, naslov));
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }
}
