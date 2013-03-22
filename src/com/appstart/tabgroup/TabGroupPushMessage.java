package com.appstart.tabgroup;

import com.appstart.PushMessage;
import com.appstart.PushMessageDetails;
import com.appstart.database.DBAdapter;
import com.appstart.utility.Constant;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

public class TabGroupPushMessage extends TabGroupActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
		DBAdapter dba = new DBAdapter(this);
		dba.open();
		Cursor c = dba.getPushMessages(Constant.LANGUAGE_ID);
		if (c.getCount() == 1) {
			
			Bundle b = new Bundle();
			b.putString("pushmessage_id", c.getString(1));
			b.putBoolean("showBack", false);
			
			startChildActivity("PushMessageDetails", new Intent(this, PushMessageDetails.class).putExtras(b));
			
		} else {
			Bundle b = new Bundle();
		
			b.putBoolean("showBack", false);
			startChildActivity("PushMessage", new Intent(this, PushMessage.class).putExtras(b));
			
		}
		c.close();
		dba.close();
		
		}catch(Exception e){
			e.printStackTrace();
			//Toast.makeText(getParent(), "Error", Toast.LENGTH_SHORT).show();
		}
		
	}
}
