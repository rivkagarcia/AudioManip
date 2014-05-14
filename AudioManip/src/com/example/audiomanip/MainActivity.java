package com.example.audiomanip;

import android.R.bool;
import android.R.layout;
import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.media.MediaRecorder;
import android.media.MediaPlayer;
import android.widget.SeekBar;

import java.io.IOException;

import android.graphics.Color;

public class MainActivity extends Activity {
	private static final String LOG_TAG = "AudioRecordTest";
	private static String mFileName = null;
	private static ImageButton mRecordButton = null;
	private static MediaRecorder mRecorder = null;
	private static ImageButton mPlayButton = null;
	private static bool playSeekBar = null;
	private static MediaPlayer mPlayer = null;
	private long duration;
	boolean mStartRecording = true;
	boolean mStartPlaying = true;
	// boolean Button;
	Spinner spinner;
	String[] paths = { "1st song", "2nd song", "3rd song" };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				MainActivity.this, android.R.layout.simple_spinner_item, paths);
		spinner = (Spinner) findViewById(R.id.spinner1);
		spinner.setAdapter(adapter);
		mRecordButton = (ImageButton) findViewById(R.id.recButton);
		mRecordButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				onRecord(mStartRecording);
				Toast.makeText(getApplicationContext(),
						"mStartRecording = " + mStartRecording,
						Toast.LENGTH_SHORT).show();
				if (mStartRecording) {
					Toast.makeText(getApplicationContext(),
							"Press Record again to stop", Toast.LENGTH_SHORT)
							.show();
					findViewById(R.id.recButton).setBackgroundResource(
							R.drawable.pause);
				}
				mStartRecording = !mStartRecording;
			}

		});

		mPlayButton = (ImageButton) findViewById(R.id.playButton);
		mPlayButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mPlayButton.setImageResource(R.drawable.pause);
				Handler handler = new Handler();

				// runnable set up to hold code which will fun after the handler
				Runnable r = new Runnable() {

					@Override
					public void run() {
						mPlayButton.setImageResource(R.drawable.play);
					}
				};
			
				handler.postDelayed(r, duration);

				onPlay(mStartPlaying);
				Toast.makeText(getApplicationContext(),
						"Duration = " + duration, Toast.LENGTH_SHORT).show();
				//mStartPlaying = !mStartPlaying;
			}
		});
	}

	private void onRecord(boolean start) {
		if (start) {
			startRecording();
		} else {
			stopRecording();
		}
	}

	private void onPlay(boolean start) {
		if (start) {
			startPlaying();
		} else {
			stopPlaying();
		}
	}

	private void startPlaying() {
		mPlayer = new MediaPlayer();

		Thread timer = new Thread() {
			public void run() {
				try {
					mPlayer.setDataSource(mFileName);
					mPlayer.prepare();
					mPlayer.start();
					duration = mPlayer.getDuration();
					Log.d("Duration of Recording", "" + duration);
				} catch (IOException e) {
					Log.e(LOG_TAG, "prepare() failed");
				} finally {
					// mPlayButton.setBackgroundResource(R.drawable.pause);
				}
			}
		};
		timer.start();
	}

	private void stopPlaying() {
		mPlayer.release();
		mPlayer = null;
	}

	private void startRecording() {
		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mRecorder.setOutputFile(mFileName);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		try {
			mRecorder.prepare();
		} catch (IOException e) {
			Log.e(LOG_TAG, "prepare() failed");
		}

		mRecorder.start();
	}

	private void stopRecording() {
		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;
	}

	public MainActivity() {
		mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
		mFileName += "/audiorecordtest.3gp";
	}




	@Override
	public void onPause() {
		super.onPause();
		if (mRecorder != null) {
			mRecorder.release();
			mRecorder = null;
		}

		if (mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
	}
}