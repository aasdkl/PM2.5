package com.example.pm25.exception;

import com.example.pm25.R;
import com.example.pm25.util.MyLog;
import com.example.pm25.util.myComponent.MyApplication;

public class DecodeException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 176495460344605398L;

	private static final String MSG_STRING = MyApplication.getContext().getResources().getStringArray(R.array.errors)[4];

	public DecodeException(){
		super(MSG_STRING);
		MyLog.e("DecodeException", MSG_STRING);
	}
	
}
