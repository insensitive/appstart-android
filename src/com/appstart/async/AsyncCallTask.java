package com.appstart.async;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import com.appstart.Splash;
import com.appstart.TabSample;
import com.appstart.database.DBAdapter;
import com.appstart.parsing.CmsData;
import com.appstart.parsing.CmsData1;
import com.appstart.parsing.CmsData2;
import com.appstart.parsing.ContactData;
import com.appstart.parsing.DocumentData;
import com.appstart.parsing.EventsData;
import com.appstart.parsing.HomeWallpaperData;
import com.appstart.parsing.ImageGalleryData;
import com.appstart.parsing.ImageGalleryData1;
import com.appstart.parsing.MusicData;
import com.appstart.parsing.PushMessageData;
import com.appstart.parsing.SocialMediaData;
import com.appstart.parsing.WebsiteData;
import com.appstart.parsing.WebsiteData1;
import com.appstart.tabgroup.TabGroupMore;
import com.appstart.utility.Constant;
import com.appstart.utility.Util;
import com.appstart.webservice.Webservice;
import com.google.android.gms.internal.c;

public class AsyncCallTask extends AsyncTask<String, Integer, Object> {
	String TAG = AsyncCallTask.class.getSimpleName();
	String response;
	Context context;
	// Appstartapplication app;

	// ---progressdialog bar variables---
	ProgressDialog progressBar;
	private int progressBarStatus = 0;
	private Handler progressBarHandler = new Handler();

	public AsyncCallTask(Context context) {
		super();
		this.context = context;

		// prepare for a progress bar dialog
		progressBar = new ProgressDialog(context);
		progressBar.setCancelable(false);
		progressBar.setMessage("Data updating ...");
		progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressBar.setProgress(0);
		progressBar.setMax(100);
		progressBar.show();
		// reset progress bar status
		progressBarStatus = 0;

		if ((Activity) context instanceof TabGroupMore) {

			// app = new Appstartapplication();
			// app.startProgressDialog(context, "", "Loading...");

		}

	}

	@Override
	protected Object doInBackground(String... params) {

		Data_sync_module(Constant.CUSTOMER_ID);
		return this;
	}

	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);

		if ((Activity) context instanceof Splash) {
			((Splash) context).Intent_main();
			progressBar.dismiss();
		} else if ((Activity) context instanceof TabGroupMore) {
			// app.stopProgressDialog();
			progressBar.dismiss();
			// ---set default value---

			DBAdapter dba = new DBAdapter(context);
			dba.open();

			Constant.LANGUAGE_ID = dba.getDefaultLanguage();

			// ---set font part---
			String[] font_data = dba.getConfigration();
			Constant.FONT_TYPE = font_data[0];
			Constant.FONT_COLOR = font_data[1];
			Constant.FONT_SIZE = font_data[2];
			Constant.SPACING = font_data[3];
			Constant.THEME_COLOR = font_data[4];

			dba.close();

			// Util.recycle_bitmap();
			Constant.GET_PARENT = null;
			Intent i = new Intent(context, TabSample.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(i);

		}

	}

	// ---updating methods

	public void Data_sync_module(String customerid) {

		String response = Webservice.GlobalSynch(customerid, CalResolutionID());

		progressBarStatus = 8;
		// Update the progress bar
		progressBarHandler.post(new Runnable() {
			public void run() {
				progressBar.setProgress(progressBarStatus);
			}
		});

		DBAdapter dba = new DBAdapter(context);
		dba.open();

		try {

			JSONObject jo = new JSONObject(response);

			if (jo.getString("status").equals("success")) {

				JSONObject jo1 = (JSONObject) jo.get("data");

				JSONArray ja = (JSONArray) jo1.get("customer_module");

				List<String> list = new ArrayList<String>();

				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo2 = (JSONObject) ja.get(i);
					JSONObject jobj = (JSONObject) jo2
							.get("tbl_customer_module");

					String is_publish = jobj.getString("is_publish");

					System.out.println("customer module publish status::::"
							+ is_publish);

					list.add(jobj.getString("customer_module_id"));

					progressBarStatus = progressBarStatus + 8;
					// Update the progress bar
					progressBarHandler.post(new Runnable() {
						public void run() {
							progressBar.setProgress(progressBarStatus);
						}
					});

					if (is_publish.equalsIgnoreCase("YES")) {

						//dba.open();
						Cursor c = dba.getCustomerModuleDate(jobj
								.getString("customer_module_id"));

						if (c.getCount() > 0) {

							Util.DeleteFile(c.getString(1));

							String img_name0 = "icon" + i
									+ Util.getRandomFileName();
							String img_url0 = jobj.getString("icon");

							Util.Store_file_phone(img_url0, img_name0);

						
							dba.updateModuleIcon(
									jobj.getString("customer_module_id"),
									img_name0);

							System.out.println("Module id:::"
									+ jobj.getString("module_id"));

							if (Util.compareDateTime(c.getString(2),
									jobj.getString("sync_date_time"))) {

								System.out
										.println("module should be update:::::");

								Util.DeleteFile(c.getString(1));

								String img_name = "icon"
										+ Util.getRandomFileName();
								String img_url = jobj.getString("icon");

								Log.e("Icon Image Url:::",
										"http://203.88.158.98/appstart_live/public/"
												+ img_url);

								System.out
										.println("icon updated for module id::"
												+ jobj.getString("customer_module_id"));

								Util.Store_file_phone(img_url, img_name);

								dba.updateCustomerModule(
										jobj.getString("customer_module_id"),
										jobj.getString("module_id"),
										jobj.getString("customer_id"),
										jobj.getString("order_number"),
										jobj.getString("visibility"),
										jobj.getString("status"),
										jobj.getString("share"), img_name,
										jobj.getString("sync_date_time"));

								JSONArray jarr = (JSONArray) jo2
										.get("tbl_customer_module_detail");

								for (int j = 0; j < jarr.length(); j++) {

									JSONObject jobj1 = (JSONObject) jarr.get(j);

									Cursor cur = dba
											.row_query("select BackgroundImage from tbl_Customer_Module_Details where RecordID='"
													+ jobj1.getString("customer_module_detail_id")
													+ "'");

									if (cur.getCount() > 0) {

										System.out
												.println("image name to be deleted::"
														+ cur.getString(0));
										Util.DeleteFile(cur.getString(0));

									}

									String img_name1 = "Back"
											+ Util.getRandomFileName();
									String img_url1 = jobj1
											.getString("background_image");

									Log.e("Background Image Url:::",
											"http://203.88.158.98/appstart_live/public/"
													+ img_url1);

									if (img_url1.contains(".jpg")
											|| img_url1.contains(".png")) {
										Util.Store_file_phone(img_url1,
												img_name1);

									}

									System.out
											.println(jobj1
													.getString("customer_module_detail_id"));
									System.out.println(jobj1
											.getString("customer_module_id"));
									System.out.println(jobj1
											.getString("language_id"));
									System.out.println(jobj1
											.getString("screen_name"));
									System.out.println(jobj1
											.getString("background_image"));
									
									dba.updateCustomerModuleDetails(
											jobj1.getString("customer_module_detail_id"),
											jobj1.getString("customer_module_id"),
											jobj.getString("customer_id"),
											jobj1.getString("language_id"),
											jobj1.getString("screen_name"),
											img_name1,
											jobj1
											.getString("background_color"),
											jobj1
											.getString("background_type"));

								
								cur.close();
								}

								// --check for which module to update ---
								if (jobj.getString("module_id")
										.equalsIgnoreCase("2")) {

									HomeWallpaperData obj_homewallpaper = new HomeWallpaperData(
											context, Constant.CUSTOMER_ID);
									obj_homewallpaper
											.Homewallpaper_sync_module();

								} else if (jobj.getString("module_id")
										.equalsIgnoreCase("1")) {

									ContactData obj_contact = new ContactData(
											context, Constant.CUSTOMER_ID);
									obj_contact.contact_sync_module();

								} else if (jobj.getString("module_id")
										.equalsIgnoreCase("3")) {

									PushMessageData obj_pushmessage = new PushMessageData(
											context, Constant.CUSTOMER_ID);
									obj_pushmessage.PushMessage_sync_module();

								} else if (jobj.getString("module_id")
										.equalsIgnoreCase("4")) {

									CmsData obj_cms = new CmsData(context,
											Constant.CUSTOMER_ID);
									obj_cms.Cms_sync_module();

								} else if (jobj.getString("module_id")
										.equalsIgnoreCase("5")) {

									EventsData obj_events = new EventsData(
											context, Constant.CUSTOMER_ID);
									obj_events.Events_sync_module();

								} else if (jobj.getString("module_id")
										.equalsIgnoreCase("6")) {

									DocumentData obj_document = new DocumentData(
											context, Constant.CUSTOMER_ID);
									obj_document.Document_sync_module();

								} else if (jobj.getString("module_id")
										.equalsIgnoreCase("7")) {

									ImageGalleryData obj_ig = new ImageGalleryData(
											context, Constant.CUSTOMER_ID);
									obj_ig.ImageGallery_sync_module();

								} else if (jobj.getString("module_id")
										.equalsIgnoreCase("8")) {

									//
									WebsiteData obj_website = new WebsiteData(
											context, Constant.CUSTOMER_ID);
									obj_website.Website_sync_module();

								} else if (jobj.getString("module_id")
										.equalsIgnoreCase("9")) {

									//
									SocialMediaData obj_socialmedia = new SocialMediaData(
											context, Constant.CUSTOMER_ID);
									obj_socialmedia.SocialMedia_sync_module();

								} else if (jobj.getString("module_id")
										.equalsIgnoreCase("10")) {

									CmsData1 obj_cms1 = new CmsData1(context,
											Constant.CUSTOMER_ID);
									obj_cms1.Cms_sync_module1();

								} else if (jobj.getString("module_id")
										.equalsIgnoreCase("11")) {

									CmsData2 obj_cms2 = new CmsData2(context,
											Constant.CUSTOMER_ID);
									obj_cms2.Cms_sync_module2();

								} else if (jobj.getString("module_id")
										.equalsIgnoreCase("12")) {
									
									ImageGalleryData1 obj_ig1 = new ImageGalleryData1(
											context, Constant.CUSTOMER_ID);
									obj_ig1.ImageGallery_sync_module();
									
								} else if (jobj.getString("module_id")
										.equalsIgnoreCase("13")) {

									MusicData obj_music = new MusicData(
											context, Constant.CUSTOMER_ID);
									obj_music.MusicDataSync();

								} else if (jobj.getString("module_id")
										.equalsIgnoreCase("14")) {

									WebsiteData1 obj_web1 = new WebsiteData1(
											context, Constant.CUSTOMER_ID);
									obj_web1.Website_sync_module();

								}
								System.out
										.println("update in customer module table:::");
							} else {
								System.out
										.println("No updateing in customer module table:::");

							}
						} else {

							// --- need to add record in database

							String img_name = "icon" + Util.getRandomFileName();
							String img_url = jobj.getString("icon");

							Log.e("Image Url:::",
									"http://203.88.158.98/appstart_live/public/"
											+ img_url);

							Util.Store_file_phone(img_url, img_name);

							dba.insertCustomerModule(
									jobj.getString("customer_module_id"),
									jobj.getString("module_id"),
									jobj.getString("customer_id"),
									jobj.getString("order_number"),
									jobj.getString("visibility"),
									jobj.getString("status"),
									jobj.getString("share"), img_name,
									jobj.getString("sync_date_time"));

							JSONArray jarr = (JSONArray) jo2
									.get("tbl_customer_module_detail");

							for (int j = 0; j < jarr.length(); j++) {
								JSONObject jobj1 = (JSONObject) jarr.get(j);

								img_name = "Back" + Util.getRandomFileName();

								img_url = jobj1.getString("background_image");

								Log.e("Image Url:::",
										"http://203.88.158.98/appstart_live/public/"
												+ img_url);

								Util.Store_file_phone(img_url, img_name);

								dba.insertCustomerModuleDetails(
										jobj1.getString("customer_module_detail_id"),
										jobj1.getString("customer_module_id"),
										jobj.getString("customer_id"), jobj1
												.getString("language_id"),
										jobj1.getString("screen_name"),
										img_name,
										jobj1.getString("background_color"),
										jobj1.getString("background_type"));

							}

						}
					
					c.close();
					}
					
					
					//--Update only Module order Number 
					dba.updateCustomerModuleOrderNumber(
							jobj.getString("customer_module_id"),
							jobj.getString("module_id"),
							jobj.getString("customer_id"),
							jobj.getString("order_number"),
							jobj.getString("status")); 
					
					//----Update only Module Details
					
					/*JSONArray jarr = (JSONArray) jo2
							.get("tbl_customer_module_detail");

					for (int j = 0; j < jarr.length(); j++) {

						JSONObject jobj1 = (JSONObject) jarr.get(j);

						Cursor cur = dba
								.row_query("select BackgroundImage from tbl_Customer_Module_Details where RecordID='"
										+ jobj1.getString("customer_module_detail_id")
										+ "'");

						if (cur.getCount() > 0) {

							System.out
									.println("image name to be deleted::"
											+ cur.getString(0));
							Util.DeleteFile(cur.getString(0));

						}

						String img_name1 = "Back"
								+ Util.getRandomFileName();
						String img_url1 = jobj1
								.getString("background_image");

						Log.e("Background Image Url:::",
								"http://203.88.158.98/appstart_live/public/"
										+ img_url1);

						if (img_url1.contains(".jpg")
								|| img_url1.contains(".png")) {
							Util.Store_file_phone(img_url1,
									img_name1);

						}
						
						dba.updateCustomerModuleDetails(
								jobj1.getString("customer_module_detail_id"),
								jobj1.getString("customer_module_id"),
								jobj.getString("customer_id"),
								jobj1.getString("language_id"),
								jobj1.getString("screen_name"),
								img_name1,
								jobj1.getString("background_color"),
								jobj1.getString("background_type"));

					cur.close();
					}*/
					  

				}

				String[] arr_customermodule_moduleid = list
						.toArray(new String[list.size()]);
				DeleteRecord("tbl_Customer_Module", "RecordID",
						arr_customermodule_moduleid);

				JSONArray ja2 = (JSONArray) jo1.get("tbl_customer_language");

				for (int i = 0; i < ja2.length(); i++) {

					JSONObject jobj = (JSONObject) ja2.get(i);

					//dba.open();

					if (dba.updateCustomerLanguage(
							jobj.getString("customer_id"),
							jobj.getString("language_id"),
							jobj.getString("is_default"))) {

					} else {
						dba.insertCustomerLanguage(
								jobj.getString("customer_id"),
								jobj.getString("language_id"),
								jobj.getString("is_default"));
					}

				}

				JSONArray ja3 = (JSONArray) jo1
						.get("tbl_customer_configuration");

				for (int i = 0; i < ja3.length(); i++) {

					JSONObject jobj = (JSONObject) ja3.get(i);

					if (dba.updateConfiguration(
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
							jobj.getString("created_at"))) {

					} else {

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
				}
			}

			//dba.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			dba.close();
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

		DBAdapter db = new DBAdapter(context);
		db.open();

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

			db.row_query("delete from " + table + " where " + id + " not in("
					+ nameBuilder.toString() + ")");

		} else {

		}

		db.close();

	}

	public void DeleteDetailTableRecords(String table, String id, String mid,
			String mid_Value, final String name[]) {

		DBAdapter db = new DBAdapter(context);
		db.open();

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

			db.row_query("delete from " + table + " where " + id + " not in("
					+ nameBuilder.toString() + ") and " + mid + "=" + mid_Value);

		} else {

		}

		db.close();

	}

	// String info;
	// private Handler mHandler = new Handler() {
	//
	// @Override
	// public void handleMessage(Message message) {
	//
	// Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
	//
	// }
	//
	// };

}