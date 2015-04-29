package com.example.pm25.model;

import com.example.pm25.util.Constants;

public final class City {

	private int id;
	private String cityName;
	
	public static final String TABLE_NAME = "City";
	public static final String ID = "id";
	public static final String NAME = "city_name";
	
	/**
	 * 构造无id的City对象，多用于保存
	 * @param cityName
	 */
	public City(String cityName) {
		this(Constants.NO_VALUE, cityName);
	}

	/**
	 * 构造有id的City对象，多用于读取、更新
	 * @param cityName
	 */
	public City(int id, String cityName) {
		this.id = id;
		this.cityName = cityName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public boolean isIdExist(){
		if (id==Constants.NO_VALUE) {
			return false;
		} else {
			return true;
		}
	}
	
}
