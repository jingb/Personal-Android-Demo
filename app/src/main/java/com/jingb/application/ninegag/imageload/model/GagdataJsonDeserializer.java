package com.jingb.application.ninegag.imageload.model;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by jingb on 16/7/18.
 *
 * the Media field will be an Object if it has data,
 * and boolean if it has no data.
 * So this Custom JsonDeserializer is to handle this situation
 * And actually such dynamic data type is a bad design and should be avoided
 */
public class GagdataJsonDeserializer implements JsonDeserializer<GagDatagram.Media> {

    @Override
    public GagDatagram.Media deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        GagDatagram.Media media = null;
        if (json.isJsonObject()) {
            media = new Gson().fromJson(json, GagDatagram.Media.class);
            media.hasMedia = true;
        } else {
            media = new GagDatagram().new Media();
            media.hasMedia = false;
        }
        return media;
    }

}
