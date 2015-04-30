package com.example.pm25.model;

import android.R.color;

import com.example.pm25.util.Constants;

public final class City {

	private int id;
	private String cityName;
	private String citySpell;
	
	public static final String TABLE_NAME = "City";
	public static final String ID = "id";
	public static final String NAME = "city_name";
	public static final String SPELL = "city_spell";
	
	/**
	 * 构造无id的City对象，多用于保存
	 * @param cityName
	 */
	public City(String cityName) {
		this(Constants.NO_VALUE, cityName, null);
	}

	/**
	 * 构造有id的City对象，多用于读取、更新
	 * @param id
	 * @param cityName
	 * @param eachSpell 
	 */
	public City(int id, String cityName, String citySpell) {
		this.id = id;
		this.cityName = cityName;
		this.citySpell = citySpell;
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

	public void setCitySpell(String citySpell) {
		this.citySpell = citySpell;
	}
	
	public String getCitySpell() {
		return citySpell;
	}
	
	@Override
	public String toString() {
		return ID + ": " + id + " " + NAME + ": " + cityName + " " + SPELL + " " + ": " + citySpell;
	}
	
}
