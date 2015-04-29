package com.example.pm25.model.db;

import com.example.pm25.model.City;
import com.example.pm25.model.Station;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper{
	/**
	 * 所有城市建表语句
	 */
	private static final String CREATE_CITY = "create table " + City.TABLE_NAME + " (" +
			City.ID + " integer primary key autoincrement, " +
			City.NAME + " text)";
	/**
	 * 观测点建表语句
	 */
	private static final String CREATE_STATION = "create table " + Station.TABLE_NAME + " (" +
			Station.ID + " integer primary key autoincrement, " +
			Station.NAME +" text, " +
			Station.CODE + " text, " +
			Station.CITY_ID + " integer)";
	
	public DBOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_CITY);
		db.execSQL(CREATE_STATION);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch (oldVersion) {
		//TODO 进行更新时需要添加，无break返回值
//		case 2:
//		case 1:
		default:
			break;
		}
	}
	
}
