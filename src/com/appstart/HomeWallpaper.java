package com.appstart;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.Toast;

import com.appstart.database.DBAdapter;
import com.appstart.utility.Constant;
import com.appstart.utility.HorizontalPager;
import com.appstart.utility.HorizontalPager.OnScreenSwitchListener;

public class HomeWallpaper extends Activity implements Runnable {
	
	Drawable d[];
	
	HorizontalPager realViewSwitcher;
	int i = 0;
	Bitmap bmap[];
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		
		Thread thread = new Thread(HomeWallpaper.this);
		thread.start();
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		for (int i = 0; i < d.length; i++) {
			
			if(d[i]!=null)
			{
			d[i].setCallback(null);
			}
			
			if (bmap[i] != null) {
				bmap[i].recycle();
			}
		}

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		try {
			DBAdapter dba = new DBAdapter(this);
			dba.open();
			
			Cursor cur = dba
					.row_query("select tbl_Module_Home_Wallpaper_Details.ImagePathAndroid from tbl_Home_Wallpaper,tbl_Module_Home_Wallpaper_Details where tbl_Home_Wallpaper.WallpaperID = tbl_Module_Home_Wallpaper_Details.WallpaperID AND tbl_Module_Home_Wallpaper_Details.LanguageID='"
							+ Constant.LANGUAGE_ID
							+ "' and tbl_Home_Wallpaper.isHomeWallpaper=1 and tbl_Home_Wallpaper.Status=1 order by tbl_Home_Wallpaper.Ord");
			
			d = null;
			d = new Drawable[cur.getCount()];
			bmap = new Bitmap[cur.getCount()];

			if (cur.getCount() > 0) {
				for (int i = 0; i < cur.getCount(); i++) {
					
					
					try {

						File imgFile = new File(
								"/data/data/com.appstart/app_my_sub_dir/"
										+ cur.getString(0) + ".jpg");
						if (imgFile.exists()) {

							bmap[i] = BitmapFactory.decodeFile(imgFile
									.getAbsolutePath());

							d[i] = new BitmapDrawable(getResources(), bmap[i]);

						}

					} catch (OutOfMemoryError e) {
						e.printStackTrace();

						System.gc();

						try {
							File imgFile = new File(
									"/data/data/com.appstart/app_my_sub_dir/"
											+ cur.getString(0) + ".jpg");
							if (imgFile.exists()) {
								
								bmap[i] = BitmapFactory.decodeFile(imgFile
										.getAbsolutePath());
								
								d[i] = new BitmapDrawable(getResources(), bmap[i]);
							
							}

						} catch (OutOfMemoryError e2) {
							e2.printStackTrace();
							// handle gracefully.
						}
					}

					//

					cur.moveToNext();

				}

			}

			dba.close();
			handler.sendEmptyMessage(0);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (Constant.ISLANGUAGECHANGE_HOMEWALL) {
			setContentView(R.layout.activity_main);
			if (realViewSwitcher != null) {
				realViewSwitcher.removeAllViews();

				// Util.recycle_bitmap();

				Thread thread = new Thread(HomeWallpaper.this);
				thread.start();

			}
			Constant.ISLANGUAGECHANGE_HOMEWALL = false;
		}

	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			if (msg.what == 0) {
				
				try {

					realViewSwitcher = new HorizontalPager(HomeWallpaper.this);

					// Add some views to it
					final int[] backgroundColors = { Color.RED, Color.BLUE,
							Color.CYAN, Color.GREEN, Color.YELLOW };
					for (int i = 0; i < d.length; i++) {

						ImageView ImgView = new ImageView(
								getApplicationContext());

						// TextView textView = new TextView(
						// getApplicationContext());
						// textView.setText("No image from server");
						// textView.setTextSize(100);
						// textView.setTextColor(Color.BLACK);
						// textView.setGravity(Gravity.CENTER);
						// textView.setBackgroundColor(Color.RED);

						//

						// ImageView myImage = (ImageView)
						// findViewById(R.id.iv_back);
						// myImage.setImageBitmap(Util.ImgBitFromFile(cur.getString(0)));

						// Drawable d = new
						// BitmapDrawable(getResources(),myBitmap);
						if (d[i] != null) {
							ImgView.setBackgroundDrawable(d[i]);
							realViewSwitcher.addView(ImgView);

						}

					}

					// set as content view
					if (realViewSwitcher.getChildCount() > 0) {

						setContentView(realViewSwitcher);

						realViewSwitcher
								.setOnScreenSwitchListener(new OnScreenSwitchListener() {

									@Override
									public void onScreenSwitched(int screen) {
										// TODO Auto-generated method stub

										i = screen;

									}
								});

						Timer myTimer = new Timer();
						myTimer.schedule(new TimerTask() {
							@Override
							public void run() {

								i++;
								if (i < realViewSwitcher.getChildCount()) {

								} else {
									i = 0;
								}

								handler1.sendEmptyMessage(0);

							}

						}, 0, 5000);

					}

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			} else {

			}

		}
	};

	private Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {

			if (msg.what == 0) {

				try {

					realViewSwitcher.setCurrentScreen(i, true);

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			} else {

			}

		}
	};
	
	
	//---
	

}