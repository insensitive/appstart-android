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

public class DocumentData {

	Context context;
	String customerid;
	DBAdapter dba;

	public DocumentData(Context ctx, String cid) {

		this.context = ctx;
		this.customerid = cid;
		this.dba = new DBAdapter(ctx);

	}

	public void add_Document() {
		String response = Webservice.Document(customerid, "android");

		dba = new DBAdapter(context);
		dba.open();

		try {

			JSONObject jo = new JSONObject(response);
			if (jo.getString("status").equals("success")) {

				JSONObject jo1 = (JSONObject) jo.get("data");
				JSONArray ja = (JSONArray) jo1.get("data");

				for (int i = 0; i < ja.length(); i++) {

					JSONObject jo2 = (JSONObject) ja.get(i);

					if (jo2.has("tbl_module_document")) {
						System.out.println("document table reponse found::::");
						JSONObject jo3 = (JSONObject) jo2
								.get("tbl_module_document");

						dba.insertDocument(jo3.getString("module_document_id"),
								jo3.getString("module_document_category_id"),
								jo3.getString("customer_id"),
								jo3.getString("status"),
								jo3.getString("order"),
								jo3.getString("last_updated_by"),
								jo3.getString("last_updated_at"),
								jo3.getString("created_by"),
								jo3.getString("created_at"));

						JSONArray jarr = (JSONArray) jo2
								.get("tbl_module_document_detail");

						for (int j = 0; j < jarr.length(); j++) {

							JSONObject jo4 = (JSONObject) jarr.get(j);

							dba.insertDocumentDetails(
									jo4.getString("module_document_detail_id"),
									jo4.getString("module_document_id"),
									jo4.getString("language_id"),
									jo4.getString("title"),
									jo4.getString("description"),
									jo4.getString("document_path"),
									jo4.getString("keywords"),
									jo4.getString("type"),
									jo4.getString("size"),
									jo4.getString("last_updated_by"),
									jo4.getString("last_updated_at"),
									jo4.getString("created_by"),
									jo4.getString("created_at"));

						}
					} else if (jo2.has("tbl_module_document_category")) {

						JSONObject jo3 = (JSONObject) jo2
								.get("tbl_module_document_category");

						dba.insertDocumentCategory(
								jo3.getString("module_document_category_id"),
								jo3.getString("parent_id"),
								jo3.getString("customer_id"),
								jo3.getString("order"),
								jo3.getString("status"),
								jo3.getString("last_updated_by"),
								jo3.getString("last_updated_at"),
								jo3.getString("created_by"),
								jo3.getString("created_at"));

						JSONArray jarr = (JSONArray) jo2
								.get("tbl_module_document_category_detail");

						for (int j = 0; j < jarr.length(); j++) {

							JSONObject jo4 = (JSONObject) jarr.get(j);

							dba.insertDocumentCategoryDetails(
									jo4.getString("module_document_category_detail_id"),
									jo4.getString("module_document_category_id"),
									jo4.getString("language_id"), jo4
											.getString("title"), jo4
											.getString("last_updated_by"), jo4
											.getString("last_updated_at"), jo4
											.getString("created_by"), jo4
											.getString("created_at"));

						}

					}

				}
			} else {

				System.out.println("Document resopnse status is fail::::");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		dba.close();
	}

	public void Document_sync_module() {
		String response = Webservice.Document(customerid, "android");

		try {

			dba = new DBAdapter(context);
			dba.open();

			JSONObject jo = new JSONObject(response);
			if (jo.getString("status").equals("success")) {

				JSONObject jo1 = (JSONObject) jo.get("data");
				JSONArray ja = (JSONArray) jo1.get("data");

				List<String> list = new ArrayList<String>();
				List<String> list_cat = new ArrayList<String>();

				for (int i = 0; i < ja.length(); i++) {

					JSONObject jo2 = (JSONObject) ja.get(i);

					if (jo2.has("tbl_module_document")) {
						JSONObject jo3 = (JSONObject) jo2
								.get("tbl_module_document");

						Cursor c = dba.getDocumentDate(jo3
								.getString("module_document_id"));

						list.add(jo3.getString("module_document_id"));

						if (c.getCount() > 0) {

							if (Util.compareDateTime(c.getString(0),
									jo3.getString("last_updated_at"))) {

								dba.updateDocument(
										jo3.getString("module_document_id"),
										jo3.getString("customer_id"),
										jo3.getString("status"),
										jo3.getString("order"),
										jo3.getString("last_updated_by"),
										jo3.getString("last_updated_at"),
										jo3.getString("created_by"),
										jo3.getString("created_at"));

								JSONArray jarr = (JSONArray) jo2
										.get("tbl_module_document_detail");

								List<String> list1 = new ArrayList<String>();

								for (int j = 0; j < jarr.length(); j++) {

									JSONObject jo4 = (JSONObject) jarr.get(j);

									Cursor cur_hd = dba
											.getDocumentDetailsDate(jo4
													.getString("module_document_detail_id"));

									list1.add(jo4
											.getString("module_document_detail_id"));

									if (cur_hd.getCount() > 0) {
										if (Util.compareDateTime(cur_hd
												.getString(0), jo4
												.getString("last_updated_at"))) {

											dba.updateDocumentDetails(
													jo4.getString("module_document_detail_id"),
													jo4.getString("module_document_id"),
													jo4.getString("language_id"),
													jo4.getString("title"),
													jo4.getString("description"),
													jo4.getString("document_path"),
													jo4.getString("keywords"),
													jo4.getString("type"),
													jo4.getString("size"),
													jo4.getString("last_updated_by"),
													jo4.getString("last_updated_at"),
													jo4.getString("created_by"),
													jo4.getString("created_at"));

										}
									}

								}

								String[] arr1 = list1.toArray(new String[list1
										.size()]);
								DeleteDetailTableRecords("tbl_Document_Details",
										"RecordID","DocumentID",jo3.getString("module_document_id"), arr1);

							} else {

							}

						} else {

							// ---record not available so need to add in local

							dba.insertDocument(jo3
									.getString("module_document_id"), jo3
									.getString("module_document_category_id"),
									jo3.getString("customer_id"), jo3
											.getString("status"), jo3
											.getString("order"), jo3
											.getString("last_updated_by"), jo3
											.getString("last_updated_at"), jo3
											.getString("created_by"), jo3
											.getString("created_at"));

							JSONArray jarr = (JSONArray) jo2
									.get("tbl_module_document_detail");

							for (int j = 0; j < jarr.length(); j++) {

								JSONObject jo4 = (JSONObject) jarr.get(j);

								dba.insertDocumentDetails(
										jo4.getString("module_document_detail_id"),
										jo4.getString("module_document_id"),
										jo4.getString("language_id"), jo4
												.getString("title"), jo4
												.getString("description"), jo4
												.getString("document_path"),
										jo4.getString("keywords"), jo4
												.getString("type"), jo4
												.getString("size"), jo4
												.getString("last_updated_by"),
										jo4.getString("last_updated_at"), jo4
												.getString("created_by"), jo4
												.getString("created_at"));

							}

						}

					} else if (jo2.has("tbl_module_document_category")) {

						JSONObject jo3 = (JSONObject) jo2
								.get("tbl_module_document_category");

						Cursor c = dba.getDocumentCategoryDate(jo3
								.getString("module_document_category_id"));

						list_cat.add(jo3
								.getString("module_document_category_id"));

						if (c.getCount() > 0) {

							if (Util.compareDateTime(c.getString(0),
									jo3.getString("last_updated_at"))) {

								dba.updateDocumentCategory(
										jo3.getString("module_document_category_id"),
										jo3.getString("parent_id"), jo3
												.getString("customer_id"), jo3
												.getString("order"), jo3
												.getString("status"), jo3
												.getString("last_updated_by"),
										jo3.getString("last_updated_at"), jo3
												.getString("created_by"), jo3
												.getString("created_at"));

								JSONArray jarr = (JSONArray) jo2
										.get("tbl_module_document_category_detail");

								List<String> list1_cat = new ArrayList<String>();

								for (int j = 0; j < jarr.length(); j++) {

									JSONObject jo4 = (JSONObject) jarr.get(j);

									Cursor cur_hd = dba
											.getDocumentCategoryDetailsDate(jo4
													.getString("module_document_category_detail_id"));

									list1_cat
											.add(jo4.getString("module_document_category_detail_id"));

									if (cur_hd.getCount() > 0) {
										if (Util.compareDateTime(cur_hd
												.getString(0), jo4
												.getString("last_updated_at"))) {
											
											dba.updateDocumentCategoryDetails(
													jo4.getString("module_document_category_detail_id"),
													jo4.getString("module_document_category_id"),
													jo4.getString("language_id"),
													jo4.getString("title"),
													jo4.getString("last_updated_by"),
													jo4.getString("last_updated_at"),
													jo4.getString("created_by"),
													jo4.getString("created_at"));
										}
									}

								}
								String[] arr1_cat = list1_cat
										.toArray(new String[list1_cat.size()]);
								DeleteDetailTableRecords("tbl_Document_Category_Details",
										"RecordID","CategoryID",jo3.getString("module_document_category_id"), arr1_cat);
							}

						} else {

							dba.insertDocumentCategory(jo3
									.getString("module_document_category_id"),
									jo3.getString("parent_id"), jo3
											.getString("customer_id"), jo3
											.getString("order"), jo3
											.getString("status"), jo3
											.getString("last_updated_by"), jo3
											.getString("last_updated_at"), jo3
											.getString("created_by"), jo3
											.getString("created_at"));

							JSONArray jarr = (JSONArray) jo2
									.get("tbl_module_document_category_detail");

							for (int j = 0; j < jarr.length(); j++) {

								JSONObject jo4 = (JSONObject) jarr.get(j);

								dba.insertDocumentCategoryDetails(
										jo4.getString("module_document_category_detail_id"),
										jo4.getString("module_document_category_id"),
										jo4.getString("language_id"), jo4
												.getString("title"), jo4
												.getString("last_updated_by"),
										jo4.getString("last_updated_at"), jo4
												.getString("created_by"), jo4
												.getString("created_at"));

							}

						}

					}
				}

				// ---delete records which are not in server now
				String[] arr = list.toArray(new String[list.size()]);
				DeleteRecord("tbl_Document", "DocumentID", arr);
				
				String[] arr_cat = list_cat
						.toArray(new String[list_cat.size()]);
				DeleteRecord("tbl_Document_Category", "CategoryID", arr_cat);

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
