package com.appstart.tabgroup;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import com.appstart.Document;
import com.appstart.DocumentCategory;
import com.appstart.DocumentDetails;
import com.appstart.database.DBAdapter;
import com.appstart.utility.Constant;

public class TabGroupDocument extends TabGroupActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

try{
		
		DBAdapter dba = new DBAdapter(getParent());
			dba.open();

			Cursor c = dba
					.getDocumentCategoryCursor(Constant.LANGUAGE_ID);

			System.out.println("DocCategory count:::::::"
					+ c.getCount());

			Cursor c2 = dba.getDocument(c.getString(0),
					Constant.LANGUAGE_ID);

			if (c.getCount() == 1) {

				Bundle b = new Bundle();
				b.putString("category_id",c.getString(0));
				b.putBoolean("showBack",false);
			
				startChildActivity("Document", new Intent(this, Document.class).putExtras(b));
				
				/*Bundle b = new Bundle();
				b.putString("DocumentID", c2.getString(0));
				b.putBoolean("showBack", false);

				startChildActivity("DocumentDetails", new Intent(this, DocumentDetails.class).putExtras(b));*/
			} else {

				Bundle b = new Bundle();
				b.putBoolean("showBack", false);
				startChildActivity("DocumentCategory", new Intent(this, DocumentCategory.class).putExtras(b));
			}

			c.close();
			dba.close(); 
		
		
		
}catch(Exception e){
	e.printStackTrace();
	//Toast.makeText(getParent(), "Error", Toast.LENGTH_SHORT).show();
}
		
		
		//		DBAdapter dba = new DBAdapter(this);
//		dba.open();
//		Cursor c = dba.getContacts(Constant.LANGUAGE_ID);
//		if (c.getCount() == 1) {
//			
//			Bundle b = new Bundle();
//			b.putString("contact_id", c.getString(1));
//			
//			startChildActivity("ContactDetails", new Intent(this, ContactDetails.class).putExtras(b));
//			
//		} else {
			
			
			
//		}
//		dba.close();
			
	}
}