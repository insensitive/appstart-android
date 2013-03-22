/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import android.location.Criteria;
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

public class BasicMapActivity1 extends android.support.v4.app.FragmentActivity {
	GoogleMap mMap;
	double latitude = 0;
	double longitude = 0;
	String title =" ";
	String encodedString=" ";
	LatLng TO_DESTINATION;

	List<LatLng> polyz;
	GPSTracker gps;
	double lat,lng;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.basic_demo1);

		Bundle b = getIntent().getExtras();
		latitude = Double.parseDouble(b.getString("latitude"));
		longitude = Double.parseDouble(b.getString("longitude"));
		title=b.getString("title");
	//	encodedString=b.getString("encodedString");
		

		TextView txt_title = (TextView) findViewById(R.id.txt_music);
		txt_title.setText(title);

		 setUpMapIfNeeded();
	
		 
		TO_DESTINATION = new LatLng(latitude, longitude);
		
		mMap.addMarker(new MarkerOptions().position(TO_DESTINATION)
				.title(title));

		
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
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
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
			}
		}
	}
	
	private void setUpMap() {

	//	mMap.addMarker(new MarkerOptions().position(
				//new LatLng(latitude, longitude)).title(title));
		 
		mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		//mMap.setMyLocationEnabled(true);
			 
		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				latitude, longitude), 12.0f));

	}

	public void setHeaderBackground() {

		((RelativeLayout) findViewById(R.id.rel_music))
				.setBackgroundDrawable(Util.HeaderBackground());

	}
}
