package com.example.pm25.util;

import java.text.Collator;
import java.util.Comparator;

import com.example.pm25.model.City;

/**
 * 对城市的拼音排序
 */
public class CityComparator implements Comparator<City> {

	Collator cmp = Collator.getInstance();

	@Override
	public int compare(City lhs, City rhs) {
		return cmp.compare(lhs.getCitySpell(), rhs.getCitySpell()) ;
	}
}
