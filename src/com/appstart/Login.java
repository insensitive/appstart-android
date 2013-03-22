package com.appstart;

import java.io.File;
import java.io.InputStream;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.appstart.database.DBAdapter;
import com.appstart.database.ImportDatabase;
import com.appstart.utility.AlertMessages;
import com.appstart.webservice.Webservice;

public class Login extends Activity implements OnClickListener, Runnable {

	Button btn_login;

	EditText edt_username, edt_password;
	AlertMessages message;
	
	ProgressDialog pd;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.login1);

		message = new AlertMessages(this);

		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(this);

		edt_username = (EditText) findViewById(R.id.edt_username);
		edt_password = (EditText) findViewById(R.id.edt_password);

		
		
		try {
			File f = new File("/data/data/com.appstart/databases/database");
			if (!f.exists()) {
				InputStream databaseInputStream1;
				databaseInputStream1 = getAssets().open("database");

				DBAdapter db = new DBAdapter(this);
				db.open();
				db.close();

				ImportDatabase ipd = new ImportDatabase(databaseInputStream1);
				ipd.copyDataBase();
				System.out.println("Database copied");

			} else {
				System.out.println("Database file already exist");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		DBAdapter dba = new DBAdapter(this);
		dba.open();

		if (dba.check_login()) {

			Intent i = new Intent(getApplicationContext(), Splash.class);
			startActivity(i);
			finish();
			
		}
		
		dba.close();

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		Intent i = null;
		if (arg0 == btn_login) {

			if (edt_username.getText().toString().equals("")) {

				message.showCutomMessage("Please enter username");

			} else if (edt_password.getText().toString().equals("")) {
				message.showCutomMessage("Please enter password");
			} else {

				pd = ProgressDialog.show(Login.this, "", "Loading...", true,
						false);
				
				Thread thread = new Thread(Login.this);
				thread.start();
				
				
			}

		}

		if (i != null) {
			startActivity(i);
			finish();
		}
	}

	String response;

	@Override
	public void run() {
		// TODO Auto-generated method stub

		try {
			
			response = Webservice.GetLogin(edt_username.getText().toString(),
					edt_password.getText().toString());
			
			handler.sendEmptyMessage(0);
		} catch (Exception e) {
			handler.sendEmptyMessage(1);
		}

	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			pd.dismiss();
			if (msg.what == 0) {

				try {

					JSONObject jobj = new JSONObject(response);

					if (jobj.getString("status").equals("success")) {

						JSONObject jo = (JSONObject) jobj.get("data");

						// adding login information into tbl_login
						DBAdapter dba = new DBAdapter(Login.this);
						dba.open();
						dba.insertContact(jo.getString("customer_id"),
								jo.getString("app_access_id"),
								jo.getString("password"),
								jo.getString("status"));
						dba.close();
						
						Intent i=new Intent(getApplicationContext(),Splash.class);
						startActivity(i);
						finish();
						
					} else {
						
						edt_password.setText("");
						message.showCutomMessage("Please enter valid username and password");
						
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					message.showserverdataerror();
				}
			} else {

				message.showNetworkAlert();
			}

		}
	};

}
