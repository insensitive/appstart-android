package com.appstart.async;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.KeyStore;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.appstart.utility.MySSLSocketFactory;
import com.appstart.utility.Util;

public class ImageLoadFromDownload extends AsyncTask<Object, Void, Bitmap> {

	ImageView img_view;
	String img_ttl;

	@Override
	protected Bitmap doInBackground(Object... urls) {

		Bitmap bm = null;
		try {

			img_view = (ImageView) urls[0];
			img_ttl = urls[1].toString();

			System.out.println("Urls........" + String.valueOf(urls[1]));
			
			bm = Util.ImgBitFromFile(img_ttl);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bm;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		try {

			if (result != null) {

				img_view.setImageBitmap(result);

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public Bitmap DownloadFromUrl(String imageURL) {
		Bitmap bm = null;

		try {
			URI url = new URI(imageURL);

			HttpGet httpRequest = null;

			httpRequest = new HttpGet(url);

			HttpClient httpclient = getNewHttpClient();
			HttpResponse response = (HttpResponse) httpclient
					.execute(httpRequest);

			HttpEntity entity = response.getEntity();
			BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
			InputStream instream = bufHttpEntity.getContent();
			bm = BitmapFactory.decodeStream(instream);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return bm;

	}

	public DefaultHttpClient getNewHttpClient() {
		try {

			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);
			SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
			// sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));
			ClientConnectionManager ccm = new ThreadSafeClientConnManager(
					params, registry);
			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			return new DefaultHttpClient();
		}

	}

	public void Store_file_phone(String urlstring, String name) {

		URL url;
		try {
			url = new URL("http://loginv2.appstart.ch/"
					+ urlstring);
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
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
