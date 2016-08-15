package com.jingb.application.ninegag.imageload.model;

public enum Category {
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
