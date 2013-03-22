package com.appstart.tabgroup;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import com.appstart.Contact;
import com.appstart.ContactDetails;
import com.appstart.database.DBAdapter;
import com.appstart.utility.Constant;

public class TabGroupContact extends TabGroupActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			DBAdapter dba = new DBAdapter(getParent());
			dba.open();
			Cursor c = dba.getContacts(Constant.LANGUAGE_ID);
			if (c.getCount() == 1) {

				Bundle b = new Bundle();
				b.putString("contact_id", c.getString(1));
				b.putBoolean("showBack", false);

				startChildActivity("ContactDetails", new Intent(this,
						ContactDetails.class).putExtras(b));

			} else {
				Bundle b = new Bundle();
				b.putBoolean("showBack", false);
				startChildActivity("Contact",
						new Intent(this, Contact.class).putExtras(b));

			}
			dba.close();
		} catch (Exception e) {
			e.printStackTrace();
			//Toast.makeText(getParent(), "Error", Toast.LENGTH_SHORT).show();
		}
	}

}