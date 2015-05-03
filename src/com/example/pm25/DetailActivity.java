package com.example.pm25;

import java.util.Iterator;

import com.example.pm25.R.id;
import com.example.pm25.util.MyLog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class DetailActivity extends Activity {

	private LinearLayout[] circles = new LinearLayout[8];
	private View aqiAskBtn;
	private LinearLayout elseAskBtn;
	private ViewFlipper aqiAns;
	private ViewFlipper elseAns;

	public static void actionStart(Context context, String city) {
		Intent intent = new Intent(context, DetailActivity.class);
		intent.putExtra("name", city);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_detail);

		Intent intent = getIntent();
		String cityName = intent.getStringExtra("name");
		Toast.makeText(this, cityName, Toast.LENGTH_SHORT).show();

		setCircles();

		aqiAns = (ViewFlipper) findViewById(R.id.what_is_aqi);
		aqiAskBtn = (View) findViewById(R.id.aqi_btn);
		aqiAskBtn.setOnTouchListener(new OnAQITouchListener(aqiAns,
				this));

		elseAns = (ViewFlipper) findViewById(R.id.what_is_else);
		elseAskBtn = (LinearLayout) findViewById(R.id.circleScene);
		elseAskBtn.setOnTouchListener(new OnCirclesTouchListener(
				elseAns, this));

	}

	private void setCircles() {
		circles[0] = (LinearLayout) findViewById(R.id.circle1);
		circles[1] = (LinearLayout) findViewById(R.id.circle2);
		circles[2] = (LinearLayout) findViewById(R.id.circle3);
		circles[3] = (LinearLayout) findViewById(R.id.circle4);
		circles[4] = (LinearLayout) findViewById(R.id.circle5);
		circles[5] = (LinearLayout) findViewById(R.id.circle6);
		circles[6] = (LinearLayout) findViewById(R.id.circle7);
		circles[7] = (LinearLayout) findViewById(R.id.circle8);
	}

}

final class OnCirclesTouchListener implements OnTouchListener {

	private ViewFlipper elseAns;
	private Context context;

	public OnCirclesTouchListener(ViewFlipper elseAns, Context context) {
		this.elseAns = elseAns;
		this.context = context;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			MyLog.d("Detail Activity", "ACTION_DOWN");
			elseAns.setVisibility(View.VISIBLE);
			elseAns.startAnimation(AnimationUtils.loadAnimation(
					context, R.anim.push_in));
			break;
		case MotionEvent.ACTION_UP:
			MyLog.d("Detail Activity", "ACTION_UP");
			v.performClick();
			elseAns.setVisibility(View.INVISIBLE);
			elseAns.startAnimation(AnimationUtils.loadAnimation(
					context, R.anim.push_out));
			break;
		default:
			break;
		}
		return true;
	}
}

final class OnAQITouchListener implements OnTouchListener {

	private ViewFlipper aqiAns;
	private TextView aqiTextArea;
	private Context context;
	private String[] details;

	public OnAQITouchListener(ViewFlipper aqiAns, Context context) {
		this.aqiAns = aqiAns;
		this.context = context;
		aqiTextArea = (TextView) aqiAns.findViewById(R.id.elseText);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (isInCircleArea()) {
			switch (event.getAction()) {
			// 根据圆形判断给text赋值
			case MotionEvent.ACTION_DOWN:
				aqiAns.setVisibility(View.VISIBLE);
				aqiAns.startAnimation(AnimationUtils.loadAnimation(
						context, R.anim.push_in));
				break;
			case MotionEvent.ACTION_UP:
				v.performClick();
				aqiAns.setVisibility(View.INVISIBLE);
				aqiAns.startAnimation(AnimationUtils.loadAnimation(
						context, R.anim.push_out));
				break;
			default:
				break;
			}
			return true;
		} else {
			return false;
		}
	}

	// 圆形区域的矩形范围
	private boolean isInCircleArea() {
		return false;
	}

}
