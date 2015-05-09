package com.example.pm25;

import com.example.pm25.util.MyLog;
import com.example.pm25.util.myComponent.MyApplication;

import android.app.Activity;
import android.app.ProgressDialog;

public class BaseActivity extends Activity {

	public BaseActivity() {
		MyLog.v("Which Activity is ?", getClass().getSimpleName());
	}
	
	private ProgressDialog progressDialog;

	public void showProgressDialog() {
		showProgressDialog("正在加载……");
	}
	
	public void showProgressDialog(String hint) {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage(hint);
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	public void closeProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}



}
