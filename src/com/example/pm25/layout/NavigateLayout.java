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

public class NavigateLayout extends LinearLayout{

	public NavigateLayout(final Context context, AttributeSet attrs) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.navigate, this);
		Button add = (Button) findViewById(R.id.add);
		Button back = (Button) findViewById(R.id.back);
		
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO
				Toast.makeText(getContext(), context.getResources().getString(R.string.addHint), Toast.LENGTH_SHORT).show();
			}
		});
		
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO
				Toast.makeText(getContext(), "?", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
}
