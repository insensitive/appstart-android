package com.appstart.tabgroup;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import com.appstart.Cms;
import com.appstart.CmsChild;
import com.appstart.database.DBAdapter;
import com.appstart.utility.Constant;

public class TabGroupCms extends TabGroupActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try{
		
		DBAdapter dba = new DBAdapter(getParent());
		dba.open();

		Cursor c = dba.getCms(Constant.LANGUAGE_ID, "0");
		
		
		if (c.getCount() == 1) {

			Cursor cur_child=dba.getCms(Constant.LANGUAGE_ID,c.getString(0));
			
			if(cur_child.getCount()>0)
			{
				Bundle b = new Bundle();
				b.putBoolean("showBack",false);
				
				startChildActivity("Cms", new Intent(this, Cms.class).putExtras(b));
				
			}else
			{
				Bundle b = new Bundle();
				b.putString("CmsID", c.getString(0));
				b.putBoolean("showBack",false);
				Intent edit = new Intent(getParent(), CmsChild.class);
				edit.putExtras(b);
				startChildActivity("CmsChild", edit);
			}
			
			
			
		} else {
			Bundle b = new Bundle();
			b.putBoolean("showBack",false);
			
			startChildActivity("Cms", new Intent(this, Cms.class).putExtras(b));

		}
		
		c.close();
		dba.close();
		}catch(Exception e){
			e.printStackTrace();
			
			//Toast.makeText(getParent(), "Error", Toast.LENGTH_SHORT).show();
		}
		
		
	}
}