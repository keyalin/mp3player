package com.example.mp3player;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import service.DownloadService;

import mp3model.Mp3Info;

import download.HttpDownloader;
import Mp3XML.MP3ListContentHandler;
import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Mp3ListActivity extends ListActivity {
	
	private static final int UPDATE = 1;
	private static final int ABOUT = 2;
	List<Mp3Info> mp3Infos = null;
	
	/**
	 * pop up a menu when menu is clicked. And we can add several item for this popped menu
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remote_mp3_list);
		
		this.UpdateListView();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		
		menu.add(0, UPDATE, 1, R.string.mp3list_update);
		menu.add(0, ABOUT, 2, R.string.mp3list_about);
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		System.out.println(item.getItemId());
		
		if(item.getItemId() == UPDATE){
			this.UpdateListView();
		}
		else if(item.getItemId() == ABOUT){
			
		}
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
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
	
	public void UpdateListView(){		
		String url = ("http://192.168.2.5:8080/music/resources.xml");
		ExecutorService exc = Executors.newFixedThreadPool(1);
		String xml = null;
		try {
		
			//TimeUnit.MILLISECONDS.sleep(1000);
			Future result = exc.submit(new Download(url));
			xml = result.get(1000, TimeUnit.MILLISECONDS).toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Line 95: " + e);
		}
		System.out.println(xml);
		mp3Infos = this.ParseXML(xml);
		SimpleAdapter adapter = BuildSimpleAdapter(mp3Infos);
		Mp3ListActivity.this.setListAdapter(adapter);
	}
	
	public String DownloadXML(String urlStr){
		HttpDownloader  downloader = new HttpDownloader();
		return downloader.DownloadString(urlStr);
	}
	
	public List<Mp3Info> ParseXML(String xml){
		SAXParserFactory factory = SAXParserFactory.newInstance();
		List<Mp3Info> list = null;
		try{
			list = new ArrayList<Mp3Info>();
			XMLReader reader = factory.newSAXParser().getXMLReader();
			MP3ListContentHandler handler = new MP3ListContentHandler(list);
			reader.setContentHandler(handler);
			reader.parse(new InputSource(new StringReader(xml)));
			for(Mp3Info mp3 : list){
				System.out.println(mp3);
			}
		}catch(Exception e){
			System.out.println(e);
		}
		return list;
	}
	
	class Download implements Callable{
		private String url;
		Download(String url){
			this.url = url;
		}

		@Override
		public Object call(){
			return DownloadXML(url);
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		Mp3Info info = mp3Infos.get(position);
		System.out.println("mp3 clicked: " + info);
		Intent intent = new Intent();
		intent.putExtra("Mp3Info", info);
		intent.setClass(this, DownloadService.class);
		this.startService(intent);
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
	}
	
//	protected void onResume() {
//		if(mp3Infos == null){
//			this.UpdateListView();
//		}else{
//			SimpleAdapter adapter = BuildSimpleAdapter(mp3Infos);
//			this.setListAdapter(adapter);
//		}
//		// TODO Auto-generated method stub
//		super.onResume();
//	}

}
