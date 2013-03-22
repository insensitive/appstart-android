package com.appstart;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.appstart.database.DBAdapter;
import com.appstart.tabgroup.TabGroupActivity;
import com.appstart.utility.Constant;
import com.appstart.utility.Util;

public class Contact extends Activity {

	ListView list;
	ListAdapter adapter;
	Button ib_back;

	ArrayList<Object> a;
	ArrayList<Object> contact_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.contact);

		setHeaderBackground();
		

		ib_back = (Button) findViewById(R.id.ib_back_music);
		
		
		
		Bundle b=new Bundle();
		boolean showback=b.getBoolean("showback");
		if(showback){
			ib_back.setVisibility(View.VISIBLE);
		}else{
			ib_back.setVisibility(View.INVISIBLE);
		}
		
		
		
		ib_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				TabGroupActivity parentActivity = (TabGroupActivity) getParent();
				parentActivity.onBackPressed();

			}
		});
		
		//Button ib_back = (Button) findViewById(R.id.ib_back_music);
		
		
		list = (ListView) findViewById(R.id.list);
		
		// Getting adapter by passing xml data ArrayList
		
		a = new ArrayList<Object>();
		contact_id = new ArrayList<Object>();
		
		DBAdapter dba = new DBAdapter(this);
		dba.open();
		Cursor cur = dba.getContacts(Constant.LANGUAGE_ID);
		
		for (int j = 0; j < cur.getCount(); j++) {
			// c.getString(0);
			a.add(cur.getString(3));
			contact_id.add(cur.getString(1));

			cur.moveToNext();
		}
		
		dba.close();
		
		adapter = new ListAdapter(this, a, contact_id);
		list.setAdapter(adapter);
		
		// Click event for single list row
		
		list.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				Bundle b = new Bundle();
				b.putString("contact_id", contact_id.get(arg2).toString());
				b.putBoolean("showBack", true);
				
				Intent edit = new Intent(getParent(), ContactDetails.class);
				
				edit.putExtras(b);
				TabGroupActivity parentActivity = (TabGroupActivity) getParent();
				parentActivity.startChildActivity("ContactDetails", edit);
				
			}

		});

		list.setDivider(new ColorDrawable(Util.Colorcode()));
		list.setDividerHeight(1);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		((TextView) findViewById(R.id.txt_music)).setText(R.string.contact);

		if (Constant.ISLANGUAGECHANGE_CONTACT) {
			if (list != null) {

			}
			Constant.ISLANGUAGECHANGE_CONTACT = false;

		}

	}

	public void Display_Detail_contact() {

		LayoutInflater linf;
		LinearLayout rr;

		linf = (LayoutInflater) getApplicationContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		linf = LayoutInflater.from(Contact.this);

		rr = (LinearLayout) findViewById(R.id.lay_main_contact);

		final View v = linf.inflate(R.layout.row_contactdetails, null);

		rr.addView(v);

	}

	// ---header---
	public void setHeaderBackground() {

		((RelativeLayout) findViewById(R.id.rel_music))
				.setBackgroundDrawable(Util.HeaderBackground());

	}

}