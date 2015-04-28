package com.example.pm25.model;

import com.example.pm25.util.Constants;

public final class Station {
	private int id;
	private String stationName;
	private String stationCode;
	private int cityId;
	
	public static final String TABLE_NAME = " Station ";
	public static final String ID = " id ";
	public static final String NAME = " station_name ";
	public static final String CODE = " station_code ";
	public static final String CITY_ID = " city_id ";
	
	public Station(String stationName, String stationCode) {
		this(Constants.NO_VALUE, stationName, stationCode, Constants.NO_VALUE);
	}

	public Station(String stationName, String stationCode, int cityId) {
		this(Constants.NO_VALUE, stationName, stationCode, cityId);
	}
	
	public Station(int id, String stationName, String stationCode, int cityId) {
		this.id = id;
		this.stationName = stationName;
		this.stationCode = stationCode;
		this.cityId = cityId;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getStationCode() {
		return stationCode;
	}
	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public boolean isIdExist(){
		if (id==Constants.NO_VALUE) {
			return false;
		} else {
			return true;
		}
	}

}
