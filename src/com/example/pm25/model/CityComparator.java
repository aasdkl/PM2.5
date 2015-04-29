package com.example.pm25.model;

import java.text.Collator;
import java.util.Comparator;

/**
 * 直接对中文排序: Comparator cmp =
 * (RuleBasedCollator)java.text.Collator.getInstance(java.util.Locale.CHINA);
 * 
 */
public class CityComparator implements Comparator<City> {
	Collator cmp = Collator.getInstance(java.util.Locale.CHINA);

	@Override
	public int compare(City lhs, City rhs) {
		return cmp.compare(lhs.getCityName(), rhs.getCityName()) ;
	}

}
