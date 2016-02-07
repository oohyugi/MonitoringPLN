package yogiputra.com.monitoringpln.AutoCompleteText;

public class SuggestGetSet {

	String id,name,lokasi;
	public SuggestGetSet(String id, String name,String lokasi){
		this.setId(id);
		this.setName(name);
		this.setLokasi(lokasi);
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLokasi(String lokasi) {
		this.lokasi = lokasi;
	}
	public String getLokasi(){
		return lokasi;
	}
}
