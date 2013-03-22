package com.appstart.adapter;

import com.appstart.R;

import android.media.MediaPlayer;
import android.widget.Button;
import android.widget.SeekBar;

public class MediaAdapter {

	static MediaPlayer mediaPlayer;
	static String txtttl;

	public static void initView() {

		mediaPlayer = new MediaPlayer();

	}

	public static MediaPlayer getMediaPlayer() {

		return mediaPlayer;

	}

	public static String getText() {
		return txtttl;
	}

	public static void startMediaPlayer(String path, String textString) {

		try {

			mediaPlayer.reset();
			mediaPlayer.setDataSource(path);// setup
											// source
			mediaPlayer.prepare(); // you must call this method after
									// setup
									// the datasource in setDataSource
									// method. After calling prepare()
									// the
									// instance of MediaPlayer starts
									// load
									// data from URL to internal buffer.
			mediaPlayer.start();
			txtttl = textString;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void pauseMeidaPlayer() {

		if (mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
		} else {
			mediaPlayer.start();
		}
	}

	public static int durationMediaPlayer() {
		return mediaPlayer.getDuration();
	}

}
