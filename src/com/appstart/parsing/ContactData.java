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

public class ContactData {

	Context context;
	String customerid;
	DBAdapter dba;

	public ContactData(Context ctx, String cid) {

		this.context = ctx;
		this.customerid = cid;
		this.dba = new DBAdapter(ctx);

	}

	public void add_contact_synch() {

		String response = Webservice.ContactSynch(customerid, "Android");

		dba = new DBAdapter(context);
		dba.open();

		try {

			JSONObject jo = new JSONObject(response);

			if (jo.getString("status").equals("success")) {

				JSONObject jo1 = (JSONObject) jo.get("data");
				JSONArray ja = (JSONArray) jo1.get("data");

				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo2 = (JSONObject) ja.get(i);
					JSONObject jo3 = (JSONObject) jo2.get("tbl_contact");

					jo3.getString("contact_id");
					jo3.getString("customer_id");
					jo3.getString("order");
					jo3.getString("status");
					jo3.getString("last_updated_by");
					jo3.getString("last_updated_at");
					jo3.getString("created_by");
					jo3.getString("created_at");

					System.out.println("contact id:;:"
							+ jo3.getString("contact_id"));

					dba.insertModuleContact(jo3.getString("contact_id"),
							jo3.getString("customer_id"),
							jo3.getString("status"), jo3.getString("order"),
							jo3.getString("last_updated_by"),
							jo3.getString("last_updated_at"),
							jo3.getString("created_by"),
							jo3.getString("created_at"));

					JSONArray jarr = (JSONArray) jo2.get("tbl_contact_detail");

					for (int j = 0; j < jarr.length(); j++) {

						JSONObject jo4 = (JSONObject) jarr.get(j);

						String img_name = Util.getRandomFileName();

						String img_url = jo4.getString("logo");

						if (img_url.contains(".jpg")
								|| img_url.contains(".png")) {
							Util.Store_file_phone(img_url, img_name);
						}
						Log.e("Image Url:::",
								"http://203.88.158.98/appstart_live/public/"
										+ img_url);

						dba.insertModuleContactDetails(
								jo4.getString("contact_detail_id"),
								jo4.getString("contact_id"),
								jo4.getString("language_id"),
								jo4.getString("location"),
								jo4.getString("address"),
								jo4.getString("phone_1"),
								jo4.getString("phone_2"),
								jo4.getString("phone_3"), jo4.getString("fax"),
								jo4.getString("latitude"),
								jo4.getString("longitude"),
								jo4.getString("email_1"),
								jo4.getString("email_2"),
								jo4.getString("email_3"),
								jo4.getString("website"),
								jo4.getString("timings"), img_name,
								jo4.getString("information"),
								jo4.getString("last_updated_by"),
								jo4.getString("last_updated_at"),
								jo4.getString("created_by"),
								jo4.getString("created_at"));

					}
				}

			}
			dba.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void contact_sync_module() {

		String response = Webservice.ContactSynch(customerid, "Android");

		try {
			DBAdapter dba = new DBAdapter(context);
			dba.open();
			JSONObject jo = new JSONObject(response);

			if (jo.getString("status").equals("success")) {

				JSONObject jo1 = (JSONObject) jo.get("data");
				JSONArray ja = (JSONArray) jo1.get("data");

				List<String> list = new ArrayList<String>();

				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo2 = (JSONObject) ja.get(i);
					JSONObject jo3 = (JSONObject) jo2.get("tbl_contact");

					Cursor c = dba
							.getModuleContact(jo3.getString("contact_id"));

					list.add(jo3.getString("contact_id"));

					if (c.getCount() > 0) {
						if (Util.compareDateTime(c.getString(0),
								jo3.getString("last_updated_at"))) {

							System.out.println("contact id:;:"
									+ jo3.getString("contact_id"));

							dba.updateModuleContact(
									jo3.getString("contact_id"),
									jo3.getString("customer_id"),
									jo3.getString("status"),
									jo3.getString("order"),
									jo3.getString("last_updated_by"),
									jo3.getString("last_updated_at"),
									jo3.getString("created_by"),
									jo3.getString("created_at"));

							JSONArray jarr = (JSONArray) jo2
									.get("tbl_contact_detail");

							List<String> list1 = new ArrayList<String>();

							for (int j = 0; j < jarr.length(); j++) {

								JSONObject jo4 = (JSONObject) jarr.get(j);

								list1.add(jo4.getString("contact_detail_id"));

								String img_name = Util.getRandomFileName();

								String img_url = jo4.getString("logo");

								if (img_url.contains(".jpg")
										|| img_url.contains(".png")) {
									Util.Store_file_phone(img_url, img_name);
								}
								Log.e("Image Url:::",
										"http://203.88.158.98/appstart_live/public/"
												+ img_url);
								
								dba.updateModuleContactDetails(
										jo4.getString("contact_detail_id"),
										jo4.getString("contact_id"),
										jo4.getString("language_id"),
										jo4.getString("location"),
										jo4.getString("address"),
										jo4.getString("phone_1"),
										jo4.getString("phone_2"),
										jo4.getString("phone_3"),
										jo4.getString("fax"),
										jo4.getString("latitude"),
										jo4.getString("longitude"),
										jo4.getString("email_1"),
										jo4.getString("email_2"),
										jo4.getString("email_3"),
										jo4.getString("website"),
										jo4.getString("timings"), img_name,
										jo4.getString("information"),
										jo4.getString("last_updated_by"),
										jo4.getString("last_updated_at"),
										jo4.getString("created_by"),
										jo4.getString("created_at"));

							}

							// ---delete record which are not available

							String[] arr1 = list1.toArray(new String[list1
									.size()]);

							DeleteDetailTableRecords(
									"tbl_Module_Contact_Details", "RecordID",
									"ContactID", jo3.getString("contact_id"),
									arr1);

							
						}
					} else {
						// record not abailble so need to add it in local
						// database

						dba.insertModuleContact(jo3.getString("contact_id"),
								jo3.getString("customer_id"),
								jo3.getString("status"),
								jo3.getString("order"),
								jo3.getString("last_updated_by"),
								jo3.getString("last_updated_at"),
								jo3.getString("created_by"),
								jo3.getString("created_at"));

						JSONArray jarr = (JSONArray) jo2
								.get("tbl_contact_detail");

						for (int j = 0; j < jarr.length(); j++) {

							JSONObject jo4 = (JSONObject) jarr.get(j);
							
							String img_name = Util.getRandomFileName();

							String img_url = jo4.getString("logo");

							if (img_url.contains(".jpg")
									|| img_url.contains(".png")) {
								Util.Store_file_phone(img_url, img_name);
							}
							Log.e("Image Url:::",
									"http://203.88.158.98/appstart_live/public/"
											+ img_url);

							dba.insertModuleContactDetails(
									jo4.getString("contact_detail_id"),
									jo4.getString("contact_id"),
									jo4.getString("language_id"),
									jo4.getString("location"),
									jo4.getString("address"),
									jo4.getString("phone_1"),
									jo4.getString("phone_2"),
									jo4.getString("phone_3"),
									jo4.getString("fax"),
									jo4.getString("latitude"),
									jo4.getString("longitude"),
									jo4.getString("email_1"),
									jo4.getString("email_2"),
									jo4.getString("email_3"),
									jo4.getString("website"),
									jo4.getString("timings"), img_name,
									jo4.getString("information"),
									jo4.getString("last_updated_by"),
									jo4.getString("last_updated_at"),
									jo4.getString("created_by"),
									jo4.getString("created_at"));

						}

					}
				}

				// ---delete record which are not available in server
				String[] arr = list.toArray(new String[list.size()]);
				DeleteRecord("tbl_Module_Contact", "ContactID", arr);

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
