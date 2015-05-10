package com.example.pm25.util.myComponent;

import java.util.LinkedList;
import java.util.List;

import com.example.pm25.R;
import com.example.pm25.po.City;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ListViewLetterIndicator extends LinearLayout implements OnScrollListener {

	private LinearLayout llMain;

	private ListView mListView; // ListView
	private List<City> mData; // 数据源
	private List<String> indicatorList; // 数据序列
	private TextView tvAlert; // 显示当前的字母

	private int mIndex; // 当前所处的indicator位置
	private boolean scrollable = true; //
	private int ignoreLength;

	public ListViewLetterIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);

		setOrientation(LinearLayout.VERTICAL);
		setBackgroundColor(getResources().getColor(R.color.very_alpha_white));

		indicatorList = new LinkedList<>();
		llMain = new LinearLayout(getContext());
		llMain.setOrientation(LinearLayout.VERTICAL);
		llMain.setGravity(Gravity.CENTER);

		int width = (int) getResources().getDimension(R.dimen.letter_indicator_width);
		addView(llMain, width, LinearLayout.LayoutParams.MATCH_PARENT);
	}

	/**
	 * 设置数据源
	 * 
	 * @param lv
	 *            绑定的ListView
	 * @param data
	 *            排序用的数据源
	 */
	public void setData(ListView lv, List<City> data, int ignoreLength) {
		setData(lv, data, null, ignoreLength);
	}

	/**
	 * 设置数据源
	 * 
	 * @param lv
	 *            绑定的ListView
	 * @param data
	 *            排序用的数据源
	 * @param tv
	 *            显示当前所处字母的TextView
	 * @param iLength 
	 */
	public void setData(ListView lv, List<City> data, TextView tv, int ignoreLength) {
		mListView = lv;
		mData = data;
		tvAlert = tv;
		this.ignoreLength = ignoreLength;
		
		indicatorList.add("#");
		for (City city : data) {
			if (!city.isCity() && city.getCityName().length()==1) {
				indicatorList.add(city.getCityName());
			}
		}

		mListView.setOnScrollListener(this);
		mIndex = 0;
		changeIndicatorColor(0, false);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		initView();
	}

	private void initView() {
		int childCount = llMain.getChildCount();
		if (childCount == indicatorList.size()) {
			// llMain.invalidate();
			return;
		}

		int height = llMain.getHeight();
		int textHeight = (int) Math.floor(height / (indicatorList.size() + 6));

		LinearLayout.LayoutParams llpText = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		for (int i = 0; i < indicatorList.size(); i++) {
			String str = String.valueOf(indicatorList.get(i));
			TextView tvIndicator = new TextView(getContext());
			tvIndicator.setText(str.toUpperCase());
			tvIndicator.setIncludeFontPadding(false);
			tvIndicator.setTextSize(TypedValue.COMPLEX_UNIT_PX, textHeight);
			tvIndicator.setTextColor(getResources().getColor(R.color.gray));
			llMain.addView(tvIndicator, llpText);
		}
		
	}

	@SuppressLint("DefaultLocale")
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			scrollable = false;
			float y = ev.getY();
			float childY = 0;
			int index = 0;
			for (int i = 0; i < indicatorList.size(); i++) {
				TextView view = (TextView) llMain.getChildAt(i);
				childY = view.getTop();
				int height = view.getHeight();
				if (childY < y && childY + height > y) {
					index = i;
					break;
				}
				view = null; // not neccessary
			}

			TextView view = (TextView) llMain.getChildAt(indicatorList.size() - 1);
			if (y > view.getTop()) {
				index = indicatorList.size() - 1;
			}
			view = null;

			changeIndicatorColor(index, true);

			String indexIndicator = indicatorList.get(index);
			if (indexIndicator.equals("#")) {
				mListView.setSelection(0);
			} else {
				for (int i = 0; i < mData.size(); i++) {
					City city = mData.get(i);
					String cityName = city.getCityName();
					if (!city.isCity() && cityName.length()==1 
							&& cityName.equals(indexIndicator)) {
						mListView.setSelection(i);
						return true;
					}
				}
			}

			break;
		case MotionEvent.ACTION_UP:
			postDelayed(new Runnable() {
				@Override
				public void run() {
					scrollable = true;
				}
			}, 100);
			showText("", false);
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@SuppressLint("DefaultLocale")
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		try {
			if (!scrollable || null == mData || mData.size() == 0 || mData.get(firstVisibleItem).isCity()) {
				return;
			}
			City now = mData.get(firstVisibleItem);
			if (firstVisibleItem < ignoreLength) {
				changeIndicatorColor(0, false);
				return;
			}
			String indicator = now.getCityName();
			changeIndicatorColor(indicatorList.indexOf(indicator), false);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void changeIndicatorColor(int index, boolean isScroll) {
		if (mIndex != 0 && mIndex == index) {
			return;
		}
		
		TextView tv = (TextView) llMain.getChildAt(mIndex);
		
		if(tv==null){
			showText("", false);
			return;
		}
		
		tv.setTextColor(getResources().getColor(R.color.gray));
		tv = (TextView) llMain.getChildAt(index);
		tv.setTextColor(getResources().getColor(R.color.black));
		mIndex = index;
		if (isScroll) {
			showText(indicatorList.get(mIndex), true);
		}
	}

	private void showText(String text, boolean isShow) {
		if (null == tvAlert) {
			return;
		} else {
			tvAlert.setText(text.toUpperCase());
			tvAlert.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
		}
	}

}