package com.example.vspot4.database.entity;

import com.google.gson.annotations.SerializedName;

public class ChannelTimestamp {

    @SerializedName("lastUpdate")
    private String lastupdate;

    public ChannelTimestamp(String lastupdate) {
        this.lastupdate = lastupdate;
    }

    public String getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(String lastupdate) {
        this.lastupdate = lastupdate;
    }

}
