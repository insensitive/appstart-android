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

public class CmsData {

	Context context;
	String customerid;
	DBAdapter dba;

	public CmsData(Context ctx, String cid) {

		this.context = ctx;
		this.customerid = cid;
		this.dba=new DBAdapter(ctx);

	}

	public void add_Cms() {
		
		String response = Webservice.Cms(customerid, "android");

		dba = new DBAdapter(context);
		dba.open();
		try {

			JSONObject jo = new JSONObject(response);
			if (jo.getString("status").equals("success")) {

				JSONObject jo1 = (JSONObject) jo.get("data");
				JSONArray ja = (JSONArray) jo1.get("data");

				for (int i = 0; i < ja.length(); i++) {

					
					 
					
					JSONObject jo2 = (JSONObject) ja.get(i);
					JSONObject jo3 = (JSONObject) jo2.get("tbl_module_cms");

					dba.insertCms(jo3.getString("module_cms_id"),
							jo3.getString("customer_id"),
							jo3.getString("parent_id"),
							jo3.getString("status"), jo3.getString("order"),
							jo3.getString("last_updated_by"),
							jo3.getString("last_updated_at"),
							jo3.getString("created_by"),
							jo3.getString("created_at"));

					JSONArray jarr = (JSONArray) jo2
							.get("tbl_module_cms_detail");

					for (int j = 0; j < jarr.length(); j++) {

						JSONObject jo4 = (JSONObject) jarr.get(j);

						String img_url = jo4.getString("thumb");
						String img_name ="thumb_cms"+Util.getRandomFileName();

						if (img_url.contains(".jpg")
								|| img_url.contains(".png")) {
							Util.Store_file_phone(img_url, img_name);
						}
						Log.e("Url:::",
								"http://203.88.158.98/appstart_live/public/"
										+ img_url);

						dba.insertCmsDetails(
								jo4.getString("module_cms_detail_id"),
								jo4.getString("module_cms_id"),
								jo4.getString("language_id"),
								jo4.getString("title"), img_name,
								jo4.getString("content"));

					}
				}
			} else {

				System.out.println("cms resopnse status is fail::::");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		dba.close();
	}

	public void Cms_sync_module() {
		String response = Webservice.Cms(customerid, "android");

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
					
					if(jo2.get("tbl_module_cms").toString().equalsIgnoreCase("[]"))
					{
						dba.deleteCms();
						dba.deleteCmsDetails();
					}
					
					
					JSONObject jo3 = (JSONObject) jo2.get("tbl_module_cms");

					dba.open();
					
					Cursor c = dba.getCmsDate(jo3
							.getString("module_cms_id"));
					
					list.add(jo3.getString("module_cms_id"));

					if (c.getCount() > 0) {

						if (Util.compareDateTime(c.getString(0),
								jo3.getString("last_updated_at"))) {

							dba.updateCms(jo3.getString("module_cms_id"),
									jo3.getString("customer_id"),
									jo3.getString("parent_id"),
									jo3.getString("status"),
									jo3.getString("order"),
									jo3.getString("last_updated_by"),
									jo3.getString("last_updated_at"),
									jo3.getString("created_by"),
									jo3.getString("created_at"));

							JSONArray jarr = (JSONArray) jo2
									.get("tbl_module_cms_detail");

							List<String> list1 = new ArrayList<String>();

							for (int j = 0; j < jarr.length(); j++) {
								
								JSONObject jo4 = (JSONObject) jarr.get(j);
								
								String img_url = jo4.getString("thumb");
								String img_name = "thumb_cms"+Util.getRandomFileName();
								
								Util.Store_file_phone(img_url, img_name);
								
								Log.e("Url:::",
										"http://203.88.158.98/appstart_live/public/"
												+ img_url);

								list1.add(jo4.getString("module_cms_detail_id"));

								dba.updateCmsDetails(
										jo4.getString("module_cms_detail_id"),
										jo4.getString("module_cms_id"),
										jo4.getString("language_id"),
										jo4.getString("title"), img_name,
										jo4.getString("content"));

							}

							// ---delete records which are not in server now
							String[] arr1 = list1.toArray(new String[list1
									.size()]);
							DeleteDetailTableRecords("tbl_Cms_Details",
									"RecordID", "CmsID",
									jo3.getString("module_cms_id"), arr1);

							System.out.println("Updation in Cms module::");
						} else {
							System.out
									.println("No Updation in Social Media module::");
						}
					} else {

						// ---record not available so need to add in local

						dba.insertCms(jo3.getString("module_cms_id"),
								jo3.getString("customer_id"),
								jo3.getString("parent_id"),
								jo3.getString("status"),
								jo3.getString("order"),
								jo3.getString("last_updated_by"),
								jo3.getString("last_updated_at"),
								jo3.getString("created_by"),
								jo3.getString("created_at"));

						JSONArray jarr = (JSONArray) jo2
								.get("tbl_module_cms_detail");

						for (int j = 0; j < jarr.length(); j++) {

							JSONObject jo4 = (JSONObject) jarr.get(j);

							String img_url = jo4.getString("thumb");
							String img_name = "thumb_cms"+Util.getRandomFileName();

							Util.Store_file_phone(img_url, img_name);

							Log.e("Url:::",
									"http://203.88.158.98/appstart_live/public/"
											+ img_url);

							dba.insertCmsDetails(
									jo4.getString("module_cms_detail_id"),
									jo4.getString("module_cms_id"),
									jo4.getString("language_id"),
									jo4.getString("title"), img_name,
									jo4.getString("content"));

						}

					}
				}

				// ---delete records which are not in server now
				String[] arr = list.toArray(new String[list.size()]);
				DeleteRecord("tbl_Cms", "CmsID", arr);

			} else {

				System.out.println("Cms  resopnse status is fail::::");

			}

			dba.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

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

		} else {

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

		} else {

		}

		

	}

}
