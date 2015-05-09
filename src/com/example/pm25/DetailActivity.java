package com.example.pm25;

import java.util.ArrayList;
import java.util.List;

import com.example.pm25.model.ModelCallBackListener;
import com.example.pm25.model.ModelService;
import com.example.pm25.po.BasePlace;
import com.example.pm25.po.City;
import com.example.pm25.po.Station;
import com.example.pm25.po.AirQuality;
import com.example.pm25.util.Constants;
import com.example.pm25.util.PM25Constants;
import com.example.pm25.util.myComponent.StationAdapter;

import android.R.bool;
import android.R.integer;
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class DetailActivity extends BaseActivity {

	public static final int CIRCLES_NUM = 8;
	// 下方细节的按钮
	private LinearLayout[] circles = new LinearLayout[CIRCLES_NUM];
	
	private View aqiAskBtn;
	private ViewFlipper aqiAns;

	private LinearLayout elseAskBtn1;
	private LinearLayout elseAskBtn2;
	private ViewFlipper elseAns;

	private Spinner titleSpinner;
	private StationAdapter<BasePlace> stationAdapter;
	
	private City selectedCity;
	private boolean isInterested;
	
	public static final int REQUEST_CODE = 233;
	// 所选择的位置数据
	private List<BasePlace> placeSelectedList = new ArrayList<>();

	public static void actionStart(Fragment preFragment, City selectedCity, boolean isInterest) {
		Intent intent = new Intent(preFragment.getActivity(), DetailActivity.class);
		intent.putExtra("city", selectedCity);
		intent.putExtra("isInterest", isInterest);
		preFragment.startActivityForResult(intent, REQUEST_CODE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		Intent intent = getIntent();
		selectedCity = intent.getParcelableExtra("city");
		isInterested = intent.getBooleanExtra("isInterest", false);

		setHelperButtons();

		//准备
		View actionbarLayout = LayoutInflater.from(this).inflate(R.layout.navigate, null);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowCustomEnabled(true);

//		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) actionbarLayout.getLayoutParams();
//	        mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK | Gravity.CENTER_HORIZONTAL;
		ActionBar.LayoutParams lp =new ActionBar.LayoutParams(
				ActionBar.LayoutParams.MATCH_PARENT,
				ActionBar.LayoutParams.MATCH_PARENT,
				Gravity.CENTER);
		actionBar.setCustomView(actionbarLayout, lp);
		
		
		
		titleSpinner = (Spinner) actionbarLayout.findViewById(R.id.title);
		
		// 初始化只有当前区域的spinner
		placeSelectedList.add(selectedCity);
		
		stationAdapter = new StationAdapter<BasePlace>(this, 
				R.layout.spinner_checked_text, placeSelectedList);
		titleSpinner.setAdapter(stationAdapter);
		titleSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent,
					View view, int position, long id) {
				BasePlace selected = stationAdapter.getItem(position);
				getCityDetails(BasePlace.getStationInstanceOrReturnNull(selected));
			}
			@Override  
			public void onNothingSelected(AdapterView<?> parent) {  
			}
		});
		
		
		// 获取station列表
		ModelService.getStations(selectedCity, new ModelCallBackListener<Station>() {
			@Override
			public void onFinish(final List<Station> stations) {
				if (stations==null || stations.size()==0) {
					closeProgressDialog();
				} else {
					updateStation(stations);
				}
			}
			private void updateStation(final List<Station> stations) {
				// 刷新界面
				runOnUiThread(new Runnable() {
					public void run() {
						for (Station station : stations) {
							placeSelectedList.add(station);
						}
						stationAdapter.notifyDataSetChanged();
					}
				});
			}
			@Override
			public void onError(final Exception e) {
				e.printStackTrace();
				runOnUiThread(new Runnable() {
					public void run() {
						showAlertDialog(e.getMessage());
					}
				});
			}
		});

		getCityDetails(null);
	}

	
	private void addInterestOrReomve() {
		if (isInterested) {
			ModelService.removeInterested(selectedCity);
		} else {
			ModelService.addInterested(selectedCity);
		}
		isInterested = !isInterested;
	}
	
	/**
	 * 获取空气质量的数据
	 * 包括观测点Station列表、每个Station的值
	 * @param Stations station 如果为null则代表是城市平均水平
	 */
	private void getCityDetails(Station station) {
		
		showProgressDialog( "正在更新数据……");
		
		// 获取数据
		ModelService.getDetails(selectedCity, station, new ModelCallBackListener<AirQuality>() {
			@Override
			public void onFinish(final List<AirQuality> cityDetails) {
				if (cityDetails==null || cityDetails.size()==0) {
					closeProgressDialog();
				} else {
					updateDetails(cityDetails);
				}
			}
			private void updateDetails(final List<AirQuality> cityDetails) {
				// 刷新界面
				runOnUiThread(new Runnable() {
					public void run() {
						AirQuality quality = cityDetails.get(0);
						putDataOnViews(quality);
						closeProgressDialog();
					}
				});
			}
			@Override
			public void onError(final Exception e) {
				e.printStackTrace();
				runOnUiThread(new Runnable() {
					public void run() {
						closeProgressDialog();
						showAlertDialog(e.getMessage());
					}
				});
			}
		});
	}

	/**
	 * 将数据放入view中
	 * @param quality
	 */
	private void putDataOnViews(AirQuality quality) {
		if (quality==null) {
			return;
		}
		
		// set background
		findViewById(R.id.matchScene).setBackground(quality.getAqiDetail().getColor());
		
		((TextView) findViewById(R.id.pm)).setText(""+quality.getAqiDetail().getAqi());
	
		String aqiDesc = getResources().getString(R.string.describe);
		aqiDesc = String.format(aqiDesc, quality.getAqiDetail().getLevel(), quality.getAqiDetail().getQuality()); 
		((TextView) findViewById(R.id.pm_describe)).setText(aqiDesc);
		
		((TextView) findViewById(R.id.time)).setText(quality.getTime_point());

		String primaryPoll = getResources().getString(R.string.primary_pollutant_detail);
		primaryPoll = String.format(primaryPoll, quality.getPrimaryPollutant()); 
		((TextView) findViewById(R.id.primary_pollutant)).setText(primaryPoll);
		
		((TextView) findViewById(R.id.aqi_effect)).setText(quality.getAqiDetail().getEffect());
		((TextView) findViewById(R.id.aqi_suggestion)).setText(quality.getAqiDetail().getSuggestion());
		
		((TextView) circles[0].findViewById(R.id.num)).setText(""+quality.getPm25());
		((TextView) circles[1].findViewById(R.id.num)).setText(""+quality.getPm10());
		((TextView) circles[2].findViewById(R.id.num)).setText(""+quality.getCo());
		((TextView) circles[3].findViewById(R.id.num)).setText(""+quality.getNo2());
		((TextView) circles[4].findViewById(R.id.num)).setText(""+quality.getO3_1h());
		((TextView) circles[5].findViewById(R.id.num)).setText(""+quality.getO3_8h());
		((TextView) circles[6].findViewById(R.id.num)).setText(""+quality.getSo2());

	}
	/**
	 * 初始化所有的提示按钮
	 */
	private void setHelperButtons() {
		// 初始化下方按钮
		setCircles();

		// 初始化AQI按钮及其监听
		aqiAns = (ViewFlipper) findViewById(R.id.what_is_aqi);
		aqiAskBtn = (View) findViewById(R.id.aqi_btn);
		aqiAskBtn.setOnTouchListener(new OnAQITouchListener(aqiAns,
				this));

		// 初始化下方按钮监听
		elseAns = (ViewFlipper) findViewById(R.id.what_is_else);
		elseAskBtn1 = (LinearLayout) findViewById(R.id.c1);
		elseAskBtn1.setOnTouchListener(new OnCirclesTouchListener(
				elseAns, OnCirclesTouchListener.Part.UP, this));
		elseAskBtn2 = (LinearLayout) findViewById(R.id.c2);
		elseAskBtn2.setOnTouchListener(new OnCirclesTouchListener(
				elseAns, OnCirclesTouchListener.Part.DOWN, this));
	}
	
	/**
	 * 初始化下方8个提示按钮
	 */
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

	@Override
	public void onBackPressed() {
		doFinish();
	}
	
	public static final String RETURN_IS_INTERESTED = "isInterested";
	public static final String RETURN_SELECTED_CITY = "city";
	private void doFinish() {
		// 数据回调
		Intent intent = new Intent();
		intent.putExtra(RETURN_IS_INTERESTED, isInterested);
		intent.putExtra(RETURN_SELECTED_CITY, selectedCity);
		setResult(RESULT_OK, intent);
		finish();
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem add = menu.findItem(R.id.action_add);
		if (isInterested) {
			add.setIcon(getResources().getDrawable(R.drawable.remove));
			add.setTitle(getResources().getString(R.string.minus));
		} else {
			add.setIcon(getResources().getDrawable(R.drawable.add));
			add.setTitle(getResources().getString(R.string.add));
		}
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_detail, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// 处理动作按钮的点击事件
		switch (item.getItemId()) {
		case android.R.id.home:
			doFinish();
			return true;
		case R.id.action_add:
			addInterestOrReomve();
			if (isInterested) {
				Toast.makeText(DetailActivity.this, getResources().getString(R.string.addHint), Toast.LENGTH_SHORT).show();
				item.setTitle(getResources().getString(R.string.minus));
				item.setIcon(getResources().getDrawable(R.drawable.remove));
			} else {
				Toast.makeText(DetailActivity.this, getResources().getString(R.string.removeHint), Toast.LENGTH_SHORT).show();
				item.setTitle(getResources().getString(R.string.add));
				item.setIcon(getResources().getDrawable(R.drawable.add));
			}
			getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
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

	// 该点击块所在位置的enum
	public enum Part {
		UP, DOWN;
	}

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

		if (circleIndex == DetailActivity.CIRCLES_NUM - 1) {
			return false;
		} else {
			// 因为第一个是AQI，在圆中没有AQI
			toShow = PM25Constants.getDescribeArray()[circleIndex + 1];
			return true;
		}
	}
	

}

