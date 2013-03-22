package com.appstart;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


import com.appstart.adapter.ListAdapterCms1;
import com.appstart.database.DBAdapter;
import com.appstart.tabgroup.TabGroupActivity;
import com.appstart.utility.Constant;
import com.appstart.utility.Util;

public class Cms1 extends Activity {

	ListView list;
	ListAdapterCms1 adapter;

	ArrayList<Object> a;
	ArrayList<Object> CmsID;
	ArrayList<Object> thumb_path;

	Button ib_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.cms);

		Bundle b = getIntent().getExtras();

		boolean showBack = b.getBoolean("showBack");

		if (!showBack) {
			((Button) findViewById(R.id.ib_back_music))
					.setVisibility(View.INVISIBLE);
		}

		list = (ListView) findViewById(R.id.list_cms);

		a = new ArrayList<Object>();
		CmsID = new ArrayList<Object>();
		thumb_path = new ArrayList<Object>();

		setHeaderBackground();

		((TextView) findViewById(R.id.txt_music)).setText(R.string.cms);

		ib_back = (Button) findViewById(R.id.ib_back_music);
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

		Cursor c1 = dba.getScreenName("10", Constant.LANGUAGE_ID);

		if (c1.getCount() > 0) {

			((TextView) findViewById(R.id.txt_music)).setText(c1.getString(0));

		}
		
		/*Cursor c1 = dba.getBackgroundImage("10", Constant.LANGUAGE_ID);
		
		Cursor c2 = dba.getBackgroundColor("10", Constant.LANGUAGE_ID);
		
		
		if(c1.getCount()>0)
		{
		((LinearLayout) findViewById(R.id.lay_cms)).setBackgroundDrawable(ImgDrawableFromFile(getResources(), c1.getString(0)));
		}
		
		if(c2.getCount()>0){
			
			((LinearLayout) findViewById(R.id.lay_cms)).setBackgroundColor(Color.parseColor(c2.getString(0)));
		}*/
		
		

		Cursor c = dba.getCms1(Constant.LANGUAGE_ID, "0");

		for (int i = 0; i < c.getCount(); i++) {

			System.out.println("" + c.getString(1));

			a.add(c.getString(1));
			CmsID.add(c.getString(0));
			thumb_path.add(c.getString(2));
			c.moveToNext();

		}

		dba.close();

		adapter = new ListAdapterCms1(this, a, CmsID, thumb_path);
		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				Bundle b = new Bundle();
				b.putString("CmsID", CmsID.get(arg2).toString());
				b.putBoolean("showBack", true);
				
				Intent edit = new Intent(getParent(), CmsChild1.class);

				edit.putExtras(b);
				TabGroupActivity parentActivity = (TabGroupActivity) getParent();
				parentActivity.startChildActivity("CmsChild1", edit);

			}

		});

		list.setDivider(new ColorDrawable(Util.Colorcode()));
		list.setDividerHeight(1);

	}

	public void setHeaderBackground() {

		((RelativeLayout) findViewById(R.id.rel_music))
				.setBackgroundDrawable(Util.HeaderBackground());

	}

}