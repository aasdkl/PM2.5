package com.example.pm25.po;

import android.os.Parcelable;

public abstract class BasePlace implements Parcelable {

	protected int id;
	public static final String ID = "id";

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		if (this instanceof City) {
			return ((City) this).getCityName();
		} else {
			return ((Station) this).getStationName();
		}
	}
	
	@Override
	public String toString() {
		return getName();
	}

	public static Station getStationInstanceOrReturnNull(BasePlace selected) {
		if (selected instanceof Station) {
			return (Station) selected;
		}
		return null;
	}
}
