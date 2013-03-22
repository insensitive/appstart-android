package com.appstart.parsing;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.appstart.database.DBAdapter;
import com.appstart.utility.Util;
import com.appstart.webservice.Webservice;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class EventsData {

	Context context;
	String customerid;
	DBAdapter dba;
	
	public EventsData(Context ctx, String cid) {
		this.context = ctx;
		this.customerid = cid;
		this.dba=new DBAdapter(ctx);

	}

	public void add_Events() {

		String response = Webservice.Events(customerid, "android");

		dba = new DBAdapter(context);
		dba.open();

		try {

			JSONObject jo = new JSONObject(response);
			if (jo.getString("status").equals("success")) {

				JSONObject jo1 = (JSONObject) jo.get("data");
				JSONArray ja = (JSONArray) jo1.get("data");

				for (int i = 0; i < ja.length(); i++) {

					JSONObject jo2 = (JSONObject) ja.get(i);
					JSONObject jo3 = (JSONObject) jo2.get("tbl_module_events");

					dba.insertEvents(jo3.getString("module_events_id"),
							jo3.getString("customer_id"),
							jo3.getString("status"), jo3.getString("order"),
							jo3.getString("last_updated_by"),
							jo3.getString("last_updated_at"),
							jo3.getString("created_by"),
							jo3.getString("created_at"));

					JSONArray jarr = (JSONArray) jo2
							.get("tbl_module_events_detail");

					for (int j = 0; j < jarr.length(); j++) {

						JSONObject jo4 = (JSONObject) jarr.get(j);

						String img_url = jo4.getString("image");
						String img_name = "event" + Util.getRandomFileName();

						if (img_url.contains(".jpg")
								|| img_url.contains(".png")) {
							Util.Store_file_phone(img_url, img_name);
						}
						
						
						Log.e("Event Logo Url:::",
								"http://203.88.158.98/appstart_live/public/"
										+ img_url);

						
						dba.insertEventsDetails(
								jo4.getString("module_events_detail_id"),
								jo4.getString("module_events_id"),
								jo4.getString("language_id"),
								jo4.getString("start_date_time"),
								jo4.getString("end_date_time"),
								jo4.getString("title"),
								jo4.getString("description"), img_name, "", "",
								jo4.getString("recurrence"),
								jo4.getString("notes"),
								jo4.getString("last_updated_by"),
								jo4.getString("last_updated_at"),
								jo4.getString("created_by"),
								jo4.getString("created_at"));

						
					}
					
					
					
				}
			} else {
				
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		dba.close();
	}
	
	public void Events_sync_module() {

		String response = Webservice.Events(customerid, "android");

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
					JSONObject jo3 = (JSONObject) jo2.get("tbl_module_events");

					list.add(jo3.getString("module_events_id"));

					Cursor c = dba.getEventsDate(jo3
							.getString("module_events_id"));

					if (c.getCount() > 0) {

						if (Util.compareDateTime(c.getString(0),
								jo3.getString("last_updated_at"))) {

							dba.updateEvents(jo3.getString("module_events_id"),
									jo3.getString("customer_id"),
									jo3.getString("status"),
									jo3.getString("order"),
									jo3.getString("last_updated_by"),
									jo3.getString("last_updated_at"),
									jo3.getString("created_by"),
									jo3.getString("created_at"));

							JSONArray jarr = (JSONArray) jo2
									.get("tbl_module_events_detail");

							List<String> list1 = new ArrayList<String>();

							for (int j = 0; j < jarr.length(); j++) {

								JSONObject jo4 = (JSONObject) jarr.get(j);

								Cursor cur_hd = dba.getEventsDetailsDate(jo4
										.getString("module_events_detail_id"));

								list1.add(jo4
										.getString("module_events_detail_id"));

								if (cur_hd.getCount() > 0) {
									if (Util.compareDateTime(
											cur_hd.getString(0),
											jo4.getString("last_updated_at"))) {

										String img_url = 
												 jo4.getString("image");
										String img_name = "event"+Util
												.getRandomFileName();

										Util.Store_file_phone(img_url, img_name);

										
										Log.e("updated Event logo Url:::",
												"http://203.88.158.98/appstart_live/public/"
														+ img_url);
										Log.e("updated Event logo name:::",img_name);
										
										dba.updateEventsDetails(
												jo4.getString("module_events_detail_id"),
												jo4.getString("module_events_id"),
												jo4.getString("language_id"),
												jo4.getString("start_date_time"),
												jo4.getString("end_date_time"),
												jo4.getString("title"),
												jo4.getString("description"),
												img_name,
												"",
												"",
												jo4.getString("recurrence"),
												jo4.getString("notes"),
												jo4.getString("last_updated_by"),
												jo4.getString("last_updated_at"),
												jo4.getString("created_by"),
												jo4.getString("created_at"));

									}
								}

							}

							String[] arr1 = list1.toArray(new String[list1
									.size()]);
							DeleteDetailTableRecords("tbl_Events_Details",
									"RecordID", "EventID",
									jo3.getString("module_events_id"), arr1);

							

						}
					} else {

						// ---record not available so need to add

						dba.insertEvents(jo3.getString("module_events_id"),
								jo3.getString("customer_id"),
								jo3.getString("status"),
								jo3.getString("order"),
								jo3.getString("last_updated_by"),
								jo3.getString("last_updated_at"),
								jo3.getString("created_by"),
								jo3.getString("created_at"));

						JSONArray jarr = (JSONArray) jo2
								.get("tbl_module_events_detail");

						for (int j = 0; j < jarr.length(); j++) {

							JSONObject jo4 = (JSONObject) jarr.get(j);

							String img_url = jo4.getString("image");
							String img_name = "event"
									+ Util.getRandomFileName();

							Util.Store_file_phone(img_url, img_name);

							Log.e("Url:::",
									"http://203.88.158.98/appstart_live/public/"
											+ img_url);

							dba.insertEventsDetails(
									jo4.getString("module_events_detail_id"),
									jo4.getString("module_events_id"),
									jo4.getString("language_id"),
									jo4.getString("start_date_time"),
									jo4.getString("end_date_time"),
									jo4.getString("title"),
									jo4.getString("description"), img_name, "",
									"", jo4.getString("recurrence"),
									jo4.getString("notes"),
									jo4.getString("last_updated_by"),
									jo4.getString("last_updated_at"),
									jo4.getString("created_by"),
									jo4.getString("created_at"));

						}

					}
				}

				// ---delete records which are not in server now
				String[] arr = list.toArray(new String[list.size()]);
				DeleteRecord("tbl_Events", "EventID", arr);

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
