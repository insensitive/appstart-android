package com.appstart;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore.Audio.Media;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appstart.adapter.ListAdapterMore;
import com.appstart.async.AsyncCallTask;
import com.appstart.database.DBAdapter;
import com.appstart.tabgroup.TabGroupActivity;
import com.appstart.utility.AlertMessages;
import com.appstart.utility.Constant;
import com.appstart.utility.Util;

public class More extends Activity {

	ListView list;
	ListAdapterMore adapter;
	AlertMessages message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more);

		message = new AlertMessages(getParent());
		list = (ListView) findViewById(R.id.list_more);

		Constant.GET_PARENT = getParent();

		setHeaderBackground();
		((Button) findViewById(R.id.ib_back_music))
				.setVisibility(View.INVISIBLE);
		((TextView) findViewById(R.id.txt_music)).setText(R.string.more);

		ArrayList<Object> a = new ArrayList<Object>();

		final ArrayList<Object> moduleid = new ArrayList<Object>();
		final ArrayList<Object> icon_name = new ArrayList<Object>();

		try{
		
		DBAdapter dba = new DBAdapter(this);
		dba.open();

		Cursor c = dba.getModules(Constant.LANGUAGE_ID);

		for (int i = 0; i < c.getCount(); i++) {
			if (i > 2) {

				a.add(c.getString(2));
				moduleid.add(c.getString(0));
				icon_name.add(c.getString(1));

				System.out.println(c.getString(2) + "------" + c.getString(0));

			}
			c.moveToNext();
		}

		c.close();
		dba.close();
		}catch(Exception e){
			e.printStackTrace();
		}

		a.add("Settings");
		moduleid.add("0");
		icon_name.add("");

		adapter = new ListAdapterMore(this, a, icon_name);
		list.setAdapter(adapter);

		list.setDivider(new ColorDrawable(Util.Colorcode()));
		list.setDividerHeight(1);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				if (moduleid.get(arg2).toString().equalsIgnoreCase("1")) {

					try {

						DBAdapter dba = new DBAdapter(getParent());
						dba.open();
						Cursor c = dba.getContacts(Constant.LANGUAGE_ID);
						if (c.getCount() == 1) {

							Intent edit = new Intent(getParent(),
									ContactDetails.class);
							Bundle b = new Bundle();
							b.putString("contact_id", c.getString(1));
							b.putBoolean("showBack", true);
							edit.putExtras(b);
							TabGroupActivity parentActivity = (TabGroupActivity) getParent();
							parentActivity.startChildActivity("ContactDetails",
									edit);

						} else {

							Intent edit = new Intent(getParent(), Contact.class);

							Bundle b = new Bundle();

							b.putBoolean("showBack", true);
							edit.putExtras(b);
							TabGroupActivity parentActivity = (TabGroupActivity) getParent();
							parentActivity.startChildActivity("Contact", edit);
						}
						c.close();
						dba.close();
					} catch (Exception e) {
						e.printStackTrace();
						//Toast.makeText(getParent(), "Error", Toast.LENGTH_SHORT)
							//	.show();
					}

				} else if (moduleid.get(arg2).toString().equalsIgnoreCase("2")) {
					try {
						Intent edit = new Intent(getParent(),
								HomeWallpaper.class);
						TabGroupActivity parentActivity = (TabGroupActivity) getParent();
						parentActivity.startChildActivity("MainActivity", edit);
					} catch (Exception e) {
						e.printStackTrace();
						//Toast.makeText(getParent(), "Error", Toast.LENGTH_SHORT)
							//	.show();
					}

				} else if (moduleid.get(arg2).toString().equalsIgnoreCase("3")) {
					try {
						DBAdapter dba = new DBAdapter(getParent());
						dba.open();
						Cursor c = dba.getPushMessages(Constant.LANGUAGE_ID);
						if (c.getCount() == 1) {

							Bundle b = new Bundle();
							b.putString("pushmessage_id", c.getString(1));
							b.putBoolean("showBack", true);

							Intent edit = new Intent(getParent(),
									PushMessageDetails.class);
							edit.putExtras(b);
							TabGroupActivity parentActivity = (TabGroupActivity) getParent();
							parentActivity.startChildActivity(
									"PushMessageDetails", edit);

						} else {
							Bundle b = new Bundle();

							b.putBoolean("showBack", true);
							Intent edit = new Intent(getParent(),
									PushMessage.class);
							edit.putExtras(b);
							TabGroupActivity parentActivity = (TabGroupActivity) getParent();
							parentActivity.startChildActivity("PushMessage",
									edit);

						}
						c.close();
						dba.close();
					} catch (Exception e) {
						e.printStackTrace();
						//Toast.makeText(getParent(), "Error", Toast.LENGTH_SHORT)
							//	.show();
					}

				} else if (moduleid.get(arg2).toString().equalsIgnoreCase("4")) {
					try {
						DBAdapter dba = new DBAdapter(getParent());
						dba.open();

						Cursor c = dba.getCms(Constant.LANGUAGE_ID, "0");

						if (c.getCount() == 1) {

							Cursor cur_child = dba.getCms(Constant.LANGUAGE_ID,
									c.getString(0));

							if (cur_child.getCount() > 0) {
								Bundle b = new Bundle();
								b.putBoolean("showBack", true);
								b.putString("cms_title", c.getString(1));
								Intent edit = new Intent(getParent(), Cms.class);
								edit.putExtras(b);
								TabGroupActivity parentActivity = (TabGroupActivity) getParent();
								parentActivity.startChildActivity("Cms", edit);
							} else {
								Bundle b = new Bundle();
								b.putString("CmsID", c.getString(0));
								b.putBoolean("showBack", true);

								Intent edit = new Intent(getParent(),
										CmsChild.class);

								edit.putExtras(b);
								TabGroupActivity parentActivity = (TabGroupActivity) getParent();
								parentActivity.startChildActivity("CmsChild",
										edit);
							}
						} else {

							Bundle b = new Bundle();
							b.putBoolean("showBack", true);
							Intent edit = new Intent(getParent(), Cms.class);
							edit.putExtras(b);

							TabGroupActivity parentActivity = (TabGroupActivity) getParent();
							parentActivity.startChildActivity("Cms", edit);

						}
						c.close();
						dba.close();
					} catch (Exception e) {
						e.printStackTrace();
						//Toast.makeText(getParent(), "Error", Toast.LENGTH_SHORT)
								//.show();
					}

				} else if (moduleid.get(arg2).toString().equalsIgnoreCase("5")) {

					try {
						DBAdapter dba = new DBAdapter(getParent());
						dba.open();
						Cursor c = dba.getEvents(Constant.LANGUAGE_ID);
						ArrayList<String> EventID = new ArrayList<String>();

						System.out.println("event count:::::::" + c.getCount());

						for (int i = 0; i < c.getCount(); i++) {

							System.out.println("Event titles" + c.getString(3));

							String row[] = c.getString(1).split(" ");
							String row_end[] = c.getString(2).split(" ");
							if (IsEventWithinMonth(row[0], row[1], row_end[0],
									row_end[1])) {

								EventID.add(c.getString(0));

							}

							c.moveToNext();
						}
						if (EventID.size() > 1) {
							Bundle b = new Bundle();
							b.putBoolean("showBack", true);
							Intent edit = new Intent(getParent(), Events.class);
							edit.putExtras(b);
							TabGroupActivity parentActivity = (TabGroupActivity) getParent();
							parentActivity.startChildActivity("Events", edit);

						} else if (EventID.size() == 1) {

							Bundle b = new Bundle();
							b.putString("EventID", EventID.get(0).toString());
							b.putBoolean("showBack", true);
							Intent edit = new Intent(getParent(),
									EventsDetails.class);

							edit.putExtras(b);
							TabGroupActivity parentActivity = (TabGroupActivity) getParent();
							parentActivity.startChildActivity("EventsDetails",
									edit);

						} else {
							message.showCutomMessage("No Events Available");
						}

						c.close();
						dba.close();
					} catch (Exception e) {
						//Toast.makeText(getApplicationContext(),
								//String.valueOf(e), Toast.LENGTH_SHORT).show();
						e.printStackTrace();
					}
				} else if (moduleid.get(arg2).toString().equalsIgnoreCase("6")) {

					/*
					 * Intent edit = new Intent(getParent(),
					 * DocumentCategory.class); TabGroupActivity parentActivity
					 * = (TabGroupActivity) getParent();
					 * parentActivity.startChildActivity("DocumentCategory",
					 * edit);
					 */
					try {

						DBAdapter dba = new DBAdapter(getParent());
						dba.open();

						Cursor c = dba
								.getDocumentCategoryCursor(Constant.LANGUAGE_ID);

						System.out.println("In More Document Category count:::::::"
								+ c.getCount());

						Cursor c2 = dba.getDocument(c.getString(0),
								Constant.LANGUAGE_ID);

						if (c.getCount() == 1) {

							
							Bundle b = new Bundle();
							b.putString("category_id",c.getString(0));
							b.putBoolean("showBack",false);
							
							Intent edit = new Intent(getParent(), Document.class);

							edit.putExtras(b);
							TabGroupActivity parentActivity = (TabGroupActivity) getParent();
							parentActivity.startChildActivity("Document", edit);
							
							
							/*Bundle b = new Bundle();
							b.putString("DocumentID", c2.getString(0));
							b.putBoolean("showBack", true);

							Intent edit = new Intent(getParent(),
									DocumentDetails.class);
							edit.putExtras(b);
							TabGroupActivity parentActivity = (TabGroupActivity) getParent();
							parentActivity.startChildActivity(
									"DocumentDetails", edit);*/
						} else {

							Bundle b = new Bundle();
							b.putBoolean("showBack", true);

							Intent edit = new Intent(getParent(),
									DocumentCategory.class);
							edit.putExtras(b);
							TabGroupActivity parentActivity = (TabGroupActivity) getParent();
							parentActivity.startChildActivity(
									"DocumentCategory", edit);
						}

						c.close();
						c2.close();
						dba.close();
					} catch (Exception e) {
						e.printStackTrace();
						//Toast.makeText(getParent(), "Error", Toast.LENGTH_SHORT)
								//.show();
					}

				} else if (moduleid.get(arg2).toString().equalsIgnoreCase("7")) {

					/*
					 * Intent edit = new Intent(getParent(),
					 * ImageGalleryCategory.class);
					 * 
					 * Bundle b = new Bundle(); b.putBoolean("showBack", true);
					 * edit.putExtras(b); TabGroupActivity parentActivity =
					 * (TabGroupActivity) getParent();
					 * parentActivity.startChildActivity("ImageGalleryCategory",
					 * edit);
					 */
					try {
						DBAdapter dba = new DBAdapter(getParent());
						dba.open();

						Cursor c = dba
								.getImageGalleryCategory(Constant.LANGUAGE_ID);

						System.out.println("GalleryCategory count:::::::"
								+ c.getCount());

						if (c.getCount() == 1) {
							
							Bundle b = new Bundle();
							b.putString("cat_id", c.getString(0));
							b.putString("cat_name", c.getString(1));
							b.putBoolean("showBack", true);
							Intent edit = new Intent(getParent(),
									ImageGallery.class);
							edit.putExtras(b);
							TabGroupActivity parentActivity = (TabGroupActivity) getParent();
							parentActivity.startChildActivity("ImageGallery",
									edit);
						} else {
							Bundle b = new Bundle();
							b.putBoolean("showBack", true);
							Intent edit = new Intent(getParent(),
									ImageGalleryCategory.class);
							edit.putExtras(b);
							TabGroupActivity parentActivity = (TabGroupActivity) getParent();
							parentActivity.startChildActivity(
									"ImageGalleryCategory", edit);
						}

						c.close();
						dba.close();
					} catch (Exception e) {
						e.printStackTrace();
						//Toast.makeText(getParent(), "Error", Toast.LENGTH_SHORT)
								//.show();
					}

				} else if (moduleid.get(arg2).toString().equalsIgnoreCase("8")) {

					/*
					 * Intent edit = new Intent(getParent(), Website.class);
					 * TabGroupActivity parentActivity = (TabGroupActivity)
					 * getParent(); parentActivity.startChildActivity("Website",
					 * edit);
					 */
					try {

						DBAdapter dba = new DBAdapter(getParent());
						dba.open();

						Cursor cur = dba.getWebsite();

						Cursor c = dba.getWebsiteDetails(cur.getString(0),
								Constant.LANGUAGE_ID);

						if (cur.getCount() == 1) {

							Intent edit = new Intent(getParent(), Webview.class);

							Bundle b = new Bundle();

							b.putString("title", c.getString(0));
							b.putString("website_url", c.getString(1));
							b.putBoolean("showBack", true);

							edit.putExtras(b);
							TabGroupActivity parentActivity = (TabGroupActivity) getParent();
							parentActivity.startChildActivity("Webview", edit);
						} else {
							Bundle b = new Bundle();
							b.putBoolean("showBack", true);
							Intent edit = new Intent(getParent(), Website.class);
							edit.putExtras(b);
							TabGroupActivity parentActivity = (TabGroupActivity) getParent();
							parentActivity.startChildActivity("Website", edit);
						}
						cur.close();
						c.close();
						dba.close();
					} catch (Exception e) {
						e.printStackTrace();
						//Toast.makeText(getParent(), "Error", Toast.LENGTH_SHORT)
								//.show();
					}

				} else if (moduleid.get(arg2).toString().equalsIgnoreCase("9")) {

					/*
					 * Intent edit = new Intent(getParent(), SocialMedia.class);
					 * TabGroupActivity parentActivity = (TabGroupActivity)
					 * getParent();
					 * parentActivity.startChildActivity("SocialMedia", edit);
					 */
					try {

						DBAdapter dba = new DBAdapter(getParent());
						dba.open();

						Cursor cur = dba.getSocialMedia(Constant.LANGUAGE_ID);

						if (cur.getCount() == 1) {

							Intent edit = new Intent(getParent(), Webview.class);

							Bundle b = new Bundle();

							b.putString("title", cur.getString(1));
							b.putString("website_url", cur.getString(0));
							b.putBoolean("showBack", true);

							edit.putExtras(b);
							TabGroupActivity parentActivity = (TabGroupActivity) getParent();
							parentActivity.startChildActivity("Webview", edit);
						} else {
							Bundle b = new Bundle();

							b.putBoolean("showBack", true);
							Intent edit = new Intent(getParent(),
									SocialMedia.class);
							edit.putExtras(b);
							TabGroupActivity parentActivity = (TabGroupActivity) getParent();
							parentActivity.startChildActivity("SocialMedia",
									edit);
						}
						cur.close();

						dba.close();
					} catch (Exception e) {
						e.printStackTrace();
						//Toast.makeText(getParent(), "Error", Toast.LENGTH_SHORT)
								//.show();
					}

				} else if (moduleid.get(arg2).toString().equalsIgnoreCase("10")) {

					try {
						DBAdapter dba = new DBAdapter(getParent());
						dba.open();

						Cursor c = dba.getCms1(Constant.LANGUAGE_ID, "0");

						if (c.getCount() == 1) {

							Cursor cur_child = dba.getCms1(
									Constant.LANGUAGE_ID, c.getString(0));

							if (cur_child.getCount() > 0) {
								Bundle b = new Bundle();
								b.putBoolean("showBack", true);

								Intent edit = new Intent(getParent(),
										Cms1.class);
								edit.putExtras(b);

								TabGroupActivity parentActivity = (TabGroupActivity) getParent();
								parentActivity.startChildActivity("Cms1", edit);
							} else {
								Bundle b = new Bundle();

								b.putBoolean("showBack", true);
								b.putString("CmsID", c.getString(0));

								Intent edit = new Intent(getParent(),
										CmsChild1.class);

								edit.putExtras(b);
								TabGroupActivity parentActivity = (TabGroupActivity) getParent();
								parentActivity.startChildActivity("CmsChild1",
										edit);
							}
						} else {
							Bundle b = new Bundle();
							b.putBoolean("showBack", true);
							Intent edit = new Intent(getParent(), Cms1.class);
							edit.putExtras(b);
							TabGroupActivity parentActivity = (TabGroupActivity) getParent();
							parentActivity.startChildActivity("Cms1", edit);

						}
						c.close();
						dba.close();
					} catch (Exception e) {
						e.printStackTrace();
						//Toast.makeText(getParent(), "Error", Toast.LENGTH_SHORT)
								//.show();
					}
				} else if (moduleid.get(arg2).toString().equalsIgnoreCase("11")) {

					try {
						DBAdapter dba = new DBAdapter(getParent());
						dba.open();

						Cursor c = dba.getCms2(Constant.LANGUAGE_ID, "0");

						if (c.getCount() == 1) {

							Cursor cur_child = dba.getCms2(
									Constant.LANGUAGE_ID, c.getString(0));

							if (cur_child.getCount() > 0) {
								Bundle b = new Bundle();
								b.putBoolean("showBack", true);

								Intent edit = new Intent(getParent(),
										Cms2.class);
								edit.putExtras(b);
								TabGroupActivity parentActivity = (TabGroupActivity) getParent();
								parentActivity.startChildActivity("Cms2", edit);
							} else {
								Bundle b = new Bundle();
								b.putString("CmsID", c.getString(0));

								b.putBoolean("showBack", true);
								Intent edit = new Intent(getParent(),
										CmsChild2.class);

								edit.putExtras(b);
								TabGroupActivity parentActivity = (TabGroupActivity) getParent();
								parentActivity.startChildActivity("CmsChild2",
										edit);
							}

						} else {

							Bundle b = new Bundle();
							b.putBoolean("showBack", true);

							Intent edit = new Intent(getParent(), Cms2.class);
							edit.putExtras(b);
							TabGroupActivity parentActivity = (TabGroupActivity) getParent();
							parentActivity.startChildActivity("Cms2", edit);
						}
						c.close();
						dba.close();
					} catch (Exception e) {
						e.printStackTrace();
						//Toast.makeText(getParent(), "Error", Toast.LENGTH_SHORT)
								//.show();
					}

				} else if (moduleid.get(arg2).toString().equalsIgnoreCase("12")) {

					/*
					 * Intent edit = new Intent(getParent(),
					 * ImageGalleryCategory1.class);
					 * 
					 * Bundle b = new Bundle(); b.putBoolean("showBack", true);
					 * edit.putExtras(b);
					 * 
					 * TabGroupActivity parentActivity = (TabGroupActivity)
					 * getParent();
					 * parentActivity.startChildActivity("ImageGalleryCategory1"
					 * , edit);
					 */
					try {
						DBAdapter dba = new DBAdapter(getParent());
						dba.open();

						Cursor c = dba
								.getImageGalleryCategory1(Constant.LANGUAGE_ID);

						System.out.println("GallryCategory1 count:::::::"
								+ c.getCount());

						if (c.getCount() == 1) {

							Bundle b = new Bundle();
							b.putString("cat_id", c.getString(0));
							b.putBoolean("showBack", true);
							b.putString("cat_name", c.getString(1));

							Intent edit = new Intent(getParent(),
									ImageGallery1.class);
							edit.putExtras(b);
							TabGroupActivity parentActivity = (TabGroupActivity) getParent();
							parentActivity.startChildActivity("ImageGallery1",
									edit);
						} else {
							Bundle b = new Bundle();
							b.putBoolean("showBack", true);
							Intent edit = new Intent(getParent(),
									ImageGalleryCategory1.class);
							edit.putExtras(b);
							TabGroupActivity parentActivity = (TabGroupActivity) getParent();
							parentActivity.startChildActivity(
									"ImageGalleryCategory1", edit);
						}

						c.close();
						dba.close();
					} catch (Exception e) {
						e.printStackTrace();
						//Toast.makeText(getParent(), "Error", Toast.LENGTH_SHORT)
								//.show();
					}

				} else if (moduleid.get(arg2).toString().equalsIgnoreCase("13")) {
					try {
						Intent edit = new Intent(getParent(), Music.class);

						Bundle b = new Bundle();
						b.putBoolean("showBack", true);
						edit.putExtras(b);
						TabGroupActivity parentActivity = (TabGroupActivity) getParent();
						parentActivity.startChildActivity("Music",
								edit.putExtras(b));
					} catch (Exception e) {
						e.printStackTrace();
						//Toast.makeText(getParent(), "Error", Toast.LENGTH_SHORT)
								//.show();
					}

				} else if (moduleid.get(arg2).toString().equalsIgnoreCase("14")) {

					/*
					 * Intent edit = new Intent(getParent(), Website1.class);
					 * TabGroupActivity parentActivity = (TabGroupActivity)
					 * getParent();
					 * parentActivity.startChildActivity("Website1", edit);
					 */

					try {
						DBAdapter dba = new DBAdapter(getParent());
						dba.open();

						Cursor cur = dba.getWebsite1();

						Cursor c = dba.getWebsiteDetails1(cur.getString(0),
								Constant.LANGUAGE_ID);

						if (cur.getCount() == 1) {

							

							Bundle b = new Bundle();

							b.putString("title", c.getString(0));
							b.putString("website_url", c.getString(1));
							b.putBoolean("showBack", true);
							
							Intent edit = new Intent(getParent(), Webview.class);
							
							edit.putExtras(b);
							TabGroupActivity parentActivity = (TabGroupActivity) getParent();
							parentActivity.startChildActivity("Webview", edit);
						} else {

							Bundle b = new Bundle();
							b.putBoolean("showBack", true);
							Intent edit = new Intent(getParent(),
									Website1.class);
							edit.putExtras(b);
							TabGroupActivity parentActivity = (TabGroupActivity) getParent();
							parentActivity.startChildActivity("Website1", edit);
						}
						cur.close();
						c.close();
						dba.close();
					} catch (Exception e) {
						e.printStackTrace();
						//Toast.makeText(getParent(), "Error", Toast.LENGTH_SHORT)
								//.show();
					}

				} else if (moduleid.get(arg2).toString().equalsIgnoreCase("0")) {
					try {
						Intent edit = new Intent(getParent(), Settings.class);
						TabGroupActivity parentActivity = (TabGroupActivity) getParent();
						parentActivity.startChildActivity("Settings", edit);
					} catch (Exception e) {
						e.printStackTrace();
						//Toast.makeText(getParent(), "Error", Toast.LENGTH_SHORT)
							//	.show();
					}

				}
			}

		});

	}

	// ---header---
	public void setHeaderBackground() {

		((RelativeLayout) findViewById(R.id.rel_music))
				.setBackgroundDrawable(Util.HeaderBackground());

	}

	public boolean IsEventWithinMonth(String date, String time, String enddate,
			String endtime) {

		Calendar cal_start = Calendar.getInstance();
		Calendar cal_end = Calendar.getInstance();

		Calendar cal_current = Calendar.getInstance();

		try {

			String row[] = date.split("-");
			String row1[] = time.split(":");

			cal_start.set(Calendar.DATE, Integer.parseInt(row[2]));
			cal_start.set(Calendar.MONTH, Integer.parseInt(row[1]) - 1);
			cal_start.set(Calendar.YEAR, Integer.parseInt(row[0]));

			cal_start.set(Calendar.HOUR_OF_DAY, Integer.parseInt(row1[0]));
			cal_start.set(Calendar.MINUTE, Integer.parseInt(row1[1]));

			String row_end[] = enddate.split("-");
			String row_end1[] = endtime.split(":");

			cal_end.set(Calendar.DATE, Integer.parseInt(row_end[2]));
			cal_end.set(Calendar.MONTH, Integer.parseInt(row_end[1]) - 1);
			cal_end.set(Calendar.YEAR, Integer.parseInt(row_end[0]));

			cal_end.set(Calendar.HOUR_OF_DAY, Integer.parseInt(row_end1[0]));
			cal_end.set(Calendar.MINUTE, Integer.parseInt(row_end1[1]));

			System.out.println("start time::::::"
					+ cal_start.getTime().toString());
			System.out.println("end time::::::" + cal_end.getTime().toString());

			if (cal_start.before(cal_current)) {

				if (cal_end.before(cal_current)) {
					System.out.println("1");
					return false;
				} else {
					System.out.println("2");
					cal_current.add(Calendar.DAY_OF_YEAR, 30);
					if (cal_end.before(cal_current)) {

						System.out.println("update currnet time::::::"
								+ cal_current.getTime().toString());

						System.out.println("3");
						return true;
					} else {
						System.out.println("4");
						return false;
					}
				}

			}

			System.out.println("5");
			cal_current.add(Calendar.DAY_OF_YEAR, 30);

			if (cal_start.after(cal_current)) {
				return false;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}

		return true;

	}

}
