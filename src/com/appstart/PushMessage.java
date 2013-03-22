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

import com.appstart.adapter.ListAdapterPush;
import com.appstart.database.DBAdapter;
import com.appstart.tabgroup.TabGroupActivity;
import com.appstart.utility.Constant;
import com.appstart.utility.Util;

public class PushMessage extends Activity implements OnClickListener{

	ListView list;
	ListAdapterPush adapter;
	Button ib_back;
	ArrayList<Object> pushmessage_id;

	ArrayList<Object> a;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.pushmessage);

		setHeaderBackground();
		//((Button) findViewById(R.id.ib_back_music)).setVisibility(View.VISIBLE);
		
		((TextView) findViewById(R.id.txt_music))
				.setText(R.string.push_message);
		
		Bundle b=getIntent().getExtras();
		
		boolean showback=b.getBoolean("showBack");

		ib_back = (Button) findViewById(R.id.ib_back_music);
		
		if(showback){
			ib_back.setVisibility(View.VISIBLE);
		}else{
			ib_back.setVisibility(View.INVISIBLE);
		}
		
		
		
		
		ib_back.setOnClickListener(this);
		
		list = (ListView) findViewById(R.id.push_list);
		
		
		/*Bundle b = getIntent().getExtras();

		boolean showBack = b.getBoolean("showBack");

		if (showBack) {
			((Button) findViewById(R.id.ib_back_music))
					.setVisibility(View.VISIBLE);
		}else{
			((Button) findViewById(R.id.ib_back_music))
			.setVisibility(View.INVISIBLE);
		}*/

		// working on the design of this project //---

		System.out.println("push message page open:::::");

		a = new ArrayList<Object>();
		pushmessage_id = new ArrayList<Object>();

		
		
		DBAdapter dba = new DBAdapter(this);
		dba.open();
		Cursor cur = dba.getPushMessages(Constant.LANGUAGE_ID);

		for (int j = 0; j < cur.getCount(); j++) {
			// c.getString(0);
			a.add(cur.getString(3));
			pushmessage_id.add(cur.getString(1));
			cur.moveToNext();
		}
		
		dba.close();
		
		adapter = new ListAdapterPush(this, a, pushmessage_id);
		list.setAdapter(adapter);

		// Click event for single list row

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				Bundle b = new Bundle();
				b.putString("pushmessage_id", pushmessage_id.get(arg2)
						.toString());
				b.putBoolean("showBack", true);
				
				Intent edit = new Intent(getParent(), PushMessageDetails.class);

				edit.putExtras(b);
				TabGroupActivity parentActivity = (TabGroupActivity) getParent();
				parentActivity.startChildActivity("PushMessageDetails", edit);

			}

		});

		list.setDivider(new ColorDrawable(Util.Colorcode()));
		list.setDividerHeight(1);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

//		((TextView) findViewById(R.id.txt_ttl_pm))
//				.setText(R.string.push_message);

		if (Constant.ISLANGUAGECHANGE_PUSHMESSAGE) {

			if (list != null) {

				a.clear();
				pushmessage_id.clear();

				DBAdapter dba = new DBAdapter(this);
				dba.open();

				Cursor cur = dba.getPushMessage();

				if (cur.getCount() > 0) {

					for (int i = 0; i < cur.getCount(); i++) {
						Cursor c = dba.getPushMessagetDetails(cur.getString(0),
								Constant.LANGUAGE_ID);

						if (c.getCount() > 0) {

							for (int j = 0; j < c.getCount(); j++) {
								// c.getString(0);
								a.add(c.getString(0));
								pushmessage_id.add(cur.getString(0));
								
								c.moveToNext();
							}
						}

						cur.moveToNext();
					}
				}
				
				dba.close();
				
				adapter.notifyDataSetChanged();
				list.invalidate();
				// Click event for single list row

				list.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub

						Bundle b = new Bundle();
						b.putBoolean("showBack", false);
						b.putString("pushmessage_id", pushmessage_id.get(arg2)
								.toString());

						Intent edit = new Intent(getParent(),
								PushMessageDetails.class);

						edit.putExtras(b);
						TabGroupActivity parentActivity = (TabGroupActivity) getParent();
						parentActivity.startChildActivity("PushMessageDetails",
								edit);
						
					}

				});

			}
			
			Constant.ISLANGUAGECHANGE_PUSHMESSAGE = false;
			
		}

	}@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v == ib_back) {
			TabGroupActivity parentActivity = (TabGroupActivity) getParent();
			parentActivity.onBackPressed();
		}

	}

	public void setHeaderBackground() {

		((RelativeLayout) findViewById(R.id.rel_music))
				.setBackgroundDrawable(Util.HeaderBackground());

	}

}
