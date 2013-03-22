package com.appstart.parsing;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.appstart.async.ImageDownload;
import com.appstart.database.DBAdapter;
import com.appstart.utility.Util;
import com.appstart.webservice.Webservice;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class ImageGalleryData1 {

	Context context;
	String customerid;
	DBAdapter dba;

	public ImageGalleryData1(Context ctx, String cid) {

		this.context = ctx;
		this.customerid = cid;
		this.dba = new DBAdapter(context);

	}

	public void add_ImageGallery() {
		String response = Webservice.ImageGallery1(customerid, "android");

		dba = new DBAdapter(context);
		dba.open();

		try {

			JSONObject jo = new JSONObject(response);

			if (jo.getString("status").equals("success")) {

				JSONObject jo1 = (JSONObject) jo.get("data");
				JSONArray ja = (JSONArray) jo1.get("data");

				for (int i = 0; i < ja.length(); i++) {

					System.out.println("adding image gallery 1 data ");

					JSONObject jo2 = (JSONObject) ja.get(i);

					if (jo2.has("tbl_module_image_gallery_1")) {

						JSONObject jo3 = (JSONObject) jo2
								.get("tbl_module_image_gallery_1");

						dba.insertImageGallery1(
								jo3.getString("module_image_gallery_1_id"),
								jo3.getString("module_image_gallery_category_1_id"),
								jo3.getString("customer_id"), jo3
										.getString("status"), jo3
										.getString("order"), jo3
										.getString("last_updated_by"), jo3
										.getString("last_updated_at"), jo3
										.getString("created_by"), jo3
										.getString("created_at"));

						JSONArray jarr = (JSONArray) jo2
								.get("tbl_module_image_gallery_detail_1");

						for (int j = 0; j < jarr.length(); j++) {

							JSONObject jo4 = (JSONObject) jarr.get(j);

							String img_url = jo4.getString("image_path");
							String img_name = "thumb" + j
									+ Util.getRandomFileName();

							if (img_url.contains(".jpg")
									|| img_url.contains(".png")) {
								Util.Store_file_phone(
										img_url.replace(".jpg", "_thumb.jpg"),
										img_name);
							}

							Log.e("Url:::",
									"http://203.88.158.98/appstart_live/public/"
											+ img_url.replace(".jpg",
													"_thumb.jpg"));

							String img_name1 = "Ig" + j
									+ Util.getRandomFileName();

							if (img_url.contains(".jpg")
									|| img_url.contains(".png")) {
								Util.Store_file_phone(img_url, img_name1);

								/*
								 * Object d[]=new Object[2]; d[0]=img_url;
								 * d[1]=img_name1;
								 */
								// new ImageDownload().execute(d);

							}

							dba.insertImageGalleryDetails1(
									jo4.getString("module_image_gallery_detail_1_id"),
									jo4.getString("module_image_gallery_1_id"),
									jo4.getString("language_id"), jo4
											.getString("title"), jo4
											.getString("description"),
									img_name, jo4.getString("keywords"),
									img_name1);

						}
					} else if (jo2.has("tbl_module_image_gallery_category_1")) {

						JSONObject jo3 = (JSONObject) jo2
								.get("tbl_module_image_gallery_category_1");

						dba.insertImageGalleryCategory1(
								jo3.getString("module_image_gallery_category_1_id"),
								jo3.getString("customer_id"), jo3
										.getString("status"), jo3
										.getString("order"), jo3
										.getString("last_updated_by"), jo3
										.getString("last_updated_at"), jo3
										.getString("created_by"), jo3
										.getString("created_at"));

						JSONArray jarr = (JSONArray) jo2
								.get("tbl_module_image_gallery_category_detail_1");

						for (int j = 0; j < jarr.length(); j++) {

							JSONObject jo4 = (JSONObject) jarr.get(j);

							dba.insertImageGalleryCategoryDetails1(
									jo4.getString("module_image_gallery_category_detail_1_id"),
									jo4.getString("module_image_gallery_category_1_id"),
									jo4.getString("language_id"), jo4
											.getString("title"), jo4
											.getString("last_updated_by"), jo4
											.getString("last_updated_at"), jo4
											.getString("created_by"), jo4
											.getString("created_at"));

						}
					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		dba.close();

	}

	public void ImageGallery_sync_module() {

		String response = Webservice.ImageGallery1(customerid, "android");

		try {

			dba = new DBAdapter(context);
			dba.open();

			JSONObject jo = new JSONObject(response);

			if (jo.getString("status").equals("success")) {

				JSONObject jo1 = (JSONObject) jo.get("data");
				JSONArray ja = (JSONArray) jo1.get("data");
				List<String> list = new ArrayList<String>();
				List<String> list_ig1 = new ArrayList<String>();

				for (int i = 0; i < ja.length(); i++) {

					JSONObject jo2 = (JSONObject) ja.get(i);

					if (jo2.has("tbl_module_image_gallery_1")) {

						JSONObject jo3 = (JSONObject) jo2
								.get("tbl_module_image_gallery_1");

						list.add(jo3.getString("module_image_gallery_1_id"));

						System.out.println("id:::::::"
								+ jo3.getString("module_image_gallery_1_id"));

						Cursor c = dba.getImageGalleryDate1(jo3
								.getString("module_image_gallery_1_id"));

						System.out.println("ig1 images counter...."
								+ c.getCount());

						if (c.getCount() > 0) {

							if (Util.compareDateTime(c.getString(0),
									jo3.getString("last_updated_at"))) {

								JSONArray jarr = (JSONArray) jo2
										.get("tbl_module_image_gallery_detail_1");

								dba.updateImageGallery1(
										jo3.getString("module_image_gallery_1_id"),
										jo3.getString("module_image_gallery_category_1_id"),
										jo3.getString("customer_id"), jo3
												.getString("status"), jo3
												.getString("order"), jo3
												.getString("last_updated_by"),
										jo3.getString("last_updated_at"), jo3
												.getString("created_by"), jo3
												.getString("created_at"));

								List<String> list1 = new ArrayList<String>();

								for (int j = 0; j < jarr.length(); j++) {

									JSONObject jo4 = (JSONObject) jarr.get(j);

									list1.add(jo4
											.getString("module_image_gallery_detail_1_id"));

									String img_url = jo4
											.getString("image_path");
									String img_name = "thumb"
											+ Util.getRandomFileName();

									if (img_url.contains(".jpg")
											|| img_url.contains(".png")) {
										Util.Store_file_phone(img_url.replace(
												".jpg", "_thumb.jpg"), img_name);
									}

									Log.e("Url:::",
											"http://203.88.158.98/appstart_live/public/"
													+ img_url.replace(".jpg",
															"_thumb.jpg"));

									String img_name1 = "Ig" + j
											+ Util.getRandomFileName();

									if (img_url.contains(".jpg")
											|| img_url.contains(".png")) {
										Util.Store_file_phone(img_url,
												img_name1);

										/*
										 * Object d[]=new Object[2];
										 * d[0]=img_url; d[1]=img_name1;
										 * 
										 * new ImageDownload().execute(d);
										 */
									}

									dba.updateImageGalleryDetails1(
											jo4.getString("module_image_gallery_detail_1_id"),
											jo4.getString("module_image_gallery_1_id"),
											jo4.getString("language_id"), jo4
													.getString("title"), jo4
													.getString("description"),
											img_name,
											jo4.getString("keywords"),
											img_name1);

								}

								String[] arr1 = list1.toArray(new String[list1
										.size()]);
								DeleteDetailTableRecords(
										"tbl_Image_Gallery_Details1",
										"RecordID",
										"ImageGalleryID",
										jo3.getString("module_image_gallery_1_id"),
										arr1);

							}
						} else {

							// ---record not available so need to add

							dba.insertImageGallery1(
									jo3.getString("module_image_gallery_1_id"),
									jo3.getString("module_image_gallery_category_1_id"),
									jo3.getString("customer_id"), jo3
											.getString("status"), jo3
											.getString("order"), jo3
											.getString("last_updated_by"), jo3
											.getString("last_updated_at"), jo3
											.getString("created_by"), jo3
											.getString("created_at"));

							JSONArray jarr = (JSONArray) jo2
									.get("tbl_module_image_gallery_detail_1");

							for (int j = 0; j < jarr.length(); j++) {

								JSONObject jo4 = (JSONObject) jarr.get(j);

								String img_url = jo4.getString("image_path");
								String img_name = "thumb"
										+ Util.getRandomFileName();

								if (img_url.contains(".jpg")
										|| img_url.contains(".png")) {
									Util.Store_file_phone(img_url.replace(
											".jpg", "_thumb.jpg"), img_name);
								}

								Log.e("Url:::",
										"http://203.88.158.98/appstart_live/public/"
												+ img_url.replace(".jpg",
														"_thumb.jpg"));

								String img_name1 = "Ig"
										+ Util.getRandomFileName();

								if (img_url.contains(".jpg")
										|| img_url.contains(".png")) {

									 Util.Store_file_phone(img_url,
									 img_name1);
									/*Object d[] = new Object[2];
									d[0] = img_url;
									d[1] = img_name1;

									new ImageDownload().execute(d);*/

								}

								dba.insertImageGalleryDetails1(
										jo4.getString("module_image_gallery_detail_1_id"),
										jo4.getString("module_image_gallery_1_id"),
										jo4.getString("language_id"), jo4
												.getString("title"), jo4
												.getString("description"),
										img_name, jo4.getString("keywords"),
										img_name1);

							}

						}

					} else if (jo2.has("tbl_module_image_gallery_category_1")) {

						JSONObject jo3 = (JSONObject) jo2
								.get("tbl_module_image_gallery_category_1");

						list_ig1.add(jo3
								.getString("module_image_gallery_category_1_id"));

						Cursor c = dba
								.getImageGalleryCategoryDate1(jo3
										.getString("module_image_gallery_category_1_id"));

						if (c.getCount() > 0) {

							if (Util.compareDateTime(c.getString(0),
									jo3.getString("last_updated_at"))) {

								dba.updateImageGalleryCategory1(
										jo3.getString("module_image_gallery_category_1_id"),
										jo3.getString("customer_id"), jo3
												.getString("status"), jo3
												.getString("order"), jo3
												.getString("last_updated_by"),
										jo3.getString("last_updated_at"), jo3
												.getString("created_by"), jo3
												.getString("created_at"));

								JSONArray jarr = (JSONArray) jo2
										.get("tbl_module_image_gallery_category_detail_1");

								List<String> list1 = new ArrayList<String>();

								for (int j = 0; j < jarr.length(); j++) {

									JSONObject jo4 = (JSONObject) jarr.get(j);

									list1.add(jo4
											.getString("module_image_gallery_category_detail_1_id"));

									dba.updateImageGalleryDetails1(
											jo4.getString("module_image_gallery_category_detail_1_id"),
											jo4.getString("module_image_gallery_category_1_id"),
											jo4.getString("language_id"), jo4
													.getString("title"),
											jo4.getString("last_updated_by"),
											jo4.getString("last_updated_at"),
											jo4.getString("created_by"), jo4
													.getString("created_at"));

								}

								String[] arr1 = list1.toArray(new String[list1
										.size()]);
								DeleteDetailTableRecords(
										"tbl_Image_Gallery_Category_Details1",
										"RecordID",
										"CategoryID",
										jo3.getString("module_image_gallery_category_1_id"),
										arr1);

							}

						}

						else {

							// ---record not available so need to add

							dba.insertImageGalleryCategory1(
									jo3.getString("module_image_gallery_category_1_id"),
									jo3.getString("customer_id"), jo3
											.getString("status"), jo3
											.getString("order"), jo3
											.getString("last_updated_by"), jo3
											.getString("last_updated_at"), jo3
											.getString("created_by"), jo3
											.getString("created_at"));

							JSONArray jarr = (JSONArray) jo2
									.get("tbl_module_image_gallery_category_detail_1");

							for (int j = 0; j < jarr.length(); j++) {

								JSONObject jo4 = (JSONObject) jarr.get(j);

								dba.insertImageGalleryCategoryDetails1(
										jo4.getString("module_image_gallery_category_detail_1_id"),
										jo4.getString("module_image_gallery_category_1_id"),
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
				DeleteRecord("tbl_Image_Gallery1", "ImageGalleryID", arr);

				// ---delete records which are not in server now
				String[] arr_cat1 = list_ig1
						.toArray(new String[list_ig1.size()]);
				DeleteRecord("tbl_Image_Gallery_Category1", "CategoryID",
						arr_cat1);

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
