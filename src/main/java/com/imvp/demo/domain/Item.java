package com.imvp.demo.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

import com.imvp.demo.domain.enumeration.Status;

/**
 * A Item.
 */
@Document(collection = "item")
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("status")
    private Status status;

    @Field("tags")
    private String tags;

    @NotNull
    @Field("text")
    private String text;

    @NotNull
    @Field("publish_time")
    private Instant publishTime;

    
    @Field("content")
    private byte[] content;

    @Field("content_content_type")
    private String contentContentType;

    @DBRef
    @Field("owner")
    @JsonIgnoreProperties("items")
    private User owner;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public Item status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getTags() {
        return tags;
    }

    public Item tags(String tags) {
        this.tags = tags;
        return this;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getText() {
        return text;
    }

    public Item text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Instant getPublishTime() {
        return publishTime;
    }

    public Item publishTime(Instant publishTime) {
        this.publishTime = publishTime;
        return this;
    }

    public void setPublishTime(Instant publishTime) {
        this.publishTime = publishTime;
    }

    public byte[] getContent() {
        return content;
    }

    public Item content(byte[] content) {
        this.content = content;
        return this;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentContentType() {
        return contentContentType;
    }

    public Item contentContentType(String contentContentType) {
        this.contentContentType = contentContentType;
        return this;
    }

    public void setContentContentType(String contentContentType) {
        this.contentContentType = contentContentType;
    }

    public User getOwner() {
        return owner;
    }

    public Item owner(User user) {
        this.owner = user;
        return this;
    }

    public void setOwner(User user) {
        this.owner = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Item)) {
            return false;
        }
        return id != null && id.equals(((Item) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Item{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", tags='" + getTags() + "'" +
            ", text='" + getText() + "'" +
            ", publishTime='" + getPublishTime() + "'" +
            ", content='" + getContent() + "'" +
            ", contentContentType='" + getContentContentType() + "'" +
            "}";
    }
}
