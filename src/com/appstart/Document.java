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

import com.appstart.adapter.ListAdapterDocument;
import com.appstart.adapter.ListAdapterEvents;
import com.appstart.database.DBAdapter;
import com.appstart.tabgroup.TabGroupActivity;
import com.appstart.utility.Constant;
import com.appstart.utility.Util;

public class Document extends Activity {

	Button ib_back;

	ListView list;
	ListAdapterDocument adapter;

	ArrayList<Object> a;
	ArrayList<Object> DocumentID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.document);

		
		Bundle b=getIntent().getExtras();
		boolean showback=b.getBoolean("showBack");
		String CategoryID=b.getString("category_id");
				
				
		ib_back = (Button) findViewById(R.id.ib_back_music);
		if(showback){
			ib_back.setVisibility(View.VISIBLE);
		}else{
			ib_back.setVisibility(View.INVISIBLE);
		}
		
	
		list = (ListView) findViewById(R.id.list_document);

		a = new ArrayList<Object>();
		DocumentID = new ArrayList<Object>();
		
		setHeaderBackground();
		
	
		
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
		
		
		Cursor c0 = dba.getScreenName("6", Constant.LANGUAGE_ID);
		
		if (c0.getCount() > 0) {
			
			((TextView) findViewById(R.id.txt_music))
			.setText(c0.getString(0));

		}
		
		
		
		Cursor c = dba.getDocument(CategoryID,Constant.LANGUAGE_ID);
		
		for (int i = 0; i < c.getCount(); i++) {

			System.out.println("Document titles" + c.getString(3));

			a.add(c.getString(1));
			DocumentID.add(c.getString(0));

			c.moveToNext();

		}
		
		dba.close();
		
		adapter = new ListAdapterDocument(this, a, DocumentID);
		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				Bundle b = new Bundle();
				b.putString("DocumentID", DocumentID.get(arg2).toString());
				b.putBoolean("showBack", true);
				
				Intent edit = new Intent(getParent(), DocumentDetails.class);
				
				edit.putExtras(b);
				TabGroupActivity parentActivity = (TabGroupActivity) getParent();
				parentActivity.startChildActivity("DocumentDetails", edit);

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