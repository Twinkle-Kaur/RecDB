package Controller;

public class ModelTable {
	

	String id,name,loc,status,drug,treat;
	
	public ModelTable(String id, String name, String loc, String status, String drug, String treat) {
		super();
		this.id = id;
		this.name = name;
		this.loc = loc;
		this.status = status;
		this.drug = drug;
		this.treat = treat;
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

	public String getLoc() {
		return loc;
	}

	public void setLoc(String loc) {
		this.loc = loc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDrug() {
		return drug;
	}

	public void setDrug(String drug) {
		this.drug = drug;
	}

	public String getTreat() {
		return treat;
	}

	public void setTreat(String treat) {
		this.treat = treat;
	}
	
	

}
