package com.example.audiomanip;

import android.R.bool;
import android.R.layout;
import android.app.Activity;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MediaController;
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
import android.view.View.OnTouchListener;
import android.view.MotionEvent;

import java.io.IOException;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.os.Build;

import java.io.File;
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
	private static MediaPlayer song1;
    private static MediaPlayer song2;
    private static MediaPlayer song3;
    private Visualizer mVisualizer;
	private byte[] mBytes;
	private MediaPlayer music;
	
	//Create a spinner(dropdown Menu) with 3 song options to play
	Spinner spinner1;
	String[] paths = { "Songs", "Dare to Dreeam",
			"Get Money", "All of Me" };
	//Create a spinner(dropdown Menu) with an option to save
	//the recorded audio
	Spinner spinner2;
	String[] filePaths = {"Options", "Save"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);
		
		//songs in the spinner loctaed in the raw file 
		//under recources are set to a variable
		song1 = MediaPlayer.create(this, R.raw.dare_to_dream);
        song2 = MediaPlayer.create(this, R.raw.get_money);
        song3 = MediaPlayer.create(this, R.raw.all_of_me);
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, paths);
        //call out the spinner id from the xml to set its functions
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			public boolean onItemSelected(View v, MotionEvent event) {
				
				String s = spinner1.getSelectedItem().toString();
				//if the index of paths is touched play
				//that assigned song from the spinner
				if (s == paths[1])
				{
					//start playing the song 
					song1.start();
					
				}
				else if (s == paths[2])
				{
					song2.start();
				}
				else if (s == paths[3])
				{
					song3.start();
				}
				
				return false;
			}

			private MediaPlayer findViewById(
					OnItemSelectedListener onItemSelectedListener,
					int dareToDream) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>
        (MainActivity.this, android.R.layout.simple_spinner_item, filePaths);
      //call out the spinner id from the xml to set its functions
    	spinner2 = (Spinner) findViewById(R.id.spinner2);
    	spinner2.setAdapter(adapter2);
    	
    	spinner2.setOnTouchListener(new OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
            	
            	String st = spinner2.getSelectedItem().toString();
            	//if the index of paths is touched play
				//that assigned song from the spinner
				if (st == paths[1])
				{
					 File SDCardpath = getFilesDir();
				        File myDataPath = new File(SDCardpath.getAbsolutePath()
				                + "/My Recordings");
				        // mydir = context.getDir("media", Context.MODE_PRIVATE);
				        if (!myDataPath.exists())
				            myDataPath.mkdir();
				        File audiofile = new File(myDataPath + "/" + "newSongFile.mp3");
				}
				           	
                return false;
            }
    	 });
    	//call out the button created in the xml to its java 
    	//on create funtcions which will record audio on touch
		mRecordButton = (ImageButton) findViewById(R.id.recButton);
		mRecordButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mRecordButton.setImageResource(R.drawable.stop);
				
				//mStartRecording is a boolean set to true so
				//if true then record audio
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
				if (mStartRecording == false)
				{
					mRecordButton.setImageResource(R.drawable.rec);
				}
				mStartRecording = !mStartRecording;
			}

		});
		//call out the button created in the xml to its java 
    	//on create funtcions which will play audio on touch
		mPlayButton = (ImageButton) findViewById(R.id.playButton);
		mPlayButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//Once the recording starts playing the record
				//image is changed to pause
				mPlayButton.setImageResource(R.drawable.pause);
				Handler handler = new Handler();

				// runnable set up to hold code which will fun after the handler
				Runnable r = new Runnable() {

					@Override
					public void run() {
						
						mPlayButton.setImageResource(R.drawable.play);
					}
				};
				//after the duration of the recording the pause 
				//button image changes to play to replay the audio
				//if wished by the user
				handler.postDelayed(r, duration);
				
				//mStartPlaying is a boolean set to true so
				//if true then play audio
				onPlay(mStartPlaying);
				Toast.makeText(getApplicationContext(),
						"Duration = " + duration, Toast.LENGTH_SHORT).show();
				/////mStartPlaying = !mStartPlaying;
			}
		});
	}
		public void setVisualizer(){
			//Takes in the bytes of a song and displays a static
			//bar graph ranged in different colors depending on 
			//the byte size.
			
			//call the mVisualizer in the xml file with the id
			/////mVisualizer = (MediaController) findViewById(R.id.mediaController1)
			/*mVisualizer = new Visualizer(music.getAudioSessionId());
		    mBytes = null;
			mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
		    Visualizer.OnDataCaptureListener captureListener = new Visualizer.OnDataCaptureListener(){
			    @Override
			    public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate){
			        //PCM DATA
			    	mBytes = bytes;
			    	Paint myPaint = null;
					Bitmap image;
					float x, y;
					int intX, intY, dirX, diY;
					
			    	if(mBytes != null){
			    		//int sum = 0;
			            for(int i = 0; i < bytes.length; i++) {
			                //sum += bytes[i];
			                intX = (int)(bytes[i]);
							intY = (int)(bytes[i]);
			            	if(bytes[i] >0 && bytes[i] < 20)
			            	{
								myPaint.setColor(Color.RED);
								myPaint.setStrokeWidth(5);
			            	}
			            	else if(bytes[i] >= 20 && bytes[i] < 45)
			            	{
								myPaint.setColor(Color.GREEN);
								myPaint.setStrokeWidth(5);
			            	}
			            	else if(bytes[i] >= 45 && bytes[i] < 70)
			            	{
								myPaint.setColor(Color.BLUE);
								myPaint.setStrokeWidth(5);
			            	}
			            	else if(bytes[i] >= 70)
			            	{
								myPaint.setColor(Color.WHITE);
								myPaint.setStrokeWidth(5);
			            	}
			            	
			            	
			            		
			            }
			    	}
			    }
				@Override
			    public void onFftDataCapture(Visualizer visualizer, 
			    							byte[] bytes, int samplingRate){}};
	    
	    
			   	mVisualizer.setDataCaptureListener(captureListener, Visualizer.getMaxCaptureRate() / 2, true, false);
			    // Enabled Visualizer and disable when we're done with the stream
			    mVisualizer.setEnabled(true);
				music.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
			    @Override
			    public void onCompletion(MediaPlayer mediaPlayer){
			    		mVisualizer.setEnabled(false);}
			    });*/
	}

	private MediaController findViewById(MainActivity mainActivity,
				int mediacontroller1) {
			// TODO Auto-generated method stub
			return null;
		}
	private void onRecord(boolean start) {
		//start is a boolean and these private 
		//member funtions are called in the 
		//on create of the buttons to start
		//and stop recording
		if (start) {
			startRecording();
		} else {
			stopRecording();
		}
	}

	private void onPlay(boolean start) {
		//start is a boolean and these private
		//member funtions are called in the
		//on create of the buttons to start
		//and stop recording
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
					//get duration of the audio
					Log.d("Duration of Recording", "" + duration);
				} catch (IOException e) {
					Log.e(LOG_TAG, "prepare() failed");
				} finally {
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
		//mRecordeer is a variable of MediaRecorder set to
		//locate the MIC annd record the audio and play it back
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