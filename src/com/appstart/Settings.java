package com.appstart;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appstart.adapter.ListAdapterSettings;
import com.appstart.async.AsyncCallTask;
import com.appstart.database.DBAdapter;
import com.appstart.tabgroup.TabGroupActivity;
import com.appstart.utility.AlertMessages;
import com.appstart.utility.Constant;
import com.appstart.utility.Util;

public class Settings extends Activity {

	ListView list;
	ListAdapterSettings adapter;
	AlertMessages message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		message = new AlertMessages(getParent());
		list = (ListView) findViewById(R.id.list_settings);
		
		setHeaderBackground();
		
		((Button) findViewById(R.id.ib_back_music))
				.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub

						TabGroupActivity parentActivity = (TabGroupActivity) getParent();
						parentActivity.onBackPressed();
						
					}
				});

		((TextView) findViewById(R.id.txt_music)).setText(R.string.settings);
		
		ArrayList<Object> settings_list = new ArrayList<Object>();

		settings_list.add("Language");
		settings_list.add("Refresh");
		settings_list.add("Reset");
		settings_list.add("Push Notification");
		
		
		adapter = new ListAdapterSettings(getParent(), settings_list);
		list.setAdapter(adapter);

		list.setDivider(new ColorDrawable(Util.Colorcode()));
		list.setDividerHeight(1);
		
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == 0) {
					show_alert();
				} else if (arg2 == 1) {
					if (isInternetConnected()) {
						new AsyncCallTask(getParent()).execute("");
						
						
						// Constant.ISLANGUAGECHANGE_CONTACT=true;
//						 Constant.ISLANGUAGECHANGE_PUSHMESSAGE=true;
						// Constant.ISLANGUAGECHANGE_SETTINGS=true;
//						 Constant.ISLANGUAGECHANGE_HOMEWALL=true;
						
						
					} else {
						message.showNetworkAlert();
					}
				} else if (arg2 == 2) {

					Alert();

				}
		}

		});
		
		
		
	}

	
	// --show language selection alert---//

		public void show_alert() {

			DBAdapter dba = new DBAdapter(this);
			dba.open();

			Cursor c = dba.row_query("select * from tbl_Customer_Language");

			final String[] items = new String[c.getCount()];
			final String[] language_id = new String[c.getCount()];
			final String[] language_code = new String[c.getCount()];

			for (int i = 0; i < c.getCount(); i++) {

				Cursor cur_languagettl = dba
						.row_query("select * from tbl_Language where LanguageID='"
								+ c.getString(1) + "'");
				cur_languagettl.moveToFirst();
				String lang_ttl = cur_languagettl.getString(2);
				
				items[i] = lang_ttl;
				language_id[i] = cur_languagettl.getString(0);
				language_code[i] = cur_languagettl.getString(1);
				
				c.moveToNext();
				
			}

			dba.close();

			// final CharSequence[] items = { "Info", "Rename", "Delete" };

			AlertDialog.Builder builder = new AlertDialog.Builder(getParent());
			builder.setTitle("Select Language");
			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {

					Constant.LANGUAGE_ID = language_id[item];
					System.out.println("selectd language id="
							+ Constant.LANGUAGE_ID);

//					Constant.ISLANGUAGECHANGE_CONTACT = true;
//					Constant.ISLANGUAGECHANGE_PUSHMESSAGE = true;
//					Constant.ISLANGUAGECHANGE_SETTINGS = true;
//					Constant.ISLANGUAGECHANGE_HOMEWALL = true;
					
					Locale locale = new Locale(language_code[item]);
					Locale.setDefault(locale);
					Configuration config = new Configuration();
					config.locale = locale;
					getBaseContext().getResources().updateConfiguration(config,
							getBaseContext().getResources().getDisplayMetrics());
					
					System.out.println("language code:::"
							+ Locale.getDefault().getLanguage());
					
					((TextView) findViewById(R.id.txt_music))
							.setText(R.string.settings);
					
					Constant.GET_PARENT = null;
					Intent i = new Intent(getParent(), TabSample.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					getParent().startActivity(i);
					
				}
			}).show();
		}

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
	// ---header---
	public void setHeaderBackground() {

		((RelativeLayout) findViewById(R.id.rel_music))
				.setBackgroundDrawable(Util.HeaderBackground());

	}

	// ---confimation alertbox
	public void Alert() {
		AlertDialog.Builder alert_box = new AlertDialog.Builder(getParent());
		alert_box.setMessage("Are you sure you want to reset app?");
		alert_box.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

						DBAdapter dba = new DBAdapter(Settings.this);

						dba.open();

						
						
						Cursor c=dba.row_query("select 'delete from ' || name from sqlite_master where type = 'table'");
						for(int i=0;i<c.getCount();i++)
						{
							dba.row_query(c.getString(0));
							//System.out.println("result:::::"+c.getString(0));
							c.moveToNext();
						}
						
						
						
						dba.close();
						

						Util.deleteDirectory(new File(
								"/data/data/com.appstart/app_my_sub_dir/"));

						SharedPreferences app_preferences = PreferenceManager
								.getDefaultSharedPreferences(Settings.this);
						
						SharedPreferences.Editor editor = app_preferences
								.edit();
						editor.putBoolean("isFirstTime", true);
						editor.commit();


						
						Constant.GET_PARENT = null;
						Intent i = new Intent(getParent(), Login.class);
						i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						getParent().startActivity(i);
						finish();
						
						
						
						
					}
				});
		alert_box.setNegativeButton("No",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
		alert_box.show();

	}
	
	
	
	
}