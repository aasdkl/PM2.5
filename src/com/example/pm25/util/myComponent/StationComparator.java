package com.example.pm25.util.myComponent;

import java.text.Collator;
import java.util.Comparator;

import com.example.pm25.po.City;
import com.example.pm25.po.Station;

/**
 * 对观测点的中文
 * 直接对中文String排序: Comparator cmp =
 * 	(RuleBasedCollator)java.text.Collator.getInstance(java.util.Locale.CHINA);
 */
public class StationComparator implements Comparator<Station> {

	Collator cmp = Collator.getInstance(java.util.Locale.CHINA);

	@Override
	public int compare(Station lhs, Station rhs) {
		return cmp.compare(lhs.getStationName(), rhs.getStationName()) ;
	}

}
