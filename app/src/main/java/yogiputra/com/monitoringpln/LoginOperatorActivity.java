package yogiputra.com.monitoringpln;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by oohyugi on 04/09/15.
 */
public class LoginOperatorActivity extends AppCompatActivity {
    Button aLogin,aKeluar;
    EditText aUser,aPass;
   String varUser,varPass;
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private ProgressDialog pdialog;
    private Toolbar toolbar;

    String link="http://10.0.3.3/";

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_operator);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        aLogin=(Button)findViewById(R.id.btLogin);
        aKeluar=(Button)findViewById(R.id.btKeluar);
        aUser=(EditText)findViewById(R.id.edUserad);
        aPass=(EditText)findViewById(R.id.edPassad);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        pdialog=new ProgressDialog(this);
        pdialog.setIndeterminate(true);

        pdialog.setMessage("Tunggu Sebentar...");

        Cache cache = AksesJson.getIntance().getReqQueue().getCache();
        aKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginOperatorActivity.this,MainActivity.class));
            }
        });

        aLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAdmin();
            }
        });
    }



    private void loginAdmin() {
        pdialog.show();
        varUser=aUser.getText().toString();
        varPass=aPass.getText().toString();
        String varStatus="operator";

        if (varUser.equals("") && varPass.equals("")){
            Toast.makeText(getApplicationContext(),"Username/Password masih Kosong!!!",Toast.LENGTH_SHORT).show();

        }
        final String link_url=link+"pln2/loginuser.php?username="+varUser+"&password="+varPass+"&status="+varStatus;
        JsonObjectRequest jreq= new JsonObjectRequest(Request.Method.GET, link_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject responses) {
                try {
                    int sukses = responses.getInt("sukses");
                    if (sukses == 4) {
                        jsonArray = responses.getJSONArray("login");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            aUser.setText("");
                            aPass.setText("");
                            startActivity(new Intent(LoginOperatorActivity.this, OperatorActivity.class));
                            Log.d("users", link_url);
                            pdialog.cancel();
                        }
                    }




                } catch (JSONException e) {

                    e.printStackTrace();
                    pdialog.cancel();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),"Gagal Terhubung ke Server..",Toast.LENGTH_SHORT).show();
                Log.d("users", link_url);
                pdialog.cancel();
            }
        });
        AksesJson.getIntance().addToReqQueue(jreq);
    }
    public boolean onOptionsItemSelected(MenuItem item) {


        onBackPressed();

        return true;
    }
}
