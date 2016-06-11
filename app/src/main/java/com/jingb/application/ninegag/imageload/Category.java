package com.jingb.application.ninegag.imageload;

public enum Category {
    hot("hot"), trending("trending"), fresh("fresh");
    private String mDisplayName;

    Category(String displayName) {
        mDisplayName = displayName;
    }

    public String getDisplayName() {
        return mDisplayName;
    }
}
