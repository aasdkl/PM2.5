package com.example.pm25.model;

import java.util.LinkedList;
import java.util.List;

import android.R.color;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 封装了数据库服务，单例模式，使用getInstance
 * @author Administrator
 */
public class DBService {
	/**
	 * 数据库名
	 */
	private static final String DB_NAME = "PM25";
	/**
	 * 数据库版本
	 */
	private static final int DB_VERSION = 1;
	
	private static DBService instance;
	private SQLiteDatabase db;
	
	public static DBService getInstance(Context context) {
		if (instance==null) {
			instance = new DBService(context);
		}
		return instance;
	}
	
	private DBService(Context context) {
		DBOpenHelper dbhelper = new DBOpenHelper(context, DB_NAME, null, DB_VERSION);
		db = dbhelper.getWritableDatabase();
	}
	
	public void saveCity(City city) {
		if (city != null) {
			ContentValues values = new ContentValues();
			values.put(City.NAME, city.getCityName());
			db.insert(City.TABLE_NAME, null, values);
		}
	}
	
	public void saveStation(Station station) {
		if (station != null) {
			ContentValues values = new ContentValues();
			values.put(Station.NAME, station.getStationName());
			values.put(Station.CODE, station.getStationCode());
			db.insert(Station.TABLE_NAME, null, values);
		}
	}
	
	public List<City> loadCities() {
		List<City> list = new LinkedList<City>();
		try(
			Cursor cursor = db.query(City.TABLE_NAME, null, null, null, null, null, null, null);
		) {
			if (cursor.moveToFirst()) {
				int eachId;
				String eachName;
				do {
					eachId = cursor.getInt(cursor.getColumnIndex(City.ID));
					eachName = cursor.getString(cursor.getColumnIndex(City.NAME));
					list.add(new City(eachId, eachName));
				} while (cursor.moveToNext());
			}
		}
		return list;
	}

	public List<Station> loadStations(int cityId) {
		List<Station> list = new LinkedList<Station>();
		try(
			Cursor cursor = db.query(Station.NAME, null, Station.CITY_ID + " = ? "
					, new String[]{String.valueOf(cityId)}, null, null, null, null);
		) {
			if (cursor.moveToFirst()) {
				int eachId;
				String eachName;
				String eachCode;
				do {
					eachId = cursor.getInt(cursor.getColumnIndex(Station.ID));
					eachName = cursor.getString(cursor.getColumnIndex(Station.NAME));
					eachCode = cursor.getString(cursor.getColumnIndex(Station.CODE));
					list.add(new Station(eachId, eachName, eachCode, cityId));
				} while (cursor.moveToNext());
			}
		}
		return list;
	}
	
}
