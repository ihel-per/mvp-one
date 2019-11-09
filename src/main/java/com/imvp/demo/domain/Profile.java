package com.imvp.demo.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Profile.
 */
@Document(collection = "profile")
public class Profile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("nick")
    private String nick;

    @NotNull
    @Field("sencha")
    private String sencha;

    @DBRef
    @Field("owner")
    @JsonIgnoreProperties("profiles")
    private User owner;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public Profile nick(String nick) {
        this.nick = nick;
        return this;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getSencha() {
        return sencha;
    }

    public Profile sencha(String sencha) {
        this.sencha = sencha;
        return this;
    }

    public void setSencha(String sencha) {
        this.sencha = sencha;
    }

    public User getOwner() {
        return owner;
    }

    public Profile owner(User user) {
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
        if (!(o instanceof Profile)) {
            return false;
        }
        return id != null && id.equals(((Profile) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Profile{" +
            "id=" + getId() +
            ", nick='" + getNick() + "'" +
            ", sencha='" + getSencha() + "'" +
            "}";
    }
}
