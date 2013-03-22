package com.appstart;

import java.util.ArrayList;

import com.appstart.adapter.ListAdapter;
import com.appstart.adapter.ListAdapterImageGallery;
import com.appstart.database.DBAdapter;
import com.appstart.tabgroup.TabGroupActivity;
import com.appstart.utility.Constant;
import com.appstart.utility.Util;

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

public class ImageGalleryCategory extends Activity {

	ListView list;
	ListAdapterImageGallery adapter;

	ArrayList<Object> cat_name;
	ArrayList<Object> cat_id;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imagegallerycategory);

		
		Bundle b = getIntent().getExtras();


		boolean showBack = b.getBoolean("showBack");

		if (showBack) {
			((Button) findViewById(R.id.ib_back_music))
					.setVisibility(View.VISIBLE);
		}else{
			((Button) findViewById(R.id.ib_back_music))
			.setVisibility(View.INVISIBLE);
		}
		
		setHeaderBackground();

		list = (ListView) findViewById(R.id.list_igc);

		// Getting adapter by passing xml data ArrayList

		cat_name = new ArrayList<Object>();
		cat_id = new ArrayList<Object>();

		((Button)findViewById(R.id.ib_back_music)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TabGroupActivity parentActivity = (TabGroupActivity) getParent();
				 parentActivity.onBackPressed();
			}
		});

		DBAdapter dba = new DBAdapter(this);
		dba.open();

		Cursor c1 = dba.getScreenName("7", Constant.LANGUAGE_ID);

		if (c1.getCount() > 0) {

			((TextView) findViewById(R.id.txt_music)).setText(c1.getString(0));

		}
		
		

		Cursor cur = dba.getImageGalleryCategory(Constant.LANGUAGE_ID);

		for (int j = 0; j < cur.getCount(); j++) {
			// c.getString(0);
			cat_name.add(cur.getString(1));
			cat_id.add(cur.getString(0));

			cur.moveToNext();
		}

		dba.close();

		adapter = new ListAdapterImageGallery(this, cat_name, cat_id);
		list.setAdapter(adapter);

		// Click event for single list row

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int poisotion,
					long arg3) {
				// TODO Auto-generated method stub

				Bundle b = new Bundle();
				b.putString("cat_id", cat_id.get(poisotion).toString());
				b.putString("cat_name", cat_name.get(poisotion).toString());
				b.putBoolean("showBack", true);

				Intent edit = new Intent(getParent(), ImageGallery.class);

				edit.putExtras(b);
				TabGroupActivity parentActivity = (TabGroupActivity) getParent();
				parentActivity.startChildActivity("ImageGallery", edit);

			}

		});

		list.setDivider(new ColorDrawable(Util.Colorcode()));
		list.setDividerHeight(1);

	}

	// ---header---
	public void setHeaderBackground() {

		((RelativeLayout) findViewById(R.id.rel_music))
				.setBackgroundDrawable(Util.HeaderBackground());

	}

}
