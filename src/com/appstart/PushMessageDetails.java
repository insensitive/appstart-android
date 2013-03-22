package com.appstart;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appstart.database.DBAdapter;
import com.appstart.tabgroup.TabGroupActivity;
import com.appstart.utility.Constant;
import com.appstart.utility.Util;

public class PushMessageDetails extends Activity implements OnClickListener {

	Button ib_back;
	String back_ground_color="#c5c678";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pushdetails);

		setHeaderBackground();
		// set_text_format();

		Bundle b = getIntent().getExtras();

		String pushmessage_id = b.getString("pushmessage_id");
		boolean showBack = b.getBoolean("showBack");
		
		ib_back = (Button) findViewById(R.id.ib_back_music);

		ib_back.setOnClickListener(this);
		
		if (showBack) {
			ib_back.setVisibility(View.VISIBLE);
		}else{
			ib_back.setVisibility(View.INVISIBLE);
		}
		
		
	
		
		((TextView) findViewById(R.id.txt_music))
		.setText(R.string.push_message);

		

		System.out.println("Push message id::::" + pushmessage_id);

		
		
		DBAdapter dba = new DBAdapter(this);
		dba.open();

		Cursor c = dba.getAllPushMessageDetails(pushmessage_id,
				Constant.LANGUAGE_ID);
		try{
		
		
Cursor c1 = dba.getBackgroundImage("3", Constant.LANGUAGE_ID);
		
		Cursor c2 = dba.getBackgroundColor("3", Constant.LANGUAGE_ID);
		
		
		if(c1.getCount()>0)
		{System.out.print(" set background image: "+c1.getString(0));
		((LinearLayout) findViewById(R.id.lay_pd)).setBackgroundDrawable(ImgDrawableFromFile(getResources(), c1.getString(0)));
		}
		
		if(c2.getCount()>0){
			
			if((c2.getString(0).equals(" ")) ||(c2.getString(0).equals(null))){
				System.out.print(" set background color: ");
				((LinearLayout) findViewById(R.id.lay_pd)).setBackgroundColor(Color
						.parseColor(back_ground_color));
				
			}else{
				if(!c2.getString(0).startsWith("#")){
					System.out.print(" set background color: ");
					((LinearLayout) findViewById(R.id.lay_pd)).setBackgroundColor(Color
							.parseColor(back_ground_color));
				}else{
				
				
					System.out.print(" set background color default: ");
					((LinearLayout) findViewById(R.id.lay_pd)).setBackgroundColor(Color
							.parseColor(c2.getString(0)));
			}
			
			}
			
			
		
		}
		else{
			System.out.print(" set background color default: ");
			((LinearLayout) findViewById(R.id.lay_pd)).setBackgroundColor(Color
					.parseColor(back_ground_color));
		}
		
		}catch(Exception e){
	e.printStackTrace();
	//Toast.makeText(getParent(), "Error", Toast.LENGTH_LONG).show();
	}

		/*Cursor c1 = dba.getBackgroundImage("3", Constant.LANGUAGE_ID);

		if (c1.getCount() > 0) {

			((LinearLayout) findViewById(R.id.lay_pd))
					.setBackgroundDrawable(ImgDrawableFromFile(getResources(),
							c1.getString(0)));
		}*/

		dba.close();

		if (c.getCount() > 0) {

			System.out.println("Loop called:::");

			((TextView) findViewById(R.id.txt_date)).setText(getDatein_format(c
					.getString(5)));
			((TextView) findViewById(R.id.txt_ttl_pm)).setText(c.getString(3));
			((TextView) findViewById(R.id.txt_desc_pm)).setText(Html.fromHtml(c
					.getString(4)));

			((TextView) findViewById(R.id.txt_date)).setTextColor(Util
					.FontColorcode());
			((TextView) findViewById(R.id.txt_ttl_pm)).setTextColor(Util
					.FontColorcode());
			((TextView) findViewById(R.id.txt_desc_pm)).setTextColor(Util
					.FontColorcode());

		}

	}

	public String getDatein_format(String Date1) {

		String outDate = " ";
		
		System.out.print(Date1);
		
		if (outDate.equals(" ")) {
			return outDate;
		
		} else {
			
			String prev_Date = Date1.split(" ")[0].trim();
			String year = prev_Date.split("-")[0].replace("-", "");
			String mon = prev_Date.split("-")[1].replace("-", "");
			String day = prev_Date.split("-")[2].replace("-", "");

			
			Date dT = new Date(Integer.valueOf(year) - 1900,
					Integer.valueOf(mon) - 1, Integer.valueOf(day));
			// SimpleDateFormat sdf = new SimpleDateFormat("EEE,MMM dd,yyyy");

			SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM, yyyy");
			outDate = sdf.format(dT);

			System.out.print(dT);

			return outDate;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v == ib_back) {
			TabGroupActivity parentActivity = (TabGroupActivity) getParent();
			parentActivity.onBackPressed();
		}

	}

	// ---
	public void setHeaderBackground() {

		((RelativeLayout) findViewById(R.id.rel_music))
				.setBackgroundDrawable(Util.HeaderBackground());

	}

	public void set_text_format() {

		Typeface tf = Util.font(this, Constant.FONT_TYPE);

		((TextView) findViewById(R.id.txt_ttl_pm)).setTypeface(tf);
		((TextView) findViewById(R.id.txt_desc_pm)).setTypeface(Util.font(this,
				Constant.FONT_TYPE));

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	public void setBack() {
		DBAdapter dba = new DBAdapter(this);
		dba.open();
		Cursor cc = dba.getPushMessages(Constant.LANGUAGE_ID);
		if (cc.getCount() == 1) {

			((Button) findViewById(R.id.ib_back_music))
					.setVisibility(View.INVISIBLE);
		}
		cc.close();
		dba.close();
	}

	Bitmap myBitmap = null;
	Drawable d = null;

	public Drawable ImgDrawableFromFile(Resources res, String file_name) {

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