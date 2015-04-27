package com.example.fm25.exception;

import com.example.fm25.util.MyLog;

public class DataRangeException extends Exception {
	public DataRangeException(String className, String msg) {
		super(className+": "+msg);
		MyLog.e(className, "AQI的值不能为负数");
	}
}
