package com.example.pm25.connectivity;

import java.util.LinkedList;
import java.util.List;

import com.example.pm25.model.City;
import com.example.pm25.model.ModelCallBackListener;
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
		return cities;
	}

}
