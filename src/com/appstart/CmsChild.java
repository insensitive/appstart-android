package com.appstart;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appstart.adapter.ListAdapterCms;
import com.appstart.database.DBAdapter;
import com.appstart.tabgroup.TabGroupActivity;
import com.appstart.utility.Constant;
import com.appstart.utility.Util;
import com.google.android.gms.internal.m;

public class CmsChild extends Activity {

	ListView list;
	ListAdapterCms adapter;

	ArrayList<Object> a;
	ArrayList<Object> CmsID;
	ArrayList<Object> thumb_path;
	String layout_background_color;

	Button ib_back;

	Typeface tf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cms);

		Bundle b = getIntent().getExtras();
		String ParentID = b.getString("CmsID");
		boolean showBack = b.getBoolean("showBack");

		if (!showBack) {
			((Button) findViewById(R.id.ib_back_music))
					.setVisibility(View.INVISIBLE);
		}

		System.out.println("Parent id:::" + ParentID);

		setHeaderBackground();

		// ((TextView)findViewById(R.id.txt_cms)).setTypeface(Util.font(this,
		// Constant.FONT_TYPE));

		ib_back = (Button) findViewById(R.id.ib_back_music);
		ib_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				TabGroupActivity parentActivity = (TabGroupActivity) getParent();
				parentActivity.onBackPressed();

			}
		});

		list = (ListView) findViewById(R.id.list_cms);

		a = new ArrayList<Object>();
		CmsID = new ArrayList<Object>();
		thumb_path = new ArrayList<Object>();

		DBAdapter dba = new DBAdapter(this);
		dba.open();

		Cursor c0 = dba.getScreenName("4", Constant.LANGUAGE_ID);

		if (c0.getCount() > 0) {

			((TextView) findViewById(R.id.txt_music)).setText(c0.getString(0));

		}

		Cursor c = dba.getCms(Constant.LANGUAGE_ID, ParentID);

		for (int i = 0; i < c.getCount(); i++) {

			// System.out.println("" + c.getString(1));

			a.add(c.getString(1));
			CmsID.add(c.getString(0));
			thumb_path.add(c.getString(2));

			c.moveToNext();
		}

		try{
		
		
		if (!(c.getCount() > 0)) {

			Cursor c2 = dba.getBackgroundColor("10", Constant.LANGUAGE_ID);

			if (c2.getCount() > 0) {

				if (!c2.getString(0).startsWith("#")) {
					System.out.print(" set background color  default:: ");
					layout_background_color = "#c5c678";
				} else {
					System.out.print(" set background color ");
					((LinearLayout) findViewById(R.id.lay_cd))
							.setBackgroundColor(Color.parseColor(c2
									.getString(0)));
				}

			} else {
				layout_background_color = "#c5c678";
			}


			Cursor cur = dba
					.row_query("select * from tbl_Cms_Details where CmsID='"
							+ ParentID + "'  and LanguageID="
							+ Constant.LANGUAGE_ID + "");


			
			try{
				
			String Webview_data = cur.getString(5);
			String img_url1 = "http://loginv2.appstart.ch";
			String content = " ",cnt=" No Data Found";
			String regExp = "<img.*src=(\"|')(.*?)(\"|').*\\/? />";
			String replacement = "<img src=\""+img_url1+"$2\" />";
			
			if (!Webview_data.equals(null)) {
				
				Webview_data.replaceAll(regExp, replacement);
						
				
				
						System.out.print(Webview_data.replaceAll(regExp, replacement).toString());
				
				/*if (Webview_data.contains("<img")) {

					String row[] = Webview_data.split("<img");

					for (int i = 1; i < row.length; i++) {

						if (row[i].contains(" src=")) {

							String[] find_main = row[i].split(" src=");

							System.out.println("find main[" + i + "]: "
									+ find_main[i]);

							System.out.println(img_url1.length());

							String img_final = img_url1 + find_main[i];

							String strw = img_final.substring(27,
									img_final.length());

							String web_view_content_final = img_url1 + strw;

							System.out.println(" web_view_content_final: "
									+ web_view_content_final);

							aad = web_view_content_final.split("/>");

							imgrt = ("<img src=\"" + aad[0] + " />");

							System.out.println("imgrt: " + imgrt);

						}

					}
				}

					if (aad.length > 0) {

						for (int i = 1; i < aad.length; i++) {

							Webview_data += aad[i];
						}
					}
					
					content = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
							+ "<html><head>"
							+ "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />"
							+ "<head><body>";

					content += imgrt + Webview_data + "</body></html>";

					System.out.print(content);*/
				
				/*else {
					content = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
							+ "<html><head>"
							+ "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />"
							+ "<head><body>";

					content += "image path not valid "+Webview_data + "</body></html>";
				}*/

				Webview_data+=Webview_data;

			} else {
				
				
				
				//content += " ";
				
				content = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
						+ "<html><head>"
						+ "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />"
						+ "<head><body>";

				content += cnt+ "</body></html>";

				System.out.print(content);
				

			}

			((WebView) findViewById(R.id.web_cms)).loadDataWithBaseURL(null,
					content, "text/html", "UTF-8", null);

			((WebView) findViewById(R.id.web_cms)).setBackgroundColor(Color
					.parseColor(layout_background_color));
			
			
			
			}catch(Exception e){
				//Toast.makeText(getParent(), "Data not Found", Toast.LENGTH_SHORT).show();
			}

			((WebView) findViewById(R.id.web_cms))
					.setHorizontalScrollBarEnabled(false);
			WebView mWebView = (WebView) findViewById(R.id.web_cms);
			mWebView.setInitialScale(120);
			mWebView.setScrollContainer(true);
			mWebView.bringToFront();
			mWebView.setScrollbarFadingEnabled(false);
			mWebView.setVerticalScrollBarEnabled(true);
			mWebView.setHorizontalScrollBarEnabled(false);
			mWebView.getSettings().setBuiltInZoomControls(true);
			mWebView.getSettings().setJavaScriptEnabled(false);
			mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);


			((LinearLayout) findViewById(R.id.lay_child_txt_dlt))
					.setVisibility(View.VISIBLE);
		}
		}catch(Exception e){
			e.printStackTrace();
			//Toast.makeText(getParent(), "Error", Toast.LENGTH_SHORT).show();
		}

		dba.close();
		adapter = new ListAdapterCms(this, a, CmsID, thumb_path);
		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				Bundle b = new Bundle();
				b.putString("CmsID", CmsID.get(arg2).toString());
				b.putString("cms_title", a.get(arg2).toString());
				b.putBoolean("showBack", true);
				Intent edit = new Intent(getParent(), CmsChild.class);

				edit.putExtras(b);
				TabGroupActivity parentActivity = (TabGroupActivity) getParent();
				parentActivity.startChildActivity(CmsID.get(arg2).toString(),
						edit);

			}

		});

		list.setDivider(new ColorDrawable(Util.Colorcode()));
		list.setDividerHeight(1);

	}

	public void setHeaderBackground() {

		((RelativeLayout) findViewById(R.id.rel_music))
				.setBackgroundDrawable(Util.HeaderBackground());

	}

	Bitmap myBitmap = null;
	Drawable d = null;

	public Drawable ImgDrawableFromFile(Resources res, String file_name) {

		myBitmap = null;
		File imgFile = new File("/data/data/com.appstart/app_my_sub_dir/"
				+ file_name + ".jpg");
		if (imgFile.exists()) {

			myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

			Toast.makeText(getBaseContext(), imgFile.getAbsolutePath(),
					Toast.LENGTH_LONG).show();

			if (myBitmap != null) {
				d = new BitmapDrawable(res, myBitmap);
				return d;
			} else {
				return null;
			}
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