package com.jingb.application.ninegag.imageload.model;

import java.io.Serializable;

public enum Category implements Serializable {
    hot("hot"), trending("trending"), fresh("fresh");
    private String mDisplayName;

    Category(String displayName) {
        mDisplayName = displayName;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public static Category getCategory(String category) {
        for (Category item : values()) {
            if (item.getDisplayName().equalsIgnoreCase(category)) {
                return item;
            }
        }
        return hot;
    }
}
