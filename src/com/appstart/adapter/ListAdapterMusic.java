package com.appstart.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.appstart.MusicDetails;
import com.appstart.R;
import com.appstart.Webview;
import com.appstart.parsing.MusicData;
import com.appstart.tabgroup.TabGroupActivity;
import com.appstart.utility.Util;

public class ListAdapterMusic extends BaseAdapter {

	private Activity activity;
	// private ArrayList&lt;HashMap&lt;String, String&gt;&gt; data;
	private static ArrayList Title, Album, Artist, AlbumArt, TrackUrl,PreviUrl;
	private static LayoutInflater inflater = null;

	public ListAdapterMusic(Activity act, ArrayList arrTitle,
			ArrayList arrAlbum, ArrayList arrArtist, ArrayList arrAlbumArt,
			ArrayList arrTrackUrl,ArrayList prviewUrl) {

		activity = act;
		Title = arrTitle;
		Artist = arrArtist;
		Album = arrAlbum;
		AlbumArt = arrAlbumArt;
		TrackUrl = arrTrackUrl;
		PreviUrl=prviewUrl;

		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public int getCount() {
		return Title.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.row_music, null);

		TextView title = (TextView) vi.findViewById(R.id.txt_ttl_album);
		title.setText(String.valueOf(Title.get(position)));

		TextView album = (TextView) vi.findViewById(R.id.txt_album);
		album.setText(String.valueOf(Album.get(position)));

		TextView artist = (TextView) vi.findViewById(R.id.txt_artist);
		artist.setText(String.valueOf(Artist.get(position)));

		//((ImageView)vi.findViewById(R.id.iv_albumart)).setVisibility(View.VISIBLE);
		
		((ImageView)vi.findViewById(R.id.iv_albumart)).setImageBitmap(
				Util.ImgBitFromFile(AlbumArt.get(position).toString()));
		
		
		/*((ImageView) vi.findViewById(R.id.iv_albumart))
				.setBackgroundDrawable((Drawable) AlbumArt.get(position));*/

		if (PreviUrl.get(position).toString().contains("beatport")) {

			((ImageView) vi.findViewById(R.id.iv_ic_mu))
					.setBackgroundResource(R.drawable.beatport);

		} else if (PreviUrl.get(position).toString().contains("7digital")) {
			
			((ImageView) vi.findViewById(R.id.iv_ic_mu))
					.setBackgroundResource(R.drawable.seven_digital);

		}else if(PreviUrl.get(position).toString().contains("soundcloud"))
		{
			((ImageView) vi.findViewById(R.id.iv_ic_mu))
			.setBackgroundResource(R.drawable.soundcloud);
			
		}

		((Button) vi.findViewById(R.id.btn_music_play1))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub

						Bundle b = new Bundle();
						b.putString("website_url", TrackUrl.get(position)
								.toString());

						Intent edit = new Intent(activity.getParent(),
								Webview.class);

						edit.putExtras(b);
						TabGroupActivity parentActivity = (TabGroupActivity) activity
								.getParent();
						parentActivity.startChildActivity("Webview", edit);

					}
				});

//		((TextView) vi.findViewById(R.id.txt_msc_info))
//				.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View arg0) {
//						// TODO Auto-generated method stub
//
//						
//						
//						Bundle b = new Bundle();
//						b.putString("Title", TrackUrl.get(position)
//								.toString());
//						b.putString("Album", Album.get(position)
//								.toString());
//						b.putString("Artis", Artist.get(position)
//								.toString());
//						
//						System.out.println("album art value:::"+AlbumArt.get(position)
//								.toString());
//						
//						
//						b.putString("AlbumArt", AlbumArt.get(position)
//								.toString());
//						
//						
//						
//
//						Intent edit = new Intent(activity.getParent(),
//								MusicDetails.class);
//
//						edit.putExtras(b);
//						TabGroupActivity parentActivity = (TabGroupActivity) activity
//								.getParent();
//						parentActivity.startChildActivity("MusicDetails", edit);
//
//					}
//				});

		// title.setTextColor(Util
		// .Colorcode());
		// title.setTypeface(Util.font(activity,
		// Constant.FONT_TYPE));
		// title.setTextSize(
		// TypedValue.COMPLEX_UNIT_DIP,
		// Float.parseFloat(Constant.FONT_SIZE));

		return vi;

	}
}