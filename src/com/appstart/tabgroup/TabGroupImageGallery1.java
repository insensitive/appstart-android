package com.appstart.tabgroup;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import com.appstart.ImageGallery1;
import com.appstart.ImageGalleryCategory1;
import com.appstart.database.DBAdapter;
import com.appstart.utility.Constant;

public class TabGroupImageGallery1 extends TabGroupActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
try{
		DBAdapter dba = new DBAdapter(getParent());
		dba.open();

		Cursor c = dba.getImageGalleryCategory1(Constant.LANGUAGE_ID);

		System.out.println("GalleryCategory count:::::::" + c.getCount());

		if (c.getCount() == 1) {
			Bundle b = new Bundle();
			b.putString("cat_id", c.getString(0));
			b.putString("cat_name", c.getString(1));
			b.putBoolean("showBack", false);

			startChildActivity("ImageGallery1", new Intent(this,
					ImageGallery1.class).putExtras(b));
		} else {
			Bundle b = new Bundle();
			b.putBoolean("showBack", false);

			startChildActivity("ImageGalleryCategory1", new Intent(this,
					ImageGalleryCategory1.class).putExtras(b));

		}

		c.close();
		dba.close();
}catch(Exception e){
	e.printStackTrace();
	//Toast.makeText(getParent(), "Error", Toast.LENGTH_SHORT).show();
}
		
		/*Bundle b = new Bundle();
		b.putBoolean("showBack", false);
		
		startChildActivity("ImageGalleryCategory1", new Intent(this, ImageGalleryCategory1.class).putExtras(b));
		*/
		
	}
}