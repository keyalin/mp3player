package com.example.mp3player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import service.DownloadService;

import mp3model.Mp3Info;

import download.HttpDownloader;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class LocalMp3ListActivity extends ListActivity {
	List<Mp3Info> mp3Infos = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_local_mp3_list);
		
//		this.UpdateView();
	}
	
	public void UpdateView(){
		System.out.println("local line 29");
		HttpDownloader downloader = new HttpDownloader();
		File[] dir = downloader.GetFileList("mp3/");
		mp3Infos = new ArrayList<Mp3Info>();
		
		for(File file : dir){
			System.out.println("Local: " + file.getName());
			if(file.getName().endsWith("mp3")){
				Mp3Info info = new Mp3Info();
				info.setMp3Name(file.getName());
				info.setMp3Size(file.length() + "");
				mp3Infos.add(info);
			}
		}
		SimpleAdapter adapter = BuildSimpleAdapter(mp3Infos);
		this.setListAdapter(adapter);
	}
	
	
	public SimpleAdapter BuildSimpleAdapter(List<Mp3Info> infos){
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		for(Mp3Info info : infos){
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("mp3_name", info.getMp3Name());
			map.put("mp3_size", info.getMp3Size());
			list.add(map);
		}
		SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.mp3info_item, 
				new String[]{"mp3_name", "mp3_size"}, new int[]{R.id.mp3_name, R.id.mp3_size});
		return adapter;
	}

	@Override
	protected void onResume() {
		this.UpdateView();
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		Mp3Info info = mp3Infos.get(position);
		System.out.println("mp3 clicked: " + info);
		Intent intent = new Intent();
		intent.putExtra("Mp3Info", info);
		intent.setClass(this, PlayerActivity.class);
		this.startActivity(intent);
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
	}
}
