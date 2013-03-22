package com.appstart.tabgroup;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import com.appstart.Music;
import com.appstart.database.DBAdapter;
import com.appstart.utility.Constant;

public class TabGroupMusic extends TabGroupActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		try{
		Bundle b = new Bundle();
		b.putBoolean("showBack", false);
		
		startChildActivity("Music", new Intent(this, Music.class).putExtras(b));
		}catch(Exception e){
			e.printStackTrace();
			//Toast.makeText(getParent(), "Error", Toast.LENGTH_SHORT).show();
		}
		
	}
}