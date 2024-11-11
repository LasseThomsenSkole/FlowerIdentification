package org.example.floweridentification.dto;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ImageURL {
    private String url;

    // Constructor
    public ImageURL(String url) {
        this.url = url;
    }

    // Getter and Setter
    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }
}


