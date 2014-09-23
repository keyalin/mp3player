package com.example.mp3player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Queue;

import service.PlayerService;

import mp3model.Mp3Info;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class PlayerActivity extends Activity {
	private boolean isPlaying = false;
	private Mp3Info mp3 = null;
	private Long begin = 0L, currentTime = 0L, nextTime =0L, pauseTime = 0L;
	private Queue<String> messages = null;
	private Queue<Long> timepoints = null;
	private Handler handler = new Handler();
	private Runnable updatelrc = new UpdateLrc();
	private String message;
	private TextView lrcfield = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		
		Intent intent = this.getIntent();
		mp3 = (Mp3Info) intent.getSerializableExtra("Mp3Info");
		System.out.println("Player line 19: " + mp3);
		
		lrcfield = (TextView)this.findViewById(R.id.lrcField);
		
	}

	public void BeginPlay(View view){
		Intent intent = new Intent();
		intent.putExtra("Mp3Info", mp3);

		intent.putExtra("MSG", AppConstant.PlayerMsg.PLAY_MSG);
		intent.setClass(this, PlayerService.class);
		PrepareLrc();
		begin = System.currentTimeMillis();
		this.startService(intent);
		if(this.messages != null){
			System.out.println("begin handler");
			handler.postDelayed(updatelrc, 5);
		}
		isPlaying  = true;
		currentTime = 0L;
		nextTime = 0L;
	}
	
	public void PrepareLrc(){
		InputStream input = null;
		try{
			String mp3Name = mp3.getMp3Name();
			String lrcfileName = mp3Name.substring(0, mp3Name.length() - 4) + ".lrc"; 
			input = new FileInputStream(this.GetMP3Path() + lrcfileName);
			LrcProcessor lrcs = new LrcProcessor();
			lrcs.Process(input);
			this.messages = lrcs.getMessages();
			this.timepoints = lrcs.getTimMills();
			System.out.println("length messages: " + messages.size());
			System.out.println("length timepoints: " + timepoints.size());
		}catch(Exception e){
			this.messages = null;
			this.timepoints = null;
			try {
				input.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	public void PausePlay(View view){
		Intent intent = new Intent();
		intent.putExtra("MSG", AppConstant.PlayerMsg.PAUSE_MSG);
		intent.setClass(this, PlayerService.class);
		this.startService(intent);
		if(isPlaying){
			if(this.messages != null){
				handler.removeCallbacks(updatelrc);
			};
			pauseTime = System.currentTimeMillis();			
			isPlaying = false;
		}else{
			if(this.messages != null){
				handler.postDelayed(updatelrc, 5);
			}
			begin = System.currentTimeMillis() - pauseTime + begin;

			isPlaying = true;
		}
	}
	
	public void StopPlay(View view){
		Intent intent = new Intent();
		intent.putExtra("MSG", AppConstant.PlayerMsg.STOP_MSG);
		intent.setClass(this, PlayerService.class);
		this.startService(intent);
		if(this.messages != null){
			handler.removeCallbacks(updatelrc);
		};
		isPlaying = false;
	}

	private String GetMP3Path(){
		String sdpath = Environment.getExternalStorageDirectory() + File.separator;
		return sdpath + "mp3/";
	}

	class UpdateLrc implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Long offset = System.currentTimeMillis() - begin;
			if(currentTime == 0){
				nextTime = (Long)timepoints.poll();
				message = (String)messages.poll();
			}
			if(offset >= nextTime){
				System.out.println("offset: " + offset);
				System.out.println("nextTime: " + nextTime);
				lrcfield.setText(message);
				message = (String)messages.poll();
				nextTime = (Long)timepoints.poll();
			}
			currentTime += 5;
			handler.postDelayed(updatelrc, 5);
		}
		
	}
}
