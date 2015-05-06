package com.example.pm25.po;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.pm25.util.Constants;

public final class City extends BasePlace {

	private String cityName;
	private String citySpell;
	
	public static final String TABLE_NAME = "City";
	public static final String NAME = "city_name";
	public static final String SPELL = "city_spell";
	
	// 必须要创建一个名叫CREATOR的常量。  
	public static final Parcelable.Creator<City> CREATOR = new Parcelable.Creator<City>() {  
	        @Override  
	        public City createFromParcel(Parcel source) {  
	        	return new City(source);  
	        }  
	        //重写createFromParcel方法，创建并返回一个获得了数据的user对象  
	        @Override  
	        public City[] newArray(int size) {  
	            return new City[size];  
	        }  
	};  

	// 带参构造器方法私用化，本构造器仅供类的方法createFromParcel调用    
	private City(Parcel source){
		id=source.readInt();
		cityName=source.readString();
		citySpell=source.readString();
	}

	
	/**
	 * 构造无id的City对象，多用于保存
	 * @param cityName
	 */
	public City(String cityName) {
		this(Constants.NO_VALUE, cityName, null);
	}

	/**
	 * 构造有id的City对象，多用于读取、更新
	 * @param id
	 * @param cityName
	 * @param eachSpell 
	 */
	public City(int id, String cityName, String citySpell) {
		this.id = id;
		this.cityName = cityName;
		this.citySpell = citySpell;
	}
	
	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public boolean isIdExist(){
		if (id==Constants.NO_VALUE) {
			return false;
		} else {
			return true;
		}
	}

	public void setCitySpell(String citySpell) {
		this.citySpell = citySpell;
	}
	
	public String getCitySpell() {
		return citySpell;
	}
	
	@Override
	public String toString() {
		return ID + ": " + id + " " + NAME + ": " + cityName + " " + SPELL + " " + ": " + citySpell;
	}
	
	public boolean isCity(){
		if (cityName.length()==1 && Character.isLetter(cityName.charAt(0))) {
			return false;
		} else if (citySpell == null) {
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	// 将对象中的属性保存至目标对象dest中  
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(cityName);  		
		dest.writeString(citySpell);  
	}

	@Override
	public boolean equals(Object o) {
		if (! (o instanceof City)) {
			return false;
		}
		return cityName.equals(((City)o).getCityName());
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
}
