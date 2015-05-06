package com.example.pm25.connectivity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.example.pm25.model.CitiesSpellAdder;
import com.example.pm25.model.ModelCallBackListener;
import com.example.pm25.model.SharedPreferenceHelper;
import com.example.pm25.po.City;
import com.example.pm25.po.Station;
import com.example.pm25.po.AirQuality;
import com.example.pm25.util.MyLog;
import com.example.pm25.util.PM25APIs;
import com.example.pm25.util.myComponent.CityComparator;
import com.example.pm25.util.myComponent.StationComparator;

public class ConnectService {
	
	/**
	 * 获取城市数据
	 * @param listener是回调的接口，负责传递错误的发生
	 * @return
	 */
	public static List<City> getCities(ModelCallBackListener listener) {
		List<City> cities = new LinkedList<>();
		String api = PM25APIs.CITY.toString();
		String response = ConnectHelper.sendRequest(api, listener);
		if (response!="") {
			cities = JsonTools.parseAllCity(response, listener);
		}
		// 获取城市拼音
		cities = CitiesSpellAdder.invoke(cities);
		// 对中文的拼音排序
		Collections.sort(cities,new CityComparator());

		return cities;
	}

	public static List<Station> getStations(City city, ModelCallBackListener listener) {
		List<Station> stations = new LinkedList<>();
		String api = PM25APIs.STATION.addCity(city.getCityName()).toString();
		MyLog.v("API", api);
		String response = ConnectHelper.sendRequest(api, listener);
		
		if (response != "") {
			stations = JsonTools.parseAllStations(city, response, listener);
		}

		// 对中文的拼音排序
		Collections.sort(stations,new StationComparator());

		return stations;
	}

	public static List<AirQuality> getQualities(City city, Station station,
			ModelCallBackListener<AirQuality> listener) {
		List<AirQuality> quality = new LinkedList<>();
		String api = null;
		// 城市
		if (station == null) {
			api = PM25APIs.AQI_DETAIL.addCity(city.getCityName()).addStations(false).toString();
		// 观测点
		} else {
			api = PM25APIs.AQI_STATION.addStationCode(station.getStationCode()).toString();
		}
		
		MyLog.d("getQualities", api);
		String response = ConnectHelper.sendRequest(api, listener);

		// TODO 将json数据存储到SharedPreference中
		SharedPreferenceHelper.savaCacheDetails(city, station, response);
		if (response != "") {
			quality.add(JsonTools.parseQuality(city, station, response, listener));
		}

		return quality;
	}
}
