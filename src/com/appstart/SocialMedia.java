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
import com.appstart.adapter.ListAdapterSocialMedia;
import com.appstart.database.DBAdapter;
import com.appstart.tabgroup.TabGroupActivity;
import com.appstart.utility.Constant;
import com.appstart.utility.Util;

public class SocialMedia extends Activity {

	Button ib_back;

	ListView list;
	ListAdapterSocialMedia adapter;

	ArrayList<Object> title;
	ArrayList<Object> socialmedia_url;
	ArrayList<Object> icon_path;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.socialmedia);
		
		Bundle b = getIntent().getExtras();
        boolean showBack = b.getBoolean("showBack");
		
        ib_back = (Button) findViewById(R.id.ib_back_music);
		
		if (showBack) {
			ib_back.setVisibility(View.VISIBLE);
		}
		else{
			ib_back.setVisibility(View.INVISIBLE);
		}
        
		setHeaderBackground();
		((TextView) findViewById(R.id.txt_music)).setText(R.string.socialmedia);

		// ((TextView)findViewById(R.id.txt_scm)).setTypeface(Util.font(this,
		// Constant.FONT_TYPE));

		
		
		
		
		ib_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				TabGroupActivity parentActivity = (TabGroupActivity) getParent();
				parentActivity.onBackPressed();

			}
		});

		list = (ListView) findViewById(R.id.list_socialmedia);

		title = new ArrayList<Object>();
		socialmedia_url = new ArrayList<Object>();
		icon_path = new ArrayList<Object>();

		DBAdapter dba = new DBAdapter(this);
		dba.open();

		
		Cursor cur = dba.getSocialMedia(Constant.LANGUAGE_ID);

		for (int j = 0; j < cur.getCount(); j++) {
			// c.getString(0);
			title.add(cur.getString(1));
			socialmedia_url.add(cur.getString(0));
			icon_path.add(cur.getString(2));

			cur.moveToNext();
		}
		cur.close();
		dba.close();
		


		adapter = new ListAdapterSocialMedia(this, title, socialmedia_url,
				icon_path);
		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				Bundle b = new Bundle();
				b.putString("website_url", socialmedia_url.get(arg2).toString());
				b.putBoolean("showBack", true);
				b.putString("title", title.get(arg2).toString());

				Intent edit = new Intent(getParent(), Webview.class);

				edit.putExtras(b);
				TabGroupActivity parentActivity = (TabGroupActivity) getParent();
				parentActivity.startChildActivity("Webview", edit);

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
