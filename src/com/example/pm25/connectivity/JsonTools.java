package com.example.pm25.connectivity;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.id;

import com.example.pm25.exception.DecodeException;
import com.example.pm25.exception.FetchTimesException;
import com.example.pm25.exception.NoArgumentException;
import com.example.pm25.exception.NoCityException;
import com.example.pm25.exception.NoLoginException;
import com.example.pm25.model.ModelCallBackListener;
import com.example.pm25.po.AirQuality;
import com.example.pm25.po.BasePlace;
import com.example.pm25.po.City;
import com.example.pm25.po.Station;
import com.example.pm25.util.Constants;
import com.example.pm25.util.PM25APIKeys;

public class JsonTools {
	
	private static final String ERROR1 = "参数不能为空";
	private static final String ERROR2 = "该城市还未有PM2.5数据";
	private static final String ERROR3 = "Sorry，您这个小时内的API请求次数用完了，休息一下吧！";
	private static final String ERROR4 = "You need to sign in or sign up before continuing.";
	private static final String ERROR5 = "未找到这个城市的监测站";
	
	public static List<City> parseAllCity(String response, ModelCallBackListener<BasePlace> listener) {
		List<City> cities = new LinkedList<>();
		// 判断返回的json是否正确
		if (response.equals("")) {
			return cities;
		}
		int result = verifyData(response, listener);
		if (result == Constants.THREAD_FAIL) {
			return cities;
		}
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONArray jsonArray = jsonObject.getJSONArray("cities");

			for (int i = 0; i < jsonArray.length(); i++) {
				cities.add(new City(jsonArray.getString(i)));
			}
			
		} catch (Exception e) {
			if (listener == null) {
				e.printStackTrace();
			} else {
				listener.onError(e);
			}
			// 将返回值情空
			cities.clear();
		}

		return cities;
	}
	
	public static List<Station> parseAllStations(City city, String response, ModelCallBackListener<BasePlace> listener) {
		List<Station> stations = new LinkedList<>();
		// 判断返回的json是否正确
		int result = verifyData(response, listener);
		if (result == Constants.THREAD_FAIL) {
			return stations;
		}
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONArray jsonArray = jsonObject.getJSONArray("stations");

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject eachStation = jsonArray.getJSONObject(i);
				stations.add(new Station(
						eachStation.getString("station_name"), 
						eachStation.getString("station_code"),
						city.getId()));
			}
			
		} catch (Exception e) {
			listener.onError(e);
			// 将返回值情空
			stations.clear();
		}

		return stations;
	}
	

	public static AirQuality parseQuality(
			City city,
			Station station,
			String response,
			ModelCallBackListener<AirQuality> listener) {
		
		// 判断返回的json是否正确
		int result = verifyData(response, listener);
		if (result == Constants.THREAD_FAIL) {
			return null;
		}
		JSONObject jsonObject;
		AirQuality quality;
		try {
			jsonObject = new JSONArray(response).getJSONObject(0);
			quality = new AirQuality(city, null, 
					jsonObject.getInt(PM25APIKeys.AQI.toString()),
					jsonObject.getString(PM25APIKeys.QUALITY.toString())
					);
			
			quality.setPm25(jsonObject.getInt(PM25APIKeys.PM25.toString()));
			quality.setPm10(jsonObject.getInt(PM25APIKeys.PM10.toString()));
			quality.setCo(jsonObject.getDouble(PM25APIKeys.CO.toString()));
			quality.setO3_1h(jsonObject.getInt(PM25APIKeys.O3.toString()));
			quality.setO3_8h(jsonObject.getInt(PM25APIKeys.O3_8H.toString()));
			quality.setNo2(jsonObject.getInt(PM25APIKeys.NO2.toString()));
			quality.setSo2(jsonObject.getInt(PM25APIKeys.SO2.toString()));
			quality.setTime_point(jsonObject.getString(PM25APIKeys.TIME.toString()));
			quality.setPrimaryPollutant(jsonObject.getString(PM25APIKeys.PRIMARY.toString()));
		} catch (JSONException e) {
			listener.onError(e);
			// 将返回值情空
			return null;
		}
		return quality;
	}

	private static int verifyData(String response, ModelCallBackListener listener) {
		int result = Constants.THREAD_FAIL;
		if (!response.contains(PM25APIKeys.ERROR.toString())) {
			return Constants.SUCCESS;
		}
		try {
			JSONObject jsonObject = new JSONObject(response);
			String errorKey = PM25APIKeys.ERROR.toString();
			if (jsonObject != null && jsonObject.has(errorKey) && listener != null) {
				switch ((String)jsonObject.get(errorKey)) {
				case ERROR1:
					listener.onError(new NoArgumentException());
					break;
				case ERROR2:
					listener.onError(new NoCityException());
					break;
				case ERROR3: 
					listener.onError(new FetchTimesException());
					break;
				case ERROR4:
					listener.onError(new NoLoginException());
					break;
				case ERROR5:
					listener.onError(new DecodeException());
					break;
				default:
					listener.onError(new Exception((String)jsonObject.get("error")));
				}
			} else if (jsonObject != null && listener != null) { 
				result = Constants.SUCCESS;
			}
		} catch (JSONException e) {
			if (listener == null) {
				e.printStackTrace();
			} else {
				listener.onError(e);
			}
		}
		return result;
	}

	public static String toCityJsonStr(List<City> cities) {
		JSONObject resultObj = null;
		try {
			JSONArray array = new JSONArray();
	
			for (City city : cities) {
				JSONObject eachObj = new JSONObject();
				eachObj.put(City.ID, city.getId());
				eachObj.put(City.NAME, city.getCityName());
				eachObj.put(City.SPELL, city.getCitySpell());
				array.put(eachObj);
			}
			
			resultObj = new JSONObject();
			resultObj.put("cities", array);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return resultObj.toString();
	}

	public static List<City> parseInterestCity(String citiesStr) {
		List<City> cities = new LinkedList<>();
		if (citiesStr == null || citiesStr.equals("")) {
			return cities;
		}
		try {
			JSONObject jsonObject = new JSONObject(citiesStr);
			JSONArray jsonArray = jsonObject.getJSONArray("cities");
			JSONObject each = new JSONObject();
			for (int i = 0; i < jsonArray.length(); i++) {
				each = jsonArray.getJSONObject(i);
				cities.add(new City(each.getInt(City.ID), 
						each.getString(City.NAME), 
						each.getString(City.SPELL)));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			// 将返回值情空
			cities.clear();
		}

		return cities;
	}


}
