package com.jingb.application.ninegag;

/**
 * Created by jingb on 16/3/19.
 */
public class NineGagDatagram {
    String status;
    String message;
    NineGagPaging paging;
    NineGagImageDatagram[] data;

    public NineGagImageDatagram[] getData() {
        return data;
    }

    public void setData(NineGagImageDatagram[] data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NineGagPaging getPaging() {
        return paging;
    }

    public void setPaging(NineGagPaging paging) {
        this.paging = paging;
    }

}
