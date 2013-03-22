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

public class ImageGalleryData {

	Context context;
	String customerid;
	DBAdapter dba;

	public ImageGalleryData(Context ctx, String cid) {

		this.context = ctx;
		this.customerid = cid;
		this.dba=new DBAdapter(ctx);

	}

	public void add_ImageGallery() {
		String response = Webservice.ImageGallery(customerid, "android");

		 dba = new DBAdapter(context);
		dba.open();

		try {

			JSONObject jo = new JSONObject(response);
			if (jo.getString("status").equals("success")) {

				JSONObject jo1 = (JSONObject) jo.get("data");
				JSONArray ja = (JSONArray) jo1.get("data");

				for (int i = 0; i < ja.length(); i++) {

					JSONObject jo2 = (JSONObject) ja.get(i);
					
					
					if(jo2.has("tbl_module_image_gallery"))
					{
					
					JSONObject jo3 = (JSONObject) jo2
							.get("tbl_module_image_gallery");

					dba.insertImageGallery(
							jo3.getString("module_image_gallery_id"),
							jo3.getString("module_image_gallery_category_id"),
							jo3.getString("customer_id"),
							jo3.getString("status"), jo3.getString("order"),
							jo3.getString("last_updated_by"),
							jo3.getString("last_updated_at"),
							jo3.getString("created_by"),
							jo3.getString("created_at"));

					JSONArray jarr = (JSONArray) jo2
							.get("tbl_module_image_gallery_detail");

					for (int j = 0; j < jarr.length(); j++) {

						JSONObject jo4 = (JSONObject) jarr.get(j);

						String img_url = jo4.getString("image_path");
						
						String img_thumb_url=jo4.getString("image_path").replace(".jpg", "_thumb.jpg");
						
						String img_name = "thumbs" +j+ Util.getRandomFileName();

						if (img_url.contains(".jpg")
								|| img_url.contains(".png")) {
							Util.Store_file_phone(
									img_thumb_url,
									img_name);
							
						//String d[]=new String[2];
							
							//d[0]=img_thumb_url;
							//d[0]=img_name;
							
						//	new ImageDownload().execute(d);
						}

						Log.e("Url:::",
								"http://203.88.158.98/appstart_live/public/"
										+ img_thumb_url);

						String img_name1 = "Ig" + Util.getRandomFileName();

						if (img_url.contains(".jpg")
								|| img_url.contains(".png")) {
							Util.Store_file_phone(img_url, img_name1);
							
							
							
						}

						dba.insertImageGalleryDetails(
								jo4.getString("module_image_gallery_detail_id"),
								jo4.getString("module_image_gallery_id"),
								jo4.getString("language_id"),
								jo4.getString("title"),
								jo4.getString("description"), img_name,
								jo4.getString("keywords"), img_name1);

					}
					}
					else if(jo2.has("tbl_module_image_gallery_category"))
					{
						
						
						JSONObject jo3 = (JSONObject) jo2
								.get("tbl_module_image_gallery_category");

						dba.insertImageGalleryCategory(
								jo3.getString("module_image_gallery_category_id"),
								jo3.getString("customer_id"),
								jo3.getString("status"), jo3.getString("order"),
								jo3.getString("last_updated_by"),
								jo3.getString("last_updated_at"),
								jo3.getString("created_by"),
								jo3.getString("created_at"));
						
						
						JSONArray jarr = (JSONArray) jo2
								.get("tbl_module_image_gallery_category_detail");

						for (int j = 0; j < jarr.length(); j++) {

							JSONObject jo4 = (JSONObject) jarr.get(j);
							
							
							dba.insertImageGalleryCategoryDetails(
									jo4.getString("module_image_gallery_category_detail_id"),
									jo4.getString("module_image_gallery_category_id"),
									jo4.getString("language_id"),
									jo4.getString("title"),
									jo4.getString("last_updated_by"),
									jo4.getString("last_updated_at"),
									jo4.getString("created_by"),
									jo4.getString("created_at"));
							

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

		String response = Webservice.ImageGallery(customerid, "android");

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
					
					
					
					if(jo2.has("tbl_module_image_gallery"))
					{
					
					
					JSONObject jo3 = (JSONObject) jo2
							.get("tbl_module_image_gallery");

					list.add(jo3.getString("module_image_gallery_id"));

					Cursor c = dba.getImageGalleryDate(jo3
							.getString("module_image_gallery_id"));

					if (c.getCount() > 0) {

						if (Util.compareDateTime(c.getString(0),
								jo3.getString("last_updated_at"))) {
							
							dba.updateImageGallery(
									jo3.getString("module_image_gallery_id"),
									jo3.getString("module_image_gallery_category_id"),
									jo3.getString("customer_id"), jo3
											.getString("status"), jo3
											.getString("order"), jo3
											.getString("last_updated_by"), jo3
											.getString("last_updated_at"), jo3
											.getString("created_by"), jo3
											.getString("created_at"));
							
							
							JSONArray jarr = (JSONArray) jo2
									.get("tbl_module_image_gallery_detail");

							List<String> list1 = new ArrayList<String>();

							for (int j = 0; j < jarr.length(); j++) {

								JSONObject jo4 = (JSONObject) jarr.get(j);

								list1.add(jo4
										.getString("module_image_gallery_detail_id"));

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
									Util.Store_file_phone(img_url, img_name1);
								}
								
								dba.updateImageGalleryDetails(
										jo4.getString("module_image_gallery_detail_id"),
										jo4.getString("module_image_gallery_id"),
										jo4.getString("language_id"), jo4
												.getString("title"), jo4
												.getString("description"),
										img_name, jo4.getString("keywords"),
										img_name1);

							}

							String[] arr1 = list1.toArray(new String[list1
									.size()]);
							DeleteDetailTableRecords("tbl_Image_Gallery_Details",
									"RecordID", "ImageGalleryID",
									jo3.getString("module_image_gallery_id"), arr1);
							
							System.out.println("Updation in Ig module::");
							
						} else {
							System.out
									.println("No Updation in Ig module::");
						}
					} 
					
					else {

						// ---record not available so need to add

						dba.insertImageGallery(
								jo3.getString("module_image_gallery_id"),
								jo3.getString("module_image_gallery_category_id"),
								jo3.getString("customer_id"),
								jo3.getString("status"), jo3.getString("order"),
								jo3.getString("last_updated_by"),
								jo3.getString("last_updated_at"),
								jo3.getString("created_by"),
								jo3.getString("created_at"));

						JSONArray jarr = (JSONArray) jo2
								.get("tbl_module_image_gallery_detail");

						for (int j = 0; j < jarr.length(); j++) {

							JSONObject jo4 = (JSONObject) jarr.get(j);

							String img_url = jo4.getString("image_path");
							String img_name = "thumb" + Util.getRandomFileName();

							if (img_url.contains(".jpg")
									|| img_url.contains(".png")) {
								Util.Store_file_phone(
										img_url.replace(".jpg", "_thumb.jpg"),
										img_name);
							}

							Log.e("Url:::",
									"http://203.88.158.98/appstart_live/public/"
											+ img_url.replace(".jpg", "_thumb.jpg"));
							
							String img_name1 = "Ig" + Util.getRandomFileName();

							if (img_url.contains(".jpg")
									|| img_url.contains(".png")) {
								
								Util.Store_file_phone(img_url, img_name1);
								
							}
							
							dba.insertImageGalleryDetails(
									jo4.getString("module_image_gallery_detail_id"),
									jo4.getString("module_image_gallery_id"),
									jo4.getString("language_id"),
									jo4.getString("title"),
									jo4.getString("description"), img_name,
									jo4.getString("keywords"), img_name1);

						}

					}
					
					
					}else if(jo2.has("tbl_module_image_gallery_category"))
					{
						
						JSONObject jo3 = (JSONObject) jo2
								.get("tbl_module_image_gallery_category");

						list_cat.add(jo3.getString("module_image_gallery_category_id"));

						Cursor c = dba.getImageGalleryCategoryDate(jo3
								.getString("module_image_gallery_category_id"));

						if (c.getCount() > 0) {

							if (Util.compareDateTime(c.getString(0),
									jo3.getString("last_updated_at"))) {
								
								dba.updateImageGalleryCategory(
										jo3.getString("module_image_gallery_category_id"),
										jo3.getString("customer_id"), jo3
												.getString("status"), jo3
												.getString("order"), jo3
												.getString("last_updated_by"), jo3
												.getString("last_updated_at"), jo3
												.getString("created_by"), jo3
												.getString("created_at"));
								
								
								JSONArray jarr = (JSONArray) jo2
										.get("tbl_module_image_gallery_category_detail");

								List<String> list1_cat = new ArrayList<String>();

								for (int j = 0; j < jarr.length(); j++) {

									JSONObject jo4 = (JSONObject) jarr.get(j);

									list1_cat.add(jo4
											.getString("module_image_gallery_category_detail_id"));

									
									
									dba.updateImageGalleryDetails(
											jo4.getString("module_image_gallery_category_detail_id"),
											jo4.getString("module_image_gallery_category_id"),
											jo4.getString("language_id"), jo4
											.getString("title"),	jo4.getString("last_updated_by"),
											jo4.getString("last_updated_at"),
											jo4.getString("created_by"),
											jo4.getString("created_at"));

								}

								String[] arr1_cat = list1_cat.toArray(new String[list1_cat
										.size()]);
								DeleteDetailTableRecords("tbl_Image_Gallery_Category_Details",
										"RecordID", "CategoryID",
										jo3.getString("module_image_gallery_category_id"), arr1_cat);
								
								
								
							}  
						} 
						
						else {

							// ---record not available so need to add

							dba.insertImageGalleryCategory(
									jo3.getString("module_image_gallery_category_id"),
									jo3.getString("customer_id"),
									jo3.getString("status"), jo3.getString("order"),
									jo3.getString("last_updated_by"),
									jo3.getString("last_updated_at"),
									jo3.getString("created_by"),
									jo3.getString("created_at"));
							
							
							JSONArray jarr = (JSONArray) jo2
									.get("tbl_module_image_gallery_category_detail");

							for (int j = 0; j < jarr.length(); j++) {

								JSONObject jo4 = (JSONObject) jarr.get(j);
								
								

								dba.insertImageGalleryCategoryDetails(
										jo4.getString("module_image_gallery_category_detail_id"),
										jo4.getString("module_image_gallery_category_id"),
										jo4.getString("language_id"),
										jo4.getString("title"),
										jo4.getString("last_updated_by"),
										jo4.getString("last_updated_at"),
										jo4.getString("created_by"),
										jo4.getString("created_at"));

							}

						}
						
						
						
						
					}
				}
				
				// ---delete records which are not in server now
				String[] arr = list.toArray(new String[list.size()]);
				DeleteRecord("tbl_Image_Gallery", "ImageGalleryID", arr);
				
				
				// ---delete records which are not in server now
				String[] arr_cat = list_cat.toArray(new String[list_cat.size()]);
				DeleteRecord("tbl_Image_Gallery_Category", "CategoryID", arr_cat);

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
