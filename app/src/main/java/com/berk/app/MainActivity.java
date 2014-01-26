package com.berk.app;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.CursorJoiner.Result;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	 
	/** Called when the user clicks the Send button */
	public void sendMessage(View view) {
	    // Do something in response to button
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String message = editText.getText().toString();
		intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
	}
	
	public void goLogin(View view) {
	    // Do something in response to button
		Intent intent = new Intent(this, Login.class);
		startActivity(intent);
	}
	
	public void gps(View view) {
	    // Do something in response to button
		Intent intent = new Intent(this, Gps.class);
		startActivity(intent);
	}
	
	public void camera(View view) {
	    // Do something in response to button
		Intent intent = new Intent(this, Camera.class);
		startActivity(intent);
	}
	
	public void newsfeed(View view) {
	    // Do something in response to button
		Intent intent = new Intent(this, NewsFeed.class);
		startActivity(intent);
	}

    public static class Access extends AsyncTask {
        @Override
        //This i where i get json objects from the server and also where i post them
        protected Object doInBackground(Object... urls) {
            String site = "http://warm-ridge-1785.herokuapp.com/";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost request = new HttpPost( site + (String) urls[1]);
            //ResponseHandler<String> handler = new BasicResponseHandler();
            HttpResponse result;
            String todo = (String) urls[0];
            try {
                if(todo.equals("comment") || todo.equals("picture")){
                    StringEntity s = new StringEntity(urls[2].toString());
                    request.setEntity(s);
                }
                result = httpclient.execute(request);
                if(todo.equals("comnews")){
                    ArrayList<String> vals = (ArrayList<String>) urls[2];
                    JSONArray jsons = new JSONArray(result);
                    int n = jsons.length();
                    for(int i = 0; i < n; i++){
                        JSONObject id = jsons.getJSONObject(i);
                        vals.add(id.getString("u'_id'"));
                    }
                }
            }
            catch (Exception e) {
                Log.d("error5", e.toString());
                return null;
            }
            Log.d("done", result.toString());
            httpclient.getConnectionManager().shutdown();
            return null;
        }

    }

    public void newsfeed3(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, NewsFeed3.class);
        startActivity(intent);
    }

	public void sendserver(View view){
		//forget this its old
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String message = editText.getText().toString();
		new Access().execute(message);
	}
}