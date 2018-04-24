package com.ilya4.rssreader


import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Entity(val image: String, val title: String, val link: String, pubDate: String) : Comparable<Entity> {
    private val pubDate: Date


    init {
        val format = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)
        var date = Date()
        try {
            date = format.parse(pubDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        this.pubDate = date
    }


    override fun compareTo(o: Entity): Int {
        return pubDate.compareTo(o.pubDate)
    }
}
