package com.appstart.application;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Appstartapplication extends Application {
	String TAG = Appstartapplication.class.getSimpleName();
	 
	private ProgressDialog progressDialog = null;

	public static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		Appstartapplication.context = getApplicationContext();
		
		System.out.println("Application started:::::::");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onLowMemory() {
		Log.i(TAG, "onLowMemory()");
		super.onLowMemory();
		System.gc();
		Runtime.getRuntime().gc();
	}

	@Override
	public void onTerminate() {
		Log.i(TAG, "onTerminate()");
		super.onTerminate();
	}

	

	public void startProgressDialog(Context context, String title,
			String message) {
		if (context != null) {
			if (progressDialog == null)
				progressDialog = new ProgressDialog(context);

			if (!progressDialog.isShowing())
				progressDialog = ProgressDialog.show(context, title, message);
		} else {
			Log.d(getClass().getSimpleName(),
					"startProgressDialog : Context is null");
		}
	}

	public void stopProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing())
			progressDialog.dismiss();
	}

	/*
	 * This will work only in device. Simulator will always return true
	 */
	public boolean isInternetConnected() {
		ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean ret = true;
		if (conMgr != null) {
			NetworkInfo i = conMgr.getActiveNetworkInfo();

			if (i != null) {
				if (!i.isConnected()) {
					ret = false;
				}

				if (!i.isAvailable()) {
					ret = false;
				}
			}

			if (i == null)
				ret = false;
		} else
			ret = false;
		return ret;
	}

	public boolean checkInternetConnectionAndDisplayAlertIfNotExist(
			Context context) {
		if (isInternetConnected())
			return true;
		else {
//			Utils.ShowAlertMessage(Constants.ALERT_OK, context,
//					Constants.ALERT_TITLE_ERROR,
//					Constants.ALERT_MSG_NO_INTERNET);
			return false;
		}
	}
}