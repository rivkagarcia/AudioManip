package com.example.audiomanip;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

public class Splash extends Activity{
	//Creates an opening splash screen that
	//will display an image, text and opening 
	//sound for a breif amount of time
	MediaPlayer ourSong;

	@Override
	protected void onCreate(Bundle SplashScreen) {
		// TODO Auto-generated method stub
		super.onCreate(SplashScreen);
		setContentView(R.layout.splash);
		
		//calls song from the raw folder located in resources file
		ourSong = MediaPlayer.create(this, R.raw.sci_fi_engine_shut_down);
		//start playing song
		ourSong.start();
		Thread timer = new Thread(){
			public void run(){
				try{
					//set screen to 4 seconds
					sleep(4000);
				}catch(InterruptedException e){
					e.printStackTrace();
				}finally{
					//after splash screen ends it moves onto opening up
					//main activity where the recording and playback take place
					Intent openMainActivity = new Intent(Splash.this, MainActivity.class);
					Splash.this.startActivity(openMainActivity);
					Splash.this.finish();
						
				}
			}
	};
	timer.start();
}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ourSong.release();
		finish();
	}

}
