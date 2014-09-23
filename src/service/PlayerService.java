package service;

import java.io.File;

import com.example.mp3player.AppConstant;

import mp3model.Mp3Info;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;

public class PlayerService extends Service{
	private MediaPlayer player = null;
	private boolean isPause = false, isPlaying = false, isReleased = false;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Mp3Info mp3 = (Mp3Info) intent.getSerializableExtra("Mp3Info");
		System.out.println("service mp3" + mp3);
		int msg = intent.getIntExtra("MSG", 0);
		if(mp3 != null){
			Stop();
			if(msg == AppConstant.PlayerMsg.PLAY_MSG){
				System.out.println("line1: 32");
				Play(mp3);
			}
		}else{
			if(msg == AppConstant.PlayerMsg.PAUSE_MSG){
				Pause();
			}else if(msg == AppConstant.PlayerMsg.STOP_MSG){
				Stop();
			}
		}
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	public void Play(Mp3Info mp3){
		String path = GetMP3Path(mp3);
		player = MediaPlayer.create(this, Uri.parse("file://" + path));
		player.setLooping(false);
		player.start();
		isPlaying = true;
		isReleased = false;
		isPause = false;		
	}
	
	public void Pause(){
		if(player != null){
			if(!isReleased){
				if(!isPause){
					player.pause();
					isPause = true;
					isPlaying = false;
				}
				else{
					player.start();
					isPause = false;
					isPlaying = true;
				}
			}
		}		
	}
	public void Stop(){
		if(player != null){
			if(!isReleased){
				player.stop();
				player.release();
				isReleased = true;
				isPlaying = false;
				isPause = false;
			}
		}		
	}
	private String GetMP3Path(Mp3Info mp3){
		String sdpath = Environment.getExternalStorageDirectory() + File.separator;
		return sdpath + "mp3/" + mp3.getMp3Name();
	}
}
