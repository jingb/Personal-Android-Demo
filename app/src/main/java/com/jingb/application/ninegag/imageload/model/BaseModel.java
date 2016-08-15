package com.jingb.application.ninegag.imageload.model;

import com.google.gson.Gson;

public abstract class BaseModel {
    public String toJson() {
        return new Gson().toJson(this);
    }
}
