package com.example.pm25.connectivity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.example.pm25.model.CitiesSpellAdder;
import com.example.pm25.model.City;
import com.example.pm25.model.ModelCallBackListener;
import com.example.pm25.util.CityComparator;
import com.example.pm25.util.PM25APIs;

public class ConnectService {
	
	/**
	 * 获取城市数据
	 * @param listener是回调的接口，负责传递错误的发生
	 * @return
	 */
	public static List<City> getCities(ModelCallBackListener<City> listener) {
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

}
