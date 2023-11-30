package com.example.belongingsbuddy;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * an instance of this class represent an Item that the app will add to the inventory
 * the class uses standard setter and getter methods
 */
public class Item implements Serializable {
    private String name;
    private Date date;
    private String description;
    private String make;
    private String model;
    private String serialNumber;
    private Float estimatedValue;
    private String comment;
    private ArrayList<Tag> tags;
    private ArrayList<Photo> photos;
    private String epoch;

    /**
     * Constructor for the Item class (without a provided serial number)
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
        this.epoch = Long.toString(System.currentTimeMillis());
        tags = new ArrayList<Tag>();
        photos = new ArrayList<Photo>();
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
                Float estimatedValue, String comment, String serialNumber){
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
     * public constructor that takes no parameters.
     * This is used to load items from FireStore collection
     */
    public Item(){
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

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
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

    public String getEpoch() {return epoch;}

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

    /**
     * Add the Item to the FireStore CollectionReference passed in as a parameter
     * @param collection Firestore CollectionReference the Item is being added to
     */
    public void addToDatabase(CollectionReference collection){
        collection.document(this.name).set(this);
    }

    public void updateInDatabase(CollectionReference collection, String oldName){
        collection.document(oldName).delete();
        collection.document(this.name).set(this);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item i = (Item) o;
        return i.hashCode() == o.hashCode();
    }
    @Override
    public int hashCode() {
        return Objects.hash(
                name,
                epoch,
                description,
                make,
                model,
                serialNumber,
                estimatedValue,
                comment,
                tags,
                photos
        );
    }
}


