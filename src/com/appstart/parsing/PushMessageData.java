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

public class PushMessageData {

	Context context;
	String customerid;
	DBAdapter dba;

	public PushMessageData(Context ctx, String cid) {
		
		this.context = ctx;
		this.customerid = cid;
		this.dba=new DBAdapter(ctx);

	}

	
	public void add_Push_Message() {

		String response = Webservice.PushMessage(customerid, "android");

		dba = new DBAdapter(context);
		dba.open();
		try {
			

			JSONObject jo = new JSONObject(response);
			if (jo.getString("status").equals("success")) {

				JSONObject jo1 = (JSONObject) jo.get("data");
				JSONArray ja = (JSONArray) jo1.get("data");

				for (int i = 0; i < ja.length(); i++) {

					JSONObject jo2 = (JSONObject) ja.get(i);
					JSONObject jo3 = (JSONObject) jo2.get("tbl_push_message");

					jo3.getString("push_message_id");
					jo3.getString("customer_id");
					jo3.getString("order");
					jo3.getString("status");
					jo3.getString("last_updated_by");
					jo3.getString("last_updated_at");

					jo3.getString("created_by");
					jo3.getString("created_at");

					dba.insertPushMessage(jo3.getString("push_message_id"),
							jo3.getString("customer_id"),
							jo3.getString("status"), jo3.getString("order"),
							jo3.getString("last_updated_by"),
							jo3.getString("last_updated_at"),
							jo3.getString("created_by"),
							jo3.getString("created_at"));

					JSONArray jarr = (JSONArray) jo2
							.get("tbl_push_message_detail");

					for (int j = 0; j < jarr.length(); j++) {

						JSONObject jo4 = (JSONObject) jarr.get(j);

						dba.insertPushMessageDetails(
								jo4.getString("push_message_detail_id"),
								jo4.getString("push_message_id"),
								jo4.getString("language_id"),
								jo4.getString("title"),
								jo4.getString("description"),
								jo4.getString("message_date"));

					}
				}
			}  

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		dba.close();
	}
	
	
	
	
	// ----push message sync module
	public void PushMessage_sync_module() {
		String response = Webservice.PushMessage(customerid, "android");

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
					JSONObject jo3 = (JSONObject) jo2.get("tbl_push_message");

					list.add(jo3.getString("push_message_id"));

					Cursor c = dba.getPushMessageDate(jo3
							.getString("push_message_id"));

					if (c.getCount() > 0) {

						if (Util.compareDateTime(c.getString(0),
								jo3.getString("last_updated_at"))) {

							dba.updatePushMessage(
									jo3.getString("push_message_id"),
									jo3.getString("customer_id"),
									jo3.getString("status"),
									jo3.getString("order"),
									jo3.getString("last_updated_by"),
									jo3.getString("last_updated_at"),
									jo3.getString("created_by"),
									jo3.getString("created_at"));

							JSONArray jarr = (JSONArray) jo2
									.get("tbl_push_message_detail");

							List<String> list1 = new ArrayList<String>();

							for (int j = 0; j < jarr.length(); j++) {

								JSONObject jo4 = (JSONObject) jarr.get(j);

								list1.add(jo4
										.getString("push_message_detail_id"));

								dba.UpdatePushMessageDetails(
										jo4.getString("push_message_detail_id"),
										jo4.getString("push_message_id"),
										jo4.getString("language_id"),
										jo4.getString("title"),
										jo4.getString("description"),
										jo4.getString("message_date"));

							}

							// ---delete record which are not in server now
							String[] arr1 = list1.toArray(new String[list1
									.size()]);
							DeleteDetailTableRecords(
									"tbl_Push_Message_Details", "RecordID",
									"PushMessageID",
									jo3.getString("push_message_id"), arr1);

							 
						}  
					} else {

						// ----record not available so need to add in local
						// database--///

						dba.insertPushMessage(jo3.getString("push_message_id"),
								jo3.getString("customer_id"),
								jo3.getString("status"),
								jo3.getString("order"),
								jo3.getString("last_updated_by"),
								jo3.getString("last_updated_at"),
								jo3.getString("created_by"),
								jo3.getString("created_at"));

						JSONArray jarr = (JSONArray) jo2
								.get("tbl_push_message_detail");

						for (int j = 0; j < jarr.length(); j++) {

							JSONObject jo4 = (JSONObject) jarr.get(j);

							dba.insertPushMessageDetails(
									jo4.getString("push_message_detail_id"),
									jo4.getString("push_message_id"),
									jo4.getString("language_id"),
									jo4.getString("title"),
									jo4.getString("description"),
									jo4.getString("message_date"));

						}

					}
				}

				// ---delete record which are not in server now
				String[] arr = list.toArray(new String[list.size()]);
				DeleteRecord("tbl_Push_Message", "PushMessageID", arr);

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
