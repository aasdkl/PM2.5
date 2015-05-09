package com.example.pm25;

import com.example.pm25.util.MyLog;
import com.example.pm25.util.myComponent.MyApplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class BaseActivity extends Activity {

	public BaseActivity() {
		MyLog.v("Which Activity is ?", getClass().getSimpleName());
	}
	
	private Builder alertDialog;
	private ProgressDialog progressDialog;
	private boolean isAlertShowing = false;;
	public void showAlertDialog(String hint) {
		if (alertDialog == null) {
			alertDialog = new AlertDialog.Builder(this);
			alertDialog.setTitle("发生意外错误");
			alertDialog.setPositiveButton("取消", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					isAlertShowing = false;
				}
			});
		}
		alertDialog.setMessage(hint);
		if (!isAlertShowing) {
			alertDialog.show();
			isAlertShowing=true;
		}
	}
	
	
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
