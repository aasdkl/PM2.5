package com.example.pm25.model;

import java.util.List;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

/**
 * 文字转拼音，调用JPinYin包
 * @author Administrator
 *
 */
public class CitiesSpellAdder {
	
	public static List<City> invoke(List<City> cities) {
		for (City city : cities) {
			String spell = PinyinHelper.convertToPinyinString(city.getCityName(), "", PinyinFormat.WITHOUT_TONE);
			city.setCitySpell(spell);
		}
		return cities;
	}

	
}
