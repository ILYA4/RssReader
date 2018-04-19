package com.ilya4.rssreader;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.ilya4.rssreader.adapter.CardviewArticleAdapter;

import org.xmlpull.v1.XmlPullParserException;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Entity> items =  new ArrayList<>();
    private RecyclerView topPanel;
    private final String url1 = "https://lifehacker.com/rss";
    private final String url2 = "http://feeds.feedburner.com/TechCrunch/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        topPanel = (RecyclerView) findViewById(R.id.top_panel);
        topPanel.setLayoutManager(new LinearLayoutManager(this));
        new ParseFeedTask(url1, url2).execute();
    }



    private class ParseFeedTask extends AsyncTask<Void, Void , Boolean>{

        private String urlLink1;
        private String urlLink2;

        public ParseFeedTask(String url1, String url2) {
            this.urlLink1 = url1;
            this.urlLink2 = url2;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            try{
                URL url1 = new URL(urlLink1);
                InputStream inputStream1 = url1.openConnection().getInputStream();
                URL url2= new URL(urlLink2);
                InputStream inputStream2 = url2.openConnection().getInputStream();
                Log.v("Stream", inputStream1.toString());
                List<Entity> items1 = RssParser.parseNews(inputStream1);
                Log.v("SizeItems", String.valueOf(items1.size()));
                List<Entity> items2 = RssParser.parseNews(inputStream2);
                items.addAll(items1);
                items.addAll(items2);
                Collections.sort(items, Collections.<Entity>reverseOrder());
                return true;
            }catch (XmlPullParserException e){
                Log.e("XmlPullParserError" , e.getMessage());
            }catch (Exception e){
                Log.e("UrlError", e.getMessage());
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success){
                topPanel.setAdapter(new CardviewArticleAdapter(items, MainActivity.this));
            }
        }
    }
}
