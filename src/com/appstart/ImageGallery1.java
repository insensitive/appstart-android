package com.appstart;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.appstart.coverflow.CoverAdapterView;
import com.appstart.coverflow.CoverAdapterView.OnItemClickListener;
import com.appstart.coverflow.CoverFlow;
import com.appstart.database.DBAdapter;
import com.appstart.tabgroup.TabGroupActivity;
import com.appstart.utility.Constant;
import com.appstart.utility.Util;

public class ImageGallery1 extends Activity {

	ArrayList<String> image_name;
	CoverFlow coverFlow;
	ArrayList<Bitmap> img_bitmap;
	String back_ground_color="#101010";
	ArrayList<String> img_name;

	boolean isGrid = true;

	Button btn_back;
	public static ArrayList<String> GALLERY_IMAGENAME;
	public static int index;
	float density = 1;
	String cat_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imagegallery);

		setHeaderBackground();

		Bundle b = getIntent().getExtras();
		cat_id = b.getString("cat_id");
		boolean showback=b.getBoolean("showBack");
		String title = b.getString("cat_name");
		
		
		btn_back = (Button) findViewById(R.id.ib_back_ig);
		
		if(showback){
			btn_back.setVisibility(View.VISIBLE);
		}else{
			btn_back.setVisibility(View.INVISIBLE);
		}

		TextView tv = (TextView) findViewById(R.id.txt_ig);
		tv.setText("Image Gallery");

		System.out.println("category id::::" + cat_id);

		// ((TextView)findViewById(R.id.txt_ig)).setTypeface(Util.font(this,
		// Constant.FONT_TYPE));

		// For support diffrent size and density device////
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		// (metrics.densityDpi/160);

		switch (metrics.densityDpi) {
		case DisplayMetrics.DENSITY_LOW:
			density = 0.7625f;
			System.out.println("Low Density");
			break;

		case DisplayMetrics.DENSITY_MEDIUM:
			density = 1;
			System.out.println("Medium Density");
			break;
		case DisplayMetrics.DENSITY_HIGH:
			density = 1.333333333f;
			System.out.println("High Density");
			break;
		}

		GALLERY_IMAGENAME = new ArrayList<String>();

		image_name = new ArrayList<String>();
		img_bitmap = new ArrayList<Bitmap>();
		img_name = new ArrayList<String>();

		btn_back = (Button) findViewById(R.id.ib_back_ig);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				TabGroupActivity parentActivity = (TabGroupActivity) getParent();
				parentActivity.onBackPressed();

			}
		});

		
		
			
		
		DBAdapter dba = new DBAdapter(this);
		dba.open();
		
		try{
		//Cursor c1 = dba.getBackgroundImage("12", Constant.LANGUAGE_ID);

		
		Cursor c2 = dba.getBackgroundColor("12", Constant.LANGUAGE_ID);

		if(c2.getCount()>0){
			
			if((c2.getString(0).equals(" ")) ||(c2.getString(0).equals(null))){
				System.out.print(" set background color: ");
				((LinearLayout) findViewById(R.id.lay_id)).setBackgroundColor(Color
						.parseColor(back_ground_color));
				
			}else{
				if(!c2.getString(0).startsWith("#")){
					System.out.print(" set background color: ");
					((LinearLayout) findViewById(R.id.lay_id)).setBackgroundColor(Color
							.parseColor(back_ground_color));
				}else{
				
				
					System.out.print(" set background color default: ");
					((LinearLayout) findViewById(R.id.lay_id)).setBackgroundColor(Color
							.parseColor(c2.getString(0)));
			}
			}
			
			
			
		}
		else{
			System.out.print(" set background color default: ");
			((LinearLayout) findViewById(R.id.lay_id)).setBackgroundColor(Color
					.parseColor(back_ground_color));
		}

		
	}catch(Exception e){
		e.printStackTrace();
		//Toast.makeText(getParent(), "Error", Toast.LENGTH_SHORT).show();
	}
		Cursor c = dba.getImageGallery1(cat_id, Constant.LANGUAGE_ID);

		for (int i = 0; i < c.getCount(); i++) {

			c.getString(1);

			System.out.println("title::::" + c.getString(3));
			image_name.add(c.getString(3));
			img_bitmap.add(Util.ImgBitFromFile(c.getString(3)));
			img_name.add(c.getString(5));

			c.moveToNext();

		}

		dba.close();

		GridView gridview = (GridView) findViewById(R.id.grid_view);
		gridview.setAdapter(new ImageAdapter2(this));

		// ---
		coverFlow = (CoverFlow) findViewById(R.id.coverflowreflect);

		coverFlow.setAdapter(new ImageAdapter3(this));

		ImageAdapter3 coverImageAdapter = new ImageAdapter3(this);

		coverImageAdapter.createReflectedImages();

		coverFlow.setAdapter(coverImageAdapter);

		coverFlow.setSpacing(-30);
		coverFlow.setSelection(1, true);

		coverFlow.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(CoverAdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(ImageGallery1.this,
						com.appstart.ghlab.ghgallery.GHGalleryDetail1.class);

				String url[] = { img_name.get(position) };

				GALLERY_IMAGENAME = img_name;
				index = position;

				intent.putExtra("position", position);
				// intent.putExtra("arr_url", url);

				startActivityForResult(intent, 1);

			}
		});

		((Button) findViewById(R.id.btn_altview))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub

						if (isGrid) {

							((GridView) findViewById(R.id.grid_view))
									.setVisibility(View.GONE);
							((LinearLayout) findViewById(R.id.lay_cover))
									.setVisibility(View.VISIBLE);
							isGrid = false;
						} else {
							((GridView) findViewById(R.id.grid_view))
									.setVisibility(View.VISIBLE);
							((LinearLayout) findViewById(R.id.lay_cover))
									.setVisibility(View.GONE);
							isGrid = true;
						}

					}
				});

		// Gallery ga = (Gallery) findViewById(R.id.Gallery01);
		// ga.setAdapter(new ImageAdapter2(this));

	}

	// ---

	public class ImageAdapter2 extends BaseAdapter {
		private Context context;

		public ImageAdapter2(Context c) {
			context = c;
		}

		// ---returns the number of images---
		public int getCount() {
			return image_name.size();
		}

		// ---returns the ID of an item---
		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		int density = 1;

		// ---returns an ImageView view---
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ImageView imageView;
			if (convertView == null) {

				WindowManager mWinMgr = (WindowManager) context
						.getSystemService(Context.WINDOW_SERVICE);
				int displayWidth = mWinMgr.getDefaultDisplay().getWidth();
				int displayHeight = mWinMgr.getDefaultDisplay().getHeight();

				imageView = new ImageView(context);
				imageView.setLayoutParams(new GridView.LayoutParams(
						LayoutParams.WRAP_CONTENT, (int) (displayWidth / 3)));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setPadding(5, 5, 5, 5);
			} else {
				imageView = (ImageView) convertView;
			}

			// try {
			// imageView.setBackgroundDrawable(Util.ImgDrawableFromFile(
			// getResources(), image_name.get(position)));
			// } catch (Exception e) {
			//
			// }

			imageView.setImageBitmap(img_bitmap.get(position));

			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					Intent intent = new Intent(ImageGallery1.this,
							com.appstart.ghlab.ghgallery.GHGalleryDetail1.class);

					String url[] = { img_name.get(position) };

					GALLERY_IMAGENAME = img_name;
					index = position;

					// intent.putExtra("position", 0);
					// intent.putExtra("arr_url", url);

					startActivityForResult(intent, 1);

				}
			});

			return imageView;
		}

	}

	// ---adapter for cover---

	public class ImageAdapter3 extends BaseAdapter {
		int mGalleryItemBackground;
		private Context mContext;

		private FileInputStream fis;

		// private Integer[] mImageIds = { R.drawable.bar1, R.drawable.bar2,
		// R.drawable.bar3, R.drawable.bar4, R.drawable.bar5 };

		private ImageView[] mImages;

		public ImageAdapter3(Context c) {
			mContext = c;
			mImages = new ImageView[img_bitmap.size()];
		}

		public boolean createReflectedImages() {
			// The gap we want between the reflection and the original image
			final int reflectionGap = (int) (4 * 1.5);

			int index = 0;

			for (Bitmap imageId : img_bitmap) {
				Bitmap originalImage = imageId;

				int width = originalImage.getWidth();
				int height = originalImage.getHeight();

				// This will not scale but will flip on the Y axis
				Matrix matrix = new Matrix();
				matrix.preScale(1, -1);

				// Create a Bitmap with the flip matrix applied to it.
				// We only want the bottom half of the image
				Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
						height / 2, width, height / 2, matrix, false);

				// Create a new bitmap with same width but taller to fit
				// reflection
				Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
						(height + height / 3), Config.ARGB_8888);

				// Create a new Canvas with the bitmap that's big enough for
				// the image plus gap plus reflection
				Canvas canvas = new Canvas(bitmapWithReflection);
				// Draw in the original image
				canvas.drawBitmap(originalImage, 0, 0, null);
				// Draw in the gap
				Paint deafaultPaint = new Paint();
				canvas.drawRect(0, height, width, height + reflectionGap,
						deafaultPaint);
				// Draw in the reflection
				canvas.drawBitmap(reflectionImage, 0, height + reflectionGap,
						null);

				// Create a shader that is a linear gradient that covers the
				// reflection
				Paint paint = new Paint();

				LinearGradient shader = new LinearGradient(0,
						originalImage.getHeight(), 0,
						bitmapWithReflection.getHeight() + reflectionGap,
						0x70ffffff, 0x00ffffff, TileMode.MIRROR);
				// Set the paint to use this shader (linear gradient)
				paint.setShader(shader);
				// Set the Transfer mode to be porter duff and destination in
				paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));

				// paint.setAlpha(0);
				// paint.setColor(Color.TRANSPARENT);
				// paint.setMaskFilter(new BlurMaskFilter(15, Blur.OUTER));
				// Draw a rectangle using the paint with our linear gradient
				canvas.drawRect(0, height, width,
						bitmapWithReflection.getHeight() + reflectionGap, paint);

				ImageView imageView = new ImageView(mContext);
				imageView.setImageBitmap(bitmapWithReflection);
				imageView.setLayoutParams(new CoverFlow.LayoutParams(
						(int) (170 * density), (int) (250 * density)));
				imageView.setScaleType(ScaleType.MATRIX);
				mImages[index++] = imageView;

			}
			return true;
		}

		public int getCount() {
			return img_bitmap.size();
		}

		public Object getItem(int position) {

			// Log.e("", "Position = " + position);
			return position;
		}

		public long getItemId(int position) {

			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			// Use this code if you want to load from resources
			// ImageView i = new ImageView(mContext);
			// i.setImageResource(mImageIds[position]);
			// i.setLayoutParams(new CoverFlow.LayoutParams(130, 130));
			// i.setScaleType(ImageView.ScaleType.MATRIX);
			// return i;

			// sb.setProgress(position);

			System.out.println("postion :::::" + position);
			return mImages[position];

		}

		/**
		 * Returns the size (0.0f to 1.0f) of the views depending on the
		 * 'offset' to the center.
		 */
		public float getScale(boolean focused, int offset) {
			/* Formula: 1 / (2 ^ offset) */
			return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
		}

	}

	public void setHeaderBackground() {

		((RelativeLayout) findViewById(R.id.rel_ig)).setBackgroundDrawable(Util
				.HeaderBackground());

	}

	static Bitmap myBitmap = null;
	static Drawable d = null;

	public static Drawable ImgDrawableFromFile(Resources res, String file_name) {

		myBitmap = null;
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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		for (int i = 0; i < img_bitmap.size(); i++) {

			if (img_bitmap.get(i) != null) {
				img_bitmap.get(i).recycle();
			}

		}

		System.out.println("Images recycled");

	}

}
