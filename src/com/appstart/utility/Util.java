package com.appstart.utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.appstart.database.DBAdapter;

public class Util {

	public static String getRandomFileName() {

		long unixTime = System.currentTimeMillis() / 1000L;
		Random r = new Random();
		int i1 = r.nextInt(80 - 65) + 65;
		return String.valueOf(unixTime) + String.valueOf(i1);

	}

	public static boolean compareDateTime(String Date1, String Date2) {

			
		try {
			
			Calendar prev_cal = Calendar.getInstance();
			Calendar next_cal = Calendar.getInstance();

			String prev_Date = Date1.split(" ")[0].trim();
			String prev_Time = Date1.split(" ")[1].trim();

			String prev_year = prev_Date.split("-")[0].replace("-", "");
			String prev_month = prev_Date.split("-")[1].replace("-", "");
			String prev_day = prev_Date.split("-")[2].replace("-", "");

			String prev_hour = prev_Time.split(":")[0].replace(":", "");
			String prev_minuite = prev_Time.split(":")[1].replace(":", "");
			String prev_second = prev_Time.split(":")[2].replace(":", "");

			prev_cal.set(Calendar.YEAR, Integer.parseInt(prev_year));
			prev_cal.set(Calendar.MONTH, Integer.parseInt(prev_month) - 1);
			prev_cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(prev_day));

			prev_cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(prev_hour));
			prev_cal.set(Calendar.MINUTE, Integer.parseInt(prev_minuite));
			prev_cal.set(Calendar.SECOND, Integer.parseInt(prev_second));
			prev_cal.set(Calendar.MILLISECOND,0);
			
			
			String next_Date = Date2.split(" ")[0].trim();
			String next_Time = Date2.split(" ")[1].trim();

			String next_year = next_Date.split("-")[0].replace("-", "");
			String next_month = next_Date.split("-")[1].replace("-", "");
			String next_day = next_Date.split("-")[2].replace("-", "");

			String next_hour = next_Time.split(":")[0].replace(":", "");
			String next_minuite = next_Time.split(":")[1].replace(":", "");
			String next_second= next_Time.split(":")[2].replace(":", "");
			
			next_cal.set(Calendar.YEAR, Integer.parseInt(next_year));
			next_cal.set(Calendar.MONTH, Integer.parseInt(next_month) - 1);
			next_cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(next_day));
			
			next_cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(next_hour));
			next_cal.set(Calendar.MINUTE, Integer.parseInt(next_minuite));
			next_cal.set(Calendar.SECOND,Integer.parseInt(next_second));
			next_cal.set(Calendar.MILLISECOND,0);
			
			System.out.println("prev date---"+Date1);
			System.out.println("next date---"+Date2);
			
			 
			System.out.println("");
			 
			System.out.println("prev time---"+prev_cal.getTimeInMillis());
			System.out.println("next time---"+next_cal.getTimeInMillis());
			 
			 
			if (next_cal.after(prev_cal)) {

				
				System.out.println("retunring true");
				
				
				 
				 
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}

		return false;
	}

	public static boolean DeleteFile(String file_name) {

		File file = new File("/data/data/com.appstart/app_my_sub_dir/"
				+ file_name + ".jpg");
		boolean deleted = file.delete();

		return deleted;

	}

	public static void Store_file_phone(String urlstring, String name) {

		URL url;
		try {
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

			System.out.println("download sucess of image:::" + name);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}
	

	public static void Store_doc_file_sdcard(String urlstring, String name) {
		URL url;
		String fileName = null;

		try {
		     
         	url = new URL("http://loginv2.appstart.ch/"
					+ urlstring.replace(" ", "%20"));
         	
			String riw[]=urlstring.split("/");
			
			for (int i = 1; i < riw.length; i++) {
				
			if(riw[i].endsWith(".pdf")){
				fileName=riw[i];
			}else if(riw[i].endsWith(".jpg")){
				fileName=riw[i];
			}else if(riw[i].endsWith(".doc")||riw[i].endsWith(".DOC")){
				fileName=riw[i];
			}
					
			}
			
			File saveFolder = new File(Environment
					.getExternalStorageDirectory(), "apps");

			System.out.print(saveFolder.getPath().toString());
	
				saveFolder.mkdir();

			File outputFile = new File(saveFolder, fileName);
		
             long startTime = System.currentTimeMillis();
        
             HttpURLConnection c = (HttpURLConnection) url.openConnection();
             c.setRequestMethod("GET");
             c.setDoOutput(true);
             c.connect();
           
             FileOutputStream f = new FileOutputStream(outputFile);
             InputStream in = c.getInputStream();
             
             Log.e("DownloadManager", "download begining");
             Log.e("DownloadManager", "download url:" + url);
             Log.e("DownloadManager", "downloaded file name:" + outputFile.getName());	
            
             
            byte[] buffer = new byte[1024];
             int len1 = 0;
             while ((len1 = in.read(buffer)) > 0) {
                     f.write(buffer, 0, len1);
             }
             in.close();
             f.close();
          
             
             Log.e("DownloadManager", "download ready in" + ((System.currentTimeMillis() - startTime) / 1000) + " sec");

			System.out.println("--document downloaded--ok--" + fileName);

		} catch (MalformedURLException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		    } catch (ProtocolException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		    } catch (FileNotFoundException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		    } catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		    }
	}
	
	public static void  Store_file_sdcard(String urlstring, String name) {

		URL url;

		String fileName = " ";
		try {
			url = new URL("http://loginv2.appstart.ch/"
					+ urlstring.replace(" ", "%20"));
			HttpURLConnection c = (HttpURLConnection) url.openConnection();
			c.setRequestMethod("GET");
			c.setDoOutput(true);
			c.connect();
			
			File root= Environment.getExternalStorageDirectory();

			File dir = new File(root.getAbsolutePath().toString()+"/appstart/");
			
			if(dir.exists()==true){
				//dir.mkdir();
			}else{
				dir.mkdir();
			}
			
			
			
			if(urlstring.endsWith(".jpg"))
			{
				fileName = name + ".jpg";
			}else if(urlstring.endsWith(".pdf"))
			{
				fileName = name + ".pdf";
			}else if(urlstring.endsWith(".doc"))
			{
				fileName = name + ".doc";
			}
				
	
			File outputFile = new File(dir, fileName);
			FileOutputStream fos = new FileOutputStream(outputFile);

			InputStream is = c.getInputStream();

			byte[] buffer = new byte[1024];
			int len1 = 0;
			while ((len1 = is.read(buffer)) != -1) {
				fos.write(buffer, 0, len1);
			}
			fos.close();
			is.close();

		     System.out.println("--document downloaded--ok--"+fileName);
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
		e.printStackTrace();


		} 
	}

	public static Bitmap ImgBitFromFile(String file_name) {

		File imgFile = new File("/data/data/com.appstart/app_my_sub_dir/"
				+ file_name + ".jpg");
		if (imgFile.exists()) {

			Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
					.getAbsolutePath());
			
		
			return myBitmap;

			// Drawable d = new BitmapDrawable(getResources(),myBitmap);
			// myImage.setBackgroundDrawable(d);

		}
		return null;
	}

	// public static void recycle_bitmap() {
	// if (myBitmap != null) {
	// myBitmap.recycle();
	// myBitmap = null;
	//
	// }
	//
	// }

	// static Bitmap myBitmap = null;
	// static Drawable d = null;
	//
	// public static Drawable ImgDrawableFromFile(Resources res, String
	// file_name) {
	//
	// myBitmap = null;
	// File imgFile = new File("/data/data/com.appstart/app_my_sub_dir/"
	// + file_name + ".jpg");
	// if (imgFile.exists()) {
	//
	// myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
	//
	// if (myBitmap != null) {
	// d = new BitmapDrawable(res, myBitmap);
	// return d;
	// } else {
	// return null;
	// }
	// }
	// return null;
	//
	// }

	// call function //------

	public static void call(Context ctx, String number) {
		try {
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:" + number));
			ctx.startActivity(callIntent);
		} catch (ActivityNotFoundException activityException) {
			Log.e("helloandroid dialing example", "Call failed",
					activityException);
		}
	}

	// public static void website(Context ctx, String website) {
	//
	// if (!website.startsWith("http://") && !website.startsWith("https://"))
	// website = "http://" + website;
	//
	//
	// Intent browserIntent = new Intent(Intent.ACTION_VIEW,
	// Uri.parse(website));
	// ctx.startActivity(browserIntent);
	//
	// }

	public static void SendEmail(Context ctx, String emailadd) {

		final Intent emailIntent = new Intent(
				android.content.Intent.ACTION_SEND);
		emailIntent.setType("plain/text");

		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
				new String[] { emailadd });
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
		ctx.startActivity(Intent.createChooser(emailIntent, "Send mail..."));

	}

	public static void SetLocale(Context context, String language_code) {
		Locale locale = new Locale(language_code);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		context.getResources().updateConfiguration(config,
				context.getResources().getDisplayMetrics());
	}

	public static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			if (files == null) {
				return true;
			}
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}

	public static Bitmap scaledBitmap(Bitmap bmm, int w, int h) {

		int imgWidth = bmm.getWidth();
		int imgHeight = bmm.getHeight();

		float scaleFactor = Math.min(((float) w) / imgWidth, ((float) h)
				/ imgHeight);

		Matrix scale = new Matrix();
		scale.postScale(scaleFactor, scaleFactor);
		final Bitmap scaledImage = Bitmap.createBitmap(bmm, 0, 0, imgWidth,
				imgHeight, scale, false);

		return scaledImage;

	}

	public static Typeface font(Context ctx, String fontname) {

		Typeface tf = null;

		if (fontname.equalsIgnoreCase("Arial")) {
			tf = Typeface.createFromAsset(ctx.getAssets(), "arial.ttf");

		} else if (fontname.equalsIgnoreCase("Helvetica")) {
			tf = Typeface
					.createFromAsset(ctx.getAssets(), "Helvetica neue.ttf");
		} else if (fontname.equalsIgnoreCase("Helvetica New")) {
			tf = Typeface
					.createFromAsset(ctx.getAssets(), "Helvetica neue.ttf");
		} else if (fontname.equalsIgnoreCase("Courier")) {
			tf = Typeface.createFromAsset(ctx.getAssets(), "cour.ttf");
		} else if (fontname.equalsIgnoreCase("Georgia")) {
			tf = Typeface.createFromAsset(ctx.getAssets(), "georgia.ttf");
		} else if (fontname.equalsIgnoreCase("Times New Roman")) {
			tf = Typeface.createFromAsset(ctx.getAssets(), "times.ttf");
		} else if (fontname.equalsIgnoreCase("Trebuchet MS")) {
			tf = Typeface.createFromAsset(ctx.getAssets(), "trebuc.ttf");
		} else if (fontname.equalsIgnoreCase("Verdana")) {
			tf = Typeface.createFromAsset(ctx.getAssets(), "verdana.ttf");
		} else {
			tf = Typeface.DEFAULT;
		}

		return tf;

	}

	public static int Colorcode() {

		if (Constant.THEME_COLOR != null) {

			System.out.println("theme color::::" + Constant.THEME_COLOR);

			if (Constant.THEME_COLOR.equals("")) {
				return Color.parseColor("#000000");
			}

			return Color.parseColor("#" + Constant.THEME_COLOR);
		} else {
			return Color.parseColor("#000000");
		}
	}

	public static int FontColorcode() {

		if (Constant.FONT_COLOR != null) {

			return Color.parseColor("#" + Constant.FONT_COLOR);

		} else {
			return Color.parseColor("#000000");
		}
	}

	public static Drawable HeaderBackground() {

		String colorcode = null;

		if (Constant.THEME_COLOR.equals("")) {
			colorcode = "af968d";
		} else {
			colorcode = Constant.THEME_COLOR;
		}

		// String colorcode="9900CC";

		float[] hsv = new float[3];
		int color = Color.parseColor("#" + colorcode);
		Color.colorToHSV(color, hsv);
		hsv[2] *= 0.8f; // value component
		color = Color.HSVToColor(hsv);

		float[] hsv1 = new float[3];
		int color1 = Color.parseColor("#" + colorcode);
		Color.colorToHSV(color1, hsv1);
		hsv1[2] *= 1.4f; // value component
		color1 = Color.HSVToColor(hsv1);

		GradientDrawable g = new GradientDrawable(Orientation.TOP_BOTTOM,
				new int[] { color1, Color.parseColor("#" + colorcode), color });
		g.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		return g;

	}

	public static String GetResolutionID(int width, int height, Context ctx) {

		DBAdapter dba = new DBAdapter(ctx);
		dba.open();
		Cursor c = dba
				.row_query("select ResolutionID from tbl_Resolution where width='"
						+ width + "' and height='" + height + "'");
		
		if (c.getCount() > 0) {

			String value = c.getString(0);
			c.close();
			dba.close();
			return value;
		}
		else {

			
			double ratio=width/height;
			
			Cursor c1=dba.row_query("select * from tbl_Resolution Where Ratio >="+ratio+" limit 1");
			if(c1.getCount()>0)
			{
				String value = c.getString(0);
				c.close();
				dba.close();
				return value;
				
			}

		}
	
		dba.close();
		
		

		return "4";
	}

	
	
	
	public static Calendar GetCalendar(String date, String time) {

		Calendar cal = null;

		try {
			String row[] = date.split("-");
			String row1[] = time.split(":");

			cal = Calendar.getInstance();

			cal.set(Calendar.DATE, Integer.parseInt(row[2]));
			cal.set(Calendar.MONTH, Integer.parseInt(row[1]) - 1);
			cal.set(Calendar.YEAR, Integer.parseInt(row[0]));

			cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(row1[0]));
			cal.set(Calendar.MINUTE, Integer.parseInt(row1[1]));

			System.out.println("time::::::" + cal.getTime().toString());

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return cal;
	}

	
	
	
}
