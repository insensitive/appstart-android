package com.appstart;

import java.io.File;

import com.appstart.database.DBAdapter;
import com.appstart.tabgroup.TabGroupActivity;
import com.appstart.utility.Constant;
import com.appstart.utility.Util;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MusicDetails extends Activity implements OnClickListener {

	Button btn_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.musicdetails);

		Bundle b = getIntent().getExtras();

		b.getString("Title");
		b.getString("Album");
		b.getString("Artis");
		b.getString("AlbumArt");

		((TextView) findViewById(R.id.txt_ttl_md))
				.setText(b.getString("Title"));
		((TextView) findViewById(R.id.txt_album_name)).setText(b
				.getString("Album"));
		((TextView) findViewById(R.id.txt_artist_name)).setText("- "
				+ b.getString("Artis"));
		
		((ImageView) findViewById(R.id.iv_md_albumart))
				.setBackgroundDrawable(ImgDrawableFromFile(getResources(),
						b.getString("AlbumArt")));
		
		DBAdapter dba=new DBAdapter(getParent());
		
		
/*Cursor c1 = dba.getBackgroundImage("13", Constant.LANGUAGE_ID);
		
		Cursor c2 = dba.getBackgroundColor("13", Constant.LANGUAGE_ID);
		
		
		if(c1.getCount()>0)
		{
		((LinearLayout) findViewById(R.id.lay_md)).setBackgroundDrawable(ImgDrawableFromFile(getResources(), c1.getString(0)));
		}
		
		if(c2.getCount()>0){
			
			((LinearLayout) findViewById(R.id.lay_md)).setBackgroundColor(Color.parseColor(c2.getString(0)));
		}*/
		
		
		
	/*	Cursor c1 = dba.getBackgroundImage("13", Constant.LANGUAGE_ID);

		if (c1.getCount() > 0) {

			((LinearLayout) findViewById(R.id.lay_md))
					.setBackgroundDrawable(ImgDrawableFromFile(
							getResources(), c1.getString(0)));

		}*/
		
		
		 setHeaderBackground();
		
		// btn_back = (Button) findViewById(R.id.ib_back_music);
		// btn_back.setOnClickListener(this);
		//
		// ((TextView) findViewById(R.id.txt_music))
		// .setText(R.string.musicdetails);

	}

	public void setHeaderBackground() {

		((RelativeLayout) findViewById(R.id.rel_music))
				.setBackgroundDrawable(Util.HeaderBackground());

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		if (arg0 == btn_back) {

			TabGroupActivity parentActivity = (TabGroupActivity) getParent();
			parentActivity.onBackPressed();

		}

	}

	Bitmap myBitmap = null;
	Drawable d = null;

	public Drawable ImgDrawableFromFile(Resources res, String file_name) {

		myBitmap = null;
		System.out.print("\n call ImgeDrawable in musicDetails");
		
		File imgFile = new File("/data/data/com.appstart/app_my_sub_dir/"
				+ file_name + ".jpg");
		if (imgFile.exists()) {

			myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

			if (myBitmap != null) {
				d = new BitmapDrawable(res, myBitmap);
				return d;
			} else {
				return null;
			}
		}
		return null;

	}

}
