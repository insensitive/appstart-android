package com.appstart;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.appstart.adapter.ListAdapterMusic;
import com.appstart.adapter.MediaAdapter;
import com.appstart.database.DBAdapter;
import com.appstart.tabgroup.TabGroupActivity;
import com.appstart.utility.Constant;
import com.appstart.utility.Util;

public class Music extends Activity implements OnClickListener,
		OnTouchListener, OnCompletionListener, OnBufferingUpdateListener {

	Button btn_back;
	ListView list;
	int i = 0;
	int song_no = 0;

	ListAdapterMusic adapter;

	ArrayList<Object> Title;
	ArrayList<Object> Album;
	ArrayList<Object> Artist;
	ArrayList<Object> AlbumArt;
	ArrayList<String> StrAlbumArt;

	ArrayList<String> TrackUrl;
	ArrayList<String> PreviewUrl;

	Button btn_next, btn_prev;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.music);

		setHeaderBackground();

		initView();
		Bundle b = getIntent().getExtras();

		boolean showBack = b.getBoolean("showBack");

		btn_back = (Button) findViewById(R.id.ib_back_music);

		if (showBack) {
			((Button) findViewById(R.id.ib_back_music))
					.setVisibility(View.VISIBLE);
		} else {
			((Button) findViewById(R.id.ib_back_music))
					.setVisibility(View.INVISIBLE);
		}

		// final MediaAdapter mAdapter=new MediaAdapter();

		((TextView) findViewById(R.id.txt_music)).setText(R.string.music);

		btn_next = (Button) findViewById(R.id.ib_next);
		btn_prev = (Button) findViewById(R.id.ib_prev);

		btn_next.setOnClickListener(this);
		btn_prev.setOnClickListener(this);

		/*
		 * Cursor c1 = dba.getBackgroundImage("13", Constant.LANGUAGE_ID);
		 * 
		 * Cursor c2 = dba.getBackgroundColor("13", Constant.LANGUAGE_ID);
		 * 
		 * 
		 * if(c1.getCount()>0) { ((LinearLayout)
		 * findViewById(R.id.lay_md)).setBackgroundDrawable
		 * (ImgDrawableFromFile(getResources(), c1.getString(0))); }
		 * 
		 * if(c2.getCount()>0){
		 * 
		 * ((LinearLayout)
		 * findViewById(R.id.lay_md)).setBackgroundColor(Color.parseColor
		 * (c2.getString(0))); }
		 */

		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				TabGroupActivity parentActivity = (TabGroupActivity) getParent();
				parentActivity.finishFromChild(Music.this);

			}
		});

		if (MediaAdapter.getMediaPlayer().isPlaying()) {
			((TextView) findViewById(R.id.txt_ttl_music)).setText(MediaAdapter
					.getText());

			mediaFileLengthInMilliseconds = MediaAdapter.durationMediaPlayer(); // gets
			primarySeekBarProgressUpdater();

		}

		((ImageButton) findViewById(R.id.ib_play_stp))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub

						MediaAdapter.pauseMeidaPlayer();

						if (MediaAdapter.getMediaPlayer().isPlaying()) {
							primarySeekBarProgressUpdater();
						}
					}
				});

		list = (ListView) findViewById(R.id.list_music);

		Title = new ArrayList<Object>();
		Album = new ArrayList<Object>();
		Artist = new ArrayList<Object>();
		AlbumArt = new ArrayList<Object>();
		StrAlbumArt = new ArrayList<String>();

		TrackUrl = new ArrayList<String>();
		PreviewUrl = new ArrayList<String>();

		// if (mediaPlayer == null) {
		// initView();
		// }

		DBAdapter dba = new DBAdapter(this);
		dba.open();

		Cursor c = dba.getMusic(Constant.LANGUAGE_ID);

		for (int i = 0; i < c.getCount(); i++) {

			System.out.println("" + c.getString(3));

			if (!c.getString(7).contains("apple")) {

				Title.add(c.getString(3));
				Album.add(c.getString(5));
				Artist.add(c.getString(4));

				System.out.print("image name:-> " + c.getString(8));
				// System.out.println("\n album art which we adding here-> "+
				// ImgDrawableFromFile(getApplicationContext().getResources(),
				// c.getString(8)));

				// AlbumArt.add(ImgDrawableFromFile(getResources(),
				// c.getString(8)));

				AlbumArt.add(c.getString(8));

				PreviewUrl.add(c.getString(7));

				TrackUrl.add(c.getString(6));

				StrAlbumArt.add(c.getString(8));

			}

			c.moveToNext();

		}

		dba.close();

		dba.close();
		adapter = new ListAdapterMusic(this, Title, Album, Artist, AlbumArt,
				TrackUrl, PreviewUrl);
		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub

				((TextView) findViewById(R.id.txt_ttl_music)).setText(Title
						.get(position).toString());

				MediaAdapter.startMediaPlayer(PreviewUrl.get(position), Title
						.get(position).toString());

				song_no = position;

				mediaFileLengthInMilliseconds = MediaAdapter
						.durationMediaPlayer(); // gets
				primarySeekBarProgressUpdater();
				// the

			}

		});

		list.setDivider(new ColorDrawable(Util.Colorcode()));
		list.setDividerHeight(1);

	}

	public void setHeaderBackground() {

		((RelativeLayout) findViewById(R.id.rel_music))
				.setBackgroundDrawable(Util.HeaderBackground());

	}

	// ---

	private SeekBar seekBarProgress;

	private MediaPlayer mediaPlayer;
	private int mediaFileLengthInMilliseconds; // this value contains the song
												// duration in milliseconds.
												// Look at getDuration() method
												// in MediaPlayer class

	private final Handler handler = new Handler();

	/** This method initialise all the views in project */

	/*
	 * public void setDialog(String title, String album, String artis, String
	 * album_art) {
	 * 
	 * LayoutInflater factory = LayoutInflater.from(getParent()); View aboutView
	 * = factory.inflate(R.layout.musicdetails, null);
	 * 
	 * final Dialog dialog = new AlertDialog.Builder(getParent())
	 * 
	 * .setView(aboutView).setOnCancelListener(new OnCancelListener() {
	 * 
	 * @Override public void onCancel(DialogInterface dialog) {
	 * 
	 * dialog.dismiss();
	 * 
	 * } }).create();
	 * 
	 * dialog.setTitle("Music Details"); dialog.setCanceledOnTouchOutside(true);
	 * 
	 * // there are a lot of settings, for dialog, check them all out!
	 * 
	 * // set up text
	 * 
	 * ((TextView) aboutView.findViewById(R.id.txt_ttl_md)).setText(title);
	 * ((TextView) aboutView.findViewById(R.id.txt_album_name)).setText(album);
	 * ((TextView) aboutView.findViewById(R.id.txt_artist_name))
	 * .setText(artis); ((ImageView)
	 * aboutView.findViewById(R.id.iv_md_albumart))
	 * .setBackgroundDrawable(ImgDrawableFromFile(getResources(), album_art));
	 * 
	 * // now that the dialog is set up, it's time to show it
	 * 
	 * dialog.show();
	 * 
	 * }
	 */

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_next:
			song_no = song_no+1;
			if(PreviewUrl.size()>=song_no){
				MediaAdapter.startMediaPlayer(PreviewUrl.get(song_no),
						Title.get(song_no).toString());
			}else if(song_no>PreviewUrl.size()){
				MediaAdapter.startMediaPlayer(PreviewUrl.get(PreviewUrl.size()),
						Title.get(PreviewUrl.size()).toString());
			}
		

			break;
		case R.id.ib_prev:
			
			song_no = song_no-1;
			if (song_no>=0) {
				MediaAdapter.startMediaPlayer(PreviewUrl.get(song_no), Title.get(song_no)
						.toString());
				
			} else if(song_no<0){
				MediaAdapter.startMediaPlayer(PreviewUrl.get(0), Title.get(0)
						.toString());
			}

			break;
		default:
			break;
		}

	}

	Bitmap myBitmap = null;
	Drawable d = null;

	public Drawable ImgDrawableFromFile(Resources res, String file_name) {

		myBitmap = null;
		System.out.print("\n called ImgDrawable in Music");
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

	// --seekbar methods--

	/** This method initialise all the views in project */
	private void initView() {

		seekBarProgress = (SeekBar) findViewById(R.id.SeekBarTestPlay);
		seekBarProgress.setMax(99); // It means 100% .0-99
		seekBarProgress.setOnTouchListener(this);

		// mediaPlayer = new MediaPlayer();
		MediaAdapter.getMediaPlayer().setOnBufferingUpdateListener(this);
		MediaAdapter.getMediaPlayer().setOnCompletionListener(this);

	}

	/**
	 * Method which updates the SeekBar primary progress by current song playing
	 * position
	 */
	Runnable notification;

	private void primarySeekBarProgressUpdater() {
		seekBarProgress
				.setProgress((int) (((float) MediaAdapter.getMediaPlayer()
						.getCurrentPosition() / mediaFileLengthInMilliseconds) * 100)); // This
		// math

		// "was playing"/"song length"
		if (MediaAdapter.getMediaPlayer().isPlaying()) {
			notification = new Runnable() {
				public void run() {
					primarySeekBarProgressUpdater();
				}
			};
			handler.postDelayed(notification, 1000);
		}
	}

	// ---
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v.getId() == R.id.SeekBarTestPlay) {
			/**
			 * Seekbar onTouch event handler. Method which seeks MediaPlayer to
			 * seekBar primary progress position
			 */
			if (MediaAdapter.getMediaPlayer().isPlaying()) {
				SeekBar sb = (SeekBar) v;
				int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100)
						* sb.getProgress();
				MediaAdapter.getMediaPlayer().seekTo(
						playPositionInMillisecconds);
			}
		}
		return false;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		/**
		 * MediaPlayer onCompletion event handler. Method which calls then song
		 * playing is complete
		 */
		// buttonPlayPause.setImageResource(R.drawable.button_play);

	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		/**
		 * Method which updates the SeekBar secondary progress by current song
		 * loading from URL position
		 */

		seekBarProgress.setSecondaryProgress(percent);

	}
}
