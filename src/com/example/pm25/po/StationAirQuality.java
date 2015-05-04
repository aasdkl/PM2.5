package com.example.pm25.po;

import com.example.pm25.R.id;
import com.example.pm25.util.AQIDetail;
import com.example.pm25.util.Constants;

public class StationAirQuality {

	private Station station;

	private AQIDetail aqiDetail;
	
	private int pm25 = Constants.NO_VALUE;
	private int pm10 = Constants.NO_VALUE;
	private int co = Constants.NO_VALUE;
	private int o3_1h = Constants.NO_VALUE;
	private int o3_8h = Constants.NO_VALUE;
	private int no2 = Constants.NO_VALUE;
	private int so2 = Constants.NO_VALUE;

	private String primaryPollutant = "";
	private String quality = "";
	private String time_point = "";

	public StationAirQuality(Station station, AQIDetail aqiDetail) {
		this.aqiDetail = aqiDetail;
		this.station = station;
	}

	public AQIDetail getAqiDetail() {
		return aqiDetail;
	}

	public void setAqiDetail(AQIDetail aqiDetail) {
		this.aqiDetail = aqiDetail;
	}

	public int getPm25() {
		return pm25;
	}

	public void setPm25(int pm25) {
		this.pm25 = pm25;
	}

	public int getPm10() {
		return pm10;
	}

	public void setPm10(int pm10) {
		this.pm10 = pm10;
	}

	public int getCo() {
		return co;
	}

	public void setCo(int co) {
		this.co = co;
	}

	public int getO3_1h() {
		return o3_1h;
	}

	public void setO3_1h(int o3_1h) {
		this.o3_1h = o3_1h;
	}

	public int getO3_8h() {
		return o3_8h;
	}

	public void setO3_8h(int o3_8h) {
		this.o3_8h = o3_8h;
	}

	public int getNo2() {
		return no2;
	}

	public void setNo2(int no2) {
		this.no2 = no2;
	}

	public int getSo2() {
		return so2;
	}

	public void setSo2(int so2) {
		this.so2 = so2;
	}

	public String getPrimaryPollutant() {
		return primaryPollutant;
	}

	public void setPrimaryPollutant(String primaryPollutant) {
		if (primaryPollutant.equals("")) {
			primaryPollutant = "无";
		}
		this.primaryPollutant = primaryPollutant;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getTime_point() {
		return time_point;
	}

	public void setTime_point(String time_point) {
		this.time_point = time_point.replace("T", " ").replace("Z", "");
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public Station getStation() {
		return station;
	}
	
}