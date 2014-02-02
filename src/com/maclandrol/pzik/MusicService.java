package com.maclandrol.pzik;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MusicService extends Service implements
		MediaPlayer.OnPreparedListener {
	private static MediaPlayer mplayer;
	public static String audioFile = "";
	public static boolean musicPlaying = false;
	public static boolean isUnbind = false;
	public final IBinder binder = new LocalBinder();

	public class LocalBinder extends Binder {

		public MusicService getService() {
			return MusicService.this;
		}

	}

	@Override
	public IBinder onBind(Intent intent) {
		isUnbind = false;
		mplayer.setOnPreparedListener(this);
		mplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		Log.d("MusicService", "Service successfully binded");
		return binder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		isUnbind = true;
		Log.d("MusicService", "Service successfully unbinded");
		return super.onUnbind(intent);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("MusicService", "Service successfully started");
		mplayer = new MediaPlayer();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		stop();
		mplayer.release();
		super.onDestroy();
	}

	public static void playSong(String url) {

		Log.d("AudioFile", "Link= " + audioFile);
		Log.d("url Passed", "Link= " + url);
		
		if (!url.equals(audioFile)) {
			if (musicPlaying) {
				mplayer.stop();
				Log.d("AudioFile", "Stopped Playing");

			}
			try {
				mplayer.reset();
				mplayer.setDataSource(url);
				mplayer.prepare();
				mplayer.start();
			} catch (IOException e) {
				Log.d("PlaySongError", e.getMessage());
			}
		}
		musicPlaying = true;
		audioFile = url;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {

	}

	public void start() {
		mplayer.start();
	}

	public void stop() {
		musicPlaying = false;
		audioFile="";
		mplayer.stop();

	}

	public void pause() {
		mplayer.pause();
	}

	public int getDuration() {
		return mplayer.getDuration();
	}

	public int getCurrentPosition() {
		return mplayer.getCurrentPosition();
	}

	public void seekTo(int i) {
		mplayer.seekTo(i);
	}

	public boolean isPlaying() {
		return mplayer.isPlaying();
	}

}
