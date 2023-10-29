package com.example.belongingsbuddy;

import android.nfc.Tag;

import java.util.ArrayList;

public class Item {
    private String name;
    private Date date;
    private String description;
    private String make;
    private String model;
    private Integer serialNumber;
    private Integer estimatedValue;
    private String comment;
    private ArrayList<Tag> tags;
    private ArrayList<Photo> photos;

    public Item(String name, Date date, String description, String make, String model,
                Integer serialNumber,Integer estimatedValue, String comment){
        this.name = name;
        this.date = date;
        this.description = description;
        this.make = make;
        this.model = model;
        this.serialNumber = serialNumber;
        this.estimatedValue = estimatedValue;
        this.comment = comment;
        tags = new ArrayList<Tag>();
        photos = new ArrayList<Photo>();
    }

    public Item(String name, Date date, String description, String make, String model,
                Integer estimatedValue, String comment){
        this.name = name;
        this.date = date;
        this.description = description;
        this.make = make;
        this.model = model;
        this.serialNumber = null;
        this.estimatedValue = estimatedValue;
        this.comment = comment;
        tags = new ArrayList<Tag>();
        photos = new ArrayList<Photo>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getEstimatedValue() {
        return estimatedValue;
    }

    public void setEstimatedValue(Integer estimatedValue) {
        this.estimatedValue = estimatedValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }

    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<Photo> photos) {
        this.photos = photos;
    }

    public void addTag(Tag t){
        tags.add(t);
    }

    public  void addPhoto(Photo p){
        photos.add(p);
    }
}
