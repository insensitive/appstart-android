package com.appstart;

import static com.appstart.CommonUtilities.EXTRA_MESSAGE;
import static com.appstart.CommonUtilities.SENDER_ID;
import static com.appstart.CommonUtilities.SERVER_URL;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.widget.Toast;

import com.appstart.DocumentDetails.DownloadFileAsync;
import com.appstart.async.AsyncCallTask;
import com.appstart.database.DBAdapter;
import com.appstart.parsing.CmsData;
import com.appstart.parsing.CmsData1;
import com.appstart.parsing.CmsData2;
import com.appstart.parsing.ContactData;
import com.appstart.parsing.DocumentData;
import com.appstart.parsing.EventsData;
import com.appstart.parsing.GlobalSyncData;
import com.appstart.parsing.HomeWallpaperData;
import com.appstart.parsing.ImageGalleryData;
import com.appstart.parsing.ImageGalleryData1;
import com.appstart.parsing.MusicData;
import com.appstart.parsing.PushMessageData;
import com.appstart.parsing.ResolutonData;
import com.appstart.parsing.SocialMediaData;
import com.appstart.parsing.WebsiteData;
import com.appstart.parsing.WebsiteData1;
import com.appstart.utility.AlertMessages;
import com.appstart.utility.Constant;
import com.appstart.utility.Util;
import com.appstart.webservice.Webservice;

import static com.appstart.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.appstart.CommonUtilities.EXTRA_MESSAGE;
import static com.appstart.CommonUtilities.SERVER_URL;

import com.google.android.gcm.GCMRegistrar;

public class Splash extends Activity implements Runnable {

	AlertMessages mess;

	// ---progressdialog bar variables---
	ProgressDialog progressBar;
	private int progressBarStatus = 0;
	private Handler progressBarHandler = new Handler();

	String deviceid, regid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		mess = new AlertMessages(this);

		// ----

		String locale = this.getResources().getConfiguration().locale
				.getLanguage();

		System.out.println("Locale Language code1::::" + locale);

		// prepare for a progress bar dialog
		progressBar = new ProgressDialog(this);
		progressBar.setCancelable(false);
		progressBar.setMessage("File downloading ...");
		progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressBar.setProgress(0);
		progressBar.setMax(100);

		// reset progress bar status
		progressBarStatus = 0;

		Display mDisplay = getWindowManager().getDefaultDisplay();
		Constant.WIDTH = mDisplay.getWidth();
		Constant.HEIGHT = mDisplay.getHeight();
		
		
		
		DBAdapter db = new DBAdapter(this);
		db.open();
		Cursor c = db.row_query("select * from tbl_Login");
		Constant.CUSTOMER_ID = c.getString(0);
		c.close();
		db.close();
		
		// // -----
		checkNotNull(SERVER_URL, "SERVER_URL");
		checkNotNull(SENDER_ID, "SENDER_ID");

		GCMRegistrar.checkDevice(this);

		GCMRegistrar.checkManifest(this);

		// ----------------------------
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String DeviceToken = getDeviceID(telephonyManager);
		Log.e("", "Device Token is : " + DeviceToken);
		Constant.device_token = DeviceToken;
		
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		final String regId = GCMRegistrar.getRegistrationId(this);
		Constant.regi_id = regId;
		System.out.println("registration id::" + regId);
		
		//Toast.makeText(getApplicationContext(),"Reg ID: "+regId ,Toast.LENGTH_LONG).show();
		if (regId.equals("")) {
			GCMRegistrar.register(this, SENDER_ID);

			final String regId1 = GCMRegistrar.getRegistrationId(this);
			Constant.regi_id = regId1;
			System.out.println("registration id1::" + regId1);
			
		} else {

			System.out.println("user already registered:::");
		}
		
//		Toast.makeText(getApplicationContext(),regId, Toast.LENGTH_LONG).show();

		sending_device_info();
		
		// ----

		SharedPreferences app_preferences = PreferenceManager
				.getDefaultSharedPreferences(Splash.this);

		SharedPreferences.Editor editor = app_preferences.edit();

		Boolean isFirstTime = app_preferences.getBoolean("isFirstTime", true);

		
		if (isFirstTime) {
			
			Thread thread = new Thread(Splash.this);
			thread.start();

			progressBar.show();

			// SharedPreferences.Editor editor = app_preferences.edit();
			editor.putBoolean("isFirstTime", false);
			editor.commit();

		} else {

			if (isInternetConnected()) {

				new AsyncCallTask(Splash.this).execute("");

			} else {
				// no internet connection available so direct go to tabpage
				Intent_main();

			}
		}

	}

	public void run() {
		// TODO Auto-generated method stub
		try {

			ResolutonData obj_resolution = new ResolutonData(Splash.this,
					Constant.CUSTOMER_ID);
			obj_resolution.add_resolution();

			progressBarStatus = 7;
			handlerProgressBar.sendEmptyMessage(0);
			


			GlobalSyncData obj_global = new GlobalSyncData(Splash.this,
					Constant.CUSTOMER_ID);
			obj_global.add_global_sync();

			progressBarStatus = progressBarStatus + 6;
			handlerProgressBar.sendEmptyMessage(0);
			


			ContactData obj_contact = new ContactData(Splash.this,
					Constant.CUSTOMER_ID);
			obj_contact.add_contact_synch();

			progressBarStatus = progressBarStatus + 6;
			handlerProgressBar.sendEmptyMessage(0);
		

			HomeWallpaperData obj_homewallpaper = new HomeWallpaperData(
					Splash.this, Constant.CUSTOMER_ID);
			obj_homewallpaper.add_homewallpaper();

			progressBarStatus = progressBarStatus + 6;
			handlerProgressBar.sendEmptyMessage(0);


			PushMessageData obj_pushmessage = new PushMessageData(Splash.this,
					Constant.CUSTOMER_ID);
			obj_pushmessage.add_Push_Message();

			progressBarStatus = progressBarStatus + 6;
			handlerProgressBar.sendEmptyMessage(0);
			
			
			WebsiteData obj_website = new WebsiteData(Splash.this,
					Constant.CUSTOMER_ID);
			obj_website.add_Website();
			
			progressBarStatus = progressBarStatus + 6;
			handlerProgressBar.sendEmptyMessage(0);
			
			
			SocialMediaData obj_SocialMedia = new SocialMediaData(Splash.this,
					Constant.CUSTOMER_ID);
			obj_SocialMedia.add_SocialMedia();
			
			progressBarStatus = progressBarStatus + 6;
			handlerProgressBar.sendEmptyMessage(0);
			
			
			CmsData obj_cms = new CmsData(Splash.this, Constant.CUSTOMER_ID);
			obj_cms.add_Cms();
			
			progressBarStatus = progressBarStatus + 6;
			handlerProgressBar.sendEmptyMessage(0);
			
			
			EventsData obj_events = new EventsData(Splash.this,
					Constant.CUSTOMER_ID);
			obj_events.add_Events();
			
			progressBarStatus = progressBarStatus + 6;
			handlerProgressBar.sendEmptyMessage(0);
			
			
			DocumentData obj_Document = new DocumentData(Splash.this,
					Constant.CUSTOMER_ID);
			obj_Document.add_Document();
			
			progressBarStatus = progressBarStatus + 6;
			handlerProgressBar.sendEmptyMessage(0);
			
			ImageGalleryData obj_imagegallery = new ImageGalleryData(
					Splash.this, Constant.CUSTOMER_ID);
			obj_imagegallery.add_ImageGallery();
			
			//add_ImageGallery();
			
			/* try {
                 //Thread.sleep(500);
            
             	handler2.sendEmptyMessage(0);
             	//handler2.post(task);
             	
             progressBarStatus = progressBarStatus + 6;
             handlerProgressBar.sendEmptyMessage(0);
             	
             } catch (Exception e) {
                 e.printStackTrace();
             }*/
		
		
         
			MusicData obj_music = new MusicData(Splash.this,
					Constant.CUSTOMER_ID);
			obj_music.add_Music();

			progressBarStatus = progressBarStatus + 6;
			handlerProgressBar.sendEmptyMessage(0);

			CmsData1 obj_cms1 = new CmsData1(Splash.this, Constant.CUSTOMER_ID);
			obj_cms1.add_Cms1();
			
			progressBarStatus = progressBarStatus + 6;
			handlerProgressBar.sendEmptyMessage(0);
			
			CmsData2 obj_cms2 = new CmsData2(Splash.this, Constant.CUSTOMER_ID);
			obj_cms2.add_Cms2();
			
			progressBarStatus = progressBarStatus + 6;
			handlerProgressBar.sendEmptyMessage(0);
			
			ImageGalleryData1 obj_imagegallery1 = new ImageGalleryData1(
					Splash.this, Constant.CUSTOMER_ID);
			obj_imagegallery1.add_ImageGallery();

			//add_ImageGallery1();
			
			progressBarStatus = progressBarStatus + 10;
			handlerProgressBar.sendEmptyMessage(0);
			
			
			
			WebsiteData1 obj_web1=new WebsiteData1(Splash.this, Constant.CUSTOMER_ID);
			obj_web1.add_Website();
			
			progressBarStatus = progressBarStatus + 6;
			handlerProgressBar.sendEmptyMessage(0);
			
			
			handler.sendEmptyMessage(0);
			
			
		} catch (Exception e) {

			handler.sendEmptyMessage(1);
		}
	}

	  private Handler handler2 = new Handler(){
    		
    		public void handleMessage(Message msg) {
    			
    			
    			String img_url=msg.getData().getString("image_url");
    			String img_name=msg.getData().getString("image_name");
    					
    					String[] a=new String[2];
    					a[0]=img_url;
    					a[1]=img_name;
    					
    					new DownloadFileAsync().equals(a);
    	    			
    			
    		}
    	};
    	
    	/*Runnable task=new Runnable() {
    		
    		@Override
    		public void run() {
    			
    		//handler2.postDelayed(task,1000);
    			
    		//dosomework();
    			
    		
    		}
    
    		private void dosomework() {
    			ImageGalleryData obj_imagegallery = new ImageGalleryData(
    					Splash.this, Constant.CUSTOMER_ID);
    			obj_imagegallery.add_ImageGallery();
    			
    		}
    	}; */

    	
    	
	// -----

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			if (msg.what == 0) {

				try {

					DBAdapter dba = new DBAdapter(Splash.this);
					dba.open();

					// Util.SetLocale(getBaseContext(), dba
					// .getDefaultLanguageCode(dba.getDefaultLanguage()));

					dba.close();

					Intent mainint = new Intent(Splash.this, TabSample.class);
					startActivity(mainint);
					finish();
					setActivityAnimation(Splash.this, R.anim.fade_in,
							R.anim.fade_out);

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			} else {

				showNetworkAlert();
			}

		}
	};

	public boolean isInternetConnected() {
		ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean ret = true;
		if (conMgr != null) {
			NetworkInfo i = conMgr.getActiveNetworkInfo();

			if (i != null) {
				if (!i.isConnected()) {
					ret = false;
				}

				if (!i.isAvailable()) {
					ret = false;
				}
			}

			if (i == null)
				ret = false;
		} else
			ret = false;
		return ret;
	}

	static public void setActivityAnimation(Activity activity, int in, int out) {
		try {

			Method method = Activity.class.getMethod(
					"overridePendingTransition", new Class[] { int.class,
							int.class });
			method.invoke(activity, in, out);

		} catch (Exception e) {
			// Can't change animation, so do nothing
		}
	}

	// network alert
	public void showNetworkAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(Splash.this);
		builder.setMessage("Please check your internet connection.")
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();

						finish();

					}
				});

		AlertDialog alert = builder.create();
		alert.setTitle("Connection Problem");
		alert.show();

	}

	public void Intent_main() {

		DBAdapter dba = new DBAdapter(this);
		dba.open();

		// Util.SetLocale(getBaseContext(),
		// dba.getDefaultLanguageCode(dba.getDefaultLanguage()));

		dba.close();

		Intent mainint = new Intent(Splash.this, TabSample.class);
		startActivity(mainint);
		finish();
		setActivityAnimation(Splash.this, R.anim.fade_in, R.anim.fade_out);

	}

	// ---

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public String CalResolutionID() {

		String resolutionid = "8";
		try {
			Display mDisplay = getWindowManager().getDefaultDisplay();
			int width = mDisplay.getWidth();
			int height = mDisplay.getHeight();
			resolutionid = Util.GetResolutionID(width, height, Splash.this);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resolutionid;
	}

	public void sending_device_info() {
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();

				try {

					Webservice.Gcm(Constant.CUSTOMER_ID, Constant.device_token,
							Constant.regi_id);

				} catch (Exception e) {
					e.printStackTrace();

				}

			}
		}.start();
	}

	private void checkNotNull(Object reference, String name) {
		if (reference == null) {
			throw new NullPointerException(getString(R.string.error_config,
					name));
		}
	}

	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);

		}
	};

	String getDeviceID(TelephonyManager phonyManager) {

		String id = phonyManager.getDeviceId();
		if (id == null) {
			id = "not available";
		}

		int phoneType = phonyManager.getPhoneType();
		switch (phoneType) {
		case TelephonyManager.PHONE_TYPE_NONE:
			return id;

		case TelephonyManager.PHONE_TYPE_GSM:
			return id;

		case TelephonyManager.PHONE_TYPE_CDMA:
			return id;

			/*
			 * for API Level 11 or above case TelephonyManager.PHONE_TYPE_SIP:
			 * return "SIP";
			 */

		default:
			return id;
		}

	}
	
	
	private Handler handlerProgressBar = new Handler() {
		public void handleMessage(Message msg) {

			if (msg.what == 0) {

				 
				progressBar.setProgress(progressBarStatus);
				
				
			} else {

				 
			}

		}
	};
	
	public void add_ImageGallery() {
		
		DBAdapter dba;
		
		String response = Webservice.ImageGallery(Constant.CUSTOMER_ID, "android");

		 dba = new DBAdapter(this);
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
							
							/*Util.Store_file_phone(
									img_thumb_url,
									img_name);*/
							
							String a[] = new String[2];
							a[0] = img_thumb_url;
							a[1] =img_name;
							
							try{
									            
								Message a1=new Message();
								Bundle b=new Bundle();
								b.putString("image_url",img_thumb_url);
								b.putString("image_name", img_name);
								a1.setData(b);
								
								handler2.sendMessage(a1);
							
								}catch(Exception e){
									e.printStackTrace();
									Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_SHORT).show();
								}
							
							
						}

						Log.e("Url:::",
								"http://203.88.158.98/appstart_live/public/"
										+ img_thumb_url);

						String img_name1 = "Ig" + Util.getRandomFileName();

						if (img_url.contains(".jpg")
								|| img_url.contains(".png")) {
							//Util.Store_file_phone(img_url, img_name1);
							
							String a[] = new String[2];
							a[0] = img_url;
							a[1] =img_name1;
							
							try{
					            
								Message a1=new Message();
								Bundle b=new Bundle();
								b.putString("image_url",img_thumb_url);
								b.putString("image_name", img_name);
								a1.setData(b);
								
								handler2.sendMessage(a1);
							
								}catch(Exception e){
									e.printStackTrace();
									Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_SHORT).show();
								}
							
							
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
		
		 progressBarStatus = progressBarStatus + 6;
         handlerProgressBar.sendEmptyMessage(0);
	}
	
	
	public void add_ImageGallery1() {
		DBAdapter dba;
		
		String response = Webservice.ImageGallery1(Constant.CUSTOMER_ID, "android");
	
		dba = new DBAdapter(this);
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
								
								//Util.Store_file_phone(
										//img_url.replace(".jpg", "_thumb.jpg"),
										//img_name);
								
								
						
						String a[] = new String[2];
						a[0] = img_url;
						a[1] =img_name;
						
						try{
								            
							Message a1=new Message();
							Bundle b=new Bundle();
							b.putString("image_url",img_url);
							b.putString("image_name", img_name);
							a1.setData(b);
							
							handler2.sendMessage(a1);
						
							}catch(Exception e){
								e.printStackTrace();
								Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_SHORT).show();
							}
							}

							Log.e("Url:::",
									"http://203.88.158.98/appstart_live/public/"
											+ img_url.replace(".jpg",
													"_thumb.jpg"));

							String img_name1 = "Ig" + j
									+ Util.getRandomFileName();

							if (img_url.contains(".jpg")
									|| img_url.contains(".png")) {
								//Util.Store_file_phone(img_url, img_name1);

								String a[] = new String[2];
								a[0] = img_url;
								a[1] =img_name1;
								
								try{
						            
									Message a1=new Message();
									Bundle b=new Bundle();
									b.putString("image_url",img_url);
									b.putString("image_name", img_name);
									a1.setData(b);
									
									handler2.sendMessage(a1);
								
									}catch(Exception e){
										e.printStackTrace();
										Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_SHORT).show();
									}

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
		 progressBarStatus = progressBarStatus + 10;
         handlerProgressBar.sendEmptyMessage(0);

	}
	
	class DownloadFileAsync extends AsyncTask<String, String, String> {


		@Override
		protected String doInBackground(String... aurl) {
			int count;
			String urlstring=aurl[0],name=aurl[1];

		try {
			URL url;
			
				url = new URL("http://loginv2.appstart.ch/"
						+ urlstring.replace(" ", "%20"));
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

				System.out.println("download suceess of image:::" + name);
		} catch (Exception ea) {
			ea.printStackTrace();
			Toast.makeText(getApplicationContext(), "Error in Image Module", Toast.LENGTH_LONG).show();

		}
		return null;
		}
		
	}

	

}
