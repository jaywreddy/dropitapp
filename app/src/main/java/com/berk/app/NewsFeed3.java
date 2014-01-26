package com.berk.app;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class NewsFeed3 extends Activity {
    String p1 = "http://www.pachd.com/free-images/los-angeles/santa-monica-sunset-01.jpg";
    String p2 = "http://upload.wikimedia.org/wikipedia/commons/c/cb/Sunset_in_santa_monica.jpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed3);
       // ArrayList<Drawable> d = getIms(p1, p2);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(NewsFeed3.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.news_feed3, menu);
        return true;
    }

    public static ArrayList<Drawable> getIms(String... urls){
        ArrayList<Drawable> output = new ArrayList<Drawable>();
        try{
            for(String s: urls){
                output.add(LoadImageFromWebOperations(s));
            }
        }
        catch( Exception e){ e.printStackTrace();}
        return output;
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
}
