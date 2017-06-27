package com.example.hp_06.foregroundmediaplayer;

/**
 * Created by HP-06 on 2/19/2016.
 */
public class Track {
    private String title;
    private String thumbnailUrl;




    public  String getArtworkURL() {
        return mArtworkURL;
    }

    public void setArtworkURL(String mArtworkURL) {
        this.mArtworkURL = mArtworkURL;
    }

    private String mArtworkURL;
    private String streamUrl;


    public Track() {
    }

    public Track(String name, String thumbnailUrl, String streamUrl, String mArtworkURL
    ) {
        this.title = name;
        this.thumbnailUrl = thumbnailUrl;
        this.streamUrl= streamUrl;
        this.mArtworkURL = mArtworkURL;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }


}
