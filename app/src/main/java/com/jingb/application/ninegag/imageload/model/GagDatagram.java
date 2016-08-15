package com.jingb.application.ninegag.imageload.model;

import android.database.Cursor;

import com.google.gson.Gson;
import com.jingb.application.ninegag.imageload.dao.GagDatagramHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


public class GagDatagram extends BaseModel {

    private static final HashMap<String, GagDatagram> CACHE = new HashMap<String, GagDatagram>();

    public String id;
    public String caption;
    public String link;
    public Image images;
    public Vote votes;
    public Media media;
    public Comments comments;

    public class Image {
        public String small;
        public String normal;
        public String large;
    }

    private class Vote {
        public int count;
    }

    public class Media implements Serializable {
        public String mp4;
        public String webm;
        public boolean hasMedia = true;
    }

    public class Comments {
        public int count;
    }

    private static void addToCache(GagDatagram gagDatagram) {
        CACHE.put(gagDatagram.id, gagDatagram);
    }

    private static GagDatagram getFromCache(String id) {
        return CACHE.get(id);
    }

    public static GagDatagram fromJson(String json) {
        return new Gson().fromJson(json, GagDatagram.class);
    }

    public static GagDatagram fromCursor(Cursor cursor) {
        String id = cursor.getString(cursor.getColumnIndex(GagDatagramHelper.GagDatagramDBinfo.ID));
        GagDatagram gagDatagram = getFromCache(id);
        if (gagDatagram != null) {
            return gagDatagram;
        }
        gagDatagram = new Gson().fromJson(
                cursor.getString(cursor.getColumnIndex(GagDatagramHelper.GagDatagramDBinfo.JSON)),
                GagDatagram.class);
        addToCache(gagDatagram);
        return gagDatagram;
    }

    public static class GagDatagramRequestData {
        public ArrayList<GagDatagram> data;
        public Paging paging;

        public String getPage() {
            return paging.next;
        }
    }

    private class Paging {
        public String next;
    }
}
