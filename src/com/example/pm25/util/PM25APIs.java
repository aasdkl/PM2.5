package com.example.pm25.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.R.bool;

/**默认情况下，最后一项是所有监测点的均值(即一个城市的值)**/
public enum PM25APIs {
	/**
	 * 获取实施了新《环境空气质量标准》的城市列表，即有PM2.5数据的城市列表
	 * @参数 无
	 * @返回 { "cities": [ "上海", ... , "青岛" ] }
	 */
	CITY("http://www.pm25.in/api/querys.json"),
	/**
	 * 获取全部城市的空气质量指数(AQI)排行榜
	 * @参数 无
	 * @返回 <pre style="margin:0px">[{
"aqi": 24,	"area": "昆明",	"co": 1.173,	"co_24h": 1.362,
"no2": 27,	"no2_24h": 32,		"o3": 16,	"o3_24h": 22,
"o3_8h": 7,	"o3_8h_24h": 18,	"pm10": 9, 	"pm10_24h": 24,
"pm2_5": 11, 	"pm2_5_24h": 15,	"quality": "",	"level": "一级",
"so2": 6, 	"so2_24h": 8,		"primary_pollutant": "",
"time_point": "2013-10-21T14:00:00Z"
} ... {}]</pre>
	 */
	RANK("http://www.pm25.in/api/querys/aqi_ranking.json"),
	/**
	 * 获取一个城市的监测点列表
	 * @参数 city：必选
	 * @返回 { "city": "珠海", <br>"stations": [ { <br>&nbsp;&nbsp;&nbsp;&nbsp;"station_name": "吉大",<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;"station_code": "1367A" }, ... , {} <br>] }
	 */
	STATION("http://www.pm25.in/api/querys/station_names.json"),
	
	/**
	 * 获取一个城市的监测点列表
	 * @参数 station_code=""：必选
	 * @返回 <ul><li>aqi</li><li>area
		</li><li>co
		</li><li>co_24h
		</li><li>no2
		</li><li>no2_24h
		</li><li>o3
		</li><li>o3_24h
		</li><li>o3_8h
		</li><li>o3_8h_24h
		</li><li>pm10
		</li><li>pm10_24h
		</li><li>pm2_5
		</li><li>pm2_5_24h
		</li><li>position_name
		</li><li>primary_pollutant
		</li><li>quality
		</li><li>so2
		</li><li>so2_24h
		</li><li>station_code
		</li><li>time_point</li></ul>
	 */
	AQI_STATION("http://www.pm25.in/api/querys/aqis_by_station.json"),

	/**
	 * 获取一个城市所有监测点的AQI数据（不含详情，仅AQI）
	 * @参数 <b>city=""</b>：必选<br>
		<b>avg=true</b>：是否返回一个城市所有监测点数据均值的标识，可选参数，默认是true，不需要均值时传这个参数并设置为false<br>
	 	<b>stations=yes</b>：是否只返回一个城市均值的标识，可选参数，默认是yes，不需要监测点信息时传这个参数并设置为no	 
	 * @返回 一个数组，其中每一项数据包括
		<ul><li>aqi
		</li><li>area
		</li><li>position_name
		</li><li>primary_pollutant
		</li><li>quality
		</li><li>station_code
		</li><li>time_point</li></ul>
	 */
	AQI_ONLY("http://www.pm25.in/api/querys/only_aqi.json"),

	/**
	 * 获取一个城市所有监测点的AQI数据（含详情）
	 * @参数 <b>city=""</b>：必选<br>
		<b>avg=true</b>：是否返回一个城市所有监测点数据均值的标识，可选参数，默认是true，不需要均值时传这个参数并设置为false<br>
	 	<b>stations=yes</b>：是否只返回一个城市均值的标识，可选参数，默认是yes，不需要监测点信息时传这个参数并设置为no	 
	 * @返回 <ul><li>aqi</li><li>area
		</li><li>co
		</li><li>co_24h
		</li><li>no2
		</li><li>no2_24h
		</li><li>o3
		</li><li>o3_24h
		</li><li>o3_8h
		</li><li>o3_8h_24h
		</li><li>pm10
		</li><li>pm10_24h
		</li><li>pm2_5
		</li><li>pm2_5_24h
		</li><li>position_name
		</li><li>primary_pollutant
		</li><li>quality
		</li><li>so2
		</li><li>so2_24h
		</li><li>station_code
		</li><li>time_point</li></ul>
	 */
	AQI_DETAIL("http://www.pm25.in/api/querys/aqi_details.json"),
	
	;
	
	// PM2.5的测试token
	private static final String TOKEN = "5j1znBVAsnSf5xQyNQyq";
	// url的基本地址
	private String url;
	// 用于缓存用户需要的url
	private StringBuilder temp;
	
	private PM25APIs(String url) {
		this.url=url+"?token="+TOKEN;
		temp=new StringBuilder(this.url);
	}
	
	/**
	 * 获取最终生成的url
	 * @return
	 */
	@Override
	public String toString() {
		String result = temp.toString();
		temp.setLength(0);//删除temp中旧的数据
		temp.append(this.url);
		return result;
	}
	
	/**
	 * 添加城市参数
	 * @param city
	 * @return 添加参数后的对象，允许链式调用
	 */
	public PM25APIs addCity(String city) {
		String str;
		try {
			str = URLEncoder.encode(city, "UTF-8");
			addPara("city",str);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return this;
	}

	/**
	 * 添加“返回检测点”参数
	 * @param 
	 * @return 添加参数后的对象，允许链式调用
	 */
	public PM25APIs addStations(Boolean isAdd) {
		addPara("stations", isAdd?"yes":"no");
		return this;
	}
	/**
	 * 添加“检测点代码”参数
	 * @param station_code
	 * @return 添加参数后的对象，允许链式调用
	 */
	public PM25APIs addStationCode(String city) {
		addPara("station_code",city);
		return this;
	}

	/**
	 * 增加参数，当前存储的API为：<br>
	 * xxx.json?token=xxx
	 * @param name
	 * @param value 
	 */
	private void addPara(String name, String value) {
		temp.append("&").append(name).append("=").append(value);
	}
	
	
	
	
	
	
	
	
	public enum returnKey{
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
		private returnKey(String key) {
			this.key = key;
		}
		@Override
		public String toString() {
			return key;
		}
	}
}

