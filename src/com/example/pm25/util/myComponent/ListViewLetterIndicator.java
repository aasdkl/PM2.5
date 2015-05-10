package com.example.pm25.util.myComponent;

import java.util.LinkedList;
import java.util.List;

import com.example.pm25.R;
import com.example.pm25.po.City;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
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


/**
 * 
 * 右侧字母索引。可作为一个独立的view插入自己的布局中<br>
 * <br>
 * 描述：<br>
 * 线型布局中嵌套一个线型布局（目的为了增大可滑动区域的面积，若不考虑此可不使用嵌套）。<br>
 * 每个字母插入内嵌的线性布局中，根据控件的高度计算每个字母的尺寸。<br>
 * <br>
 * 使用：<br>
 * 将此view插入你的布局文件中，初始化完成之后设置你的ListView和索引用的数据源(需要自己组织排序数据源)即可。<br>
 * <br>
 * 数据源的组织：若某项不参与排序则数据源中设置为'0'或其他小于'A'的ASCII字符，内部会将所有字符转换成大写，所以务必在外部做好排序。<br>
 * <br>
 * 建议：若你的数据中包括ASCII码为a0、20的字符，建议剔除，如：<br>
 * str.replace(' ', ' '); // a0->20 str.replaceAll(" ", "");<br>
 * <br>
 * 
 * @author ttdevs 2014-07-31
 * 
 */
public class ListViewLetterIndicator extends LinearLayout implements OnScrollListener {

	private LinearLayout llMain;

	private ListView mListView; // ListView
	private List<City> mData; // 数据源
	private List<String> indicatorList; // 数据序列
	private TextView tvAlert; // 显示当前的字母

	private int mIndex; // 当前所处的indicator位置
	private boolean scrollable = true; //

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
	public void setData(ListView lv, List<City> data) {
		setData(lv, data, null);
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
	 */
	public void setData(ListView lv, List<City> data, TextView tv) {
		mListView = lv;
		mData = data;
		tvAlert = tv;
		
		indicatorList.add("#");
		for (City city : data) {
			if (!city.isCity() && city.getCityName().length()==1) {
				indicatorList.add(city.getCityName());
			}
		}

		mListView.setOnScrollListener(this);
		mIndex = 0;
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

			changeIndicatorColor(index);

			char indexIndicator = indicatorList.get(index).charAt(0);// A:65, #:23
			if (indexIndicator < 'A') {
				mListView.setSelection(0);
			} else {
				for (int i = 0; i < mData.size(); i++) {
					City city = mData.get(i);
					String cityName = city.getCityName();
					if (!city.isCity() && cityName.length()==1 
							&& cityName.charAt(0) >= indexIndicator) {
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
		// invalidate();
	}

	@SuppressLint("DefaultLocale")
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		try {
			if (!scrollable || null == mData || mData.size() == 0) {
				return;
			}
			if (null != mData) {
				if (mData.get(firstVisibleItem).isCity() || mData.get(firstVisibleItem).getCityName().length() != 1) {
					return;
				}
				char indicator = mData.get(firstVisibleItem).getCityName().charAt(0);
				if (indicator < 'A') {
					changeIndicatorColor(0);
					return;
				}
				for (int i = 1; i < indicatorList.size(); i++) {
					if (indicatorList.get(i).charAt(0) == indicator) {
						changeIndicatorColor(i);
						return;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void changeIndicatorColor(int index) {
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

		showText(indicatorList.get(mIndex), true);
	}

	private void showText(String text, boolean isShow) {
		if (null != tvAlert) {
			tvAlert.setText(text.toUpperCase());
			tvAlert.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
		}
	}

}