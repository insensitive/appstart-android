package com.appstart;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.apache.http.util.ByteArrayBuffer;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appstart.application.Appstartapplication;
import com.appstart.async.DocumentDownload;
import com.appstart.async.ImageDownload;
import com.appstart.database.DBAdapter;
import com.appstart.tabgroup.TabGroupActivity;
import com.appstart.utility.AlertMessages;
import com.appstart.utility.Constant;
import com.appstart.utility.Util;

public class DocumentDetails extends Activity {

	Button ib_back;
	
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    public static final int DIALOG_DOWNLOAD_COMPLETE = 1;
    ProgressDialog mProgressDialog;
    
	ProgressDialog pd2;
	String download_file_name = " ", url = " ", file_name = " ";
	AlertMessages messages;
	Appstartapplication app;
	String back_ground_color="#c5c678";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.documentdetails);

		  
		app = new Appstartapplication();
	

		messages = new AlertMessages(getParent());
		Bundle b = getIntent().getExtras();
		String documentid = b.getString("DocumentID");
		boolean showback=b.getBoolean("showBack");
	

		ib_back = (Button) findViewById(R.id.ib_back_music);
		
		

		if (showback) {
			ib_back.setVisibility(View.VISIBLE);
		}
		
		else{
			ib_back.setVisibility(View.INVISIBLE);
		}
		
		ib_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				TabGroupActivity parentActivity = (TabGroupActivity) getParent();
				parentActivity.onBackPressed();

			}
		});

		setHeaderBackground();

		DBAdapter dba = new DBAdapter(this);
		dba.open();

		Cursor cwe = dba.getScreenName("6", Constant.LANGUAGE_ID);

		if (cwe.getCount() > 0) {

			((TextView) findViewById(R.id.txt_music)).setText(cwe.getString(0));
		}
		
try{
		Cursor c1 = dba.getBackgroundImage("6", Constant.LANGUAGE_ID);
		
		Cursor c2 = dba.getBackgroundColor("6", Constant.LANGUAGE_ID);
		
		
		if(c1.getCount()>0)
		{
			System.out.print(" set background image: " + c1.getString(0));
		((LinearLayout) findViewById(R.id.lay_dd)).setBackgroundDrawable(ImgDrawableFromFile(getResources(), c1.getString(0)));
		}
		
		if(c2.getCount()>0){
			
			
			if((c2.getString(0).equals("")) ||(c2.getString(0).equals(null))){
				System.out.print(" set background color: ");
				((LinearLayout) findViewById(R.id.lay_dd)).setBackgroundColor(Color
						.parseColor(back_ground_color));
				
			}else{
				
				if(!c2.getString(0).startsWith("#")){
					System.out.print(" set background color default:: ");
					((LinearLayout) findViewById(R.id.lay_dd)).setBackgroundColor(Color
							.parseColor(back_ground_color));
				}else{
					System.out.print(" set background color  ");
					((LinearLayout) findViewById(R.id.lay_dd)).setBackgroundColor(Color
							.parseColor(c2.getString(0)));
			}
			
			
			}
			
			
		}else
		{
				System.out.print(" set background color default: ");
				((LinearLayout) findViewById(R.id.lay_dd)).setBackgroundColor(Color
						.parseColor(back_ground_color));
			
			
		}
		
		
}catch(Exception e){
	e.printStackTrace();
	//Toast.makeText(getParent(), "Error", Toast.LENGTH_SHORT).show();
}
	
		
		
		final Cursor c = dba
				.row_query("select * from tbl_Document_Details where DocumentID='"
						+ documentid
						+ "' and LanguageID='"
						+ Constant.LANGUAGE_ID + "'");

		if (c.getCount() > 0) {

			((TextView) findViewById(R.id.txt_ttl_dd)).setText(c.getString(3));
			((TextView) findViewById(R.id.txt_keyword_docvalue)).setText(c
					.getString(6));
			((TextView) findViewById(R.id.txt_file_type_doc)).setText(c
					.getString(7));
			((TextView) findViewById(R.id.txt_file_size_doc)).setText(c
					.getString(8));

			((Button) findViewById(R.id.btn_download_doc))
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
					
							download_file_name=c.getString(5);
							
							try
							{
							if((c.getString(5).equals(null)) || (c.getString(3).equals(null)) ||
									(c.getString(5).equals("")) || (c.getString(3).equals(""))
									
									){
								
								messages.showCutomMessage("Document path empty");
								
							}else{
								
								String urlstr="http://loginv2.appstart.ch/"
										+ c.getString(5).replace(" ", "%20");
		
								
								String a[] = new String[2];
								a[0] = urlstr;
								a[1] = c.getString(5);
								
								 new DownloadFileAsync().execute(a);
												 
								
							}
							}catch(Exception e){
								e.printStackTrace();
								messages.showCutomMessage("Document path empty");
							}

						}

					});

			
			/*((Button) findViewById(R.id.btn_open_doc))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
			

							if ((c.getString(5).endsWith(".pdf")) || 
							(c.getString(5).endsWith(".doc"))||c.getString(5).endsWith(".DOC")){
								try {
						
											
									String fname=null;
									String riw[]=c.getString(5).split("/");
									
									for (int i = 1; i < riw.length; i++) {
										
									if(riw[i].endsWith(".pdf")){
										fname=riw[i];
									}else if(riw[i].endsWith(".jpg") ||riw[i].endsWith(".JPG")){
										fname=riw[i];
									}else if(riw[i].endsWith(".doc")||riw[i].endsWith(".DOC")){
										fname=riw[i];
									}
									}
									
									
							 File file = new File(Environment.getExternalStorageDirectory().toString()+"/apps/"+fname);
									
							 
							 if(file.exists() && file.length()!=0){
								 Intent intent = new Intent();
														
										
									intent.setClassName("com.adobe.reader", "com.adobe.reader.AdobeReader");
									
									intent.setAction(android.content.Intent.ACTION_VIEW);
									intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									
									 Uri uri = Uri.fromFile(file);
					                 intent.setDataAndType(uri, "application/pdf");
					                 
										try {
											startActivity(intent);
										} catch (Exception e) {
											
											AlertDialog.Builder builder = new AlertDialog.Builder(
													getParent());
											builder.setTitle("No Application Found");
											builder.setMessage("Download Application from Android Market?");
											builder.setPositiveButton(
													"Yes, Please",
													new DialogInterface.OnClickListener() {
														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															Intent marketIntent = new Intent(
																	Intent.ACTION_VIEW);
															marketIntent.setData(Uri
																	.parse("market://details?id=com.adobe.reader"));
															startActivity(marketIntent);
														}
													});
											builder.setNegativeButton("No, Thanks",
													null);
											builder.create().show();
										}
							 }else{
								 messages.showCutomMessage("Document not downloded properly");
							 }
							 
								} catch (Exception e) {
									e.printStackTrace();
								}
							} 
							
							
							else if (c.getString(5).endsWith(".jpg")) {

								
								Bundle b = new Bundle();
								String url="http://loginv2.appstart.ch/"+ c.getString(5);
								

								Intent edit = new Intent(getParent(), Webview.class);
								
								b.putString("website_url",url);
								b.putString("title", "Document");
								b.putBoolean("showBack", true);
								
								edit.putExtras(b);
								TabGroupActivity parentActivity = (TabGroupActivity) getParent();
								parentActivity.startChildActivity("Webview", edit);
								
								Intent intent = new Intent();
								intent.setAction(android.content.Intent.ACTION_VIEW);
								intent.setDataAndType(
										Uri.parse("http://203.88.158.98/appstart_live/public/"
												+ c.getString(5)), "image/jpeg");

								startActivity(intent);

							}

						}
					});*/

		}

	}
	 @Override
	    protected Dialog onCreateDialog(int id) {
	        switch (id) {
			case DIALOG_DOWNLOAD_PROGRESS:
				mProgressDialog = new ProgressDialog(getParent());
				mProgressDialog.setMessage("Downloading file..");
				mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				mProgressDialog.setCancelable(false);
				mProgressDialog.show();
				return mProgressDialog;
				
			case DIALOG_DOWNLOAD_COMPLETE:
				mProgressDialog.dismiss();
				AlertDialog.Builder dia = new Builder(getParent());
				dia.setTitle("Open");
				dia.setMessage("Do you want to open this document?");
				dia.setIcon(android.R.drawable.ic_menu_agenda);
				dia.setCancelable(true);
				dia.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						if ((download_file_name.endsWith(".pdf")) || (download_file_name.endsWith(".PDF")) ||
						(download_file_name.endsWith(".doc"))||(download_file_name.endsWith(".DOC"))){
							try {
					
										
								String fname=null;
								String riw[]=download_file_name.split("/");
								
								for (int i = 1; i < riw.length; i++) {
									
								if((riw[i].endsWith(".pdf"))||(riw[i].endsWith(".PDF"))){
									fname=riw[i];
								}else if((riw[i].endsWith(".doc"))||(riw[i].endsWith(".DOC"))){
									fname=riw[i];
								}
								}
								
								
						 File file = new File(Environment.getExternalStorageDirectory().toString()+"/apps/"+fname);
								
						 
						 if(file.exists() && file.length()!=0){
							 Intent intent = new Intent();
													
									
								intent.setClassName("com.adobe.reader", "com.adobe.reader.AdobeReader");
								
								intent.setAction(android.content.Intent.ACTION_VIEW);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								
								 Uri uri = Uri.fromFile(file);
				                 intent.setDataAndType(uri, "application/pdf");
				                 
									try {
										startActivity(intent);
									} catch (Exception e) {
										
										AlertDialog.Builder builder = new AlertDialog.Builder(
												getParent());
										builder.setTitle("No Application Found");
										builder.setMessage("Download Application from Android Market?");
										builder.setPositiveButton(
												"Yes, Please",
												new DialogInterface.OnClickListener() {
													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {
														Intent marketIntent = new Intent(
																Intent.ACTION_VIEW);
														marketIntent.setData(Uri
																.parse("market://details?id=com.adobe.reader"));
														startActivity(marketIntent);
													}
												});
										builder.setNegativeButton("No, Thanks",
												null);
										builder.create().show();
									}
						 }else{
							 messages.showCutomMessage("Document not downloded properly");
						 }
						 
							} catch (Exception e) {
								e.printStackTrace();
							}
						} 
						
						else if (download_file_name.endsWith(".jpg")||download_file_name.endsWith(".JPG")) {

							
							String fname=null;
							String riw[]=download_file_name.split("/");
							
							for (int i = 1; i < riw.length; i++) {
								
							if(riw[i].endsWith(".jpg")||download_file_name.endsWith(".JPG")){
								fname=riw[i];
							}
							}
							
							
					 File file = new File(Environment.getExternalStorageDirectory().toString()+"/apps/"+fname);
							
					System.out.print(file.toString());					
					 
							/*Bundle b = new Bundle();
							String url="http://loginv2.appstart.ch/"+ download_file_name;
							

							Intent edit = new Intent(getParent(), Webview.class);
							
							b.putString("website_url",url);
							b.putString("title", "Document");
							b.putBoolean("showBack", true);
							
							edit.putExtras(b);
							TabGroupActivity parentActivity = (TabGroupActivity) getParent();
							parentActivity.startChildActivity("Webview", edit);*/
							
							Intent intent = new Intent();
							intent.setAction(android.content.Intent.ACTION_VIEW);
							intent.setDataAndType(
									Uri.fromFile(file), "image/jpeg");

							startActivity(intent);

						}

						
						
					}
				});
				
				dia.setNegativeButton("No", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

				AlertDialog alrt = dia.create();
				alrt.show();
				
				return  alrt;
			default:
				return null;
	        }
	 }


	class DownloadFileAsync extends AsyncTask<String, String, String> {
		   
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(DIALOG_DOWNLOAD_PROGRESS);
		}

		@Override
		protected String doInBackground(String... aurl) {
			int count;

		try {

		URL url = new URL(aurl[0]);
		URLConnection conexion = url.openConnection();
		conexion.connect();

		int lenghtOfFile = conexion.getContentLength();
		Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);
		
		String urlstring=aurl[1]; 
		String fileName=""; 
		
		String riw[]=urlstring.split("/");
		
		for (int i = 1; i < riw.length; i++) {
			
		if((riw[i].endsWith(".pdf"))||(riw[i].endsWith(".PDF"))){
			fileName=riw[i];
		}else if((riw[i].endsWith(".jpg"))||(riw[i].endsWith(".JPG"))){
			fileName=riw[i];
		}else if((riw[i].endsWith(".doc"))||(riw[i].endsWith(".DOC"))){
			fileName=riw[i];
		}
				
		}
		
		File saveFolder = new File(Environment
				.getExternalStorageDirectory(), "apps");

		System.out.print(saveFolder.getPath().toString());

			saveFolder.mkdir();

			File outputFile = new File(saveFolder, fileName);

		InputStream input = new BufferedInputStream(url.openStream());
		OutputStream output = new FileOutputStream(outputFile);

		byte data[] = new byte[1024];

		long total = 0;

			while ((count = input.read(data)) != -1) {
				total += count;
				publishProgress(""+(int)((total*100)/lenghtOfFile));
				output.write(data, 0, count);
			}

			output.flush();
			output.close();
			input.close();
		} catch (Exception e) {}
		return null;

		}
		protected void onProgressUpdate(String... progress) {
			 Log.d("ANDRO_ASYNC",progress[0]);
			 mProgressDialog.setProgress(Integer.parseInt(progress[0]));
		}

		@Override
		protected void onPostExecute(String unused) {
			//dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
			showDialog(DIALOG_DOWNLOAD_COMPLETE);
		}
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