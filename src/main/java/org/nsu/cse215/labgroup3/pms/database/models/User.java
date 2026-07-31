package org.nsu.cse215.labgroup3.pms.database.models;

import org.nsu.cse215.labgroup3.pms.database.Field;
import org.nsu.cse215.labgroup3.pms.database.Model;

import java.time.Instant;

@Model
public class User {
    @Field
    private Long id;

    @Field
    private String name;

    @Field
    private String username;

    @Field
    private Instant createdAt;

    public User() {}

    public User(Long id, String name, String username, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
