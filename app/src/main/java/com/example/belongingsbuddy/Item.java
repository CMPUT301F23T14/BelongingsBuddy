package com.example.belongingsbuddy;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * an instance of this class represent an Item that will be put the app will add to the inventory
 * the class uses standard setter and getter methods
 */
public class Item implements Serializable {
    private String name;
    private Date date;
    private String description;
    private String make;
    private String model;
    private Integer serialNumber;
    private Float estimatedValue;
    private String comment;
    private ArrayList<Tag> tags;
    private ArrayList<Photo> photos;


    /**
     * Constructor for the item class
     *
     * @param name
     * name of the item (String)
     * @param date
     * date of purchase or acquisition (Date)
     * @param description
     * brief description of item (String)
     * @param make
     * make of the Item (String)
     * @param model
     * model of the Item (String)
     * @param serialNumber
     * serial number for the Item (Integer)
     * @param estimatedValue
     * estimated value of the Item (Float)
     * @param comment
     * comment(s) about the item (String)
     */
    public Item(String name, Date date, String description, String make, String model,
                Integer serialNumber,Float estimatedValue, String comment){
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

    /**
     * Constructor for the Item class without a provided serialNumber
     * @param name
     * name of Item (String)
     * @param date
     * date of purchase/acquisition (Date)
     * @param description
     * brief description of Item (String)
     * @param make
     * make of Item (String)
     * @param model
     * model of Item (String)
     * @param estimatedValue
     * estimated value of the Item (Float)
     * @param comment
     * comment(s) about the Item (String)
     */
    public Item(String name, Date date, String description, String make, String model,
                Float estimatedValue, String comment){
        this.name = name;
        this.date = date;
        this.description = description;
        this.make = make;
        this.model = model;
        this.serialNumber = null;
        this.estimatedValue = estimatedValue;
        this.comment = comment;
        tags = new ArrayList<Tag>();
        this.photos = new ArrayList<Photo>();
    }

    /**
     * Constructor for Item class with a serial number provided
     * @param name
     * name of Item (String)
     * @param date
     * date of purchase/acquisition (Date)
     * @param description
     * brief description of Item (String)
     * @param make
     * make of Item (String)
     * @param model
     * model of Item (String)
     * @param estimatedValue
     * estimated value of the Item (Float)
     * @param comment
     * comment(s) about the Item (String)
     * @param serialNumber
     * serial number of item (Integer)
     */
    public Item(String name, Date date, String description, String make, String model,
                Float estimatedValue, String comment, Integer serialNumber){
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

    public Float getEstimatedValue() {
        return estimatedValue;
    }

    public void setEstimatedValue(Float estimatedValue) {
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

    /**
     * add a Tag to the Item
     * @param t
     * Tag to be added
     */
    public void addTag(Tag t){
        tags.add(t);
    }

    /**
     * add a Photo to the Item
     * @param p
     * Photo to be added
     */
    public  void addPhoto(Photo p){
        photos.add(p);
    }
}
