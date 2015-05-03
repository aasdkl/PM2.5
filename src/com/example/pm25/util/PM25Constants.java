package com.example.pm25.util;

import com.example.pm25.R;
import com.example.pm25.util.myComponent.MyApplication;

import android.content.res.Resources;

/**
 * 存放PM25的所有相关污染物的描述
 * @author Administrator
 *
 */
public class PM25Constants {
	
	private static Resources context = MyApplication.getContext().getResources();
	
	public static final String AQI = context.getString(R.string.aqi);
	public static final String AQI_TRANSLATE = context.getString(R.string.translate_aqi);
	public static final String AQI_DESCRIBE = context.getString(R.string.what_is_aqi);
	
	public static final String PM25 = context.getString(R.string.pm25_1h);
	public static final String PM25_TRANSLATE = context.getString(R.string.translate_pm25_1h);
	public static final String PM25_DESCRIBE = context.getString(R.string.what_is_pm25);
	
	public static final String PM10 = context.getString(R.string.pm10_1h);
	public static final String PM10_TRANSLATE = context.getString(R.string.translate_pm10_1h);
	public static final String PM10_DESCRIBE = context.getString(R.string.what_is_pm10);
	
	public static final String CO = context.getString(R.string.co_1h);
	public static final String CO_TRANSLATE = context.getString(R.string.translate_co_1h);
	public static final String CO_DESCRIBE = context.getString(R.string.what_is_co);
	
	public static final String NO2 = context.getString(R.string.no2_1h);
	public static final String NO2_TRANSLATE = context.getString(R.string.translate_no2_1h);
	public static final String NO2_DESCRIBE = context.getString(R.string.what_is_no2);
	
	public static final String O3_1H = context.getString(R.string.o3_1h);
	public static final String O3_1H_TRANSLATE = context.getString(R.string.translate_o3_1h);
	public static final String O3_1H_DESCRIBE = context.getString(R.string.what_is_o3_1h);
	
	public static final String O3_8H = context.getString(R.string.o3_8h);
	public static final String O3_8H_TRANSLATE = context.getString(R.string.translate_o3_8h);
	public static final String O3_8H_DESCRIBE = context.getString(R.string.what_is_o3_8h);
	
	public static final String SO2 = context.getString(R.string.so2_1h);
	public static final String S02_TRANSLATE = context.getString(R.string.translate_so2_1h);
	public static final String SO2_DESCRIBE = context.getString(R.string.what_is_so2);
	
	public static String[] getDescribeArray(){
		return new String[]{
				AQI_DESCRIBE,	PM25_DESCRIBE,	PM10_DESCRIBE, 
				CO_DESCRIBE,	NO2_DESCRIBE,	O3_1H_DESCRIBE, 
				O3_8H_DESCRIBE,	SO2_DESCRIBE
				};
	}

	public static String[] getNameArray(){
		return new String[]{
				AQI,	PM25,	PM10, 
				CO,	NO2,	O3_1H, 
				O3_8H,	SO2
		};
	}
}
