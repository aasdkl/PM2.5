package com.example.pm25.connectivity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.example.pm25.model.CitiesSpellAdder;
import com.example.pm25.model.ModelCallBackListener;
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
/*
	public static List<AirQuality> getAirQuality(
			City city,
			Station station,
			ModelCallBackListener<AirQuality> listener) {
		
		List<AirQuality> quality = new LinkedList<>();
		
		String api = PM25APIs.AQI_DETAIL.addStations(false)
				.addCity(city.getCityName()).toString();
		
		MyLog.d("connectService", api);
		String response = ConnectHelper.sendRequest(api, listener);

		// TODO 将json数据存储到SharedPreference中
		
		if (response != "") {
			quality = JsonTools.parseCityQuality(city, response, listener);
		}

		return quality;
	}
*/
}
