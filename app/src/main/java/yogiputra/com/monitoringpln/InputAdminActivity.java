package yogiputra.com.monitoringpln;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yogiputra.com.monitoringpln.CustomList.AdapterListDialog;

/**
 * Created by oohyugi on 05/09/15.
 */
public class InputAdminActivity extends AppCompatActivity {
    EditText edFeeder, edLokasi, edDaya;
    Button btSimpan;
    AutoCompleteTextView edTrafo;

    private SwipeRefreshLayout swipeRefreshLayout;

    private Toolbar toolbar;

    String  feeder, lokasi,daya,kode,idtraf;



    ProgressDialog pDialog;




    private List<Objectnya> objectnyas;
    private AdapterListDialog listAdapter;

    private ListView lv;


    String link="http://10.0.3.3/";
    String url;

    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.activity_inputadmin);
        edTrafo = (AutoCompleteTextView) findViewById(R.id.edTrafo);
        edFeeder = (EditText) findViewById(R.id.edFedeer);
        edLokasi = (EditText) findViewById(R.id.edLokasi);
        edDaya = (EditText) findViewById(R.id.edDaya);
lv=(ListView)findViewById(R.id.listFeeder);

        btSimpan = (Button) findViewById(R.id.btSimpan);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefres);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                tampilData();


            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listAdapter.notifyDataSetChanged();
                tampilData();


            }
        });






        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

//edTrafo.setAdapter(new SuggestionAdapter(this, edTrafo.getText().toString()));


lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        tampilKan(position);
    }
});


        btSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog.show();





                kode=edTrafo.getText().toString();
                daya = edDaya.getText().toString();
                lokasi=edLokasi.getText().toString();
                feeder=edFeeder.getText().toString();

                if (btSimpan.getText().toString().equals("Edit")){
                    Toast.makeText(getApplicationContext(),"Edit Data",Toast.LENGTH_SHORT).show();
                    url=link+"pln2/updatetrafo.php?id_trafo="+idtraf+"&kode_trafo="+kode+"&feeder="+feeder+"&lokasi="+lokasi+"&daya="+daya;
                    JsonObjectRequest updateRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int sukses = response.getInt("sukses");
                                if (sukses == 4) {
                                    pDialog.dismiss();

                                    edTrafo.setText("");
                                    edFeeder.setText("");
                                    edLokasi.setText("");
                                    edDaya.setText("");
                                    btSimpan.setText("Simpan");
                                    Toast.makeText(getApplicationContext(), "Data Berhasil DiUpdate", Toast.LENGTH_SHORT).show();
                                    Log.d("hasil", String.valueOf(url));


                                } else {
                                    pDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "gagal Update data..", Toast.LENGTH_SHORT).show();
                                    Log.d("hasil", String.valueOf(url));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            pDialog.dismiss();
                            Log.d("hasil", String.valueOf(url));
                        }
                    });
                    AksesJson.getIntance().addToReqQueue(updateRequest);


                } else {
                    url = link+"pln2/inputadmin.php";
                    StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            pDialog.dismiss();
                            edTrafo.setText("");
                            edFeeder.setText("");
                            edLokasi.setText("");
                            edDaya.setText("");

                            Toast.makeText(getApplicationContext(), "Data Berhasil Disimpan..", Toast.LENGTH_SHORT).show();
                            Log.d("hasil", url);



                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            pDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "gagal menyimpan data..", Toast.LENGTH_SHORT).show();
                            Log.d("hasil", url);
                        }
                    }) {
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();



                            params.put("kode_trafo", kode);
                            params.put("feeder", feeder);
                            params.put("lokasi", lokasi);
                            params.put("daya", String.valueOf(daya));



                            return params;
                        }
                    };

                    AksesJson.getIntance().addToReqQueue(postRequest);

                }



                }



        });


    }

    private void tampilKan(int position) {
        btSimpan.setText("Edit");
        edTrafo.setText(objectnyas.get(position).getKode().toString());
        edFeeder.setText(objectnyas.get(position).getFeeder().toString());
        edLokasi.setText(objectnyas.get(position).getLokasi().toString());
        edDaya.setText((String.valueOf(objectnyas.get(position).getDaya()).toString()));
        idtraf = objectnyas.get(position).getIdnya();
    }

    private void tampilData() {
        final String url_feeder=link+"pln2/ambilfeeder.php";
        objectnyas = new ArrayList<Objectnya>();
        listAdapter = new AdapterListDialog(this, objectnyas);
        swipeRefreshLayout.setRefreshing(true);

        JsonObjectRequest jre = new JsonObjectRequest(Request.Method.GET, url_feeder, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int sukses = jsonObject.getInt("sukses");
                    if (sukses == 4) {
                        JSONArray jra = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jra.length(); i++) {
                            JSONObject job = jra.getJSONObject(i);
                            Objectnya obj = new Objectnya();
                            obj.setIdnya(job.getString("id_trafo"));
                            obj.setFeeder(job.getString("feeder"));
                            obj.setDaya(job.getDouble("daya"));
                            obj.setLokasi(job.getString("lokasi"));
                            obj.setKode(job.getString("kode_trafo"));
                            objectnyas.add(obj);
                            lv.setAdapter(listAdapter);
                            Log.d("data", url_feeder);

                            swipeRefreshLayout.setRefreshing(false);


                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        AksesJson.getIntance().addToReqQueue(jre);
    }


    public boolean onOptionsItemSelected(MenuItem item) {


        onBackPressed();

        return true;
    }


}
