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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.appstart.adapter.ListAdapter;
import com.appstart.adapter.ListAdapterCms;
import com.appstart.database.DBAdapter;
import com.appstart.tabgroup.TabGroupActivity;
import com.appstart.utility.Constant;
import com.appstart.utility.Util;

public class Cms extends Activity {
	
	ListView list;
	ListAdapterCms adapter;
	
	ArrayList<Object> a;
	ArrayList<Object> CmsID;
	ArrayList<Object> thumb_path;
	
	Button ib_back;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.cms);

		Bundle b=getIntent().getExtras();
		
		boolean showBack = b.getBoolean("showBack");
		String cms_title=b.getString("cms_title");
		
		//((TextView) findViewById(R.id.txt_music)).setText(cms_title);
		
	
		if (!showBack) {
			((Button) findViewById(R.id.ib_back_music))
					.setVisibility(View.INVISIBLE);
		}

		list = (ListView) findViewById(R.id.list_cms);

		a = new ArrayList<Object>();
		CmsID = new ArrayList<Object>();
		thumb_path = new ArrayList<Object>();

		setHeaderBackground();
		DBAdapter dba = new DBAdapter(this);
		dba.open();
		
		Cursor c1 = dba.getScreenName("4", Constant.LANGUAGE_ID);

		if (c1.getCount() > 0) {

			((TextView) findViewById(R.id.txt_music)).setText(c1.getString(0));

		}
		
		/*Cursor c1 = dba.getBackgroundImage("4", Constant.LANGUAGE_ID);
		
		Cursor c2 = dba.getBackgroundColor("4", Constant.LANGUAGE_ID);
		
		
		if(c1.getCount()>0)
		{
		((LinearLayout) findViewById(R.id.lay_cms)).setBackgroundDrawable(ImgDrawableFromFile(getResources(), c1.getString(0)));
		}
		
		if(c2.getCount()>0){
			
			((LinearLayout) findViewById(R.id.lay_cms)).setBackgroundColor(Color.parseColor(c2.getString(0)));
		}		
		*/
		ib_back = (Button) findViewById(R.id.ib_back_music);
		ib_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				TabGroupActivity parentActivity = (TabGroupActivity) getParent();
				parentActivity.onBackPressed();

			}
		});

		Cursor c = dba.getCms(Constant.LANGUAGE_ID, "0");

		System.out.println("cms count:::::" + c.getCount());
		
		for (int i = 0; i < c.getCount(); i++) {

			System.out.println("" + c.getString(1));

			a.add(c.getString(1));
			CmsID.add(c.getString(0));
			thumb_path.add(c.getString(2));

			System.out.println("thumb name cms" + c.getString(2));

			c.moveToNext();

		}

		dba.close();

		adapter = new ListAdapterCms(this, a, CmsID, thumb_path);
		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				Bundle b = new Bundle();
				b.putString("CmsID", CmsID.get(arg2).toString());
				b.putBoolean("showBack", true);
				
				Intent edit = new Intent(getParent(), CmsChild.class);
				
				edit.putExtras(b);
				TabGroupActivity parentActivity = (TabGroupActivity) getParent();
				parentActivity.startChildActivity("CmsChild", edit);
				
			}

		});

		list.setDivider(new ColorDrawable(Util.Colorcode()));
		list.setDividerHeight(1);

	}
	Bitmap myBitmap = null;
	Drawable d = null;
	
	public Drawable ImgDrawableFromFile(Resources res, String file_name) {

		myBitmap = null;
		File imgFile = new File("/data/data/com.appstart/app_my_sub_dir/"
				+ file_name + ".jpg");
		if (imgFile.exists()) {

			myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			
			//Toast.makeText(getBaseContext(),imgFile.getAbsolutePath(), Toast.LENGTH_LONG).show();

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