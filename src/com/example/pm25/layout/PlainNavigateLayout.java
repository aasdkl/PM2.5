package com.example.pm25.layout;

import com.example.pm25.R;
import com.example.pm25.R.*;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PlainNavigateLayout extends LinearLayout{

	public PlainNavigateLayout(final Context context, AttributeSet attrs) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.plain_navigate, this);
	}
	
}
