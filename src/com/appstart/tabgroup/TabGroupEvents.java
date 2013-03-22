package com.appstart.tabgroup;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import com.appstart.Events;
import com.appstart.EventsDetails;
import com.appstart.database.DBAdapter;
import com.appstart.utility.AlertMessages;
import com.appstart.utility.Constant;

public class TabGroupEvents extends TabGroupActivity {

	AlertMessages message;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		message = new AlertMessages(getParent());
		try {
		DBAdapter dba = new DBAdapter(getParent());
		dba.open();
		Cursor c = dba.getEvents(Constant.LANGUAGE_ID);
		ArrayList<Object> EventID = new ArrayList<Object>();

		System.out.println("evemt count:::::::" + c.getCount());

		for (int i = 0; i < c.getCount(); i++) {

			System.out.println("Event titles" + c.getString(3));

			String row[] = c.getString(1).split(" ");
			String row_end[] = c.getString(2).split(" ");
			if (IsEventWithinMonth(row[0], row[1], row_end[0], row_end[1])) {

				EventID.add(c.getString(0));

			}

			c.moveToNext();
		}
		
	
		
		if (EventID.size() > 1) {

			Bundle b = new Bundle();
			b.putBoolean("showBack", false);
			startChildActivity("Events",
					new Intent(this, Events.class).putExtras(b));

		} else if (EventID.size() == 1) {

			Bundle b = new Bundle();
			b.putString("EventID", EventID.get(0).toString());
			b.putBoolean("showBack", false);
			startChildActivity("EventsDetails", new Intent(this,
					EventsDetails.class).putExtras(b));

		} else {
			message.showCutomMessage("No Events Available");
		}
		
		c.close();
		 dba.close();
	} catch (Exception e) {
		/*Toast.makeText(getApplicationContext(),
				String.valueOf(e), Toast.LENGTH_SHORT).show();*/
		e.printStackTrace();
	}

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

			System.out.println("start time::::::"
					+ cal_start.getTime().toString());
			System.out.println("end time::::::" + cal_end.getTime().toString());

			if (cal_start.before(cal_current)) {

				if (cal_end.before(cal_current)) {
					System.out.println("1");
					return false;
				} else {
					System.out.println("2");
					cal_current.add(Calendar.DAY_OF_YEAR, 30);
					if (cal_end.before(cal_current)) {

						System.out.println("update currnet time::::::"
								+ cal_current.getTime().toString());

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