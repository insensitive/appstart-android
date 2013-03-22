package com.appstart.parsing;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.appstart.database.DBAdapter;
import com.appstart.utility.Constant;
import com.appstart.utility.Util;
import com.appstart.webservice.Webservice;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class HomeWallpaperData {

	Context context;
	String customerid;
	DBAdapter dba;

	public HomeWallpaperData(Context ctx, String cid) {
		// TODO Auto-generated constructor stub
		this.context = ctx;
		this.customerid = cid;
		this.dba=new DBAdapter(ctx);

	}

	public void add_homewallpaper() {

		System.out.println("Resolution id:::::" + CalResolutionID());

		String response = Webservice.HomeWallSynch(customerid, CalResolutionID());

		dba = new DBAdapter(context);
		dba.open();

		try {

			JSONObject jo = new JSONObject(response);
			if (jo.getString("status").equals("success")) {

				JSONObject jo1 = (JSONObject) jo.get("data");
				JSONArray ja = (JSONArray) jo1.get("data");

				for (int i = 0; i < ja.length(); i++) {

					JSONObject jo2 = (JSONObject) ja.get(i);
					JSONObject jo3 = (JSONObject) jo2.get("tbl_home_wallpaper");

					jo3.getString("home_wallpaper_id");
					jo3.getString("customer_id");
					jo3.getString("order");
					jo3.getString("status");
					jo3.getString("last_updated_by");
					jo3.getString("last_updated_at");
					
					jo3.getString("created_by");
					jo3.getString("created_at");

					dba.insertHomeWallpaper(jo3.getString("home_wallpaper_id"),
							jo3.getString("customer_id"),
							jo3.getString("status"), jo3.getString("default"),
							jo3.getString("order"),
							jo3.getString("last_updated_by"),
							jo3.getString("last_updated_at"),
							jo3.getString("created_by"),
							jo3.getString("created_at"));

					JSONArray jarr = (JSONArray) jo2
							.get("tbl_home_wallpaper_detail");

					for (int j = 0; j < jarr.length(); j++) {

						JSONObject jo4 = (JSONObject) jarr.get(j);

						String img_url = jo4.getString("image_path");
						String img_name = "Home"+Util.getRandomFileName();

						if (img_url.contains(".jpg")
								|| img_url.contains(".png")) {
							Util.Store_file_phone(img_url,img_name);
						}

						Log.e("Image Url:::",
								"http://203.88.158.98/appstart_live/public/"
										+ img_url);

						dba.insertModuleHomeWallpaperDetails(
								jo4.getString("home_wallpaper_detail_id"),
								jo4.getString("home_wallpaper_id"),
								jo4.getString("language_id"),
								jo4.getString("image_title"), "", "",img_name, "", "",
								jo4.getString("link_to_module"),
								jo4.getString("last_updated_by"),
								jo4.getString("last_updated_at"),
								jo4.getString("created_by"),
								jo4.getString("created_at"));

					}
				}
			} 

		} catch (Exception e) {
			e.printStackTrace();
		}
		dba.close();
	}

	public void Homewallpaper_sync_module() {

		String response = Webservice.HomeWallSynch(customerid,
				CalResolutionID());

		try {

			dba = new DBAdapter(context);
			dba.open();

			JSONObject jo = new JSONObject(response);
			if (jo.getString("status").equals("success")) {

				JSONObject jo1 = (JSONObject) jo.get("data");
				JSONArray ja = (JSONArray) jo1.get("data");

				List<String> list = new ArrayList<String>();
				for (int i = 0; i < ja.length(); i++) {

					JSONObject jo2 = (JSONObject) ja.get(i);
					JSONObject jo3 = (JSONObject) jo2.get("tbl_home_wallpaper");

					Cursor c = dba.getHomeWallpaperDate(jo3
							.getString("home_wallpaper_id"));

					list.add(jo3.getString("home_wallpaper_id"));
					
					if (c.getCount() > 0) {

						if (Util.compareDateTime(c.getString(0),
								jo3.getString("last_updated_at"))) {

							dba.updateHomeWallpaper(
									jo3.getString("home_wallpaper_id"),
									jo3.getString("customer_id"),
									jo3.getString("status"),
									jo3.getString("default"),
									jo3.getString("order"),
									jo3.getString("last_updated_by"),
									jo3.getString("last_updated_at"),
									jo3.getString("created_by"),
									jo3.getString("created_at"));
							
							JSONArray jarr = (JSONArray) jo2
									.get("tbl_home_wallpaper_detail");

							List<String> list1 = new ArrayList<String>();

							for (int j = 0; j < jarr.length(); j++) {

								JSONObject jo4 = (JSONObject) jarr.get(j);

								Cursor cur_hd = dba
										.getModuleHomeWallpaperDetailsDate(jo4
												.getString("home_wallpaper_detail_id"));

								list1.add(jo4
										.getString("home_wallpaper_detail_id"));

								if (cur_hd.getCount() > 0) {
									if (Util.compareDateTime(
											cur_hd.getString(1),
											jo4.getString("last_updated_at"))) {

										
										
										
										Util.DeleteFile(cur_hd.getString(0));

										System.out.println("Image deleted::::"+cur_hd.getString(0));
										
										String img_url = jo4
												.getString("image_path");
										String img_name = "Home"+Util
												.getRandomFileName();

										Log.e("Image Url:::",
												"http://203.88.158.98/appstart_live/public/"
														+ img_url);
										
										Util.Store_file_phone(img_url, img_name);
										
										dba.updateModuleHomeWallpaperDetails(
												jo4.getString("home_wallpaper_detail_id"),
												jo4.getString("home_wallpaper_id"),
												jo4.getString("language_id"),
												jo4.getString("image_title"),
												"",
												"",
												img_name,
												"",
												"",
												jo4.getString("link_to_module"),
												jo4.getString("last_updated_by"),
												jo4.getString("last_updated_at"),
												jo4.getString("created_by"),
												jo4.getString("created_at"));

									}

								}

							}

							String[] arr1 = list1.toArray(new String[list1
									.size()]);
							DeleteDetailTableRecords(
									"tbl_Module_Home_Wallpaper_Details",
									"RecordID", "WallpaperID",
									jo3.getString("home_wallpaper_id"), arr1);

							System.out
									.println("Updation in wallpaper module::");

						}
					} else {
						
						// ---no record availble in data base so need to add it

						dba.insertHomeWallpaper(
								jo3.getString("home_wallpaper_id"),
								jo3.getString("customer_id"),
								jo3.getString("status"),
								jo3.getString("default"),
								jo3.getString("order"),
								jo3.getString("last_updated_by"),
								jo3.getString("last_updated_at"),
								jo3.getString("created_by"),
								jo3.getString("created_at"));

						JSONArray jarr = (JSONArray) jo2
								.get("tbl_home_wallpaper_detail");
						for (int j = 0; j < jarr.length(); j++) {

							JSONObject jo4 = (JSONObject) jarr.get(j);

							String img_url = jo4.getString("image_path");
							String img_name = "Home"+Util.getRandomFileName();
							
							Log.e("Image Url:::",
									"http://203.88.158.98/appstart_live/public/"
											+ img_url);

							Util.Store_file_phone(img_url, img_name);
							
							dba.insertModuleHomeWallpaperDetails(
									jo4.getString("home_wallpaper_detail_id"),
									jo4.getString("home_wallpaper_id"),
									jo4.getString("language_id"),
									jo4.getString("image_title"),
									"",
									"", img_name,
									"",
									"",
									"",
									jo4.getString("last_updated_by"),
									jo4.getString("last_updated_at"),
									jo4.getString("created_by"),
									jo4.getString("created_at"));

						}

					}
				}

				String[] arr = list.toArray(new String[list.size()]);
				DeleteRecord("tbl_Home_Wallpaper", "WallpaperID", arr);

			} 

			dba.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// ---meathod for calculating resolution id---
	public String CalResolutionID() {

		String resolutionid = "8";
		try {

			int width = Constant.WIDTH;
			int height = Constant.HEIGHT;
			resolutionid = Util.GetResolutionID(width, height, context);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return resolutionid;
	}

	public void DeleteRecord(String table, String id, final String name[]) {

		 

		if (name.length > 0) {
			// to handle the ' character directly into SQL, you will probably
			// need to name.replaceAll("'", "\'");

			StringBuilder nameBuilder = new StringBuilder();

			for (String n : name) {
				nameBuilder.append("'").append(n).append("',");
			}

			nameBuilder.deleteCharAt(nameBuilder.length() - 1);

			nameBuilder.toString();

			System.out.println("generated string from the " + id
					+ " array is:::::" + nameBuilder.toString());

			dba.row_query("delete from " + table + " where " + id + " not in("
					+ nameBuilder.toString() + ")");

		}  

	 

	}

	public void DeleteDetailTableRecords(String table, String id, String mid,
			String mid_Value, final String name[]) {

		 

		if (name.length > 0) {
			// to handle the ' character directly into SQL, you will probably
			// need to name.replaceAll("'", "\'");

			StringBuilder nameBuilder = new StringBuilder();

			for (String n : name) {
				nameBuilder.append("'").append(n).append("',");
			}

			nameBuilder.deleteCharAt(nameBuilder.length() - 1);

			nameBuilder.toString();

			System.out.println("generated string from the " + id
					+ " array is:::::" + nameBuilder.toString());
			
			dba.row_query("delete from " + table + " where " + id + " not in("
					+ nameBuilder.toString() + ") and " + mid + "=" + mid_Value);
			
		}

	}

}
