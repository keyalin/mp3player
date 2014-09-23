package service;

import download.HttpDownloader;
import mp3model.Mp3Info;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class DownloadService extends Service{

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Mp3Info info = (Mp3Info) intent.getSerializableExtra("Mp3Info");
		System.out.println("Service---> " + info);
		DownloadThread task = new DownloadThread(info);
		Thread thread = new Thread(task);
		thread.start();
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	class DownloadThread implements Runnable{
		Mp3Info info;
		DownloadThread(Mp3Info info){
			this.info = info;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String mp3Url = "http://192.168.2.5:8080/music/" + info.getMp3Name();
			String lrcUrl = "http://192.168.2.5:8080/music/" + info.getLrcName();
			HttpDownloader downloader = new HttpDownloader();
			int g = downloader.DownloadFile(lrcUrl, "mp3/", info.getLrcName());
			System.out.println("g == " + g);
			int result = downloader.DownloadFile(mp3Url, "mp3/", info.getMp3Name());
			if(result == 1){
				System.out.println("file exists already");
				//Toast.makeText(getApplicationContext(), "file exists already", Toast.LENGTH_LONG).show();
			}else if(result == 0){
				System.out.println("Download success");
				//Toast.makeText(getApplicationContext(), "Download success", Toast.LENGTH_LONG).show();
			}else if(result == -1){
				//Toast.makeText(getApplicationContext(), "Download failed", Toast.LENGTH_LONG).show();			
				System.out.println("Download failed");
			}
		}
		
	}
}
