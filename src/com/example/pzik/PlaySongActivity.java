package com.example.pzik;
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import android.media.MediaPlayer.OnPreparedListener;
import android.view.MotionEvent;
import android.widget.MediaController;
import android.widget.TextView;

import java.io.IOException;

public class PlaySongActivity extends Activity implements OnPreparedListener, MediaController.MediaPlayerControl{
  private static final String TAG = "AudioPlayer";
  private MediaPlayer mediaPlayer;
  private MediaController mediaController;
  private SongData song;
  private String audioFile;
  private String title;
  private String duration;
  private String size;
  private String quality;
  private final String playing_echec ="Can't play song!";
  private Handler handler = new Handler();

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.song_view);

    song = (SongData) this.getIntent().getParcelableExtra("songInfos");
    title=song.title;
    duration=song.duration;
    size=song.size;
    quality=song.quality;
    ((TextView)findViewById(R.id.now_playing_text)).setText(title);
    ((TextView)findViewById(R.id.duration)).setText(duration);
    ((TextView)findViewById(R.id.size)).setText(size);
    ((TextView)findViewById(R.id.quality)).setText(quality);
    audioFile=song.link;
    mediaPlayer = new MediaPlayer();
    mediaPlayer.setOnPreparedListener(this);

    mediaController = new MediaController(this);

    try {
      mediaPlayer.setDataSource(audioFile);
      mediaPlayer.prepare();
      mediaPlayer.start();
    } catch (IOException e) {
      Log.e(TAG, "Could not open file " + audioFile + " for playback.", e);
      ((TextView)findViewById(R.id.state)).setText(playing_echec);

    }

  }

  @Override
  protected void onStop() {
    super.onStop();
    mediaController.hide();
    mediaPlayer.stop();
    mediaPlayer.release();
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    //the MediaController will hide after 3 seconds - tap the screen to make it appear again
    mediaController.show();
    return false;
  }

  //--MediaPlayerControl methods----------------------------------------------------
  public void start() {
    mediaPlayer.start();
  }

  public void pause() {
    mediaPlayer.pause();
  }

  public int getDuration() {
    return mediaPlayer.getDuration();
  }

  public int getCurrentPosition() {
    return mediaPlayer.getCurrentPosition();
  }

  public void seekTo(int i) {
    mediaPlayer.seekTo(i);
  }

  public boolean isPlaying() {
    return mediaPlayer.isPlaying();
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
  //--------------------------------------------------------------------------------

  public void onPrepared(MediaPlayer mediaPlayer) {
    Log.d(TAG, "onPrepared");
    mediaController.setMediaPlayer(this);
    mediaController.setAnchorView(findViewById(R.id.main_audio_view));

    handler.post(new Runnable() {
      public void run() {
        mediaController.setEnabled(true);
        mediaController.show();
      }
    });
  }
}