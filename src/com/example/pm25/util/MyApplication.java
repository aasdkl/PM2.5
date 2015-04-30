package com.example.pm25.util;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

public class MyApplication extends Application{
	
	private static Context context;
	
	@Override
	public void onCreate() {
		context = getApplicationContext();
	}
	
	public static Context getContext() {
		return context;
	}
	
}
