package com.example.pm25.util.myComponent;

import java.util.List;
import java.util.Locale;

import com.example.pm25.R;
import com.example.pm25.R.drawable;
import com.example.pm25.model.City;

import android.R.string;
import android.content.Context;
import android.opengl.Visibility;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CityAdapter extends ArrayAdapter<City>{
	
	private int viewId;
	
	public CityAdapter(Context context, int textViewResourceId, List<City> objects) {
		super(context, textViewResourceId, objects);
		viewId = textViewResourceId;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		City city = getItem(position);
		View view;
		ViewHolder vh;
		if (convertView==null) {
			view = LayoutInflater.from(getContext()).inflate(viewId, null);
			vh= new ViewHolder((TextView) view.findViewById(R.id.city_item), (TextView) view.findViewById(R.id.key_item));
			view.setTag(vh);
		} else {
			view = convertView;
			vh=(ViewHolder)view.getTag();
		}
		
		String name = city.getCityName();
		
		if (!city.isCity()) {
			name = name.toUpperCase(Locale.ENGLISH);
			vh.textView2.setText(name);
			vh.textView.setVisibility(View.GONE);
			vh.textView2.setVisibility(View.VISIBLE);
		} else {
			vh.textView.setText(name);
			vh.textView2.setVisibility(View.GONE);
			vh.textView.setVisibility(View.VISIBLE);
		}
		return view;
	}
	
	@Override
	public boolean isEnabled(int position) {
		City city = getItem(position);
		if (!city.isCity()) {
			return false;
		}
		return true;
	}
	

	private class ViewHolder{
		TextView textView;
		TextView textView2;
		public ViewHolder(TextView textView, TextView textView2) {
			this.textView=textView;
			this.textView2=textView2;
		}
	}
}

