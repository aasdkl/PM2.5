package com.example.pm25.exception;

import com.example.pm25.util.MyLog;

public class DataErrorException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2787850901099512837L;

	public DataErrorException(String className, String msg) {
		super(className+": "+msg);
		MyLog.e(className, msg);
	}
}
