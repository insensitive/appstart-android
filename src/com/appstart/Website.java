package com.appstart;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appstart.adapter.ListAdapter;
import com.appstart.adapter.ListAdapterWebsite;
import com.appstart.database.DBAdapter;
import com.appstart.tabgroup.TabGroupActivity;
import com.appstart.utility.Constant;
import com.appstart.utility.Util;

public class Website extends Activity {

	ListView list;
	ListAdapterWebsite adapter;

	ArrayList<Object> title;
	ArrayList<Object> website_url;

	Button ib_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.website);

		setHeaderBackground();
		
		Bundle b=getIntent().getExtras();
		boolean showback=b.getBoolean("showBack");
		ib_back = (Button) findViewById(R.id.ib_back_music);
		
		if(showback){
			ib_back.setVisibility(View.VISIBLE);
		}else{
			ib_back.setVisibility(View.INVISIBLE);
		}
		
		
		//((TextView) findViewById(R.id.txt_music)).setText(R.string.website);

	
		
		
		ib_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				TabGroupActivity parentActivity = (TabGroupActivity) getParent();
				parentActivity.onBackPressed();

			}
		});

		list = (ListView) findViewById(R.id.list_website);

		// Getting adapter by passing xml data ArrayList

		title = new ArrayList<Object>();
		website_url = new ArrayList<Object>();

		DBAdapter dba = new DBAdapter(this);
		dba.open();
		
		Cursor c1 = dba.getScreenName("8", Constant.LANGUAGE_ID);

		if (c1.getCount() > 0) {

			((TextView) findViewById(R.id.txt_music)).setText(c1.getString(0));

		}
		
/*Cursor cur2 = dba.getBackgroundImage("8", Constant.LANGUAGE_ID);
		
		Cursor c2 = dba.getBackgroundColor("8", Constant.LANGUAGE_ID);
		
		
		if(cur2.getCount()>0)
		{
			System.out.print(" set background image: "+cur2.getString(0));
		((LinearLayout) findViewById(R.id.lay_ed)).setBackgroundDrawable(ImgDrawableFromFile(getResources(), c1.getString(0)));
		}
		
		if(c2.getCount()>0){
			System.out.print(" set background color: "+c2.getString(0));
			((LinearLayout) findViewById(R.id.lay_ed)).setBackgroundColor(Color.parseColor(c2.getString(0)));
		}*/
		

		Cursor cur = dba.getWebsite();

		if (cur.getCount() > 0) {

			// if (cur.getCount() > 1) {

			for (int i = 0; i < cur.getCount(); i++) {
				Cursor c = dba.getWebsiteDetails(cur.getString(0),
						Constant.LANGUAGE_ID);

				if (c.getCount() > 0) {

					for (int j = 0; j < c.getCount(); j++) {
						// c.getString(0);

						title.add(c.getString(0));
						website_url.add(c.getString(1));

						c.moveToNext();
					}
				}

				cur.moveToNext();
			}
			// } else {
			// Display_Detail_contact();
			// }
		}

		dba.close();
		adapter = new ListAdapterWebsite(this, title, website_url);
		list.setAdapter(adapter);

		// Click event for single list row

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				Bundle b = new Bundle();
				b.putString("website_url", website_url.get(arg2).toString());
				b.putString("title", title.get(arg2).toString());
				b.putBoolean("showBack", true);

				Intent edit = new Intent(getParent(), Webview.class);

				edit.putExtras(b);
				TabGroupActivity parentActivity = (TabGroupActivity) getParent();
				parentActivity.startChildActivity("Webview", edit);

			}

		});

		list.setDivider(new ColorDrawable(Util.Colorcode()));
		list.setDividerHeight(1);

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
	public void setHeaderBackground() {

		((RelativeLayout) findViewById(R.id.rel_music))
				.setBackgroundDrawable(Util.HeaderBackground());

	}
}
