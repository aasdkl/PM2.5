package com.example.pm25.util;

import android.util.Log;

public class MyLog {
	private static final Logs LEVEL = Logs.VERBOSE;
	enum Logs{
		VERBOSE,
		DEBUG,
		INFO,
		WARN,
		ERROR,
		NOTHING;
		public boolean isLowerThan(Logs wish){
			if (this.ordinal() <= wish.ordinal()) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public static void v(String tag, String msg) {
		if (LEVEL.isLowerThan(Logs.VERBOSE)) {
			Log.v(tag, msg);
		}
	}
	public static void d(String tag, String msg) {
		if (LEVEL.isLowerThan(Logs.DEBUG)) {
			Log.d(tag, msg);
		}
	}
	public static void i(String tag, String msg) {
		if (LEVEL.isLowerThan(Logs.INFO)) {
			Log.i(tag, msg);
		}
	}
	public static void w(String tag, String msg) {
		if (LEVEL.isLowerThan(Logs.WARN)) {
			Log.w(tag, msg);
		}
	}
	public static void e(String tag, String msg) {
		if (LEVEL.isLowerThan(Logs.ERROR)) {
			Log.e(tag, msg);
		}
	}
}
