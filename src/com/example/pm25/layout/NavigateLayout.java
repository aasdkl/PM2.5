package com.example.pm25.layout;

import com.example.pm25.R;
import com.example.pm25.R.*;
import com.example.pm25.util.myComponent.MyApplication;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class NavigateLayout extends LinearLayout{

	public NavigateLayout(final Context context, AttributeSet attrs) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.navigate, this);
	}
	
}
