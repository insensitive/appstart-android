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

public class CmsData1 {

	Context context;
	String customerid;
	DBAdapter dba;
	
	
	public CmsData1(Context ctx, String cid) {
		
		this.context = ctx;
		this.customerid = cid;
		this.dba=new DBAdapter(ctx);
	}
	
	public void add_Cms1() {
		
		String response = Webservice.Cms1(customerid, "android");

		dba = new DBAdapter(context);
		dba.open();
		try {
			
			JSONObject jo = new JSONObject(response);
			if (jo.getString("status").equals("success")) {

				JSONObject jo1 = (JSONObject) jo.get("data");
				JSONArray ja = (JSONArray) jo1.get("data");

				for (int i = 0; i < ja.length(); i++) {

					JSONObject jo2 = (JSONObject) ja.get(i);
					JSONObject jo3 = (JSONObject) jo2.get("tbl_module_cms_1");

					dba.insertCms1(jo3.getString("module_cms_1_id"),
							jo3.getString("customer_id"),
							jo3.getString("parent_id"),
							jo3.getString("status"), jo3.getString("order"),
							jo3.getString("last_updated_by"),
							jo3.getString("last_updated_at"),
							jo3.getString("created_by"),
							jo3.getString("created_at"));
							
					JSONArray jarr = (JSONArray) jo2
							.get("tbl_module_cms_detail_1");

					for (int j = 0; j < jarr.length(); j++) {

						JSONObject jo4 = (JSONObject) jarr.get(j);

						String img_url = jo4.getString("thumb");
						String img_name = Util.getRandomFileName();

						if (img_url.contains(".jpg")
								|| img_url.contains(".png")) {
							Util.Store_file_phone(img_url, img_name);
						}
						Log.e("Url:::",
								"http://203.88.158.98/appstart_live/public/"
										+ img_url);
						
						dba.insertCmsDetails1(
								jo4.getString("module_cms_detail_1_id"),
								jo4.getString("module_cms_1_id"),
								jo4.getString("language_id"),
								jo4.getString("title"), img_name,
								jo4.getString("content"));
						
						
					}
				}
			} 

		} catch (Exception e) {
			e.printStackTrace();
		}
		dba.close();
	}
	
	
	
	
	
	
	
	public void Cms_sync_module1() {
		String response = Webservice.Cms1(customerid, "android");

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
					if(jo2.get("tbl_module_cms_1").toString().equalsIgnoreCase("[]"))
					{
						dba.deleteCms1();
						dba.deleteCmsDetails1();
					}
					
					JSONObject jo3 = (JSONObject) jo2.get("tbl_module_cms_1");
					
					Cursor c = dba.getCmsDate1(jo3
							.getString("module_cms_1_id"));
					
					list.add(jo3.getString("module_cms_1_id"));

					
					System.out.println("count::::"+c.getCount());
					
					if (c.getCount() > 0) {

						if (Util.compareDateTime(c.getString(0),
								jo3.getString("last_updated_at"))) {

							dba.updateCms1(jo3.getString("module_cms_1_id"),
									jo3.getString("customer_id"),
									jo3.getString("parent_id"),
									jo3.getString("status"),
									jo3.getString("order"),
									jo3.getString("last_updated_by"),
									jo3.getString("last_updated_at"),
									jo3.getString("created_by"),
									jo3.getString("created_at"));

							JSONArray jarr = (JSONArray) jo2
									.get("tbl_module_cms_detail_1");

							List<String> list1 = new ArrayList<String>();

							for (int j = 0; j < jarr.length(); j++) {
								
								JSONObject jo4 = (JSONObject) jarr.get(j);

								String img_url = jo4.getString("thumb");
								String img_name = Util.getRandomFileName();

								Util.Store_file_phone(img_url, img_name);

								Log.e("cms1 Url:::",
										"http://203.88.158.98/appstart_live/public/"
												+ img_url);

								list1.add(jo4.getString("module_cms_detail_1_id"));

								dba.updateCmsDetails1(
										jo4.getString("module_cms_detail_1_id"),
										jo4.getString("module_cms_1_id"),
										jo4.getString("language_id"),
										jo4.getString("title"), img_name,
										jo4.getString("content"));
								
							}

							// ---delete records which are not in server now
							String[] arr1 = list1.toArray(new String[list1
									.size()]);
							DeleteDetailTableRecords("tbl_Cms_Details1",
									"RecordID", "CmsID",
									jo3.getString("module_cms_1_id"), arr1);

							 
						} 
					} else {

						// ---record not available so need to add in local

						dba.insertCms1(jo3.getString("module_cms_1_id"),
								jo3.getString("customer_id"),
								jo3.getString("parent_id"),
								jo3.getString("status"),
								jo3.getString("order"),
								jo3.getString("last_updated_by"),
								jo3.getString("last_updated_at"),
								jo3.getString("created_by"),
								jo3.getString("created_at"));

						JSONArray jarr = (JSONArray) jo2
								.get("tbl_module_cms_detail_1");

						for (int j = 0; j < jarr.length(); j++) {

							JSONObject jo4 = (JSONObject) jarr.get(j);

							String img_url = jo4.getString("thumb");
							String img_name = Util.getRandomFileName();

							Util.Store_file_phone(img_url, img_name);

							Log.e("cms1 inserting Url:::",
									"http://203.88.158.98/appstart_live/public/"
											+ img_url);

							dba.insertCmsDetails1(
									jo4.getString("module_cms_detail_1_id"),
									jo4.getString("module_cms_1_id"),
									jo4.getString("language_id"),
									jo4.getString("title"), img_name,
									jo4.getString("content"));

						}

					}
				}

				// ---delete records which are not in server now
				String[] arr = list.toArray(new String[list.size()]);
				DeleteRecord("tbl_Cms1", "CmsID", arr);

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
