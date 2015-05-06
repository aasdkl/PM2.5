package com.example.pm25;

import java.util.LinkedList;
import java.util.List;

import com.example.pm25.R;
import com.example.pm25.R.id;
import com.example.pm25.model.ModelCallBackListener;
import com.example.pm25.model.ModelService;
import com.example.pm25.po.City;
import com.example.pm25.util.myComponent.CityAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

	private ListView listView;
	private CityAdapter adapter;
	private List<City> cityList = new LinkedList<>();
	private City selectedCity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
	
		listView = (ListView) findViewById(R.id.list_view);
		adapter = new CityAdapter(this, R.layout.city_item, cityList);

		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				selectedCity = cityList.get(position);
				if (selectedCity.isCity()) {
					DetailActivity.actionStart(MainActivity.this, selectedCity, 
							isInterest(selectedCity));
				}
			}

		});
		getCities();
	}

	
	private boolean isInterest(City selectedCity) {
		List<City> list = ModelService.getInterestedCities();
		return list.contains(selectedCity);
	}
	
	private void getCities() {
		showProgressDialog();
		ModelService.getCities(new ModelCallBackListener<City>() {
			@Override
			public void onFinish(final List<City> cities) {
				if (cities==null || cities.size()==0) {
					closeProgressDialog();
				} else {
					// 刷新界面
					runOnUiThread(new Runnable() {
						public void run() {
							cityList.clear();
							addInterestedCities();
							for (City city : cities) {
								cityList.add(city);
							}
							adapter.notifyDataSetChanged();
							listView.setSelection(0);
							closeProgressDialog();
						}
					});
				}
			}
			@Override
			public void onError(final Exception e) {
				e.printStackTrace();
				runOnUiThread(new Runnable() {
					public void run() {
						closeProgressDialog();
						Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

	private void addInterestedCities() {
		cityList.clear();
		List<City> cities = ModelService.getInterestedCities();
		if (cities!=null && cities.size()!=0) {
			cityList.add(new City(getResources().getString(R.string.quickIn)));
			for (City city : cities) {
				cityList.add(city);
			}
		}
	}
	
	@Override
	//TODO 返回值这里有问题
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == DetailActivity.REQUEST_CODE && resultCode == RESULT_OK) {
			boolean isInterested = data.getBooleanExtra(DetailActivity.RETURN_IS_INTERESTED, true);
			selectedCity = data.getParcelableExtra(DetailActivity.RETURN_SELECTED_CITY);
			City firstItem = cityList.get(0);
			
			// 如果有快捷入口标志先删掉
			if ((!firstItem.isCity()) && firstItem.getCityName().equals(getResources().getString(R.string.quickIn))) {
				cityList.remove(0);
			}
			boolean isShown = false;
			for (int i = 0; i < cityList.size(); i++) {
				City thisCity = cityList.get(i);
				if (thisCity.equals(selectedCity)) {
					if (!isInterested) {
						cityList.remove(i);
						break;
					} else {
						isShown = true;
					}
				} else if (!thisCity.isCity()) {
					// 遍历到了A字段发现快捷入口中没有这个值
					if (isInterested && !isShown) {
						cityList.add(i, selectedCity);
					}
					break;
				}
			}
			// 如果第一项是城市不是A，需要加入快捷入口标志
			firstItem = cityList.get(0);
			if (firstItem.isCity()) {
				cityList.add(0, new City(getResources().getString(R.string.quickIn)));
			}
			adapter.notifyDataSetChanged();
		}
	}
	
}
