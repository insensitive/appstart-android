package com.appstart.tabgroup;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

import com.appstart.Website;
import com.appstart.Webview;
import com.appstart.database.DBAdapter;
import com.appstart.utility.Constant;

public class TabGroupWebsite extends TabGroupActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
try{
		DBAdapter dba = new DBAdapter(getParent());
			dba.open();
			
			Cursor cur = dba.getWebsite();
			
			Cursor c = dba.getWebsiteDetails(cur.getString(0),
					Constant.LANGUAGE_ID);

			if(cur.getCount()==1){
				
				Bundle b = new Bundle();
		
				b.putString("title", c.getString(0));
				b.putString("website_url",c.getString(1));
				b.putBoolean("showBack", true);

				startChildActivity("Website", new Intent(this, Webview.class).putExtras(b));
			}
			else{
				Bundle b = new Bundle();
				b.putBoolean("showBack", false);
				
				
				startChildActivity("Website", new Intent(this, Website.class).putExtras(b));
			}
			cur.close();
			c.close();
		
}catch(Exception e){
	e.printStackTrace();
	//Toast.makeText(getParent(), "Error", Toast.LENGTH_SHORT).show();
}
		
		
		
		//startChildActivity("Website", new Intent(this, Website.class));
		
	}
}