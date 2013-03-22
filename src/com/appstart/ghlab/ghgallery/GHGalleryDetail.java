package com.appstart.ghlab.ghgallery;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.appstart.ImageGallery;
import com.appstart.R;
import com.appstart.utility.Util;

public class GHGalleryDetail extends Activity {

	private GHGallery mGallery;

	// String url[];
	// int position;
	RelativeLayout layout;

	private final ImageDownloader imageDownloader = new ImageDownloader();

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		return mGallery.onGalleryTouchEvent(event);

	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		// position = intent.getIntExtra("position", 0);
		// url = intent.getStringArrayExtra("arr_url");
		// setRequestedOrientation(1);

		setContentView(R.layout.gallery_detail);

		mGallery = new GHGallery(this, R.id.iv);
		mGallery.setPaddingWidth(5);

		mGallery.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
				android.R.layout.simple_list_item_1,
				ImageGallery.GALLERY_IMAGENAME) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				if (convertView == null) {
					LayoutInflater inflater = getLayoutInflater();
					convertView = inflater.inflate(R.layout.image_detail,
							parent, false);
				}

				ImageView iv = (ImageView) convertView.findViewById(R.id.iv);

				// imageDownloader.download(url[position], iv);

				iv.setImageBitmap(Util
						.ImgBitFromFile(ImageGallery.GALLERY_IMAGENAME
								.get(position)));

				return convertView;
			}
		}, ImageGallery.index);

		layout = (RelativeLayout) findViewById(R.id.gallery_detail_layout);// new
																			// RelativeLayout(getApplicationContext());

		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);

		layoutParams.setMargins(0, 0, 0, 0);

		layout.addView(mGallery, 0, layoutParams);
		
		// mGallery.setIsGalleryCircular(false);
		
		// Button backbtn = (Button)findViewById(R.id.previous_button);//new
		// Button(this);
		//
		// Button nextbtn = (Button)findViewById(R.id.next_button);//new
		// Button(this);
		
		// nextbtn.setOnClickListener(new View.OnClickListener() {
		// public void onClick(View v) {
		// mGallery.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
		// }
		// });
		//
		// backbtn.setOnClickListener(new View.OnClickListener() {
		// public void onClick(View v) {
		// mGallery.onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, null);
		// }
		// });
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		
		
	}

}