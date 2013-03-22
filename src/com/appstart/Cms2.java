package com.appstart;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.appstart.adapter.ListAdapter;
import com.appstart.adapter.ListAdapterCms;
import com.appstart.adapter.ListAdapterCms2;
import com.appstart.database.DBAdapter;
import com.appstart.tabgroup.TabGroupActivity;
import com.appstart.utility.Constant;
import com.appstart.utility.Util;

public class Cms2 extends Activity {

	ListView list;
	ListAdapterCms2 adapter;

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

		Cursor c = dba.getCms2(Constant.LANGUAGE_ID, "0");

		for (int i = 0; i < c.getCount(); i++) {

			System.out.println("" + c.getString(1));

			a.add(c.getString(1));
			CmsID.add(c.getString(0));
			thumb_path.add(c.getString(2));

			c.moveToNext();

		}

		dba.close();

		adapter = new ListAdapterCms2(this, a, CmsID, thumb_path);
		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				Bundle b = new Bundle();
				b.putString("CmsID", CmsID.get(arg2).toString());
				b.putBoolean("showBack", true);
				
				Intent edit = new Intent(getParent(), CmsChild2.class);

				edit.putExtras(b);
				TabGroupActivity parentActivity = (TabGroupActivity) getParent();
				parentActivity.startChildActivity("CmsChild2", edit);

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