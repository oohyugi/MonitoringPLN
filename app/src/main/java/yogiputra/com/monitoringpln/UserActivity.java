package yogiputra.com.monitoringpln;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import yogiputra.com.monitoringpln.CustomList.AdapterListAdmin;
import yogiputra.com.monitoringpln.CustomList.AdapterListUser;

/**
 * Created by oohyugi on 05/09/15.
 */
public class UserActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private AdapterListUser listAdapter;
    private List<Objectnya> objectnyas;
    JSONObject jobj;
    JSONArray jra;
    SwipeRefreshLayout swipeRefreshLayout;

    EditText cari;
    Spinner pilihCari;
    ImageButton btCari;
    private String url_read,idTrafo,pilihan,caribyfeeder;
    ListView lv;
    Dialog dialog;
    Context context=this;
    private EditText angka1,angka2;
    Button caripersen;
    String link="http://10.0.3.3/";

    public void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.activity_user);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        cari=(EditText)findViewById(R.id.edCari);
        pilihCari=(Spinner)findViewById(R.id.spinPilih);
        btCari=(ImageButton)findViewById(R.id.btCari);
        lv=(ListView)findViewById(R.id.listUser);

        pilihCari.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              switch (pilihCari.getSelectedItemPosition()){
                  case 3:
                      dialog= new Dialog(context);
                      dialog.setContentView(R.layout.dialog_persen);
                      dialog.setTitle("Cari Berdasarkan Persen");

                     angka1=(EditText)dialog.findViewById(R.id.angka1);
                      angka2=(EditText)dialog.findViewById(R.id.angka2);
                      caripersen= (Button)dialog.findViewById(R.id.cariPersen);
                      caripersen.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {
                              dialog.dismiss();
                              cari.setText(angka1.getText().toString() + " sampai " + angka2.getText().toString());
                              Toast.makeText(getApplicationContext(),"Klik Tombol Cari..", Toast.LENGTH_SHORT).show();
                              cari.setEnabled(false);



                          }
                      });
                      dialog.show();

                      break;

              }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listAdapter.notifyDataSetChanged();
                switch (pilihCari.getSelectedItemPosition()) {
                    case 0:
                        Toast.makeText(getApplicationContext(), "klik pilihan yang tersedia", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        pilihan = "1";
                        Toast.makeText(getApplicationContext(), "Cari Berdasarkan Kode Trafo", Toast.LENGTH_SHORT).show();
                        bacaData();


                        break;
                    case 2:
                        pilihan = "2";
                        Toast.makeText(getApplicationContext(), "Cari Berdasarkan Feeder", Toast.LENGTH_SHORT).show();
                        bacaData();
                        break;
                    case 3:
                        pilihan = "3";
                        Toast.makeText(getApplicationContext(), "Cari Berdasarkan Persen", Toast.LENGTH_SHORT).show();
                        bacaData();

                        break;

                }
            }
        });

lv=(ListView)findViewById(R.id.listUser);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipeRefres);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);

                bacaData();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listAdapter.notifyDataSetChanged();

                bacaData();
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                tampilDetail(id, position);

            }
        });
    }



    private void bacaData() {
        objectnyas= new ArrayList<Objectnya>();
        listAdapter=new AdapterListUser(this,objectnyas);
        swipeRefreshLayout.setRefreshing(true);
        url_read=link+"pln2/ambildata.php";

        if (btCari.isPressed() && pilihan.equals("1")){
            idTrafo = cari.getText().toString();
            url_read=link+"pln2/caribyid.php?kode_trafo="+idTrafo;
            cari.setText("");
        }else if (btCari.isPressed() && pilihan.equals("2")){
            caribyfeeder=cari.getText().toString();
            url_read=link+"pln2/caribyfeeder.php?feeder="+caribyfeeder;
            cari.setText("");
        }
        else if (btCari.isPressed() && pilihan.equals("3")) {
            url_read = link+"pln2/caribypersen.php?persen_beban=" + angka1.getText().toString()+"&var2="+angka2.getText().toString();
            cari.setText("");
            cari.setEnabled(true);
        }
        JsonObjectRequest jreq= new JsonObjectRequest(Request.Method.GET, url_read, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {


                    int sukses = response.getInt("sukses");
                    if (sukses == 4) {
                        jra = response.getJSONArray("data");
                        for (int i = 0; i < jra.length(); i++) {
                            jobj = jra.getJSONObject(i);
                            Objectnya obj = new Objectnya();
                            obj.setIdnya(jobj.getString("idpengukuran"));
                            obj.setKode(jobj.getString("kode_trafo"));
                            obj.setFeeder(jobj.getString("feeder"));
                            obj.setLokasi(jobj.getString("lokasi"));
                            obj.setDaya((jobj.getDouble("daya")));
                            obj.setPersen(jobj.getDouble("persen_beban"));
                            obj.setTanggal(jobj.getString("tgl_pengukuran"));
                            obj.setIndukR(jobj.getDouble("induk_R"));
                            obj.setIndukS(jobj.getDouble("induk_S"));
                            obj.setIndukT(jobj.getDouble("induk_T"));
                            obj.setIndukN(jobj.getDouble("induk_N"));

                            obj.setBlok1R(jobj.getDouble("blok1_R"));
                            obj.setBlok1S(jobj.getDouble("blok1_S"));
                            obj.setBlok1T(jobj.getDouble("blok1_T"));
                            obj.setBlok1N(jobj.getDouble("blok1_N"));

                            obj.setBlok2R(jobj.getDouble("blok2_R"));
                            obj.setBlok2S(jobj.getDouble("blok2_S"));
                            obj.setBlok2T(jobj.getDouble("blok2_T"));
                            obj.setBlok2N(jobj.getDouble("blok2_N"));

                            obj.setBlok3R(jobj.getDouble("blok3_R"));
                            obj.setBlok3S(jobj.getDouble("blok3_S"));
                            obj.setBlok3T(jobj.getDouble("blok3_T"));
                            obj.setBlok3N(jobj.getDouble("blok3_N"));
                            objectnyas.add(obj);
                            lv.setAdapter(listAdapter);
                            swipeRefreshLayout.setRefreshing(false);
                            Log.d("url",url_read);


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
                Log.d("url", url_read);
                Toast.makeText(getApplicationContext(), "Koneksi kurang bagus", Toast.LENGTH_SHORT).show();
            }
        });
        AksesJson.getIntance().addToReqQueue(jreq);


    }
    private void tampilDetail(long id, int position) {
        Intent tampil = new Intent(UserActivity.this,DetailActivity.class);
        tampil.putExtra("kode_trafo",objectnyas.get(position).getKode());
        tampil.putExtra("idpengukuran",objectnyas.get(position).getIdnya());
        tampil.putExtra("feeder", objectnyas.get(position).getFeeder());
        tampil.putExtra("lokasi", objectnyas.get(position).getLokasi());
        tampil.putExtra("daya",objectnyas.get(position).getDaya());
        tampil.putExtra("persen_beban",objectnyas.get(position).getPersen());
        tampil.putExtra("tgl_pengukuran",objectnyas.get(position).getTanggal());
        tampil.putExtra("induk_R",objectnyas.get(position).getIndukR());
        tampil.putExtra("induk_S",objectnyas.get(position).getIndukS());
        tampil.putExtra("induk_T",objectnyas.get(position).getIndukT());
        tampil.putExtra("induk_N",objectnyas.get(position).getIndukN());

        tampil.putExtra("blok1_R",objectnyas.get(position).getBlok1R());
        tampil.putExtra("blok1_S",objectnyas.get(position).getBlok1S());
        tampil.putExtra("blok1_T",objectnyas.get(position).getBlok1T());
        tampil.putExtra("blok1_N",objectnyas.get(position).getBlok1N());

        tampil.putExtra("blok2_R",objectnyas.get(position).getBlok2R());
        tampil.putExtra("blok2_S",objectnyas.get(position).getBlok2S());
        tampil.putExtra("blok2_T",objectnyas.get(position).getBlok2T());
        tampil.putExtra("blok2_N",objectnyas.get(position).getBlok2N());
        tampil.putExtra("blok3_R",objectnyas.get(position).getBlok3R());
        tampil.putExtra("blok3_S",objectnyas.get(position).getBlok3S());
        tampil.putExtra("blok3_T",objectnyas.get(position).getBlok3T());
        tampil.putExtra("blok3_N",objectnyas.get(position).getBlok3N());
        startActivity(tampil);
    }
    public boolean onOptionsItemSelected(MenuItem item) {


        onBackPressed();

        return true;
    }

}
