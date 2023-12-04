package com.example.belongingsbuddy;

import java.io.Serializable;

/**
 * Tag class which encapsulates a string
 */
public class Tag implements Serializable {
    //String Name Associated with Tag
    private String tagName;

    /**
     * Constructor for Tag Class
     * @param tagName The string associated with the tag
     */
    public Tag(String tagName) {
        this.tagName = tagName;
    }

    /**
     * Getter method for the tag name
     * @return The tag name
     */
    public String getTagName() {
        return tagName;
    }

    /**
     * Setter method for the tag name
     * @param tagName The string to set the tag name to
     */
    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    //Comparator override to compare tag strings instead of objects
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