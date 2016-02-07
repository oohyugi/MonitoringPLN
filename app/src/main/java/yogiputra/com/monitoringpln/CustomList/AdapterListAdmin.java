package yogiputra.com.monitoringpln.CustomList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import yogiputra.com.monitoringpln.Objectnya;
import yogiputra.com.monitoringpln.R;

/**
 * Created by oohyugi on 07/09/15.
 */
public class AdapterListAdmin extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Objectnya> objectnyas;

    public AdapterListAdmin(Activity activity, List<Objectnya> objectnyas) {
        this.activity = activity;
        this.objectnyas = objectnyas;
    }



    @Override
    public int getCount() {
        return objectnyas.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater==null)
            inflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView==null)
            convertView=inflater.inflate(R.layout.item_admin,null);

        TextView kodeFeeder= (TextView)convertView.findViewById(R.id.aIdFeeder);
        TextView feeder= (TextView)convertView.findViewById(R.id.aFeeder);
        TextView persen=(TextView)convertView.findViewById(R.id.aPersen);
        TextView tanggal=(TextView)convertView.findViewById(R.id.aTanggal);
        Objectnya obj= objectnyas.get(position);

        kodeFeeder.setText(obj.getKode());
        feeder.setText(obj.getFeeder());
        persen.setText(String.valueOf(obj.getPersen()));
        tanggal.setText(obj.getTanggal());

        return convertView;
    }
}
