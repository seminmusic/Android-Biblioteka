package ba.sema.biblioteka.adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import ba.sema.biblioteka.R;
import ba.sema.biblioteka.models.Emisija;
import ba.sema.biblioteka.models.Stavka;


public class SwipeListAdapter extends BaseAdapter
{
    private static final SimpleDateFormat bsDateFormat = new SimpleDateFormat("dd.MM.yyyy");

    private Activity activity;
    private LayoutInflater inflater;
    // private List<Emisija> listaEmisija;
    private List<Stavka> listaStavki;

    public SwipeListAdapter(Activity activity, List<Stavka> listaStavki)
    {
        this.activity = activity;
        this.listaStavki = listaStavki;
    }

    @Override
    public int getCount()
    {
        return listaStavki.size();
    }

    @Override
    public Object getItem(int position)
    {
        return listaStavki.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        TextView naziv_artikla = (TextView) convertView.findViewById(R.id.naziv_artikla);
        TextView datum_vrijeme = (TextView) convertView.findViewById(R.id.datum_vrijeme);
        TextView ukupna_cijena = (TextView) convertView.findViewById(R.id.ukupna_cijena);
        TextView kolicina_cijena = (TextView) convertView.findViewById(R.id.kolicina_cijena);

        Stavka stavka = listaStavki.get(position);

        naziv_artikla.setText(stavka.NazArt);
        datum_vrijeme.setText(bsDateFormat.format(stavka.Datum) + " - " + stavka.Vrijeme);
        ukupna_cijena.setText(String.valueOf(stavka.UkupnaCijenaSaPDV).replace(".", ",") + " KM");
        kolicina_cijena.setText(String.valueOf((int)stavka.Kolicina) + " x " + String.valueOf(stavka.JedinicnaCijenaSaPDV).replace(".", ",") + " KM");

        return convertView;
    }
}
