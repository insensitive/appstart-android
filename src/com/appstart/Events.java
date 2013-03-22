package com.appstart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appstart.adapter.ListAdapterEvents;
import com.appstart.database.DBAdapter;
import com.appstart.tabgroup.TabGroupActivity;
import com.appstart.utility.Constant;
import com.appstart.utility.Util;

public class Events extends Activity {

	ListView list;
	ListAdapterEvents adapter;

	ArrayList<Object> a;
	ArrayList<Object> EventID;

	Button ib_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.events);

		Bundle b = getIntent().getExtras();
		boolean showBack = b.getBoolean("showBack");
		ib_back = (Button) findViewById(R.id.ib_back_music);

		if (showBack) {
			ib_back.setVisibility(View.VISIBLE);
		}else{
			ib_back.setVisibility(View.INVISIBLE);
		}

		list = (ListView) findViewById(R.id.list_events);

		a = new ArrayList<Object>();
		EventID = new ArrayList<Object>();

	
		ib_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				TabGroupActivity parentActivity = (TabGroupActivity) getParent();
				parentActivity.onBackPressed();

			}

		});

		DBAdapter dba = new DBAdapter(this);
		dba.open();

		Cursor c0 = dba.getScreenName("5", Constant.LANGUAGE_ID);

		if (c0.getCount() > 0) {

			((TextView) findViewById(R.id.txt_music)).setText(c0.getString(0));

		}

		Cursor c = dba.getEvents(Constant.LANGUAGE_ID);

		for (int i = 0; i < c.getCount(); i++) {

			System.out.println("Event titles" + c.getString(3));

			String row[] = c.getString(1).split(" ");
			String end_row[] = c.getString(2).split(" ");

			if (IsEventWithinMonth(row[0], row[1], end_row[0], end_row[1])) {

				a.add(c.getString(3));
				EventID.add(c.getString(0));

			}

			c.moveToNext();

		}

		dba.close();

		adapter = new ListAdapterEvents(this, a, EventID);
		list.setAdapter(adapter);

		setHeaderBackground();

		((TextView) findViewById(R.id.txt_music)).setText(R.string.events);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				Bundle b = new Bundle();
				b.putString("EventID", EventID.get(arg2).toString());
				b.putBoolean("showBack", true);

				Intent edit = new Intent(getParent(), EventsDetails.class);

				edit.putExtras(b);
				TabGroupActivity parentActivity = (TabGroupActivity) getParent();
				parentActivity.startChildActivity("EventsDetails", edit);

			}

		});

		list.setDivider(new ColorDrawable(Util.Colorcode()));
		list.setDividerHeight(1);

	}

	public void setHeaderBackground() {

		((RelativeLayout) findViewById(R.id.rel_music))
				.setBackgroundDrawable(Util.HeaderBackground());

	}

	public boolean IsEventWithinMonth(String date, String time, String enddate,
			String endtime) {

		Calendar cal_start = Calendar.getInstance();
		Calendar cal_end = Calendar.getInstance();

		Calendar cal_current = Calendar.getInstance();

		try {

			String row[] = date.split("-");
			String row1[] = time.split(":");

			cal_start.set(Calendar.DATE, Integer.parseInt(row[2]));
			cal_start.set(Calendar.MONTH, Integer.parseInt(row[1]) - 1);
			cal_start.set(Calendar.YEAR, Integer.parseInt(row[0]));

			cal_start.set(Calendar.HOUR_OF_DAY, Integer.parseInt(row1[0]));
			cal_start.set(Calendar.MINUTE, Integer.parseInt(row1[1]));

			

			String row_end[] = enddate.split("-");
			String row_end1[] = endtime.split(":");

			cal_end.set(Calendar.DATE, Integer.parseInt(row_end[2]));
			cal_end.set(Calendar.MONTH, Integer.parseInt(row_end[1]) - 1);
			cal_end.set(Calendar.YEAR, Integer.parseInt(row_end[0]));

			cal_end.set(Calendar.HOUR_OF_DAY, Integer.parseInt(row_end1[0]));
			cal_end.set(Calendar.MINUTE, Integer.parseInt(row_end1[1]));

			
			System.out.println("start time::::::" + cal_start.getTime().toString());
			System.out.println("end time::::::" + cal_end.getTime().toString());
			
			
			if (cal_start.before(cal_current)) {

				if (cal_end.before(cal_current)) {
					System.out.println("1");
					return false;
				} else {
					System.out.println("2");
					cal_current.add(Calendar.DAY_OF_YEAR, 30);
					if (cal_end.before(cal_current)) {
						
						System.out.println("update currnet time::::::" + cal_current.getTime().toString());
						
						System.out.println("3");
						return true;
					} else {
						System.out.println("4");
						return false;
					}
				}

				
			}

			System.out.println("5");
			cal_current.add(Calendar.DAY_OF_YEAR, 30);

			if (cal_start.after(cal_current)) {
				return false;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}

		return true;

	}

}