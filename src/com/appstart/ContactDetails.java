package com.appstart;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.appstart.async.ImageLoadFromDownload;
import com.appstart.database.DBAdapter;
import com.appstart.tabgroup.TabGroupActivity;
import com.appstart.utility.AlertMessages;
import com.appstart.utility.Constant;
import com.appstart.utility.Util;
import com.appstart.webservice.Webservice;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

public class ContactDetails extends Activity implements OnClickListener {

	Button ib_call, ib_share, ib_vcard, ib_map, ib_get_route;
	Button ib_back;

	String location_name = "", phone1 = "", phone2 = "", phone3 = "", fax = "",
			email = "", address = "", website = "", timing = "", latitude,
			longitude, image_name, information = "";

	TableLayout tl_timings;
	String layout_background_color = "#121212";

	double lat = 0, lng = 0;
	LatLng TO_DESTINATION, TO_GPS_LOCATION;
	GPSTracker gps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contactdetails);

		gps = new GPSTracker(getParent());

		Bundle b = getIntent().getExtras();
		String contact_id = b.getString("contact_id");

		boolean showBack = b.getBoolean("showBack");

		ib_back = (Button) findViewById(R.id.ib_back_music);
		ib_back.setOnClickListener(this);

		if (showBack) {
			ib_back.setVisibility(View.VISIBLE);
		} else {
			ib_back.setVisibility(View.INVISIBLE);
		}

		tf = Util.font(this, Constant.FONT_TYPE);
		setHeaderBackground();
		set_text_format();

		ib_call = (Button) findViewById(R.id.ib_call_cd);
		ib_call.setOnClickListener(this);

		ib_vcard = (Button) findViewById(R.id.ib_vcard);
		ib_vcard.setOnClickListener(this);

		ib_get_route = (Button) findViewById(R.id.ib_get_route);
		ib_get_route.setOnClickListener(this);

		ib_map = (Button) findViewById(R.id.ib_map_contact);
		ib_map.setOnClickListener(this);

		// ---set contact background---

		DBAdapter db = new DBAdapter(this);
		db.open();

		Cursor c1 = db.getBackgroundImage("1", Constant.LANGUAGE_ID);

		Cursor c2 = db.getBackgroundColor("1", Constant.LANGUAGE_ID);

		try {

			if ((c1.getCount() > 0) && (!c1.getString(0).equals(null))) {
				((LinearLayout) findViewById(R.id.lay_cd))
						.setBackgroundDrawable(ImgDrawableFromFile(
								getResources(), c1.getString(0)));

				System.out.println("Background imageName:::::::"
						+ c1.getString(0));
			}

			if (c2.getCount() > 0) {

				if ((c2.getString(0).equals(null))
						|| (c2.getString(0).equals(" "))) {
					System.out.print(" set background color1: ");
					((LinearLayout) findViewById(R.id.lay_cd))
							.setBackgroundColor(Color
									.parseColor(layout_background_color));

				} else {
					if (!c2.getString(0).startsWith("#")) {
						System.out.print(" set background color2: ");
						((LinearLayout) findViewById(R.id.lay_cd))
								.setBackgroundColor(Color
										.parseColor(layout_background_color));
					} else {
						System.out.print(" set background color default1: ");
						((LinearLayout) findViewById(R.id.lay_cd))
								.setBackgroundColor(Color.parseColor(c2
										.getString(0)));
					}

				}

			} else {
				System.out.print(" set background color default2: ");
				((LinearLayout) findViewById(R.id.lay_cd))
						.setBackgroundColor(Color
								.parseColor(layout_background_color));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		db.close();

		DBAdapter dba = new DBAdapter(this);
		dba.open();
		Cursor c = dba.getAllContactDetails(contact_id, Constant.LANGUAGE_ID);

		if (c.getCount() > 0) {

			location_name = c.getString(3);
			phone1 = c.getString(5);
			phone2 = c.getString(6);
			phone3 = c.getString(7);
			fax = c.getString(8);
			email = c.getString(11);
			address = c.getString(4);
			timing = c.getString(15);
			latitude = c.getString(9);
			longitude = c.getString(10);
			image_name = c.getString(16);
			website = c.getString(14);
			information = c.getString(17);

			// -----
			setText();

		}

		dba.close();

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		if (arg0 == ib_call) {

			show_alert();

			//
		} else if (arg0 == ib_back) {

			TabGroupActivity parentActivity = (TabGroupActivity) getParent();
			parentActivity.onBackPressed();

		} else if (arg0 == ib_vcard) {

			add_vcard();

		} else if (arg0 == ib_map) {

			Intent edit = new Intent(getParent(), BasicMapActivity.class);

			Bundle b = new Bundle();
			b.putString("latitude", latitude);
			b.putString("longitude", longitude);
			b.putString("title", location_name);
			edit.putExtras(b);

			TabGroupActivity parentActivity = (TabGroupActivity) getParent();
			parentActivity.startChildActivity("Map", edit);
		} else if (arg0 == ib_get_route) {

			Toast.makeText(
					getParent(),
					"My Location is - \nLatitude: " + lat + "\nLongitude: "
							+ lng, Toast.LENGTH_LONG).show();

			/*
			 * String encodedString=" ";
			 * 
			 * try {
			 * 
			 * //String urlString =
			 * "http://maps.googleapis.com/maps/api/directions/json?origin=52.97441,-1.47895&destination=53.784257,-1.465985&sensor=false&mode=driving"
			 * ; String urlString=makeUrl();
			 * 
			 * //new connectAsyncTask(urlString).execute();
			 * 
			 * 
			 * String result = Webservice.getJSONFromURL(urlString);
			 * 
			 * 
			 * JSONObject json = new JSONObject(result.toString());
			 * 
			 * JSONArray routeArray = json.getJSONArray("routes");
			 * 
			 * JSONObject routes = routeArray.getJSONObject(0);
			 * 
			 * JSONObject overviewPolylines = routes
			 * .getJSONObject("overview_polyline");
			 * 
			 * System.out.print(overviewPolylines);
			 * 
			 * encodedString = overviewPolylines.getString("points");
			 * 
			 * System.out.print(encodedString.toString());
			 * 
			 * Toast.makeText(getParent(), encodedString.toString(),
			 * Toast.LENGTH_LONG).show();
			 * 
			 * } catch (JSONException e) { // TODO Auto-generated catch block
			 * System.out.print(e.getMessage());
			 * 
			 * }
			 */

			Intent intent = new Intent(
					android.content.Intent.ACTION_VIEW,
					Uri.parse("http://maps.google.com/maps?saddr=" + lat + ","
							+ lng + "&daddr=" + latitude + "," + longitude + ""));
			startActivity(intent);

			/*
			 * Intent edit = new Intent(getParent(), BasicMapActivity1.class);
			 * Bundle b = new Bundle();
			 * 
			 * b.putString("latitude", latitude); b.putString("longitude",
			 * longitude); b.putString("title", location_name); //
			 * b.putString("enncodedString", encodedString); edit.putExtras(b);
			 * 
			 * TabGroupActivity parentActivity = (TabGroupActivity) getParent();
			 * parentActivity.startChildActivity("Getroute", edit);
			 */

		}

	}

	public String makeUrl() {
		StringBuilder urlString = new StringBuilder();

		if (gps.canGetLocation()) {

			lat = gps.getLatitude();
			lng = gps.getLongitude();

			// \n is for new line
			Toast.makeText(getParent(),
					"Your Location is - \nLat: " + lat + "\nLong: " + lng,
					Toast.LENGTH_LONG).show();

		} else {
			// can't get location
			// GPS or Network is not enabled
			// Ask user to enable GPS/network in settings
			gps.showSettingsAlert();
		}
		TO_GPS_LOCATION = new LatLng(lat, lng);
		TO_DESTINATION = new LatLng(Double.parseDouble(latitude),
				Double.parseDouble(longitude));

		urlString.append("http://maps.googleapis.com/maps/api/directions/json");
		urlString.append("?origin="); // start position
		urlString.append(Double.toString(TO_GPS_LOCATION.latitude));
		urlString.append(",");
		urlString.append(Double.toString(TO_GPS_LOCATION.longitude));
		urlString.append("&destination="); // end position
		urlString.append(Double.toString(TO_DESTINATION.latitude));
		urlString.append(",");
		urlString.append(Double.toString(TO_DESTINATION.longitude));
		urlString.append("&sensor=true&mode=driving");

		return urlString.toString();
	}

	/*
	 * class connectAsyncTask extends AsyncTask<String, Void, String> { String
	 * url;
	 * 
	 * connectAsyncTask() {
	 * 
	 * } connectAsyncTask(String urlPass) { url = urlPass; }
	 * 
	 * @Override protected void onPreExecute() {
	 * 
	 * super.onPreExecute();
	 * 
	 * }
	 * 
	 * @Override protected String doInBackground(String... params) {
	 * 
	 * String result=Webservice.getJSONFromURL(url);
	 * System.out.print(result.toString()); return result; }
	 * 
	 * @Override protected void onPostExecute(String result) {
	 * super.onPostExecute(result);
	 * 
	 * if (result != null) { try { // Tranform the string into a json object
	 * final JSONObject json = new JSONObject(result.toString()); JSONArray
	 * routeArray = json.getJSONArray("routes"); JSONObject routes =
	 * routeArray.getJSONObject(0); JSONObject overviewPolylines = routes
	 * .getJSONObject("overview_polyline"); String encodedString =
	 * overviewPolylines.getString("points");
	 * 
	 * System.out.print("encodedString : "+encodedString.toString());
	 * 
	 * } catch (JSONException e) { e.printStackTrace(); } } } }
	 */

	public void add_vcard() {

		String display_name = location_name;

		ArrayList<ContentProviderOperation> ops1 = new ArrayList<ContentProviderOperation>();

		/*
		 * ArrayList<ContentProviderOperation> ops1 = new
		 * ArrayList<ContentProviderOperation>();
		 * 
		 * ops1.add(ContentProviderOperation
		 * .newInsert(ContactsContract.RawContacts.CONTENT_URI)
		 * .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
		 * .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
		 * .build());
		 * 
		 * ops1.add(ContentProviderOperation
		 * .newInsert(ContactsContract.Data.CONTENT_URI)
		 * .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		 * .withValue( ContactsContract.Data.MIMETYPE,
		 * ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
		 * .withValue(
		 * ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
		 * display_name).build());
		 * 
		 * // ----add phone number---// ops1.add(ContentProviderOperation
		 * .newInsert(ContactsContract.Data.CONTENT_URI)
		 * .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		 * .withValue( ContactsContract.Data.MIMETYPE,
		 * ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
		 * .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone1)
		 * .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
		 * ContactsContract.CommonDataKinds.Phone.TYPE_WORK) .build());
		 * 
		 * ops1.add(ContentProviderOperation
		 * .newInsert(ContactsContract.Data.CONTENT_URI)
		 * .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		 * .withValue( ContactsContract.Data.MIMETYPE,
		 * ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
		 * .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone2)
		 * .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
		 * ContactsContract.CommonDataKinds.Phone.TYPE_WORK) .build());
		 * 
		 * ops1.add(ContentProviderOperation
		 * .newInsert(ContactsContract.Data.CONTENT_URI)
		 * .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		 * .withValue( ContactsContract.Data.MIMETYPE,
		 * ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
		 * .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone3)
		 * .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
		 * ContactsContract.CommonDataKinds.Phone.TYPE_WORK) .build());
		 * 
		 * // ----add fax number----// ops1.add(ContentProviderOperation
		 * .newInsert(ContactsContract.Data.CONTENT_URI)
		 * .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		 * .withValue( ContactsContract.Data.MIMETYPE,
		 * ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
		 * .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, fax)
		 * .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
		 * ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK) .build());
		 * 
		 * // ---add email----// ops1.add(ContentProviderOperation
		 * .newInsert(ContactsContract.Data.CONTENT_URI)
		 * .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		 * .withValue( ContactsContract.Data.MIMETYPE,
		 * ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE) //
		 * .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, // email)
		 * .withValue(ContactsContract.CommonDataKinds.Email.TYPE,
		 * ContactsContract.CommonDataKinds.Email.TYPE_WORK) .build());
		 * 
		 * // ---add address---// ops1.add(ContentProviderOperation
		 * .newInsert(ContactsContract.Data.CONTENT_URI)
		 * .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		 * .withValue( ContactsContract.Data.MIMETYPE,
		 * ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
		 * .withValue( ContactsContract.CommonDataKinds.StructuredPostal.STREET,
		 * address)
		 * 
		 * .withValue( ContactsContract.CommonDataKinds.StructuredPostal.TYPE,
		 * ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK)
		 * .build());
		 */

		// ----

		ContentResolver cr = getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
				null, null, null);

		ArrayList<String> exist_name = new ArrayList<String>();

		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {

				exist_name
						.add(cur.getString(cur
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));

				String existName = cur
						.getString(cur
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

				System.out.println("existing name:::" + existName);
			}

			boolean check = check_exixst(exist_name, display_name);

			if (check) {

				Toast.makeText(
						ContactDetails.this,
						"The contact name: " + display_name + " already exists",
						Toast.LENGTH_SHORT).show();

			} else {

				add_into_phone(display_name);

				Toast.makeText(ContactDetails.this,
						"Add contact to Phone: " + display_name,
						Toast.LENGTH_SHORT).show();
			}

		}/*
		  else{
		  
		  add_into_phone(display_name);
		  
		  Toast.makeText( ContactDetails.this, "Add contact to Phone: " +
		  display_name , Toast.LENGTH_SHORT) .show(); }
		 */

		// -----
		try {

			// cr.applyBatch(ContactsContract.AUTHORITY, ops1);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean check_exixst(ArrayList<String> exist_name,String display_name) {
		
		boolean back = false;
		
		for(int i=0;i<exist_name.size();i++){
			
			//if (exist_name.get(i) != null) {
				if (exist_name.get(i).contains(display_name)) {
					
					System.out.println("name exist");
				
						back=true;
			
				}
				else{
					System.out.println("add name");
					back=false;
					
				}
			}
			
		//}
		return back;	
	}

	private void add_into_phone(String display_name) {

		ArrayList<ContentProviderOperation> ops1 = new ArrayList<ContentProviderOperation>();

		ops1.add(ContentProviderOperation
				.newInsert(ContactsContract.RawContacts.CONTENT_URI)
				.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
				.withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
				.build());

		ops1.add(ContentProviderOperation
				.newInsert(ContactsContract.Data.CONTENT_URI)
				.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
				.withValue(
						ContactsContract.Data.MIMETYPE,
						ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
				.withValue(
						ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
						display_name).build());

		// ----add phone number---//
		ops1.add(ContentProviderOperation
				.newInsert(ContactsContract.Data.CONTENT_URI)
				.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
				.withValue(
						ContactsContract.Data.MIMETYPE,
						ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
				.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
						phone1)
				.withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
						ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
				.build());

		ops1.add(ContentProviderOperation
				.newInsert(ContactsContract.Data.CONTENT_URI)
				.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
				.withValue(
						ContactsContract.Data.MIMETYPE,
						ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
				.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
						phone2)
				.withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
						ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
				.build());

		ops1.add(ContentProviderOperation
				.newInsert(ContactsContract.Data.CONTENT_URI)
				.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
				.withValue(
						ContactsContract.Data.MIMETYPE,
						ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
				.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
						phone3)
				.withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
						ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
				.build());

		// ----add fax number----//
		ops1.add(ContentProviderOperation
				.newInsert(ContactsContract.Data.CONTENT_URI)
				.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
				.withValue(
						ContactsContract.Data.MIMETYPE,
						ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
				.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, fax)
				.withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
						ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK)
				.build());

		// ---add email----//
		ops1.add(ContentProviderOperation
				.newInsert(ContactsContract.Data.CONTENT_URI)
				.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
				.withValue(
						ContactsContract.Data.MIMETYPE,
						ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
				// .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS,
				// email)
				.withValue(ContactsContract.CommonDataKinds.Email.TYPE,
						ContactsContract.CommonDataKinds.Email.TYPE_WORK)
				.build());

		// ---add address---//
		ops1.add(ContentProviderOperation
				.newInsert(ContactsContract.Data.CONTENT_URI)
				.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
				.withValue(
						ContactsContract.Data.MIMETYPE,
						ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
				.withValue(
						ContactsContract.CommonDataKinds.StructuredPostal.STREET,
						address)

				.withValue(
						ContactsContract.CommonDataKinds.StructuredPostal.TYPE,
						ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK)
				.build());

		ContentResolver cr = getContentResolver();

		try {
			cr.applyBatch(ContactsContract.AUTHORITY, ops1);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setText() {

		TableRow tr_phone = (TableRow) findViewById(R.id.tr_phone);
		TableRow tr_email = (TableRow) findViewById(R.id.tr_email);
		TableRow tr_website = (TableRow) findViewById(R.id.tr_website);
		TableRow tr_phone2 = (TableRow) findViewById(R.id.tr_phone2);
		TableRow tr_phone3 = (TableRow) findViewById(R.id.tr_phone3);
		TableRow tr_fax = (TableRow) findViewById(R.id.tr_fax);
		TableRow tr_address = (TableRow) findViewById(R.id.tr_address);

		TableLayout tbl_timing = (TableLayout) findViewById(R.id.tbl_timing);
		TableLayout tbl_info = (TableLayout) findViewById(R.id.tbl_info);

		ImageView iv_sep_email = (ImageView) findViewById(R.id.iv_sep_email);
		ImageView iv_sep_website = (ImageView) findViewById(R.id.iv_sep_website);
		ImageView iv_sep_phone2 = (ImageView) findViewById(R.id.iv_sep_phone2);
		ImageView iv_sep_phone3 = (ImageView) findViewById(R.id.iv_sep_phone3);
		ImageView iv_sep_fax = (ImageView) findViewById(R.id.iv_sep_fax);
		ImageView iv_sep_address = (ImageView) findViewById(R.id.iv_sep_address);

		if (phone1.trim().equalsIgnoreCase("")) {
			tr_phone.setVisibility(View.GONE);
			iv_sep_email.setVisibility(View.GONE);
		}
		if (email.trim().equalsIgnoreCase("")) {
			tr_email.setVisibility(View.GONE);
			iv_sep_email.setVisibility(View.GONE);
		}
		if (website.trim().equalsIgnoreCase("")) {
			tr_website.setVisibility(View.GONE);
			iv_sep_website.setVisibility(View.GONE);
		}
		if (phone2.trim().equalsIgnoreCase("")) {
			tr_phone2.setVisibility(View.GONE);
			iv_sep_phone2.setVisibility(View.GONE);
		}
		if (phone3.trim().equalsIgnoreCase("")) {
			tr_phone3.setVisibility(View.GONE);
			iv_sep_phone3.setVisibility(View.GONE);
		}
		if (fax.trim().equalsIgnoreCase("")) {
			tr_fax.setVisibility(View.GONE);
			iv_sep_fax.setVisibility(View.GONE);

		}
		if (address.trim().equalsIgnoreCase("")) {
			tr_address.setVisibility(View.GONE);
			iv_sep_address.setVisibility(View.GONE);
		}

		if (timing.trim().equalsIgnoreCase("")) {
			tbl_timing.setVisibility(View.GONE);
		}

		if (information.trim().equalsIgnoreCase("")) {
			tbl_info.setVisibility(View.GONE);
		}

		// displaying data into view

		((TextView) findViewById(R.id.txt_music)).setText(location_name);

		// ((TextView) findViewById(R.id.txt_contact_details)).setTypeface(Util
		// .font(this, Constant.FONT_TYPE));

		((TextView) findViewById(R.id.txt_address)).setText(address);
		((TextView) findViewById(R.id.txt_email)).setText(email);
		((TableRow) findViewById(R.id.tr_email))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub

						Util.SendEmail(ContactDetails.this,
								((TextView) findViewById(R.id.txt_email))
										.getText().toString());

					}
				});

		((TextView) findViewById(R.id.txt_phone)).setText(phone1);
		((TextView) findViewById(R.id.txt_phone2)).setText(phone2);
		((TextView) findViewById(R.id.txt_phone3)).setText(phone3);
		((TextView) findViewById(R.id.txt_fax)).setText(fax);
		((TextView) findViewById(R.id.txt_web)).setText(website);
		((TextView) findViewById(R.id.txt_info)).setText(Html
				.fromHtml(information));

		((TableRow) findViewById(R.id.tr_website))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// Util.website(ContactDetails.this,
						// "www.emma.com");
						Bundle b = new Bundle();
						b.putString("website_url", website);

						Intent edit = new Intent(getParent(), Webview.class);

						edit.putExtras(b);
						TabGroupActivity parentActivity = (TabGroupActivity) getParent();
						parentActivity.startChildActivity("Webview", edit);

					}
				});

		// ((TextView) findViewById(R.id.txt_tmg)).setText(timing);

		get_time_string(timing);

		// tr_phone.setPadding(10,5, 10, 5);

		// ((ImageView) findViewById(R.id.iv_ic_contact)).setImageBitmap(Util
		// .ImgBitFromFile(image_name));

		Object a[] = new Object[2];
		a[0] = (ImageView) findViewById(R.id.iv_ic_contact);
		a[1] = image_name;

		new ImageLoadFromDownload().execute(a);

		// final double lat ,lng;

		// check if GPS enabled
		if (gps.canGetLocation()) {

			lat = gps.getLatitude();
			lng = gps.getLongitude();

			// \n is for new line
			// Toast.makeText(getParent(), "Your Location is - \nLat: " + lat +
			// "\nLong: " +
			// lng, Toast.LENGTH_LONG).show();

		} else {
			// can't get location
			// GPS or Network is not enabled
			// Ask user to enable GPS/network in settings
			gps.showSettingsAlert();
		}

		/*
		 * double[] d = getlocation(); final double lat = d[0]; final double lng
		 * = d[1];
		 */

		System.out.println("FROM GPS ********************" + "latitude  " + lat
				+ "  longitude  " + lng);

		((TableRow) findViewById(R.id.tr_address))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						Intent intent = new Intent(
								android.content.Intent.ACTION_VIEW,
								Uri.parse("http://maps.google.com/maps?saddr="
										+ lat + "," + lng + "&daddr="
										+ latitude + "," + longitude + ""));
						startActivity(intent);

					}
				});

	}

	public double[] getlocation() {
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		Criteria criteria = new Criteria();

		criteria.setAccuracy(Criteria.ACCURACY_FINE);

		// Getting the name of the best provider
		String provider = lm.getBestProvider(criteria, false);

		// Getting Current Location
		Location l = lm.getLastKnownLocation(provider);

		/*
		 * List<String> providers = lm.getProviders(true);
		 * 
		 * Location l = null; for (int i = 0; i < providers.size(); i++) { l =
		 * lm.getLastKnownLocation(providers.get(i)); if (l != null) break; }
		 */

		double[] gps = new double[2];

		if (l != null) {
			gps[0] = l.getLatitude();
			gps[1] = l.getLongitude();
		}

		return gps;
	}

	// ---
	public void setHeaderBackground() {

		((RelativeLayout) findViewById(R.id.rel_music))
				.setBackgroundDrawable(Util.HeaderBackground());

	}

	Typeface tf;

	public void set_text_format() {

		((TextView) findViewById(R.id.txt_address)).setTypeface(tf);
		((TextView) findViewById(R.id.txt_email)).setTypeface(tf);
		((TextView) findViewById(R.id.txt_phone)).setTypeface(tf);
		((TextView) findViewById(R.id.txt_phone2)).setTypeface(tf);
		((TextView) findViewById(R.id.txt_phone3)).setTypeface(tf);
		((TextView) findViewById(R.id.txt_fax)).setTypeface(tf);
		((TextView) findViewById(R.id.txt_web)).setTypeface(tf);
		((TextView) findViewById(R.id.txt_tmg)).setTypeface(tf);
		((TextView) findViewById(R.id.txt_info)).setTypeface(tf);

		((TextView) findViewById(R.id.txt_address)).setTextColor(Util
				.FontColorcode());
		((TextView) findViewById(R.id.txt_email)).setTextColor(Util
				.FontColorcode());

		((TextView) findViewById(R.id.txt_phone)).setTextColor(Util
				.FontColorcode());
		((TextView) findViewById(R.id.txt_phone2)).setTextColor(Util
				.FontColorcode());
		((TextView) findViewById(R.id.txt_phone3)).setTextColor(Util
				.FontColorcode());
		((TextView) findViewById(R.id.txt_fax)).setTextColor(Util
				.FontColorcode());
		((TextView) findViewById(R.id.txt_web)).setTextColor(Util
				.FontColorcode());
		((TextView) findViewById(R.id.txt_tmg)).setTextColor(Util
				.FontColorcode());
		((TextView) findViewById(R.id.txt_info)).setTextColor(Util
				.FontColorcode());

		((TextView) findViewById(R.id.txt_p)).setTypeface(tf);
		((TextView) findViewById(R.id.txt_p2)).setTypeface(tf);
		((TextView) findViewById(R.id.txt_p3)).setTypeface(tf);
		((TextView) findViewById(R.id.txt_w)).setTypeface(tf);
		((TextView) findViewById(R.id.txt_e)).setTypeface(tf);
		((TextView) findViewById(R.id.txt_f)).setTypeface(tf);
		((TextView) findViewById(R.id.txt_a)).setTypeface(tf);
		((TextView) findViewById(R.id.txt_t)).setTypeface(tf);

		((TextView) findViewById(R.id.txt_p))
				.setTextColor(Util.FontColorcode());
		((TextView) findViewById(R.id.txt_p2)).setTextColor(Util
				.FontColorcode());
		((TextView) findViewById(R.id.txt_p3)).setTextColor(Util
				.FontColorcode());
		((TextView) findViewById(R.id.txt_w))
				.setTextColor(Util.FontColorcode());
		((TextView) findViewById(R.id.txt_e))
				.setTextColor(Util.FontColorcode());
		((TextView) findViewById(R.id.txt_f))
				.setTextColor(Util.FontColorcode());
		((TextView) findViewById(R.id.txt_a))
				.setTextColor(Util.FontColorcode());
		((TextView) findViewById(R.id.txt_t))
				.setTextColor(Util.FontColorcode());

	}

	//
	LayoutInflater linf;
	LinearLayout rr;

	public void get_time_string(String sampletest) {

		//
		linf = (LayoutInflater) getApplicationContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		linf = LayoutInflater.from(ContactDetails.this);

		tl_timings = (TableLayout) findViewById(R.id.tl_timings);
		tl_timings.removeAllViews();

		//

		// String sampletest =
		// "<node><record><order>1</order><day>Monday -Thursday</day><from>08:00</from><to>18:30</to></record><record><order>2</order><day>Friday</day><from>08:00</from><to>20:00</to></record><record><order>3</order><day>Saturday</day><from>09:00</from><to>16:00</to></record></node>";

		if (sampletest.contains("<record>")) {

			String row[] = sampletest.split("<record>");

			for (int i = 1; i < row.length; i++) {

				final View v = linf.inflate(R.layout.lay_row_tablerow, null);

				String order = row[i].split("<order>")[1].split("</order>")[0];
				String day = row[i].split("<day>")[1].split("</day>")[0];
				String from = row[i].split("<from>")[1].split("</from>")[0];
				String to = row[i].split("<to>")[1].split("</to>")[0];

				System.out.println("timings:::::" + day + " " + " " + from
						+ " " + to);

				((TextView) v.findViewById(R.id.tv_day)).setText(day);
				((TextView) v.findViewById(R.id.tv_from)).setText(from);
				((TextView) v.findViewById(R.id.tv_to)).setText(to);

				((TextView) v.findViewById(R.id.tv_day)).setTypeface(tf);
				((TextView) v.findViewById(R.id.tv_from)).setTypeface(tf);
				((TextView) v.findViewById(R.id.tv_to)).setTypeface(tf);

				((TextView) v.findViewById(R.id.tv_day)).setTextColor(Util
						.FontColorcode());
				((TextView) v.findViewById(R.id.tv_from)).setTextColor(Util
						.FontColorcode());
				((TextView) v.findViewById(R.id.tv_to)).setTextColor(Util
						.FontColorcode());
				((TextView) v.findViewById(R.id.txt_time_sep))
						.setTextColor(Util.FontColorcode());

				tl_timings.addView(v);

			}
		} else {
			((TableLayout) findViewById(R.id.tbl_timing))
					.setVisibility(View.GONE);
		}

	}

	// ---alert for showing phone number list---
	public void show_alert() {

		ArrayList<String> phone = new ArrayList<String>();

		if (!phone1.equalsIgnoreCase("")) {
			phone.add(phone1);
		}
		if (!phone2.equalsIgnoreCase("")) {
			phone.add(phone2);
		}
		if (!phone3.equalsIgnoreCase("")) {
			phone.add(phone3);
		}

		final CharSequence[] cs = phone.toArray(new CharSequence[phone.size()]);

		AlertDialog.Builder builder = new AlertDialog.Builder(getParent());
		builder.setTitle("Call ");
		builder.setItems(cs, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {

				Util.call(getParent(), cs[item].toString());

			}
		}).show();
	}

	Bitmap myBitmap;

	public Drawable ImgDrawableFromFile(Resources res, String file_name) {
		File imgFile = new File("/data/data/com.appstart/app_my_sub_dir/"
				+ file_name + ".jpg");
		if (imgFile.exists()) {

			myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			if (myBitmap != null)
				return new BitmapDrawable(res, myBitmap);
			else
				return null;
		}
		return null;

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (myBitmap != null) {
			myBitmap.recycle();
		}

	}

}