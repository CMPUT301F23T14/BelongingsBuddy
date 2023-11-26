package com.example.belongingsbuddy;

import java.io.Serializable;

public class Tag implements Serializable {
    //String Name Associated with Tag
    private String tagName;

    //Constructor
    public Tag(String tagName) {
        this.tagName = tagName;
    }

    //Getters and setters
    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    //Comparator override to comapre tag strings instead of objects
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return tagName.equals(tag.tagName);
    }

    //Return Name String Hash
    @Override
    public int hashCode() {
        return tagName.hashCode();
    }

    //Return name string on to string
    @Override
    public String toString() {
        return tagName;
    }
}
