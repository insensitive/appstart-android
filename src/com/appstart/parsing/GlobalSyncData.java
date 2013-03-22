package com.appstart.parsing;

import org.json.JSONArray;
import org.json.JSONObject;

import com.appstart.Splash;
import com.appstart.database.DBAdapter;
import com.appstart.utility.Constant;
import com.appstart.utility.Util;
import com.appstart.webservice.Webservice;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class GlobalSyncData {

	Context context;
	String customerid;

	public GlobalSyncData(Context ctx, String cid) {

		this.context = ctx;
		this.customerid = cid;

	}

	public void add_global_sync() {

		

		String response = Webservice.GlobalSynch(customerid, CalResolutionID());

		DBAdapter dba = new DBAdapter(context);
		dba.open();

		try {

			JSONObject jo = new JSONObject(response);

			if (jo.getString("status").equals("success")) {

				JSONObject jo1 = (JSONObject) jo.get("data");

				JSONArray ja = (JSONArray) jo1.get("customer_module");

				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo2 = (JSONObject) ja.get(i);
					JSONObject jobj = (JSONObject) jo2
							.get("tbl_customer_module");

					System.out.println("customer module id::::"
							+ jobj.getString("customer_module_id"));

					String img_name = "icon" + Util.getRandomFileName();

					String img_url = jobj.getString("icon");

					Log.e("Image Url:::",
							"http://203.88.158.98/appstart_live/public/"
									+ img_url);
					if (img_url.contains(".jpg") || img_url.contains(".png")) {
						Util.Store_file_phone(img_url, img_name);
					}

					dba.insertCustomerModule(
							jobj.getString("customer_module_id"),
							jobj.getString("module_id"),
							jobj.getString("customer_id"),
							jobj.getString("order_number"),
							jobj.getString("visibility"),
							jobj.getString("status"), jobj.getString("share"),
							img_name, jobj.getString("sync_date_time"));

					JSONArray jarr = (JSONArray) jo2
							.get("tbl_customer_module_detail");

					for (int j = 0; j < jarr.length(); j++) {
						JSONObject jobj1 = (JSONObject) jarr.get(j);

						jobj1.getString("customer_module_detail_id");
						jobj1.getString("customer_module_id");
						jobj1.getString("language_id");
						jobj1.getString("screen_name");
						jobj1.getString("background_image");
						jobj1.getString("background_color");
						jobj1.getString("background_type");

						String img_name1 = "Back" + Util.getRandomFileName();

						String img_url1 = jobj1.getString("background_image");

						Log.e("Image Url1:::",
								"http://203.88.158.98/appstart_live/public/"
										+ img_url1);

						if (img_url1.contains(".jpg")
								|| img_url1.contains(".png")) {
							Util.Store_file_phone(img_url1, img_name1);
						}

						
						System.out.print("Add all record of customer module details:");
						String imageType = (img_url1!="")? String.valueOf(1) : String.valueOf(0) ;
						
						dba.insertCustomerModuleDetails(
								jobj1.getString("customer_module_detail_id"),
								jobj1.getString("customer_module_id"),
								jobj.getString("customer_id"),
								jobj1.getString("language_id"),
								jobj1.getString("screen_name"), img_name1,
								jobj1.getString("background_color"),
								imageType);
								//jobj1.getString("background_type"));

					}

				}

				//
				JSONArray ja2 = (JSONArray) jo1.get("tbl_customer_language");

				for (int i = 0; i < ja2.length(); i++) {

					JSONObject jobj = (JSONObject) ja2.get(i);

					dba.insertCustomerLanguage(jobj.getString("customer_id"),
							jobj.getString("language_id"),
							jobj.getString("is_default"));

				}

				//
				JSONArray ja22 = (JSONArray) jo1.get("tbl_language");

				for (int i = 0; i < ja22.length(); i++) {

					JSONObject jobj = (JSONObject) ja22.get(i);

					dba.insertLanguage(jobj.getString("language_id"),
							jobj.getString("lang"), jobj.getString("title"));

				}

				//

				JSONArray ja3 = (JSONArray) jo1
						.get("tbl_customer_configuration");

				for (int i = 0; i < ja3.length(); i++) {

					JSONObject jobj = (JSONObject) ja3.get(i);

					dba.insertConfiguration(
							jobj.getString("customer_configuration_id"),
							jobj.getString("customer_id"),
							jobj.getString("font_type"),
							jobj.getString("font_color"),
							jobj.getString("font_size"),
							jobj.getString("spacing"),
							jobj.getString("theme_color"),
							jobj.getString("last_updated_by"),
							jobj.getString("last_updated_at"),
							jobj.getString("created_by"),
							jobj.getString("created_at"));

				}

			} else {

				System.out.println("globals sync reponse status fail::::::");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		dba.close();
	}

	// ---meathod for calculating resolution id---
	public String CalResolutionID() {

		String resolutionid = "4";
		try {

			int width = Constant.WIDTH;
			int height = Constant.HEIGHT;
			
			
			resolutionid = Util.GetResolutionID(width, height, context);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return resolutionid;
	}
}
