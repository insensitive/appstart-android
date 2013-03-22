package com.appstart;

import java.util.Locale;

import android.app.TabActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.appstart.adapter.MediaAdapter;
import com.appstart.database.DBAdapter;
import com.appstart.tabgroup.TabGroupActivity;
import com.appstart.tabgroup.TabGroupCms;
import com.appstart.tabgroup.TabGroupCms1;
import com.appstart.tabgroup.TabGroupCms2;
import com.appstart.tabgroup.TabGroupContact;
import com.appstart.tabgroup.TabGroupDocument;
import com.appstart.tabgroup.TabGroupEvents;
import com.appstart.tabgroup.TabGroupHomeWallpaper1;
import com.appstart.tabgroup.TabGroupImageGallery;
import com.appstart.tabgroup.TabGroupImageGallery1;
import com.appstart.tabgroup.TabGroupMore;
import com.appstart.tabgroup.TabGroupMusic;
import com.appstart.tabgroup.TabGroupPushMessage;
import com.appstart.tabgroup.TabGroupSocialMedia;
import com.appstart.tabgroup.TabGroupWebsite;
import com.appstart.tabgroup.TabGroupWebsite1;
import com.appstart.utility.Constant;
import com.appstart.utility.Util;

/**
 * @author www.androidcookers.co.cc {@link www.androidcookers.co.cc}
 * 
 */
public class TabSample extends TabActivity {
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// setTabs();

		MediaAdapter.initView();

		
		try{
			
	
		DBAdapter dba = new DBAdapter(this);
		dba.open();

		String locale = this.getResources().getConfiguration().locale
				.getLanguage();

		System.out.println("Locale Language code::::" + locale);

		Cursor cur_language = dba.getLanguageFromLocale(Locale.getDefault()
				.getLanguage());

		if (cur_language.getCount() > 0) {
			Constant.LANGUAGE_ID = cur_language.getString(0);
		} else {
			Constant.LANGUAGE_ID = dba.getDefaultLanguage();
		}

		// ---set font part---

		try {

			String[] font_data = dba.getConfigration();
			
			
			if(font_data!=null)
			{
			Constant.FONT_TYPE = font_data[0];
			Constant.FONT_COLOR = font_data[1];
			Constant.FONT_SIZE = font_data[2];
			Constant.SPACING = font_data[3];
			Constant.THEME_COLOR = font_data[4];
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		Cursor c1 = dba.getModules(Constant.LANGUAGE_ID);

		dba.close();

		for (int j = 0; j < 3 && j < c1.getCount(); j++) {

			if (c1.getString(0).equalsIgnoreCase("1")) {

				addTab1(c1.getString(2), c1.getString(1),
						TabGroupContact.class, 0);

			} else if (c1.getString(0).equalsIgnoreCase("2")) {

				addTab1(c1.getString(2), c1.getString(1),
						TabGroupHomeWallpaper1.class, 0);

			} else if (c1.getString(0).equalsIgnoreCase("3")) {

				addTab1(c1.getString(2), c1.getString(1),
						TabGroupPushMessage.class, 0);
			} else if (c1.getString(0).equalsIgnoreCase("4")) {

				addTab1(c1.getString(2), c1.getString(1), TabGroupCms.class, 0);
			} else if (c1.getString(0).equalsIgnoreCase("5")) {

				addTab1(c1.getString(2), c1.getString(1), TabGroupEvents.class,
						0);

			} else if (c1.getString(0).equalsIgnoreCase("6")) {

				addTab1(c1.getString(2), c1.getString(1),
						TabGroupDocument.class, 0);

			} else if (c1.getString(0).equalsIgnoreCase("7")) {

				addTab1(c1.getString(2), c1.getString(1),
						TabGroupImageGallery.class, 0);

			} else if (c1.getString(0).equalsIgnoreCase("8")) {
				addTab1(c1.getString(2), c1.getString(1),
						TabGroupWebsite.class, 0);
			} else if (c1.getString(0).equalsIgnoreCase("9")) {

				addTab1(c1.getString(2), c1.getString(1),
						TabGroupSocialMedia.class, 0);

			} else if (c1.getString(0).equalsIgnoreCase("10")) {
				addTab1(c1.getString(2), c1.getString(1), TabGroupCms1.class, 0);

			} else if (c1.getString(0).equalsIgnoreCase("11")) {
				addTab1(c1.getString(2), c1.getString(1), TabGroupCms2.class, 0);
			} else if (c1.getString(0).equalsIgnoreCase("12")) {

				addTab1(c1.getString(2), c1.getString(1),
						TabGroupImageGallery1.class, 0);
			} else if (c1.getString(0).equalsIgnoreCase("13")) {

				addTab1(c1.getString(2), c1.getString(1), TabGroupMusic.class,
						0);

			} else if (c1.getString(0).equalsIgnoreCase("14")) {

				addTab1(c1.getString(2), c1.getString(1),
						TabGroupWebsite1.class, 0);

			}
			c1.moveToNext();
		}
		c1.close();

		addTab1("More", null, TabGroupMore.class, R.drawable.more);

		dba.close();
		}catch(Exception e){
			e.printStackTrace();
			
		}
		

	}

	private void addTab1(String labelId, String image_name, Class<?> c,
			int drawableId) {
		final TabHost tabHost = getTabHost();
		Intent intent = new Intent(this, c);
		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);

		final View tabIndicator = LayoutInflater.from(this).inflate(
				R.layout.tab_indicator, getTabWidget(), false);
		tabIndicator.setId(drawableId);
		tabIndicator.setTag("tab" + labelId);

		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setText(labelId);
		final ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		// icon.setImageResource(drawableId);

		if (image_name != null) {

			icon.setImageBitmap(Util.ImgBitFromFile(image_name));
			// icon.setBackgroundDrawable(Util.ImgDrawableFromFile(getResources(),
			// image_name));

		} else {
			icon.setImageResource(drawableId);
		}

		getTabHost().setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {

				if (tabId.equalsIgnoreCase("tabMore")) {
					// icon.setImageResource(R.drawable.tab_home);

				}

			}
		});

		spec.setIndicator(tabIndicator);
		spec.setContent(intent);

		tabHost.addTab(spec);

		// click on seleccted tab
		int numberOfTabs = tabHost.getTabWidget().getChildCount();
		for (int t = 0; t < numberOfTabs; t++) {
			tabHost.getTabWidget().getChildAt(t)
					.setOnTouchListener(new View.OnTouchListener() {
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							if (event.getAction() == MotionEvent.ACTION_UP) {

								String currentSelectedTag = TabSample.this
										.getTabHost().getCurrentTabTag();
								String currentTag = (String) v.getTag();
								Log.d(this.getClass().getSimpleName(),
										"currentSelectedTag: "
												+ currentSelectedTag
												+ " currentTag: " + currentTag);

								if (currentTag.equalsIgnoreCase("tabMore")) {

									if (Constant.GET_PARENT != null) {
										TabGroupActivity parentActivity = (TabGroupActivity) Constant.GET_PARENT;
										parentActivity.backToFirst();
									}

								}
							}
							return false;
						}
					});

		}

	}

}