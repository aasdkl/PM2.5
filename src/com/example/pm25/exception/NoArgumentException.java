package com.example.pm25.exception;

import com.example.pm25.R;
import com.example.pm25.util.MyApplication;
import com.example.pm25.util.MyLog;

public class NoArgumentException extends Exception {
	
	public static final int CODE = 1;
	/**
	 * 
	 */
	private static final long serialVersionUID = -2041801605234433795L;

	private static final String MSG_STRING = MyApplication.getContext().getResources().getStringArray(R.array.errors)[0];
	
	public NoArgumentException() {
		super(MSG_STRING);
		MyLog.e("", MSG_STRING);
	}
}
