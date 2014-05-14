package com.example.audiomanip;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

public class Splash extends Activity{
	MediaPlayer ourSong;

	@Override
	protected void onCreate(Bundle SplashScreen) {
		// TODO Auto-generated method stub
		super.onCreate(SplashScreen);
		setContentView(R.layout.splash);
		
		ourSong = MediaPlayer.create(this, R.raw.sci_fi_engine_shut_down);
		ourSong.start();
		Thread timer = new Thread(){
			public void run(){
				try{
					sleep(4000);
				}catch(InterruptedException e){
					e.printStackTrace();
				}finally{
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
