package com.jingb.application.ninegag;

/**
 * Created by jingb on 16/3/19.
 */
public class NineGagImageDatagram {

    String id;
    String caption;
    NineGagImageUrls images;

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public NineGagImageUrls getImages() {
        return images;
    }

    public void setImages(NineGagImageUrls images) {
        this.images = images;
    }
}
