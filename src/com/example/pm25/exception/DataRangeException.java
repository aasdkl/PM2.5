package com.example.pm25.exception;

import com.example.pm25.util.MyLog;

public class DataRangeException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2041801605234433795L;

	public DataRangeException(String className, String msg) {
		super(className+": "+msg);
		MyLog.e(className, msg);
	}
}
