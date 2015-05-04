package com.example.pm25;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.example.pm25.R.id;
import com.example.pm25.util.Constants;
import com.example.pm25.util.MyLog;
import com.example.pm25.util.PM25Constants;
import com.example.pm25.util.myComponent.StationAdapter;

import android.R.anim;
import android.R.bool;
import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class DetailActivity extends Activity {

	public static final int CIRCLES_NUM = 8;

	private LinearLayout[] circles = new LinearLayout[CIRCLES_NUM];
	
	private View aqiAskBtn;
	private ViewFlipper aqiAns;

	private LinearLayout elseAskBtn1;
	private LinearLayout elseAskBtn2;
	private ViewFlipper elseAns;
	private Spinner titleSpinner;
	
	private StationAdapter<String> stationAdapter;

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
		elseAskBtn1 = (LinearLayout) findViewById(R.id.c1);
		elseAskBtn1.setOnTouchListener(new OnCirclesTouchListener(elseAns, Part.UP,
				this));
		elseAskBtn2 = (LinearLayout) findViewById(R.id.c2);
		elseAskBtn2.setOnTouchListener(new OnCirclesTouchListener(elseAns, Part.DOWN,
				this));

		titleSpinner = (Spinner) findViewById(R.id.title);

		// spinner
		List<String> stations = new ArrayList<>();
		stations.add("南京");
		stations.add("仙林大学城大学城");
		stations.add("鼓楼");
		stations.add("中华门");

		stationAdapter = new StationAdapter<String>(this, 
				R.layout.spinner_checked_text, stations);
		titleSpinner.setAdapter(stationAdapter);
		
		
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
		
		String[] nameArr = PM25Constants.getNameArray();
		
		// 因为nameArr中的第一位是AQI，这里需要被忽略
		for (int i = 0; i < CIRCLES_NUM - 1; i++) {
			((TextView) circles[i].findViewById(R.id.des)).setText(nameArr[i+1]);
		}
		//最后一个，是空的
		((TextView) circles[CIRCLES_NUM-1].findViewById(R.id.num)).setText(
				getResources().getString(R.string.moeEye));
		((TextView) circles[CIRCLES_NUM-1].findViewById(R.id.des)).setText(
				Html.fromHtml(getResources().getString(R.string.moeMouse)));
	}

}

/**
 * AQI按钮的点击效果
 * @author Administrator
 */
final class OnAQITouchListener implements OnTouchListener {

	private ViewFlipper aqiAns;
	private Context context;

	public OnAQITouchListener(ViewFlipper aqiAns, Context context) {
		this.aqiAns = aqiAns;
		this.context = context;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
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
	}

}

/**
 * 污染物细节的点击效果
 * @author Administrator
 */
final class OnCirclesTouchListener implements OnTouchListener {

	private ViewFlipper elseAns;
	private Context context;
	private TextView elseTextArea;
	private String toShow;
	private Part part;
	
	public OnCirclesTouchListener(ViewFlipper elseAns, Part part, Context context) {
		this.elseAns = elseAns;
		this.context = context;
		this.part = part;
		elseTextArea = (TextView) elseAns.findViewById(R.id.elseText);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (isInCircleArea(v, event)) {
					elseTextArea.setText(toShow);
					elseAns.setVisibility(View.VISIBLE);
					elseAns.startAnimation(AnimationUtils
							.loadAnimation(context,
									R.anim.push_in));
					break;
				} else {
					return false;
				}
			case MotionEvent.ACTION_UP:
				v.performClick();
				elseAns.setVisibility(View.INVISIBLE);
				elseAns.startAnimation(AnimationUtils
						.loadAnimation(context,
								R.anim.push_out));
				break;
			default:
				break;
			}
			return true;

	}

	// 圆形区域的矩形范围，直接通过坐标x在该行的四分之多少进行定位
	// ! WARN 具有幻数4
	private boolean isInCircleArea(View v, MotionEvent event) {
		int lineIndex = Constants.NO_VALUE;// 定位，从0开始
		int xTouch = (int) event.getX();
		int halfWidth = v.getWidth() >> 1;
		
		if (xTouch < halfWidth) {// 0or1
			lineIndex=0;
		} else {		// 2or3
			lineIndex=2;
		}
		// 再一半（四分之一）
		int quarterWidth = halfWidth >> 1;
		
		// 是否在右侧
		boolean isInRhs = (lineIndex == 0) ? (xTouch > quarterWidth) : (xTouch > quarterWidth + halfWidth);
		if (isInRhs) {
			lineIndex += 1;
		}
		
		int circleIndex = part.ordinal() * 4 + lineIndex;
		MyLog.e("circleIndex", ""+circleIndex);

		if (circleIndex == DetailActivity.CIRCLES_NUM - 1) {
			return false;
		} else {
			// 因为第一个是AQI，在圆中没有AQI
			toShow = PM25Constants.getDescribeArray()[circleIndex + 1];
			return true;
		}
	}

}

enum Part {
	UP, DOWN;
}

