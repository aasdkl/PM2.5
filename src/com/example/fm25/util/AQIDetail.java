package com.example.fm25.util;

import java.util.Iterator;

import android.R.integer;
import android.content.res.Resources;

import com.example.fm25.MyApplication;
import com.example.fm25.R;
import com.example.fm25.exception.DataRangeException;

public final class AQIDetail {
	
	private static Resources resources = MyApplication.getContext().getResources();
	
	private static final int[] LEVEL_TABLE = new int[]{0, 50, 100, 150, 200, 250, 300};
	private static final String[] QUALITY = resources.getStringArray(R.array.aqi_quality);
	private static final String[] LEVEL = resources.getStringArray(R.array.aqi_level);
	private static final String[] EFFECT = resources.getStringArray(R.array.aqi_effect);
	private static final String[] SUGGESTION = resources.getStringArray(R.array.aqi_suggestion);
	
	private static final int NO_VALUE = Integer.MIN_VALUE;
	private int aqi = NO_VALUE;
	private int aqiLevel = NO_VALUE;
	
	/**
	 * 仅有aqi的构造方式，等价于AQIDetail(aqi, null);
	 * @param aqi
	 */
	public AQIDetail(int aqi) {
		this(aqi, null);
	}
	/**
	 * 仅有aqi的构造方式，等价于AQIDetail(NO_VALUE, quality);
	 * @param aqi
	 */
	public AQIDetail(String quality) {
		this(NO_VALUE, quality);
	}
	/**
	 * 双验证的构造方式
	 * @param aqi AQI值
	 * @param quality 质量等级
	 */
	public AQIDetail(int aqi, String quality) {
		if (aqi < 0) {
			new DataRangeException(getClass().getSimpleName(), "AQI的值不能为负数").printStackTrace();
		}
		this.aqi = aqi;
		this.aqiLevel = locateLevel(aqi);
	}
	
	
	private int locateLevel(int aqi) {
		int nowLevel = 0;
		int tableLength= LEVEL_TABLE.length;
		for (; nowLevel < tableLength; nowLevel++) {
			if (aqi < LEVEL_TABLE[nowLevel]) {
				break;
			}
		}
		return nowLevel;
	}
	
	//Arrays.asList(arr).contains(targetValue); 
}
