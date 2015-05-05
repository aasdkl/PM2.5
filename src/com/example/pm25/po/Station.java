package com.example.pm25.po;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.pm25.util.Constants;

public final class Station extends BasePlace {
	
	private String stationName;
	private String stationCode;
	private int cityId;
	
	public static final String TABLE_NAME = "Station";
	public static final String NAME = "station_name";
	public static final String CODE = "station_code";
	public static final String CITY_ID = "city_id";
	
	// 必须要创建一个名叫CREATOR的常量。  
	public static final Parcelable.Creator<Station> CREATOR = new Parcelable.Creator<Station>() {  
	        @Override  
	        public Station createFromParcel(Parcel source) {  
	        	return new Station(source);  
	        }  
	        //重写createFromParcel方法，创建并返回一个获得了数据的user对象  
	        @Override  
	        public Station[] newArray(int size) {  
	            return new Station[size];  
	        }  
	};  

	// 带参构造器方法私用化，本构造器仅供类的方法createFromParcel调用    
	private Station(Parcel source){
		id		= source.readInt();
		stationName 	= source.readString();
		stationCode	= source.readString();
		cityId		= source.readInt();
	}
	
	public Station(String stationName, String stationCode, int cityId) {
		this(Constants.NO_VALUE, stationName, stationCode, cityId);
	}
	
	public Station(int id, String stationName, String stationCode, int cityId) {
		this.id = id;
		this.stationName = stationName;
		this.stationCode = stationCode;
		this.cityId = cityId;
	}

	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getStationCode() {
		return stationCode;
	}
	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public boolean isIdExist(){
		if (id==Constants.NO_VALUE) {
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	public String toString() {
		return ID +" "+ id + ", " + NAME +" "+ stationName +", "+CODE +" "+ stationCode +", "+CITY_ID +" "+ cityId;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	// 将对象中的属性保存至目标对象dest中  
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(stationName);  		
		dest.writeString(stationCode);  
		dest.writeInt(cityId);

	}



}
