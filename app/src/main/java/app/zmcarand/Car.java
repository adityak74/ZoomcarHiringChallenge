package app.zmcarand;

import java.util.ArrayList;

public class Car {
	private String title, image,type,hrate,rating,seater,ac,lat,lng;


	public Car() {
	}

	public Car(String name, String image,String type,String hrate,String rating,String seater,String ac,String lat,String lng) {
		this.title = name;
		this.image = image;
		this.type = type;
		this.hrate = hrate;
		this.rating = rating;
		this.seater = seater;
		this.ac = ac;
		this.lat = lat;
		this.lng = lng;
	}

	public String getName() {
		return title;
	}

	public void setName(String name) {
		this.title = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHourlyRate() {
		return hrate;
	}

	public void setHourlyRate(String hrate) {
		this.hrate = hrate;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getSeater() {
		return seater;
	}

	public void setSeater(String seater) {
		this.seater = seater;
	}

	public String getAc() {
		return ac;
	}

	public void setAc(String ac) {
		this.ac = ac;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLong() {
		return lng;
	}

	public void setLong(String lng) {
		this.lng = lng;
	}

}
