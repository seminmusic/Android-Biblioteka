package ba.sema.biblioteka.helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ba.sema.biblioteka.models.Stavka;


public class StavkeHelper
{
    private static final SimpleDateFormat enDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static List<Stavka> loadAndMapTestData()
    {
        List<Stavka> lista = new ArrayList<>();

        String jsonString = JSONHelper.loadJSONFromAsset("test-stavke.json");
        JSONArray jsonStavke = null;
        try
        {
            jsonStavke = new JSONObject(jsonString).getJSONArray("Stavke");
            if (jsonStavke.length() > 0)
            {
                for (int i = 0; i < jsonStavke.length(); i++)
                {
                    JSONObject o = jsonStavke.getJSONObject(i);
                    //
                    Stavka model = new Stavka();
                    model.ID = o.getInt("ID");
                    model.Datum = enDateFormat.parse(o.getString("Datum"));
                    model.Vrijeme = o.getString("Vrijeme").substring(0, 5);
                    model.SifArt = o.getString("SifArt");
                    model.Kolicina = o.getDouble("Kolicina");
                    model.UkupnaCijenaSaPDV = o.getDouble("UkupnaCijenaSaPDV");
                    model.NazArt = o.getString("NazArt");
                    model.JedinicnaCijenaSaPDV = o.getDouble("JedinicnaCijenaSaPDV");
                    //
                    lista.add(model);
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

        return lista;
    }
}
