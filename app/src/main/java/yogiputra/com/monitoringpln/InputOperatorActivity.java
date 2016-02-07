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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yogiputra.com.monitoringpln.CustomList.AdapterListDialog;

/**
 * Created by oohyugi on 05/09/15.
 */
public class InputOperatorActivity extends AppCompatActivity {
    EditText edFeeder, edLokasi, edDaya, edTgl, edJam, edPersen;
    EditText edIndukR, edIndukS, edIndukT, edIndukN;
    EditText edB1R, edB1S, edB1T, edB1N;
    EditText edB2R, edB2S, edB2T, edB2N;
    EditText edB3R, edB3S, edB3T, edB3N;
    Button btSimpan;
    AutoCompleteTextView edTrafo;

    private SwipeRefreshLayout swipeRefreshLayout;

    private Toolbar toolbar;

    String idTrafo, feeder, lokasi, id = null, kode;
    double daya, persen;
    double iR, iS, iT, iN;
    double b1R, b1S, b1T, b1N;
    double b2R, b2S, b2T, b2N;
    double b3R, b3S, b3T, b3N;
    double hasil1, hasil2;

    ArrayList<Objectnya> obje;


    ProgressDialog pDialog;
    JSONObject jso;
    JSONArray jra;
    String tanggal, jam, idtraf;

    private Context context = this;
    private Dialog dialog;
    private List<Objectnya> objectnyas;
    private AdapterListDialog listAdapter;
    String url_dialog;
    int pilihan;
    private ListView lv;
    private EditText edCari;

    String link="http://10.0.3.3/";

    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.activity_inputoperator);
        edTrafo = (AutoCompleteTextView) findViewById(R.id.edTrafo);
        edFeeder = (EditText) findViewById(R.id.edFedeer);
        edLokasi = (EditText) findViewById(R.id.edLokasi);
        edDaya = (EditText) findViewById(R.id.edDaya);

        edPersen = (EditText) findViewById(R.id.edPersen);
        btSimpan = (Button) findViewById(R.id.btSimpan);

        edIndukR = (EditText) findViewById(R.id.induk_R);
        edIndukS = (EditText) findViewById(R.id.induk_S);
        edIndukT = (EditText) findViewById(R.id.induk_T);
        edIndukN = (EditText) findViewById(R.id.induk_N);


        edB1R = (EditText) findViewById(R.id.blok1_R);
        edB1S = (EditText) findViewById(R.id.blok1_S);
        edB1T = (EditText) findViewById(R.id.blok1_T);
        edB1N = (EditText) findViewById(R.id.blok1_N);

        edB2R = (EditText) findViewById(R.id.blok2_R);
        edB2S = (EditText) findViewById(R.id.blok2_S);
        edB2T = (EditText) findViewById(R.id.blok2_T);
        edB2N = (EditText) findViewById(R.id.blok2_N);

        edB3R = (EditText) findViewById(R.id.blok3_R);
        edB3S = (EditText) findViewById(R.id.blok3_S);
        edB3T = (EditText) findViewById(R.id.blok3_T);
        edB3N = (EditText) findViewById(R.id.blok3_N);
        edPersen.setEnabled(false);
        edFeeder.setEnabled(false);
        edLokasi.setEnabled(false);
        edDaya.setEnabled(false);





        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

//edTrafo.setAdapter(new SuggestionAdapter(this, edTrafo.getText().toString()));
        edTrafo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(context);
                dialog.setContentView(R.layout.custom_dialog);
                dialog.setTitle("pilih id");
                dialog.setCancelable(true);


                swipeRefreshLayout = (SwipeRefreshLayout) dialog.findViewById(R.id.swipeRefres);

                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(true);
                        tampilkan();


                    }
                });
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        listAdapter.notifyDataSetChanged();
                        tampilkan();


                    }
                });

                edCari = (EditText) dialog.findViewById(R.id.edCari);
                lv = (ListView) dialog.findViewById(R.id.listCustom);
                edCari.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        pilihan = 1;
                        tampilkan();
                        listAdapter.notifyDataSetChanged();


                    }

                    @Override
                    public void afterTextChanged(Editable s) {


                    }
                });


                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        dialog.dismiss();
                        kirimData(position, id);
                    }
                });
                dialog.show();
            }
        });


        final Bundle extras = getIntent().getExtras();
        if (savedInstaceState == null) {


            if (extras != null) {

                idTrafo = extras.getString("idpengukuran");
                feeder = extras.getString("feeder");
                lokasi = extras.getString("lokasi");
                daya = extras.getDouble("daya");
                kode = extras.getString("kode_trafo");
                persen = extras.getDouble("persen_beban");
                iR=extras.getDouble("induk_R");
                iS=extras.getDouble("induk_S");
                iT=extras.getDouble("induk_T");
                iN=extras.getDouble("induk_N");
                b1R=extras.getDouble("blok1_R");
                b1S=extras.getDouble("blok1_S");
                b1T=extras.getDouble("blok1_T");
                b1N=extras.getDouble("blok1_N");
                b2R=extras.getDouble("blok2_R");
                b2S=extras.getDouble("blok2_S");
                b2T=extras.getDouble("blok2_T");
                b2N=extras.getDouble("blok2_N");
                b3R=extras.getDouble("blok3_R");
                b3S=extras.getDouble("blok3_S");
                b3T=extras.getDouble("blok3_T");
                b3N=extras.getDouble("blok3_N");

                edIndukR.setText(String.valueOf(iR));
                edIndukS.setText(String.valueOf(iS));
                edIndukT.setText(String.valueOf(iT));
                edIndukN.setText(String.valueOf(iN));
                edB1R.setText(String.valueOf(b1R));
                edB1S.setText(String.valueOf(b1S));
                edB1T.setText(String.valueOf(b1T));
                edB1N.setText(String.valueOf(b1N));
                edB2R.setText(String.valueOf(b2R));
                edB2S.setText(String.valueOf(b2S));
                edB2T.setText(String.valueOf(b2T));
                edB2N.setText(String.valueOf(b2N));
                edB3R.setText(String.valueOf(b3R));
                edB3S.setText(String.valueOf(b3S));
                edB3T.setText(String.valueOf(b3T));
                edB3N.setText(String.valueOf(b3N));
                edTrafo.setText(kode);
                edFeeder.setText(feeder);
                edLokasi.setText(lokasi);
                edDaya.setText(String.valueOf(daya));
                edPersen.setText(String.valueOf(persen));
                edTrafo.setEnabled(false);
                edFeeder.setEnabled(false);
                edLokasi.setEnabled(false);
                edDaya.setEnabled(false);



            }


        }


        btSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog.show();



                iR = Double.parseDouble(edIndukR.getText().toString());
                iS = Double.parseDouble(edIndukS.getText().toString());
                iT = Double.parseDouble(edIndukT.getText().toString());
                iN = Double.parseDouble(edIndukN.getText().toString());
              //




                daya = Double.parseDouble(edDaya.getText().toString());
                hasil1 = (((iR + iS + iT) / 3) * 1.73 * 400) / 1000;
                hasil2 = (hasil1 / daya) * 100;

//cek jika data kosong
                if ((edB1R.getText().toString().equals(""))){b1R=0;
                }else
                    b1R = Double.parseDouble(edB1R.getText().toString());

                if ((edB1S.getText().toString().equals(""))){ b1S=0;

                }
                else
                    b1S = Double.parseDouble(edB1R.getText().toString());
                if ((edB1T.getText().toString().equals(""))){ b1T=0;

                }else
                    b1T = Double.parseDouble(edB1T.getText().toString());

                if ((edB1N.getText().toString().equals(""))){ b1N=0;

                }else
                    b1N = Double.parseDouble(edB1N.getText().toString());


                if ((edB2R.getText().toString().equals(""))){b2R=0;
                }else
                    b2R = Double.parseDouble(edB2R.getText().toString());

                if ((edB2S.getText().toString().equals(""))){ b2S=0;

                }
                else
                    b2S = Double.parseDouble(edB2S.getText().toString());
                if ((edB2T.getText().toString().equals(""))){ b2T=0;

                }else
                    b2T = Double.parseDouble(edB2T.getText().toString());

                if ((edB2N.getText().toString().equals(""))){ b2N=0;

                }else
                    b2N = Double.parseDouble(edB2N.getText().toString());


                if ((edB3R.getText().toString().equals(""))){b3R=0;
                }else
                    b3R = Double.parseDouble(edB3R.getText().toString());

                if ((edB3S.getText().toString().equals(""))){ b3S=0;

                }
                else
                    b3S = Double.parseDouble(edB3S.getText().toString());
                if ((edB3T.getText().toString().equals(""))){ b3T=0;

                }else
                    b3T = Double.parseDouble(edB3T.getText().toString());

                if ((edB3N.getText().toString().equals(""))){ b3N=0;

                }else
                    b3N = Double.parseDouble(edB3N.getText().toString());






                final DecimalFormat df = new DecimalFormat("#.##");
                if (extras != null) {
                    final String url =link+"pln2/update.php?idpengukuran=" + idTrafo +
                            "&induk_R=" + iR + "&induk_S=" + iS + "&induk_T=" + iT + "&induk_N=" + iN + "&blok1_R=" + b1R + "&blok1_S=" + b1S + "&blok1_T=" + b1T + "&blok1_N=" + b1N +
                            "&blok2_R=" + b2R + "&blok2_S=" + b2S + "&blok2_T=" + b2T + "&blok2_N=" + b2N +
                            "&blok3_R=" + b3R + "&blok3_S=" + b3S + "&blok3_T=" + b3T + "&blok3_N=" + b3N + "&persen_beban=" + df.format(hasil2);

                    JsonObjectRequest updateRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int sukses = response.getInt("sukses");
                                if (sukses == 4) {
                                    pDialog.dismiss();

                                    edIndukR.setText("");
                                    edIndukS.setText("");
                                    edIndukT.setText("");
                                    edIndukN.setText("");

                                    edB1R.setText("");
                                    edB1S.setText("");
                                    edB1T.setText("");
                                    edB1N.setText("");

                                    edB2R.setText("");
                                    edB2S.setText("");
                                    edB2T.setText("");
                                    edB2N.setText("");

                                    edB3R.setText("");
                                    edB3S.setText("");
                                    edB3T.setText("");
                                    edB3N.setText("");
                                    Toast.makeText(getApplicationContext(), "Data Berhasil DiUpdate", Toast.LENGTH_SHORT).show();
                                    Log.d("hasil", String.valueOf(url));
                                    onBackPressed();

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
                    final String url = link+"pln2/input.php";

                    StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            pDialog.dismiss();

                            edIndukR.setText("");
                            edIndukS.setText("");
                            edIndukT.setText("");
                            edIndukN.setText("");

                            edB1R.setText("");
                            edB1S.setText("");
                            edB1T.setText("");
                            edB1N.setText("");

                            edB2R.setText("");
                            edB2S.setText("");
                            edB2T.setText("");
                            edB2N.setText("");

                            edB3R.setText("");
                            edB3S.setText("");
                            edB3T.setText("");
                            edB3N.setText("");
                            Toast.makeText(getApplicationContext(), "Data Berhasil Disimpan..", Toast.LENGTH_SHORT).show();
                            Log.d("hasil", String.valueOf(hasil2));
                            onBackPressed();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            pDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "gagal menyimpan data..", Toast.LENGTH_SHORT).show();

                        }
                    }) {
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("id_trafo", idtraf);


                            params.put("induk_R", String.valueOf(iR));
                            params.put("induk_S", String.valueOf(iS));
                            params.put("induk_T", String.valueOf(iT));
                            params.put("induk_N", String.valueOf(iN));

                            params.put("blok1_R", String.valueOf(b1R));
                            params.put("blok1_S", String.valueOf(b1S));
                            params.put("blok1_T", String.valueOf(b1T));
                            params.put("blok1_N", String.valueOf(b1N));

                            params.put("blok2_R", String.valueOf(b2R));
                            params.put("blok2_S", String.valueOf(b2S));
                            params.put("blok2_T", String.valueOf(b2T));
                            params.put("blok2_N", String.valueOf(b2N));

                            params.put("blok3_R", String.valueOf(b3R));
                            params.put("blok3_S", String.valueOf(b3S));
                            params.put("blok3_T", String.valueOf(b3T));
                            params.put("blok3_N", String.valueOf(b3N));
                            params.put("persen_beban", String.valueOf(df.format(hasil2)));
                            return params;
                        }
                    };

                    AksesJson.getIntance().addToReqQueue(postRequest);
                }


            }
        });


    }


    private void tampilkan() {


        url_dialog = link+"pln2/ambilfeeder.php";
        objectnyas = new ArrayList<Objectnya>();
        listAdapter = new AdapterListDialog(this, objectnyas);
        swipeRefreshLayout.setRefreshing(true);

        if (pilihan == 1) {
            url_dialog = link+"pln2/caridialog.php?kode_trafo=" + edCari.getText().toString();
        } else {
            url_dialog = link+"pln2/ambilfeeder.php";
        }

        JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.GET, url_dialog, null, new Response.Listener<JSONObject>() {
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
                            Log.d("data", url_dialog);

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
                Log.d("data", url_dialog);
                Toast.makeText(context, "Gagal", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        AksesJson.getIntance().addToReqQueue(jreq);
    }

    private void kirimData(int position, long id) {
        dialog.dismiss();
        edTrafo.setText(objectnyas.get(position).getKode().toString());
        edFeeder.setText(objectnyas.get(position).getFeeder().toString());
        edLokasi.setText(objectnyas.get(position).getLokasi().toString());
        edDaya.setText((String.valueOf(objectnyas.get(position).getDaya()).toString()));
        idtraf = objectnyas.get(position).getIdnya();


    }


    public boolean onOptionsItemSelected(MenuItem item) {


        onBackPressed();

        return true;
    }


}
