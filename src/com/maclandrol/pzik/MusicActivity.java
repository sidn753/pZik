package com.maclandrol.pzik;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

public class MusicActivity extends Activity implements
		MediaController.MediaPlayerControl {
	public static final String TAG = "MusicPlayer";
	private MediaController mediaController;
	private SongData song;
	private String audioFile, title, duration, size, quality;
	private boolean mBound = false;
	private Handler handler = new Handler();
	private MusicService mService;
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder binder) {
			// We've bound to LocalService, cast the IBinder and get
			// LocalService instance
			mBound = true;
			mService = ((MusicService.LocalBinder) binder).getService();
			Toast.makeText(getApplicationContext(), "Connected",
					Toast.LENGTH_SHORT).show();

		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;

		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.song_view);
		// Get Data from intent and set Song info in the view
		mediaController = new MediaController(this);
		this.setInfos();
		Toast.makeText(getApplicationContext(), "Create Activity",
				Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.w("BoundState", "Status: "+mBound);
		if (!mBound) {
			Intent intent = new Intent(this, MusicService.class);
			bindService(intent, mConnection,0);
			Toast.makeText(getApplicationContext(), "Start Activity",
					Toast.LENGTH_LONG).show();
		}
		initMusic();
		MusicService.playSong(audioFile);
	}

	public void setInfos() {
		song = (SongData) this.getIntent().getParcelableExtra("songInfos");
		title = song.title;
		audioFile = song.link;
		duration = song.duration;
		size = song.size;
		quality = song.quality;
		((TextView) findViewById(R.id.now_playing_text)).setText(title);
		((TextView) findViewById(R.id.duration)).setText(duration);
		((TextView) findViewById(R.id.size)).setText(size);
		((TextView) findViewById(R.id.quality)).setText(quality);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Toast.makeText(getApplicationContext(), "destroy Activity",
				Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mBound) {
			unbindService(mConnection);
		}
		mBound = false;
		Toast.makeText(getApplicationContext(), "Stop Activity",
				Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Toast.makeText(getApplicationContext(), "Resume activity",
				Toast.LENGTH_LONG).show();

	}

	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// the MediaController will hide after 3 seconds - tap the screen to
		// make it appear again
		if (mBound) {
			mediaController.show();
		}
		return false;
	}

	public void initMusic() {
		Log.d("init", "init Music executed successfully");
		mediaController.setMediaPlayer(this);
		mediaController.setAnchorView(findViewById(R.id.main_audio_view));

		handler.post(new Runnable() {
			public void run() {
				mediaController.setEnabled(true);
			}
		});
	}

	/*--------------------------MediaPlayerControl ------------------------*/

	public void start() {
		mService.start();
	}

	public void pause() {
		mService.pause();
	}

	public int getDuration() {
		return mService.getDuration();
	}

	public int getCurrentPosition() {
		return mService.getCurrentPosition();
	}

	public void seekTo(int i) {
		mService.seekTo(i);
	}

	public boolean isPlaying() {
		return mService.isPlaying();
	}

	public int getBufferPercentage() {
		return 0;
	}

	public boolean canPause() {
		return true;
	}

	public boolean canSeekBackward() {
		return true;
	}

	public boolean canSeekForward() {
		return true;
	}

}
