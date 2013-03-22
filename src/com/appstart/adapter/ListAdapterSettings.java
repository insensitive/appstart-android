package com.appstart.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.appstart.R;
import com.appstart.Splash;
import com.appstart.TabSample;

public class ListAdapterSettings extends BaseAdapter {

	private Activity activity;
	// private ArrayList&lt;HashMap&lt;String, String&gt;&gt; data;
	private static ArrayList settingid;
	private static LayoutInflater inflater = null;

	public ListAdapterSettings(Activity a, ArrayList c) {

		activity = a;

		settingid = c;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public int getCount() {
		return settingid.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;

		convertView = null;
		if (position == 3) {
			vi = inflater.inflate(R.layout.lay_row_psetting, null);
			((TextView) vi.findViewById(R.id.txt_ttl_ps)).setText(settingid
					.get(position).toString());

			final View vv = vi;

			SharedPreferences app_preferences = PreferenceManager
					.getDefaultSharedPreferences(activity);

			
			
			((ToggleButton) vi.findViewById(R.id.tb1))
					.setChecked(app_preferences.getBoolean(
							"isPushNotification", true));
			
			((ToggleButton) vi.findViewById(R.id.tb1))
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton arg0,
								boolean arg1) {
							// TODO Auto-generated method stub

							// if(arg1)
							// {
							// send_notification(activity);
							// }

							SharedPreferences app_preferences = PreferenceManager
									.getDefaultSharedPreferences(activity);

							SharedPreferences.Editor editor = app_preferences
									.edit();

							editor.putBoolean("isPushNotification", arg1);
							editor.commit();

						}
					});

			vi.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

					if (((ToggleButton) vv.findViewById(R.id.tb1)).isChecked()) {
						((ToggleButton) vv.findViewById(R.id.tb1))
								.setChecked(false);

					} else {
						((ToggleButton) vv.findViewById(R.id.tb1))
								.setChecked(true);

					}

				}
			});

		} else {
			if (convertView == null) {
				vi = inflater.inflate(R.layout.row_contact, null);
			}
			TextView title = (TextView) vi
					.findViewById(R.id.txt_ttlcontact_row); // title
			String song = settingid.get(position).toString();
			title.setText(song);

		}
		return vi;

	}

	// ------
	public static void send_notification(Context ctx) {

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				ctx).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("My notification")
				.setContentText("Hello World!");
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(ctx, TabSample.class);

		// The stack builder object will contain an artificial back stack for
		// the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(TabSample.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) ctx
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		int mId = 0;
		mBuilder.setAutoCancel(true);
		mNotificationManager.notify(mId, mBuilder.build());

	}

}