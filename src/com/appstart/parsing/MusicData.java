package com.appstart.parsing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.appstart.database.DBAdapter;
import com.appstart.utility.Util;
import com.appstart.webservice.Webservice;

public class MusicData {

	Context context;
	String customerid;
	DBAdapter dba;

	public MusicData(Context ctx, String cid) {

		this.context = ctx;
		this.customerid = cid;
		this.dba = new DBAdapter(ctx);

	}

	// ---add music data---
	public void add_Music() {

		String response = Webservice.Music(customerid, "android");

		try {

			dba = new DBAdapter(context);
			dba.open();

			JSONObject jo = new JSONObject(response);
			if (jo.getString("status").equals("success")) {

				JSONObject jo1 = (JSONObject) jo.get("data");
				JSONArray ja = (JSONArray) jo1.get("data");

				for (int i = 0; i < ja.length(); i++) {

					JSONObject jo2 = (JSONObject) ja.get(i);
					JSONObject jo3 = (JSONObject) jo2.get("tbl_module_music");

					dba.insertMusic(jo3.getString("module_music_id"),
							jo3.getString("customer_id"),
							jo3.getString("status"), jo3.getString("order"),
							jo3.getString("last_updated_by"),
							jo3.getString("last_updated_at"),
							jo3.getString("created_by"),
							jo3.getString("created_at"));

					JSONArray jarr = (JSONArray) jo2
							.get("tbl_module_music_detail");

					for (int j = 0; j < jarr.length(); j++) {

						JSONObject jo4 = (JSONObject) jarr.get(j);

						String img_url = jo4.getString("album_art_url");
						String img_name = "Music" + Util.getRandomFileName();

						if (img_url.endsWith(".jpg")
								|| img_url.endsWith(".png")) {

							System.out
									.print("If end with .jpg : Album art url1: "
											+ img_url.toString());

							if (img_url.contains("http://")) {
								Store_file_phone(
										"http://"
												+ URLEncoder
														.encode(img_url
																.split("http://")[1],
																"UTF-8")
														.replace("%2F", "/"),
										img_name);
							} else if (img_url.contains("https://")) {
								Store_file_phone(
										"https://"
												+ URLEncoder
														.encode(img_url
																.split("https://")[1],
																"UTF-8")
														.replace("%2F", "/"),
										img_name);
							}
						}

						else {
							if (img_url != null) {
								if (img_url.length() > 0)

									System.out
											.print("If not end with .jpg :  Album art url2: "
													+ img_url.toString());

								if (img_url.contains("http://")) {
									Store_file_phone(
											"http://"
													+ URLEncoder
															.encode(img_url
																	.split("http://")[1],
																	"UTF-8")
															.replace("%2F", "/"),
											img_name);
								} else if (img_url.contains("https://")) {
									Store_file_phone(
											"https://"
													+ URLEncoder
															.encode(img_url
																	.split("https://")[1],
																	"UTF-8")
															.replace("%2F", "/"),
											img_name);
								}

							}
						}

						Log.e("Image Url:::", img_url);

						dba.insertMusicDetails(
								jo4.getString("module_music_detail_id"),
								jo4.getString("module_music_id"),
								jo4.getString("language_id"),
								jo4.getString("title"),
								jo4.getString("artist"),
								jo4.getString("album"),
								jo4.getString("track_url"),
								jo4.getString("preview_url"), img_name,
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

	// ---sync part---
	public void MusicDataSync() {
		String response = Webservice.Music(customerid, "android");
		System.out.print("hello");
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
					JSONObject jo3 = (JSONObject) jo2.get("tbl_module_music");

					Cursor c = dba.getMusicDate(jo3
							.getString("module_music_id"));

					list.add(jo3.getString("module_music_id"));

					if (c.getCount() > 0) {

						if (Util.compareDateTime(c.getString(0),
								jo3.getString("last_updated_at"))) {

							dba.updateMusic(jo3.getString("module_music_id"),
									jo3.getString("customer_id"),
									jo3.getString("status"),
									jo3.getString("order"),
									jo3.getString("last_updated_by"),
									jo3.getString("last_updated_at"),
									jo3.getString("created_by"),
									jo3.getString("created_at"));

							JSONArray jarr = (JSONArray) jo2
									.get("tbl_module_music_detail");

							List<String> list1 = new ArrayList<String>();

							for (int j = 0; j < jarr.length(); j++) {

								JSONObject jo4 = (JSONObject) jarr.get(j);

								Cursor cur_hd = dba.getMusicDetailsDate(jo4
										.getString("module_music_detail_id"));

								list1.add(jo4
										.getString("module_music_detail_id"));

								if (cur_hd.getCount() > 0) {
									if (Util.compareDateTime(
											cur_hd.getString(0),
											jo4.getString("last_updated_at"))) {

										String img_url = jo4
												.getString("album_art_url");
										String img_name = "Music"
												+ Util.getRandomFileName();

										System.out
												.print("Album art url1: "
														+ jo4.getString("album_art_url"));

										if (img_url.endsWith(".jpg")
												|| img_url.endsWith(".png")) {
											System.out
													.print("If end with .jpg : Album art url1: "
															+ img_url
																	.toString());
											System.out.println("image url::::"
													+ img_url);

											if (img_url.contains("http://")) {
												Store_file_phone(
														"http://"
																+ URLEncoder
																		.encode(img_url
																				.split("http://")[1],
																				"UTF-8")
																		.replace(
																				"%2F",
																				"/"),
														img_name);
											} else if (img_url
													.contains("https://")) {
												Store_file_phone(
														"https://"
																+ URLEncoder
																		.encode(img_url
																				.split("https://")[1],
																				"UTF-8")
																		.replace(
																				"%2F",
																				"/"),
														img_name);
											}
										} else {
											// if (img_url != null) {

											// System.out.print(img_url);

											if (img_url.length() > 0)

												// System.out.print(img_url);

												// img_url =
												// img_url.split("?")[0];

												// System.out.print("Print Value::  "+img_url
												// .split("?")[0]);

												System.out
														.print("If not end with .jpg :  Album art url2: "
																+ img_url
																		.toString());

											// if(img_url.contains("https://")){

											Store_file_phone(img_url, img_name);

											/*
											 * Store_file_phone( "https://" +
											 * URLEncoder .encode(img_url
											 * .split("https://")[1], "UTF-8")
											 * .replace( "%2F", "/"), img_name);
											 * 
											 * }else{ Store_file_phone( img_url,
											 * img_name);
											 * 
											 * }
											 */

											// break;
											// }
										}

										Log.e("Image Url:::", img_url);

										dba.updateMusicDetails(
												jo4.getString("module_music_detail_id"),
												jo4.getString("module_music_id"),
												jo4.getString("language_id"),
												jo4.getString("title"),
												jo4.getString("artist"),
												jo4.getString("album"),
												jo4.getString("track_url"),
												jo4.getString("preview_url"),
												img_name,
												jo4.getString("last_updated_by"),
												jo4.getString("last_updated_at"),
												jo4.getString("created_by"),
												jo4.getString("created_at"));

									}
								}

							}

							String[] arr1 = list1.toArray(new String[list1
									.size()]);
							DeleteDetailTableRecords("tbl_Music_Details",
									"RecordID", "MusicID",
									jo3.getString("module_music_id"), arr1);

							System.out.println("Updation in Music module::");

						}
					} else {

						// ---record not available so need to add in local

						dba.insertMusic(jo3.getString("module_music_id"),
								jo3.getString("customer_id"),
								jo3.getString("status"),
								jo3.getString("order"),
								jo3.getString("last_updated_by"),
								jo3.getString("last_updated_at"),
								jo3.getString("created_by"),
								jo3.getString("created_at"));

						JSONArray jarr = (JSONArray) jo2
								.get("tbl_module_music_detail");

						for (int j = 0; j < jarr.length(); j++) {

							JSONObject jo4 = (JSONObject) jarr.get(j);

							dba.insertMusicDetails(
									jo4.getString("module_music_detail_id"),
									jo4.getString("module_music_id"),
									jo4.getString("language_id"),
									jo4.getString("title"),
									jo4.getString("artist"),
									jo4.getString("album"),
									jo4.getString("track_url"),
									jo4.getString("preview_url"),
									jo4.getString("album_art_url"),
									jo4.getString("last_updated_by"),
									jo4.getString("last_updated_at"),
									jo4.getString("created_by"),
									jo4.getString("created_at"));

						}

					}
				}

				// ---delete records which are not in server now
				String[] arr = list.toArray(new String[list.size()]);
				DeleteRecord("tbl_Music", "MusicID", arr);

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

	// ---

	public static void Store_file_phone(String urlstring, String name) {

		URL url;
		try {
			System.out.print("url in store funs: " + urlstring);

			url = new URL(urlstring);

			HttpURLConnection c = (HttpURLConnection) url.openConnection();
			c.setRequestMethod("GET");
			c.setDoOutput(true);
			c.connect();

			String PATH = "/data/data/com.appstart/app_my_sub_dir/";
			Log.v("PATH", "PATH: " + PATH);
			File file = new File(PATH);
			file.mkdirs();
			String fileName;
			fileName = name + ".jpg";
			File outputFile = new File(file, fileName);
			FileOutputStream fos = new FileOutputStream(outputFile);

			InputStream is = c.getInputStream();

			byte[] buffer = new byte[1024];
			int len1 = 0;
			while ((len1 = is.read(buffer)) != -1) {
				fos.write(buffer, 0, len1);
			}
			fos.close();
			is.close();

			System.out.println("download sucess of image:::" + name);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
