package ba.sema.biblioteka.adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ba.sema.biblioteka.R;
import ba.sema.biblioteka.models.Emisija;


public class SwipeListAdapter extends BaseAdapter
{
    private Activity activity;
    private LayoutInflater inflater;
    private List<Emisija> listaEmisija;

    public SwipeListAdapter(Activity activity, List<Emisija> listaEmisija)
    {
        this.activity = activity;
        this.listaEmisija = listaEmisija;
    }

    @Override
    public int getCount()
    {
        return listaEmisija.size();
    }

    @Override
    public Object getItem(int position)
    {
        return listaEmisija.get(position);
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

        TextView datum = (TextView) convertView.findViewById(R.id.datum);
        TextView naslov = (TextView) convertView.findViewById(R.id.naslov);
        datum.setText(String.valueOf(listaEmisija.get(position).Datum));
        naslov.setText(listaEmisija.get(position).Naslov);

        return convertView;
    }
}
