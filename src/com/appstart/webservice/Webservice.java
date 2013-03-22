package com.appstart.webservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import android.util.Log;
import com.appstart.utility.Constant;

public class Webservice {

	/* the get web service for search */

	public static String GetLogin(String username, String password) {

		HttpClient httpclient = new DefaultHttpClient();
		String response = null;
		URI url;
		try {

			String s = Constant.main_url
					+ "rest/service/authenticate/app_access_id/" + username
					+ "/password/" + password;

			url = new URI(s.replace(" ", "%20"));

			Log.e("my webservice", "My webservice : " + url);

			HttpGet httpget = new HttpGet(url);
			HttpResponse httpResponse = null;

			try {

				// List<NameValuePair> nameValuePairs = new
				// ArrayList<NameValuePair>(
				// 3);

				// httpget.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				httpResponse = httpclient.execute(httpget);

				response = EntityUtils.toString(httpResponse.getEntity(),
						"UTF-8");

				// this is what we extended for the getting the response string
				// which we going to parese for out use in database //

				System.out
						.println("get my local centre response : " + response);

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "Client Protocol exception");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "IO Exception");

			}
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return response;

	}

	public static String GetResolution(String customer_id) {

		HttpClient httpclient = new DefaultHttpClient();
		String response = null;
		URI url;
		try {

			String s = Constant.main_url + "rest/service/sync/customer_id/"
					+ customer_id;

			url = new URI(s.replace(" ", "%20"));

			Log.e("my webservice", "My webservice : " + url);

			HttpGet httpget = new HttpGet(url);

			HttpResponse httpResponse = null;

			try {

				// List<NameValuePair> nameValuePairs = new
				// ArrayList<NameValuePair>(
				// 3);

				// httpget.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				httpResponse = httpclient.execute(httpget);

				response = EntityUtils.toString(httpResponse.getEntity(),
						"UTF-8");

				// this is what we extended for the getting the response string
				// which we going to parese for out use in database //

				System.out
						.println("get my local centre response : " + response);

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "Client Protocol exception");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "IO Exception");

			}
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return response;

	}

	public static String Gcm(String customer_id, String device_id, String reg_id) {

		HttpClient httpclient = new DefaultHttpClient();
		String response = null;
		URI url;
		try {

			String s = Constant.main_url + "rest/service/gcm/customer_id/"
					+ customer_id + "/device_id/" + device_id + "/reg_id/"
					+ reg_id;

			url = new URI(s.replace(" ", "%20"));

			Log.e("my webservice", "My webservice : " + url);

			HttpGet httpget = new HttpGet(url);
			HttpResponse httpResponse = null;

			try {

				// List<NameValuePair> nameValuePairs = new
				// ArrayList<NameValuePair>(
				// 3);

				// httpget.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				httpResponse = httpclient.execute(httpget);

				response = EntityUtils.toString(httpResponse.getEntity(),
						"UTF-8");

				// this is what we extended for the getting the response string
				// which we going to parese for out use in database //

				System.out
						.println("get my local centre response : " + response);

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "Client Protocol exception");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "IO Exception");

			}
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return response;
	}

	public static String GlobalSynch(String customer_id, String resolutionid) {

		HttpClient httpclient = new DefaultHttpClient();
		String response = null;
		URI url;
		try {

			String s = Constant.main_url + "rest/service/sync/customer_id/"
					+ customer_id + "/resolution/" + resolutionid;

			url = new URI(s.replace(" ", "%20"));

			Log.e("my webservice", "My webservice : " + url);

			HttpGet httpget = new HttpGet(url);
			HttpResponse httpResponse = null;

			try {

				httpResponse = httpclient.execute(httpget);

				response = EntityUtils.toString(httpResponse.getEntity(),
						"UTF-8");

				// this is what we extended for the getting the response string
				// which we going to parese for out use in database //

				System.out
						.println("get my local centre response : " + response);

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "Client Protocol exception");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "IO Exception");

			}
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return response;
	}

	public static String ContactSynch(String customer_id, String device_type) {

		// ---
		HttpClient httpclient = new DefaultHttpClient();
		String response = null;
		URI url;
		try {

			String s = Constant.main_url
					+ "contact/rest/service/sync/customer_id/" + customer_id
					+ "/device_type/" + device_type;

			url = new URI(s.replace(" ", "%20"));

			Log.e("my webservice", "My webservice : " + url);

			HttpGet httpget = new HttpGet(url);
			HttpResponse httpResponse = null;

			try {

				httpResponse = httpclient.execute(httpget);

				response = EntityUtils.toString(httpResponse.getEntity(),
						"UTF-8");

				// this is what we extended for the getting the response string
				// which we going to parese for out use in database //
				System.out
						.println("get my local centre response : " + response);

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "Client Protocol exception");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "IO Exception");

			}
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return response;
	}

	public static String HomeWallSynch(String customer_id, String resolution_id) {

		// ---
		HttpClient httpclient = new DefaultHttpClient();
		String response = null;
		URI url;
		try {

			String s = Constant.main_url
					+ "home-wallpaper/rest/service/sync/customer_id/"
					+ customer_id + "/resolution/" + resolution_id;

			url = new URI(s.replace(" ", "%20"));

			Log.e("my webservice", "My webservice : " + url);

			HttpGet httpget = new HttpGet(url);
			HttpResponse httpResponse = null;

			try {

				httpResponse = httpclient.execute(httpget);

				response = EntityUtils.toString(httpResponse.getEntity(),
						"UTF-8");

				System.out
						.println("get my local centre response : " + response);

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "Client Protocol exception");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "IO Exception");

			}
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return response;
	}

	public static String PushMessage(String customer_id, String device_type) {

		HttpClient httpclient = new DefaultHttpClient();
		String response = null;
		URI url;
		try {

			String s = Constant.main_url
					+ "push-message/rest/service/sync/customer_id/"
					+ customer_id + "/device_type/" + device_type;

			url = new URI(s.replace(" ", "%20"));

			Log.e("my webservice", "My webservice : " + url);

			HttpGet httpget = new HttpGet(url);
			HttpResponse httpResponse = null;

			try {

				// Execute HTTP Post Request
				httpResponse = httpclient.execute(httpget);

				response = EntityUtils.toString(httpResponse.getEntity(),
						"UTF-8");

				// this is what we extended for the getting the response string
				// which we going to parese for out use in database //

				System.out
						.println("get my local centre response : " + response);

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "Client Protocol exception");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "IO Exception");

			}
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return response;

	}

	public static String Website(String customer_id, String device_type) {

		HttpClient httpclient = new DefaultHttpClient();
		String response = null;
		URI url;
		try {

			String s = Constant.main_url
					+ "website/rest/service/sync/customer_id/" + customer_id
					+ "/device_type/" + device_type;

			url = new URI(s.replace(" ", "%20"));

			Log.e("my webservice", "My webservice : " + url);

			HttpGet httpget = new HttpGet(url);
			HttpResponse httpResponse = null;

			try {

				// Execute HTTP Post Request
				httpResponse = httpclient.execute(httpget);

				response = EntityUtils.toString(httpResponse.getEntity(),
						"UTF-8");

				System.out.println("get my website1  response : " + response);

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "Client Protocol exception");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "IO Exception");

			}
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return response;

	}

	public static String Website1(String customer_id, String device_type) {

		HttpClient httpclient = new DefaultHttpClient();
		String response = null;
		URI url;
		try {

			String s = Constant.main_url
					+ "website-1/rest/service/sync/customer_id/" + customer_id
					+ "/device_type/" + device_type;

			url = new URI(s.replace(" ", "%20"));

			Log.e("my webservice", "My webservice : " + url);

			HttpGet httpget = new HttpGet(url);
			HttpResponse httpResponse = null;

			try {

				// Execute HTTP Post Request
				httpResponse = httpclient.execute(httpget);

				response = EntityUtils.toString(httpResponse.getEntity(),
						"UTF-8");

				System.out.println("get my website1  response : " + response);

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "Client Protocol exception");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "IO Exception");

			}
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return response;

	}

	public static String SocialMedia(String customer_id, String device_type) {

		HttpClient httpclient = new DefaultHttpClient();
		String response = null;
		URI url;
		try {

			String s = Constant.main_url
					+ "social-media/rest/service/sync/customer_id/"
					+ customer_id + "/device_type/" + device_type;

			url = new URI(s.replace(" ", "%20"));

			Log.e("my webservice", "My webservice : " + url);

			HttpGet httpget = new HttpGet(url);
			HttpResponse httpResponse = null;

			try {

				// Execute HTTP Post Request
				httpResponse = httpclient.execute(httpget);

				response = EntityUtils.toString(httpResponse.getEntity(),
						"UTF-8");

				// this is what we extended for the getting the response string
				// which we going to parese for out use in database //

				System.out
						.println("get my local centre response : " + response);

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "Client Protocol exception");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "IO Exception");

			}
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return response;

	}

	public static String Cms(String customer_id, String device_type) {

		HttpClient httpclient = new DefaultHttpClient();
		String response = null;
		URI url;
		try {

			String s = Constant.main_url
					+ "module-cms/rest/service/sync/customer_id/" + customer_id
					+ "/device_type/" + device_type;

			url = new URI(s.replace(" ", "%20"));

			Log.e("my webservice", "My webservice : " + url);

			HttpGet httpget = new HttpGet(url);
			HttpResponse httpResponse = null;

			try {

				httpResponse = httpclient.execute(httpget);

				response = EntityUtils.toString(httpResponse.getEntity(),
						"UTF-8");

				System.out
						.println("get my local centre response : " + response);

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "Client Protocol exception");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "IO Exception");

			}
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return response;

	}

	public static String Cms1(String customer_id, String device_type) {

		HttpClient httpclient = new DefaultHttpClient();
		String response = null;
		URI url;
		try {

			String s = Constant.main_url
					+ "module-cms-1/rest/service/sync/customer_id/"
					+ customer_id + "/device_type/" + device_type;

			url = new URI(s.replace(" ", "%20"));

			Log.e("my webservice", "My webservice : " + url);

			HttpGet httpget = new HttpGet(url);
			HttpResponse httpResponse = null;

			try {

				httpResponse = httpclient.execute(httpget);

				response = EntityUtils.toString(httpResponse.getEntity(),
						"UTF-8");

				System.out
						.println("get my local centre response : " + response);

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "Client Protocol exception");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "IO Exception");

			}
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return response;

	}

	public static String Cms2(String customer_id, String device_type) {

		HttpClient httpclient = new DefaultHttpClient();
		String response = null;
		URI url;
		try {

			String s = Constant.main_url
					+ "module-cms-2/rest/service/sync/customer_id/"
					+ customer_id + "/device_type/" + device_type;

			url = new URI(s.replace(" ", "%20"));

			Log.e("my webservice", "My webservice : " + url);

			HttpGet httpget = new HttpGet(url);
			HttpResponse httpResponse = null;

			try {

				httpResponse = httpclient.execute(httpget);

				response = EntityUtils.toString(httpResponse.getEntity(),
						"UTF-8");

				System.out
						.println("get my local centre response : " + response);

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "Client Protocol exception");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "IO Exception");

			}
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return response;

	}

	public static String Events(String customer_id, String device_type) {

		HttpClient httpclient = new DefaultHttpClient();
		String response = null;
		URI url;
		try {

			String s = Constant.main_url
					+ "events/rest/service/sync/customer_id/" + customer_id
					+ "/device_type/" + device_type;

			url = new URI(s.replace(" ", "%20"));

			Log.e("my webservice", "My webservice : " + url);

			HttpGet httpget = new HttpGet(url);
			HttpResponse httpResponse = null;

			try {

				httpResponse = httpclient.execute(httpget);

				response = EntityUtils.toString(httpResponse.getEntity(),
						"UTF-8");

				System.out
						.println("get my local centre response : " + response);

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "Client Protocol exception");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "IO Exception");

			}
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return response;

	}

	public static String ImageGallery(String customer_id, String device_type) {

		HttpClient httpclient = new DefaultHttpClient();
		String response = null;
		URI url;
		try {

			String s = Constant.main_url
					+ "module-image-gallery/rest/service/sync/customer_id/"
					+ customer_id + "/device_type/" + device_type;

			url = new URI(s.replace(" ", "%20"));

			Log.e("Image-gallery webservice", "My webservice : " + url);

			HttpGet httpget = new HttpGet(url);
			HttpResponse httpResponse = null;

			try {

				httpResponse = httpclient.execute(httpget);

				response = EntityUtils.toString(httpResponse.getEntity(),
						"UTF-8");

				System.out
						.println("get my local centre response : " + response);

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "Client Protocol exception");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "IO Exception");

			}
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return response;

	}

	public static String ImageGallery1(String customer_id, String device_type) {

		HttpClient httpclient = new DefaultHttpClient();
		String response = null;
		URI url;
		try {

			String s = Constant.main_url
					+ "module-image-gallery-1/rest/service/sync/customer_id/"
					+ customer_id + "/device_type/" + device_type;

			url = new URI(s.replace(" ", "%20"));

			Log.e("my webservice", "My webservice : " + url);

			HttpGet httpget = new HttpGet(url);
			HttpResponse httpResponse = null;

			try {

				httpResponse = httpclient.execute(httpget);

				response = EntityUtils.toString(httpResponse.getEntity(),
						"UTF-8");

				System.out
						.println("get my local centre response : " + response);

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "Client Protocol exception");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "IO Exception");

			}
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return response;

	}

	public static String Music(String customer_id, String device_type) {

		HttpClient httpclient = new DefaultHttpClient();
		String response = null;
		URI url;
		try {

			String s = Constant.main_url
					+ "music/rest/service/sync/customer_id/" + customer_id
					+ "/device_type/" + device_type;

			url = new URI(s.replace(" ", "%20"));

			Log.e("my webservice", "My webservice : " + url);

			HttpGet httpget = new HttpGet(url);
			HttpResponse httpResponse = null;

			try {

				httpResponse = httpclient.execute(httpget);

				response = EntityUtils.toString(httpResponse.getEntity(),
						"UTF-8");

				System.out
						.println("get my local centre response : " + response);

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "Client Protocol exception");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "IO Exception");

			}
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return response;

	}

	public static String Document(String customer_id, String device_type) {

		HttpClient httpclient = new DefaultHttpClient();
		String response = null;
		URI url;
		try {

			String s = Constant.main_url
					+ "document/rest/service/sync/customer_id/" + customer_id
					+ "/device_type/" + device_type;

			url = new URI(s.replace(" ", "%20"));

			Log.e("my webservice", "My webservice : " + url);

			HttpGet httpget = new HttpGet(url);
			HttpResponse httpResponse = null;

			try {

				httpResponse = httpclient.execute(httpget);

				response = EntityUtils.toString(httpResponse.getEntity(),
						"UTF-8");

				System.out
						.println("get my local centre response : " + response);

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "Client Protocol exception");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "IO Exception");

			}
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return response;

	}

	public static String get_Direction(String START_ADDRES, String END_ADDRESS) {

		HttpClient httpclient = new DefaultHttpClient();
		String response = null;
		URI url;
		try {

			String s = "http://maps.googleapis.com/maps/api/directions/json?origin="
					+ START_ADDRES.replace(" ", "+")
					+ ",+dublin&destination="
					+ END_ADDRESS.replace(" ", "+")
					+ ",+dublin&sensor=false";

			url = new URI(s);

			Log.e("my direction webservice", "My direction webservice : " + url);

			HttpGet httpget = new HttpGet(url);
			HttpResponse httpResponse = null;

			try {

				httpResponse = httpclient.execute(httpget);

				response = EntityUtils.toString(httpResponse.getEntity(),
						"UTF-8");

				System.out.println("get direction  response : " + response);

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "Client Protocol exception");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("Error : ", "IO Exception");

			}
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return response;

	}
	
	public static String getJSONFromURL(String url){
		InputStream is ;
		String json = "";
	    try {
	        DefaultHttpClient httpClient = new DefaultHttpClient();
	        HttpPost httpPost = new HttpPost(url);

	        HttpResponse httpResponse = httpClient.execute(httpPost);
	        org.apache.http.HttpEntity httpEntity = httpResponse.getEntity();

	        is = httpEntity.getContent();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
	        StringBuilder sb = new StringBuilder();
	        String line = null;

	        while((line = reader.readLine()) != null){
	            sb.append(line + "\n");
	            //Log.e("test: ", sb.toString());
	        }

	        json = sb.toString();
	        is.close();
	    } catch (Exception e) {
	        // TODO Auto-generated catch block
	        Log.e("buffer error", "Error converting result " + e.toString());
	    }

	    return json;
	}

}
