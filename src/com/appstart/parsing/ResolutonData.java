package com.appstart.parsing;

import org.json.JSONArray;
import org.json.JSONObject;

import com.appstart.database.DBAdapter;
import com.appstart.webservice.Webservice;

import android.R.integer;
import android.content.Context;

public class ResolutonData {

	Context context;
	String customerid;

	public ResolutonData(Context ctx, String cid) {

		this.context = ctx;
		this.customerid = cid;

	}

	public void add_resolution() {

		String response = Webservice.GetResolution(customerid);

		DBAdapter dba = new DBAdapter(context);
		dba.open();

		try {

			JSONObject jo = new JSONObject(response);

			if (jo.getString("status").equals("success")) {

				JSONObject jo1 = (JSONObject) jo.get("data");

				// ---add resolution record---

				dba.deleteResolution();

				JSONArray ja4 = (JSONArray) jo1.get("tbl_resolution");

				for (int i = 0; i < ja4.length(); i++) {

					JSONObject jobj = (JSONObject) ja4.get(i);

					double ratio = 0.5625;
					try {
						
						ratio = Double.parseDouble(jobj.getString("width"))
								/ Double.parseDouble(jobj.getString("height"));
						
						System.out.println("Ratio:::"+ratio);
						
					} catch (Exception e) {
						
						e.printStackTrace();
						
					}

					dba.insertResolution(jobj.getString("resolution_id"),
							jobj.getString("title"),
							jobj.getString("description"),
							jobj.getString("width"), jobj.getString("height"),ratio);
					
				}

			}

			dba.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
