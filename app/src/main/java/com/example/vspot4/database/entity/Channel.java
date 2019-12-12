package com.example.vspot4.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

@Entity
public class Channel {

    @PrimaryKey
    @NonNull
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("display_time")
    @Expose
    private String display_time;

    @SerializedName("transition_time")
    @Expose
    private String transition_time;

    @SerializedName("refresh_time")
    @Expose
    private String refresh_time;

    @SerializedName("screens")
    @Expose
    private List<Screen> screens;

    private Date lastRefresh;

    public Channel(@NonNull String name, String description, String display_time,
                   String transition_time, List<Screen> screens, String refresh_time) {
        this.name = name;
        this.description = description;
        this.display_time = display_time;
        this.transition_time = transition_time;
        this.screens = screens;
        this.refresh_time = refresh_time;
    }


    // --- GETTER ---

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplay_time() {
        return display_time;
    }

    public void setDisplay_time(String display_time) {
        this.display_time = display_time;
    }

    public String getTransition_time() {
        return transition_time;
    }


    // --- SETTER ---

    public void setTransition_time(String transition_time) {
        this.transition_time = transition_time;
    }

    public String getRefresh_time() {
        return refresh_time;
    }

    public void setRefresh_time(String refresh_time) {
        this.refresh_time = refresh_time;
    }

    public List<Screen> getScreens() {
        return screens;
    }

    public void setScreens(List<Screen> screens) {
        this.screens = screens;
    }

    public Date getLastRefresh() {
        return lastRefresh;
    }

    public void setLastRefresh(Date lastRefresh) {
        this.lastRefresh = lastRefresh;
    }
}

