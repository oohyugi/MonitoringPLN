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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by oohyugi on 04/09/15.
 */
public class LoginUserActivity extends AppCompatActivity {
    Button uLogin,uKeluar;
    EditText uUser,uUpass;
    String varUser,varPass,varStatus;
    private JSONArray jsonArray;
    private JSONObject jsonObject;
    private ProgressDialog pDialog;

    private Toolbar toolbar;

    String link="http://10.0.3.3/";

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_user);

        uLogin=(Button)findViewById(R.id.btLogin);
        uKeluar=(Button)findViewById(R.id.btKeluar);
        uUser=(EditText)findViewById(R.id.edUser);
        uUpass=(EditText)findViewById(R.id.edPass);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        pDialog= new ProgressDialog(this);
        pDialog.setIndeterminate(true);
        pDialog.setMessage("Tunggu Sebentar...");

        uLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        uKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginUserActivity.this,MainActivity.class));
            }
        });
    }

    private void loginUser() {
pDialog.show();
        varUser=uUser.getText().toString();
        varPass=uUpass.getText().toString();
        varStatus="user";
        if (varUser.equals("")&& varPass.equals("")){
            Toast.makeText(getApplicationContext(),"Username/Password Masih Kosong",Toast.LENGTH_SHORT).show();

        }

        final String url_login=link+"pln2/loginuser.php?username="+varUser+"&password="+varPass+"&status="+varStatus;

        JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.GET, url_login, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int sukses = response.getInt("sukses");
                    if (sukses == 4) {
                        jsonArray = response.getJSONArray("login");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            Log.d("users", url_login);
                            uUser.setText("");
                            uUpass.setText("");
                            startActivity(new Intent(LoginUserActivity.this, UserActivity.class));
                            pDialog.cancel();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(),"Username/Password salah",Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),"Gagal Terhubung ke Server..",Toast.LENGTH_SHORT).show();
                Log.d("users", url_login);
                pDialog.cancel();
            }
        });
        AksesJson.getIntance().addToReqQueue(jreq);
    }
    public boolean onOptionsItemSelected(MenuItem item) {


        onBackPressed();

        return true;
    }
}
