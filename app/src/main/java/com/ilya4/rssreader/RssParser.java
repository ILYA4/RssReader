package com.ilya4.rssreader;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RssParser {

    public static List<Entity> parseNews (InputStream inputStream) throws XmlPullParserException, IOException{
        List<Entity> items = new ArrayList<>();
        boolean isItem = false;
        String title =  null;
        String image = null;
        String link = null;
        String pubDate = null;
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(inputStream, null);
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            while (parser.next() != XmlPullParser.END_DOCUMENT){
                int eventType = parser.getEventType();


                String name = parser.getName(); // name of tag
                if (name == null) continue;

                if (eventType == XmlPullParser.END_TAG){
                    if (parser.getName().equalsIgnoreCase("item")){
                        isItem = false;
                    }
                    continue;
                }


                if (eventType == XmlPullParser.START_TAG){

                    if (parser.getName().equalsIgnoreCase("item")){
                        isItem = true;
                        continue;
                    }


                }

                Log.v("Parsing this ++", name);
                String res = "";
                if (name.equalsIgnoreCase("content:encoded") || name.equalsIgnoreCase("description")){

                    if (parser.nextToken() == XmlPullParser.CDSECT){

                        image = getImageUrl(parser.getText());
                        if (image.equals("http://feeds.feedburner.com/~ff/Techcrunch?d=2mJPEYqXBVI")){
                            Log.v("Check", image+" "+image.equals("http://feeds.feedburner.com/~ff/Techcrunch?d=2mJPEYqXBVI"));
                            image = "";
                        }
                    }else parser.next();
                    parser.nextTag();
                    continue;
                }

                if(parser.next() ==XmlPullParser.TEXT){
                    res = parser.getText(); //get text in tag inside
                    parser.nextTag();
                }

                if (name.equalsIgnoreCase("title")){
                    title = res;
                }else if (name.equalsIgnoreCase("link")){
                    link = res;
                }else if (name.equalsIgnoreCase("pubDate")){
                    pubDate = res;
                }

                if (title!=null && link!=null && pubDate!=null && image!=null){
                    if (isItem){
                        Entity item = new Entity(image, title, link, pubDate);
                        items.add(item);
                    }
                    title = null;
                    link = null;
                    image = null;
                    pubDate = null;
                    isItem = false;
                }

            }
            return items;
        }finally {
            inputStream.close();
        }
    }

    private static String getImageUrl(String cdata) throws XmlPullParserException, IOException{
        XmlPullParser parser = Xml.newPullParser();
        InputStream in = new ByteArrayInputStream(cdata.getBytes(StandardCharsets.UTF_8));
        parser.setInput(in, null);
        try{
        while (parser.next() != XmlPullParser.END_DOCUMENT){

            int eventType = parser.getEventType();

            String name = parser.getName(); // name of tag
            if (name == null) continue;

            if (eventType == XmlPullParser.START_TAG){
                if (name.equalsIgnoreCase("img")){
                    return parser.getAttributeValue(null, "src");
                }
            }
        }
        }catch (XmlPullParserException e){
            return "";
        }

        return "";
    }


}
