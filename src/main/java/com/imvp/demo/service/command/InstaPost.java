package com.imvp.demo.service.command;

import java.time.Instant;

public class InstaPost {

    private final PostType type;
    private final PostMimeType mimeType;
    private final String itemId;
    private final Instant publishTime;


    public InstaPost(PostType type, PostMimeType mimeType, String itemId, Instant publishTime) {
        this.type = type;
        this.mimeType = mimeType;
        this.itemId = itemId;
        this.publishTime = publishTime;
    }

    public PostType getType() {
        return type;
    }

    public PostMimeType getMimeType() {
        return mimeType;
    }

    public Instant getPublishTime() {
        return publishTime;
    }

    public String getItemId() {
        return itemId;
    }

    public enum PostType {
        STORY, ITEM
    }

    public enum PostMimeType {
        PHOTO, VIDEO
    }
}
