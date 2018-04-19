package com.ilya4.rssreader;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Entity implements Comparable<Entity>{

    private String image;
    private String title;
    private String link;
    private Date pubDate;



    public Entity(String image, String title, String link, String pubDate) {
        this.image = image;
        this.title = title;
        this.link = link;
        DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        Date date = new Date();
        try {
            date = format.parse(pubDate);
        }catch (ParseException e){
            e.printStackTrace();
        }
        this.pubDate = date;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }


    @Override
    public int compareTo(@NonNull Entity o) {
        return pubDate.compareTo(o.pubDate);
    }
}
