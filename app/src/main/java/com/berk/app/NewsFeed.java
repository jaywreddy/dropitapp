package com.berk.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.berk.app.MainActivity.Access;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.PushService;

public class NewsFeed extends Activity {
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_feed);
		String command = "getDrops?lat=1&lng=4&num=2";
		ArrayList<String> vals = new ArrayList<String>();
		AsyncTask t = new MainActivity.Access();
		//this puts what values should be in the newsfeed into vals
		t.execute(command, vals);
		try {
			t.get();
		} 
		catch (Exception e) {
			Log.d("error6", e.toString());
			return;
		} 
		this.makeList(vals);
	}
	
	public void makeList(ArrayList<String> comments){
		List<Map<String, String>> commentList = new ArrayList<Map<String,String>>();
		for(String com: comments){
			commentList.add(createComment("k", com));
		}
		ListView lv = (ListView) findViewById(R.id.list1);
        SimpleAdapter simpleAdpt = new SimpleAdapter(this, commentList, android.R.layout.simple_list_item_1, 
        new String[] {"k"}, new int[] {android.R.id.text1});
        lv.setAdapter(simpleAdpt);
	}
	
	private HashMap<String, String> createComment(String key, String name) {
		    HashMap<String, String> comment = new HashMap<String, String>();
		    comment.put(key, name);
		    return comment;
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.news_feed, menu);
		return true;
	}

}
