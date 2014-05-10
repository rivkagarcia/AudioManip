package com.example.audiomanip;

import android.R.bool;
import android.R.layout;
import android.app.Activity;
import android.widget.LinearLayout;
import android.widget.SeekBar;
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


public class MainActivity extends Activity
{
    private static final String LOG_TAG = "AudioRecordTest";
    private static String mFileName = null;
    private RecordButton mRecordButton = null;
    private MediaRecorder mRecorder = null;
    private PlayButton   mPlayButton = null;
    private bool playSeekBar = null;
    private MediaPlayer   mPlayer = null;
    private SeekBar seekbar = null;
    private long duration;
    
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
        
        Thread timer = new Thread(){
        	public void run(){
        try {
			mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
            mPlayer.getDuration();
            duration = mPlayer.getDuration();
            Log.d("Duration of Recording", "" + duration);
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }finally{
        	mPlayButton.setBackgroundResource(R.drawable.pause);
				
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
    class RecordButton extends Button {
        boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    setText("Stop recording");
                    mRecordButton.setBackgroundResource(R.drawable.pause);
                	mRecordButton.setTextColor(Color.CYAN);
                	if(mStartRecording == true){
                		mRecordButton.setBackgroundResource(R.drawable.pause);
                	}
                    
                } else {
                    setText("Start recording");
                   mRecordButton.setBackgroundResource(R.drawable.rec);
                }
                mStartRecording = !mStartRecording;
            }

			private Object getResources() {
				// TODO Auto-generated method stub
				return null;
			}
        };

        public RecordButton(Context ctx) {
            super(ctx);
            setText("Start recording");
            setOnClickListener(clicker);
        }
    }

    class PlayButton extends Button {
        boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onPlay(mStartPlaying);
                mRecordButton.setBackgroundResource(R.drawable.rec);
                Toast.makeText(getApplicationContext(), "Duration" + duration,
        				Toast.LENGTH_SHORT).show();
                if (mStartPlaying) {
                    setText("Stop playing");
                    mPlayButton.setTextColor(Color.CYAN);
                    mRecordButton.setBackgroundResource(R.drawable.rec);
                    if(mStartPlaying == true){
                		mPlayButton.setBackgroundResource(R.drawable.pause);
                	}
                    
                } 
//                Handler handler = new Handler();
//                
//                Runnable r = new Runnable(){
//                	@Override
//                	public void run(){
//                		Toast.makeText(getApplicationContext(), "Duration" + duration,
//                				Toast.LENGTH_SHORT).show();
//                		setText("Start playing");
//                        mPlayButton.setTextColor(Color.CYAN);
//                        mPlayButton.setBackgroundResource(R.drawable.play);
//                		
//                	}
//                };
//                handler.postDelayed(r, duration);
                else {
                    setText("Start playing");
                    mPlayButton.setTextColor(Color.CYAN);
                    mPlayButton.setBackgroundResource(R.drawable.play);
                }
                mStartPlaying = !mStartPlaying;
            }
        };

        public PlayButton(Context ctx) {
            super(ctx);
            setText("Start playing");
            setOnClickListener(clicker);
        }
    }

    public MainActivity() {
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";
    }

   @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        LinearLayout ll = new LinearLayout(this);
        ll.setBackgroundResource(R.drawable.sound_wave);
        mRecordButton = new RecordButton(this);
        ll.addView(mRecordButton,
            new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0));
        mPlayButton = new PlayButton(this);
        ll.addView(mPlayButton,
            new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0));
        setContentView(ll);
        mRecordButton.setBackgroundResource(R.drawable.rec);
        mRecordButton.setTextColor(Color.CYAN);
        mPlayButton.setBackgroundResource(R.drawable.play);
    	mPlayButton.setTextColor(Color.CYAN);
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