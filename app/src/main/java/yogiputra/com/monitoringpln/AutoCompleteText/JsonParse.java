package yogiputra.com.monitoringpln.AutoCompleteText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import yogiputra.com.monitoringpln.Objectnya;

public class JsonParse {
	ArrayList<Objectnya> obje;
	 public JsonParse(){}


	 public List<SuggestGetSet> getParseJsonWCF(String sName)
		{
		 final List<SuggestGetSet> ListData = new ArrayList<SuggestGetSet>();


		try {
			String temp=sName.replace(" ", "%20");
			URL js = new URL("http://10.0.3.3/pln2/ambilfeeder.php?kode_trafo="+temp);
			URLConnection jc = js.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
            String line = reader.readLine();
            JSONObject jsonResponse = new JSONObject(line);
            JSONArray jsonArray = jsonResponse.getJSONArray("data");
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject r = jsonArray.getJSONObject(i);
                ListData.add(new SuggestGetSet(r.getString("id_trafo"), r.getString("kode_trafo"), r.getString("lokasi")));

				Objectnya obj = new Objectnya();
				obj.setIdnya(r.getString("id_trafo"));
				obj.setFeeder(r.getString("feeder"));
				obj.setFeeder(r.getString("lokasi"));
				obj.setFeeder(r.getString("daya"));



        	}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 return ListData;
		 
		}
	 
}
