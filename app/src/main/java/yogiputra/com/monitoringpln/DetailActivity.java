package yogiputra.com.monitoringpln;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by oohyugi on 16/09/15.
 */
public class DetailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView kodetrafo,feeder,lokasi,daya,tanggal,ir,is,it,in,b1r,b1s,b1t,b1n,b2r,b2s,b2t,b2n,b3r,b3s,b3t,b3n,hasilP;
    private String kt,fd,lk,tgl,jm;
    private double dy,iR,iS,iT,iN,b1R,b1S,b1T,b1N,b2R,b2S,b2T,b2N,b3R,b3S,b3T,b3N,hasil;

    public void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.detail_data);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitle("Detail Pengukuran");

        kodetrafo=(TextView)findViewById(R.id.dKodeTrafo);
        feeder=(TextView)findViewById(R.id.dFeeder);
        lokasi=(TextView)findViewById(R.id.dLokasi);
        daya=(TextView)findViewById(R.id.dDaya);
        tanggal=(TextView)findViewById(R.id.dTgl);

        ir=(TextView)findViewById(R.id.ir);
        is=(TextView)findViewById(R.id.is);
        it=(TextView)findViewById(R.id.it);
        in=(TextView)findViewById(R.id.in);
        b1r=(TextView)findViewById(R.id.b1r);
        b1s=(TextView)findViewById(R.id.b1s);
        b1t=(TextView)findViewById(R.id.b1t);
        b1n=(TextView)findViewById(R.id.b1n);
        b2r=(TextView)findViewById(R.id.b2r);
        b2s=(TextView)findViewById(R.id.b2s);
        b2t=(TextView)findViewById(R.id.b2t);
        b2n=(TextView)findViewById(R.id.b2n);
        b3r=(TextView)findViewById(R.id.b3r);
        b3s=(TextView)findViewById(R.id.b3s);
        b3t=(TextView)findViewById(R.id.b3t);
        b3n=(TextView)findViewById(R.id.b3n);
        hasilP=(TextView)findViewById(R.id.dHasil);


        Bundle ambil = getIntent().getExtras();
        kt=ambil.getString("kode_trafo");
        lk=ambil.getString("lokasi");
        fd=ambil.getString("feeder");
        dy=ambil.getDouble("daya");
        tgl=ambil.getString("tgl_pengukuran");
        iR=ambil.getDouble("induk_R");
        iS=ambil.getDouble("induk_S");
        iT=ambil.getDouble("induk_T");
        iN=ambil.getDouble("induk_N");
        b1R=ambil.getDouble("blok1_R");
        b1S=ambil.getDouble("blok1_S");
        b1T=ambil.getDouble("blok1_T");
        b1N=ambil.getDouble("blok1_N");
        b2R=ambil.getDouble("blok2_R");
        b2S=ambil.getDouble("blok2_S");
        b2T=ambil.getDouble("blok2_T");
        b2N=ambil.getDouble("blok2_N");
        b3R=ambil.getDouble("blok3_R");
        b3S=ambil.getDouble("blok3_S");
        b3T=ambil.getDouble("blok3_T");
        b3N=ambil.getDouble("blok3_N");

        hasil=ambil.getDouble("persen_beban");

        kodetrafo.setText(kt);
        lokasi.setText(lk);
        feeder.setText(fd);
        daya.setText(String.valueOf(dy));
        tanggal.setText(tgl);
        ir.setText(String.valueOf(iR));
        is.setText(String.valueOf(iS));
        it.setText(String.valueOf(iT));
        in.setText(String.valueOf(iN));
        b1r.setText(String.valueOf(b1R));
        b1s.setText(String.valueOf(b1S));
        b1t.setText(String.valueOf(b1T));
        b1n.setText(String.valueOf(b1N));
        b2r.setText(String.valueOf(b2R));
        b2s.setText(String.valueOf(b2S));
        b2t.setText(String.valueOf(b2T));
        b2n.setText(String.valueOf(b2N));
        b3r.setText(String.valueOf(b3R));
        b3s.setText(String.valueOf(b3S));
        b3t.setText(String.valueOf(b3T));
        b3n.setText(String.valueOf(b3N));

        hasilP.setText(String.valueOf(hasil));


    }
    public boolean onOptionsItemSelected(MenuItem item) {


        onBackPressed();

        return true;
    }
}
