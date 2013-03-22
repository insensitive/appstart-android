package com.appstart.utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertMessages {
	Context context;

	public AlertMessages(Context context) {
		this.context = context;
	}

	public void showNetworkAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("Please check your internet connection.")
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						
						
						
					}
				});

		AlertDialog alert = builder.create();
		alert.setTitle("Connection Problem");
		alert.show();

	}

	public void showserverdataerror() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("No Data found on server.").setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		AlertDialog alert = builder.create();

		alert.show();

	}

	
	
	public void showCutomMessage(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message).setCancelable(false)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						
						dialog.cancel();

					}
				});

		AlertDialog alert = builder.create();
		alert.show();

	}

}
