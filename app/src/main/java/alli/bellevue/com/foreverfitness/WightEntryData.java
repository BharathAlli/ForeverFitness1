package alli.bellevue.com.foreverfitness;

public class WightEntryData {

	int _id;
	String curentweight;
	String date;
	public static final String BEGININGWEIGHT = "beginingWeight";
	public static final String CURRENTWEIGHT = "currentWeight";
	public static final String HEIGHT = "height";
	public static final String DATEE = "date";
	public static final String GENDER = "gender";
	public static final String TARGETED_WEIGHT = "targetedWeight";
	public static final String TARGETED_DATE = "targetedDate";
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	String imagePath;
	String weight_loss;
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public String getCurentweight() {
		return curentweight;
	}
	public void setCurentweight(String curentweight) {
		this.curentweight = curentweight;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}



	public WightEntryData(){

	}
	public WightEntryData(int id, String curentweight, String date){
		this._id = id;
		this.curentweight = curentweight;
		this.date = date;
	}

	public WightEntryData(String curentweight, String date, String weight_loss, String imgPath){
		this.curentweight = curentweight;
		this.date = date;
		this.weight_loss=weight_loss;
		this.imagePath=imgPath;

	}
	public String getWeight_loss() {
		return weight_loss;
	}

	public void setWeight_loss(String weight_loss) {
		this.weight_loss = weight_loss;
	}

}