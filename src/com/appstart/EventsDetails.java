package com.appstart;

import java.io.File;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appstart.async.ImageLoadFromDownload;
import com.appstart.database.DBAdapter;
import com.appstart.tabgroup.TabGroupActivity;
import com.appstart.utility.Constant;
import com.appstart.utility.Util;

public class EventsDetails extends Activity {

	Button ib_back, btn_add_event, btn_map;
	String back_ground_color="#c5c678";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eventsdetails);

		System.out.print("In EventsDetails");
		
		Bundle b = getIntent().getExtras();
		String eventid = b.getString("EventID");
		
		boolean showBack = b.getBoolean("showBack");
		
		ib_back = (Button) findViewById(R.id.ib_back_music);

		if (showBack) {
			ib_back.setVisibility(View.VISIBLE);
		}else{
			ib_back.setVisibility(View.INVISIBLE);
		}

		setHeaderBackground();

		btn_add_event = (Button) findViewById(R.id.ib_back_md2);
		btn_add_event.setVisibility(View.VISIBLE);

	
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

		Cursor cwe = dba.getScreenName("5", Constant.LANGUAGE_ID);

		if (cwe.getCount() > 0) {

			((TextView) findViewById(R.id.txt_music)).setText(cwe.getString(0));

		}
try{
		Cursor c1 = dba.getBackgroundImage("5", Constant.LANGUAGE_ID);

		Cursor c2 = dba.getBackgroundColor("5", Constant.LANGUAGE_ID);

		if (c1.getCount() > 0) {
			System.out.print(" set background image: " + c1.getString(0));
			((LinearLayout) findViewById(R.id.lay_ed))
					.setBackgroundDrawable(ImgDrawableFromFile(getResources(),
							c1.getString(0)));
		}

		
		if (c2.getCount() > 0) {
			
			if((c2.getString(0).equals(" ")) ||(c2.getString(0).equals(null))){
				System.out.print(" set background color: ");
				((LinearLayout) findViewById(R.id.lay_ed)).setBackgroundColor(Color
						.parseColor(back_ground_color));
				
			}else{
				if(!c2.getString(0).startsWith("#")){
					System.out.print(" set default background color: ");
					((LinearLayout) findViewById(R.id.lay_ed)).setBackgroundColor(Color
							.parseColor(back_ground_color));
				}
				else{
					System.out.print(" set background color: " + c2.getString(0));
					((LinearLayout) findViewById(R.id.lay_ed)).setBackgroundColor(Color
							.parseColor(c2.getString(0)));
				}
				
			}
			
		}else{
			System.out.print(" set default background color: " + c2.getString(0));
			((LinearLayout) findViewById(R.id.lay_ed)).setBackgroundColor(Color
					.parseColor(back_ground_color));
		}
		
}catch(Exception e){
	e.printStackTrace();
	//Toast.makeText(getParent(), "Error", Toast.LENGTH_SHORT).show();
}

		final Cursor c = dba
				.row_query("select * from tbl_Events_Details where EventID='"
						+ eventid + "' and LanguageID='" + Constant.LANGUAGE_ID
						+ "'");

		if (c.getCount() > 0) {

			Object a[] = new Object[2];
			a[0] = (ImageView) findViewById(R.id.iv_ed_logo);
			a[1] = c.getString(7);

			new ImageLoadFromDownload().execute(a);

			((TextView) findViewById(R.id.txt_ttl_ed)).setText(c.getString(5));
			((TextView) findViewById(R.id.txt_desc_ed)).setText(Html.fromHtml(c
					.getString(6)));

			((TextView) findViewById(R.id.txt_st_ed)).setText("Start time: "
					+ c.getString(3));
			((TextView) findViewById(R.id.txt_et_ed)).setText("End time: "
					+ c.getString(4));

			final String row[] = c.getString(3).split(" ");
			final String row1[] = c.getString(4).split(" ");

			btn_add_event.setBackgroundResource(R.drawable.add_reminder_btn);
			btn_add_event.setText("Add");
			btn_add_event.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

					AddEvent(c.getString(5), row[0], row[1], row1[0], row1[1]);

				}
			});

			((Button) findViewById(R.id.ib_map_event))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub

							Intent edit = new Intent(getParent(),
									BasicMapActivity1.class);

							Bundle b = new Bundle();
							b.putString("latitude", "22.38");
							b.putString("longitude", "72.34");
							b.putString("title", c.getString(5));
							edit.putExtras(b);

							TabGroupActivity parentActivity = (TabGroupActivity) getParent();
							parentActivity.startChildActivity(
									"BasicMapActivity1", edit);

						}
					});

		}

		SetProperty();

		dba.close();

	}

	// ---set text property---

	public void SetProperty() {

		Typeface tf = Util.font(this, Constant.FONT_TYPE);

		((TextView) findViewById(R.id.txt_ttl_ed)).setTextColor(Util
				.FontColorcode());
		((TextView) findViewById(R.id.txt_ttl_ed)).setTypeface(tf);
		// ((TextView) findViewById(R.id.txt_ttl_ed)).setTextSize(
		// TypedValue.COMPLEX_UNIT_DIP,
		// Float.parseFloat(Constant.FONT_SIZE));

		((TextView) findViewById(R.id.txt_desc_ed)).setTextColor(Util
				.FontColorcode());
		((TextView) findViewById(R.id.txt_desc_ed)).setTypeface(tf);
		// ((TextView) findViewById(R.id.txt_desc_ed)).setTextSize(
		// TypedValue.COMPLEX_UNIT_DIP,
		// Float.parseFloat(Constant.FONT_SIZE));

		((TextView) findViewById(R.id.txt_st_ed)).setTextColor(Util
				.FontColorcode());
		((TextView) findViewById(R.id.txt_st_ed)).setTypeface(tf);
		// ((TextView) findViewById(R.id.txt_st_ed)).setTextSize(
		// TypedValue.COMPLEX_UNIT_DIP,
		// Float.parseFloat(Constant.FONT_SIZE));

		((TextView) findViewById(R.id.txt_et_ed)).setTextColor(Util
				.FontColorcode());
		((TextView) findViewById(R.id.txt_et_ed)).setTypeface(tf);
		// ((TextView) findViewById(R.id.txt_et_ed)).setTextSize(
		// TypedValue.COMPLEX_UNIT_DIP,
		// Float.parseFloat(Constant.FONT_SIZE));

	}

	public void setHeaderBackground() {

		((RelativeLayout) findViewById(R.id.rel_music))
				.setBackgroundDrawable(Util.HeaderBackground());

	}

	// ---add event to calendar function---
	public void AddEvent(String title, String date, String time,
			String dateEnd, String timeEnd) {

		Intent i;
		Calendar cal = Calendar.getInstance();
		i = new Intent(Intent.ACTION_EDIT);
		i.setType("vnd.android.cursor.item/event");
		i.putExtra("beginTime", Util.GetCalendar(date, time).getTimeInMillis());
		i.putExtra("allDay", true);
		// i.putExtra("rrule", "FREQ=YEARLY");
		i.putExtra("endTime", Util.GetCalendar(dateEnd, timeEnd)
				.getTimeInMillis());
		i.putExtra("title", title);
		startActivity(i);

	}

	static Bitmap myBitmap = null;
	static Drawable d = null;

	public static Drawable ImgDrawableFromFile(Resources res, String file_name) {

		myBitmap = null;
		File imgFile = new File("/data/data/com.appstart/app_my_sub_dir/"
				+ file_name + ".jpg");
		if (imgFile.exists()) {

			myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

			if (myBitmap != null) {
				d = new BitmapDrawable(res, myBitmap);
				return d;
			} else {
				return null;
			}
		}
		return null;

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (myBitmap != null) {
			myBitmap.recycle();
		}

	}

}