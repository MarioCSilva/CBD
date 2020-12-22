package com.cbd;
import java.sql.Date;


public class Author {
    private String username;
    private String email;
    private String name;
    private Date timestamp;

    public Author(String username, String email, String name, Date timestamp) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.timestamp = timestamp;
    }

    public String getUsername(){
        return this.username;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}