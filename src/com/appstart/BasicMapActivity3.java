

package com.appstart;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.appstart.tabgroup.TabGroupActivity;
import com.appstart.utility.Constant;
import com.appstart.utility.Util;
import com.appstart.webservice.Webservice;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BasicMapActivity3 extends FragmentActivity implements LocationListener, LocationSource {
	
	GoogleMap mMap;
	double latitude = 0;
	double longitude = 0;
	String title =" ";
	LatLng TO_DESTINATION, TO_LOCATION;
	private OnLocationChangedListener mListener;
	private LocationManager locationManager;

	List<LatLng> polyz;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.basic_demo1);

		Bundle b = getIntent().getExtras();
		latitude = Double.parseDouble(b.getString("latitude"));
		longitude = Double.parseDouble(b.getString("longitude"));
		title=b.getString("title");
		

		TextView txt_title = (TextView) findViewById(R.id.txt_music);
		txt_title.setText("Route");

		// setUpMapIfNeeded();
		 
		// mMap.setMyLocationEnabled(true);
		 

		 locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		    if(locationManager != null)
		    {
		        boolean gpsIsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		        boolean networkIsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		        if(gpsIsEnabled)
		        {
		            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 10F, this);
		        }
		        else if(networkIsEnabled)
		        {
		            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000L, 10F, this);
		        }
		        else
		        {
		            //Show an error dialog that GPS is disabled.
		        }
		    }
		    else
		    {
		        //Show a generic error dialog since LocationManager is null for some reason
		    }

		   
			 
			 setUpMapIfNeeded();
		


		
			setHeaderBackground();
		


		

		Button ib_back = (Button) findViewById(R.id.ib_back_music);

		ib_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				TabGroupActivity parentActivity = (TabGroupActivity) getParent();
				parentActivity.onBackPressed();

			}
		});
	}

	@Override
	public void onPause()
	{
	    if(locationManager != null)
	    {
	        locationManager.removeUpdates(this);
	    }

	    super.onPause();
	}

	@Override
	public void onResume()
	{
	    super.onResume();

	    setUpMapIfNeeded();

	    if(locationManager != null)
	    {
	        mMap.setMyLocationEnabled(true);
	    }
	}


	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map1)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
				 mMap.setLocationSource(this);
			}
		}
	}
	
	private void setUpMap() {
		 mMap.setMyLocationEnabled(true);
		 
		/*mMap.addMarker(new MarkerOptions().position(
				new LatLng(latitude, longitude)).title(title));
		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				latitude, longitude), 12.0f));*/

	}
	@Override
	public void activate(OnLocationChangedListener listener) 
	{
	    mListener = listener;
	}

	@Override
	public void deactivate() 
	{
	    mListener = null;
	}

	@Override
	public void onLocationChanged(Location location) 
	{
	    if( mListener != null )
	    {
	        mListener.onLocationChanged( location );

	        TO_LOCATION=new LatLng(location.getLatitude(), location.getLongitude());
	        
	        //Move the camera to the user's location once it's available!
	        mMap.animateCamera(CameraUpdateFactory.newLatLng(TO_LOCATION));
	        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
		
	        
	    }
	}

	@Override
	public void onProviderDisabled(String provider) 
	{
	    // TODO Auto-generated method stub
	    Toast.makeText(this, "provider disabled", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderEnabled(String provider) 
	{
	    // TODO Auto-generated method stub
	    Toast.makeText(this, "provider enabled", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) 
	{
	    // TODO Auto-generated method stub
	    Toast.makeText(this, "status changed", Toast.LENGTH_SHORT).show();
	}
	public void setHeaderBackground() {

		((RelativeLayout) findViewById(R.id.rel_music))
				.setBackgroundDrawable(Util.HeaderBackground());

	}
}
