package com.example.pm25.util.myComponent;

import java.util.List;

import com.example.pm25.R;
import com.example.pm25.model.City;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CityAdapter extends ArrayAdapter<City>{
	
	int viewId;
	
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
			vh= new ViewHolder((TextView) view.findViewById(R.id.city_item));
			view.setTag(vh);
		} else {
			view = convertView;
			vh=(ViewHolder)view.getTag();
		}
		
		vh.textView.setText(city.getCityName());

		return view;

	}
	
}

class ViewHolder{
	TextView textView;
	public ViewHolder(TextView textView) {
		this.textView=textView;
	}
}