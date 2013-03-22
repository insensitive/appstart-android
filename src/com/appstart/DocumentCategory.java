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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appstart.adapter.ListAdapterCategory;
import com.appstart.database.DBAdapter;
import com.appstart.tabgroup.TabGroupActivity;
import com.appstart.utility.Constant;
import com.appstart.utility.Util;

public class DocumentCategory extends Activity {

	ListView list;
	ListAdapterCategory adapter;

	ArrayList<Object> title;
	Button ib_back;
	ArrayList<Object> category_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.documentcategory);

		setHeaderBackground();
	
		Bundle b=getIntent().getExtras();
		boolean showBack=b.getBoolean("showBack");
		ib_back = (Button) findViewById(R.id.ib_back_music);
		
		if(showBack){
			ib_back.setVisibility(View.VISIBLE);
		}else{
			ib_back.setVisibility(View.INVISIBLE);
		}
		
		

		list = (ListView) findViewById(R.id.list_category);

		title = new ArrayList<Object>();
		category_id = new ArrayList<Object>();

		DBAdapter dba = new DBAdapter(getParent());
		dba.open();

		Cursor c0 = dba.getScreenName("6", Constant.LANGUAGE_ID);

		if (c0.getCount() > 0) {

			((TextView) findViewById(R.id.txt_music)).setText(c0.getString(0));

		}

		Cursor c = dba.getDocumentCategory("0", Constant.LANGUAGE_ID);

		for (int i = 0; i < c.getCount(); i++) {

			title.add(c.getString(1));
			category_id.add(c.getString(0));

			c.moveToNext();
		}
		dba.close();

		adapter = new ListAdapterCategory(this, title, category_id);
		list.setAdapter(adapter);

		// Click event for single list row

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				Bundle b = new Bundle();
				b.putString("category_id", category_id.get(arg2).toString());
				b.putBoolean("showBack",true);
				
				Intent edit = new Intent(getParent(), Document.class);

				edit.putExtras(b);
				TabGroupActivity parentActivity = (TabGroupActivity) getParent();
				parentActivity.startChildActivity("Document", edit);
				
			}

		});

		list.setDivider(new ColorDrawable(Util.Colorcode()));
		list.setDividerHeight(1);

	

		ib_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				TabGroupActivity parentActivity = (TabGroupActivity) getParent();
				parentActivity.onBackPressed();

			}
		});

	}

	// ---header---
	public void setHeaderBackground() {

		((RelativeLayout) findViewById(R.id.rel_music))
				.setBackgroundDrawable(Util.HeaderBackground());

	}

}
