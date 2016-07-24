package com.shomen.smn.eyeprotector;

public enum UserProfile {
    DEFAULT("1"),
    NIGHT_MODE("2"),
    CUSTOM("3");

    private String val;

    UserProfile(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
