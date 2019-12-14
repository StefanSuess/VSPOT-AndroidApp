package com.example.vspot4.database.entity;

import androidx.annotation.Nullable;
import androidx.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class Screen {

    @Nullable
    @SerializedName("html_block")
    @Expose
    private String html_block;

    @SerializedName("background_color")
    @Expose
    private String background_color;

    @SerializedName("bg_img_cdn_link")
    @Expose
    private String bg_img_cdn_link;

    @SerializedName("overlay_color")
    @Expose
    private String overlay_color;

    @SerializedName("text_color")
    @Expose
    private String text_color;

    @SerializedName("heading")
    @Expose
    private String heading;

    @SerializedName("subheading")
    @Expose
    private String subheading;

    @SerializedName("text_block")
    @Expose
    private String text_block;

    @SerializedName("layout_name")
    @Expose
    private String layout_name;


    public Screen(String background_color, String bg_img_cdn_link, String overlay_color
            , String text_color, String heading, String subheading, String text_block
            , String layout_name, @Nullable String html_block) {
        this.html_block = html_block;
        this.background_color = background_color;
        this.bg_img_cdn_link = bg_img_cdn_link;
        this.overlay_color = overlay_color;
        this.text_color = text_color;
        this.heading = heading;
        this.subheading = subheading;
        this.text_block = text_block;
        this.layout_name = layout_name;
    }


    @Nullable
    public String getHtml_block() {
        return html_block;
    }

    public String getBackground_color() {
        return background_color;
    }

    public void setBackground_color(String background_color) {
        this.background_color = background_color;
    }

    public String getBg_img_cdn_link() {
        return bg_img_cdn_link;
    }

    public void setBg_img_cdn_link(String bg_img_cdn_link) {
        this.bg_img_cdn_link = bg_img_cdn_link;
    }

    public String getOverlay_color() {
        return overlay_color;
    }

    public void setOverlay_color(String overlay_color) {
        this.overlay_color = overlay_color;
    }

    public String getText_color() {
        return text_color;
    }

    public void setText_color(String text_color) {
        this.text_color = text_color;
    }

    public void setHtml_block(@Nullable String html_block) {
        this.html_block = html_block;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getSubheading() {
        return subheading;
    }

    public void setSubheading(String subheading) {
        this.subheading = subheading;
    }

    public String getText_block() {
        return text_block;
    }

    public void setText_block(String text_block) {
        this.text_block = text_block;
    }

    public String getLayout_name() {
        return layout_name;
    }

    public void setLayout_name(String layout_name) {
        this.layout_name = layout_name;
    }

}
