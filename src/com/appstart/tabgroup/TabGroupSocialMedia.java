package com.appstart.tabgroup;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import com.appstart.SocialMedia;
import com.appstart.Webview;
import com.appstart.database.DBAdapter;
import com.appstart.utility.Constant;

public class TabGroupSocialMedia extends TabGroupActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
try{
		 DBAdapter dba= new DBAdapter(getParent());
			dba.open();
			
			Cursor cur = dba.getSocialMedia(Constant.LANGUAGE_ID);

			if(cur.getCount()==1){
				
				Bundle b = new Bundle();
		
				b.putString("title", cur.getString(1));
				b.putString("website_url",cur.getString(0));
				b.putBoolean("showBack", true);

				startChildActivity("SocialMedia", new Intent(this, Webview.class).putExtras(b));
			}
			else{
				Bundle b = new Bundle();
				
				b.putBoolean("showBack", false);
				startChildActivity("SocialMedia", new Intent(this, SocialMedia.class).putExtras(b));
			}
			cur.close();

			dba.close(); 
}catch(Exception e){
	e.printStackTrace();
	//Toast.makeText(getParent(), "Error", Toast.LENGTH_SHORT).show();
}

		/*Bundle b = new Bundle();
		b.putBoolean("showBack", false);
			
			startChildActivity("SocialMedia", new Intent(this, SocialMedia.class).putExtras(b));
*/
			
	}
}