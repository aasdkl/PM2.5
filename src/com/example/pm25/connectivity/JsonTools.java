package com.example.pm25.connectivity;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.pm25.exception.DecodeException;
import com.example.pm25.exception.FetchTimesException;
import com.example.pm25.exception.NoArgumentException;
import com.example.pm25.exception.NoCityException;
import com.example.pm25.exception.NoLoginException;
import com.example.pm25.model.ModelCallBackListener;
import com.example.pm25.po.City;
import com.example.pm25.po.Station;
import com.example.pm25.po.AirQuality;
import com.example.pm25.util.AQIDetail;
import com.example.pm25.util.Constants;
import com.example.pm25.util.PM25APIs;

public class JsonTools {
	
	private static final String ERROR1 = "参数不能为空";
	private static final String ERROR2 = "该城市还未有PM2.5数据";
	private static final String ERROR3 = "Sorry，您这个小时内的API请求次数用完了，休息一下吧！";
	private static final String ERROR4 = "You need to sign in or sign up before continuing.";
	private static final String ERROR5 = "未找到这个城市的监测站";
	
	public static List<City> parseAllCity(String response, ModelCallBackListener<City> listener) {
		List<City> cities = new LinkedList<>();
		// 判断返回的json是否正确
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
			listener.onError(e);
			// 将返回值情空
			cities.clear();
		}

		return cities;
	}
	
	public static List<Station> parseAllStations(City city, String response, ModelCallBackListener listener) {
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
	/* TODO
	public static List<AirQuality> parseAirQuality(
			City city,
			Station station,
			String response,
			ModelCallBackListener<AirQuality> listener) {
		
		
		List<AirQuality> qualities = new LinkedList<>();
		// 判断返回的json是否正确
		int result = verifyData(response, listener);
		if (result == Constants.THREAD_FAIL) {
			return qualities;
		}
		try {
			
			JSONObject cityQuality = new JSONArray(response).getJSONObject(0);
			AirQuality cityObj = new AirQuality(null, 
					new AQIDetail(cityQuality.getInt(PM25APIs.returnKey.AQI.toString()),
							cityQuality.getString(PM25APIs.returnKey.QUALITY.toString()))
			);
			
			cityObj.setPm25(cityQuality.getInt(PM25APIs.returnKey.PM25.toString()));
			cityObj.setPm10(cityQuality.getInt(PM25APIs.returnKey.PM10.toString()));
			cityObj.setCo(cityQuality.getInt(PM25APIs.returnKey.CO.toString()));
			cityObj.setO3_1h(cityQuality.getInt(PM25APIs.returnKey.O3.toString()));
			cityObj.setO3_8h(cityQuality.getInt(PM25APIs.returnKey.O3_8H.toString()));
			cityObj.setNo2(cityQuality.getInt(PM25APIs.returnKey.NO2.toString()));
			cityObj.setSo2(cityQuality.getInt(PM25APIs.returnKey.SO2.toString()));
			cityObj.setTime_point(cityQuality.getString(PM25APIs.returnKey.TIME.toString()));
			cityObj.setPrimaryPollutant(cityQuality.getString(PM25APIs.returnKey.PRIMARY.toString()));
			
			qualities.add(cityObj);
			
		} catch (Exception e) {
			listener.onError(e);
			// 将返回值情空
			qualities.clear();
		}

		return qualities;

	}

	*/
	
	
	
	
	
	
	
	
	


	private static int verifyData(String response, ModelCallBackListener listener) {
		int result = Constants.THREAD_FAIL;
		try {
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject != null && jsonObject.has("error")) {
				switch ((String)jsonObject.get("error")) {
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
			} else if (jsonObject != null) { 
				result = Constants.SUCCESS;
			}
		} catch (JSONException e) {
			listener.onError(e);
		}
		return result;
	}


}
