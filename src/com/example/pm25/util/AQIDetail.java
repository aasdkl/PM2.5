package com.example.pm25.util;

import java.util.Arrays;
import java.util.Iterator;

import android.R.integer;
import android.content.res.Resources;

import com.example.fm25.R;
import com.example.pm25.MyApplication;
import com.example.pm25.exception.DataErrorException;
import com.example.pm25.exception.DataRangeException;

/**
 * 用于处理AQI详情的类.
 * @author Administrator
 */
public final class AQIDetail {
	
	private static Resources resources = MyApplication.getContext().getResources();
	
	private static final int[] LEVEL_TABLE = new int[]{51, 101, 151, 201, 251, 301};
	private static final String[] QUALITY = resources.getStringArray(R.array.aqi_quality);
	private static final String[] LEVEL = resources.getStringArray(R.array.aqi_level);
	private static final String[] EFFECT = resources.getStringArray(R.array.aqi_effect);
	private static final String[] SUGGESTION = resources.getStringArray(R.array.aqi_suggestion);
	
	private int aqi = Constants.NO_VALUE;
	private int aqiLevel = Constants.NO_VALUE;
	
	/**
	 * 仅有aqi的构造方式，等价于AQIDetail(aqi, null);
	 * @param aqi > 0
	 */
	public AQIDetail(int aqi) {
		this(aqi, null);
	}
	/**
	 * 双验证的构造方式，如果AQI范围和等级不一致将会
	 * @param aqi >= 0
	 * @param quality 质量等级
	 */
	public AQIDetail(int aqi, String quality) {
		if (aqi < 0) {
			new DataRangeException(getClass().getSimpleName(), "AQI的值不能为负数").printStackTrace();
			return;
		} 
		this.aqi = aqi;
		this.aqiLevel = locateLevel(aqi);
		// 验证AQI与质量等级是否相等
		if (quality != null) {
			int qualityIndex = Arrays.asList(QUALITY).indexOf(quality);
			if (this.aqiLevel != qualityIndex) {
				new DataErrorException(getClass().getSimpleName(), "AQI值与质量等级不对应").printStackTrace();
			}
		}
	}
	
	/**
	 * 通过AQI定位质量等级
	 * @param aqi
	 * @return 等级
	 */
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
	
	/**
	 * 获取空气质量
	 * @return
	 * <ul>	
	 * 	<li>优</li>
	 * 	<li>良</li>
	 *   	<li>轻度污染</li>
	 *   	<li>中度污染</li>
	 *   	<li>重度污染</li>
	 *   	<li>严重污染</li>
	 * </ul>
	 */
	public String getQuality() {
		return QUALITY[aqiLevel];
	}

	/**
	 * 获取AQI值
	 * @return
	 */
	public int getAqi() {
		return aqi;
	}
	
	/**
	 * 获取质量状态
	 * @return
	 * <ul>	
	 * 	<li>空气质量令人满意，基本无空气污染</li>
	 * 	<li>空气质量可接受，但某些污染物可能对极少数异常敏感人群健康有较弱影响</li>
	 *   	<li>易感人群症状有轻度加剧，健康人群出现刺激症状</li>
	 *   	<li>进一步加剧易感人群症状，可能对健康人群心脏、呼吸系统有影响</li>
	 *   	<li>心脏病和肺病患者症状显著加剧，运动耐受力降低，健康人群普遍出现症状</li>
	 *   	<li>健康人群运动耐受力降低，有明显强烈症状，提前出现某些疾病</li>
	 * </ul>
	 */
	public String getEffect() {
		return EFFECT[aqiLevel];
	}
	
	/**
	 * 获取当前空气质量等级
	 * @return
	 * <ul>	
	 * 	<li>一级</li>
	 * 	<li>二级</li>
	 *   	<li>三级</li>
	 *   	<li>四级</li>
	 *   	<li>五级</li>
	 *   	<li>六级</li>
	 * </ul>
	 */
	public String getLevel() {
		return LEVEL[aqiLevel];
	}
	
	/**
	 * 获取建议
	 * @return
	 * <ul>	
	 * 	<li>各类人群可正常活动</li>
	 * 	<li>极少数异常敏感人群应减少户外活动</li>
	 *   	<li>儿童、老年人及心脏病、呼吸系统疾病患者应减少长时间、高强度的户外锻炼</li>
	 *   	<li>儿童、老年人及心脏病、呼吸系统疾病患者避免长时间、高强度的户外锻炼，一般人群适量减少户外运动</li>
	 *   	<li>儿童、老年人和心脏病、肺病患者应停留在室内，停止户外运动，一般人群减少户外运动</li>
	 *   	<li>儿童、老年人和病人应当留在室内，避免体力消耗，一般人群应避免户外活动</li>
	 * </ul>
	 */
	public String getSuggestion() {
		return SUGGESTION[aqiLevel];
	}
}
