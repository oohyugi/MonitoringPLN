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
 * Created by oohyugi on 09/09/15.
 */
public class AdapterListDialog extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Objectnya> objectnyas;

    public AdapterListDialog(Activity activity, List<Objectnya> objectnyas) {
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
            convertView=inflater.inflate(R.layout.item_dialog,null);

        TextView kode= (TextView)convertView.findViewById(R.id.dialogKode);
        TextView feeder= (TextView)convertView.findViewById(R.id.dFeeder);

        Objectnya obj= objectnyas.get(position);

                kode.setText(obj.getKode());



        return convertView;
    }
}
