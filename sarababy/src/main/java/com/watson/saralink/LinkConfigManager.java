package com.watson.saralink;

public class LinkConfigManager {

    private static LinkConfigManager instance;

    private LinkConfig linkConfig;

    public static LinkConfigManager getInstance() {
        if(null == instance) {
            instance = new LinkConfigManager();
        }
        return instance;
    }

    public LinkConfig getLinkConfig() {
        return linkConfig;
    }

    public void setLinkConfig(LinkConfig linkConfig) {
        this.linkConfig = linkConfig;
    }
}
