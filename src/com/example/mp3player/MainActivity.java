package com.example.mp3player;

import android.os.Bundle;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.widget.TabHost;

public class MainActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
//		Intent intent = new Intent(this, Mp3ListActivity.class);
//		this.startActivity(intent);
		
		TabHost host = getTabHost();
		Intent remoteIntent = new Intent();
		remoteIntent.setClass(this, Mp3ListActivity.class);
		TabHost.TabSpec remoteSpec = host.newTabSpec("remote");
		remoteSpec.setIndicator("remote");
		remoteSpec.setContent(remoteIntent);
		host.addTab(remoteSpec);
		
		Intent localIntent = new Intent();
		localIntent.setClass(this, LocalMp3ListActivity.class);
		TabHost.TabSpec localSpec = host.newTabSpec("local");
		localSpec.setIndicator("local");
		localSpec.setContent(localIntent);
		host.addTab(localSpec);
	}

}
