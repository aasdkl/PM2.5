package com.example.pm25.util;

public enum PM25APIKeys{
	AQI("aqi"),
	AREA("area"),
	PM25("pm2_5"),
	PM25_24H("pm2_5_24h"),
	PM10("pm10"),
	PM10_24H("pm10_24h"),
	SO2("so2"),
	SO2_24H("so2_24h"),
	NO2("no2"),
	NO2_24H("no2_24h"),
	CO("co"),
	CO_24H("co_24h"),
	O3("o3"),
	O3_24H("o3_24h"),
	O3_8H("o3_8h"),
	O3_8H_24H("o3_8h_24h"),
	PRIMARY("primary_pollutant"),
	POSITION_NAME("position_name"),
	STATION_CODE("station_code"),
	TIME("time_point"),
	QUALITY("quality"),
	ERROR("error")
	;
	
	String key;
	private PM25APIKeys(String key) {
		this.key = key;
	}
	@Override
	public String toString() {
		return key;
	}
}
