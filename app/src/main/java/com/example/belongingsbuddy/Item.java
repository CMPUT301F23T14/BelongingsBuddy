package com.example.belongingsbuddy;


import static java.lang.Integer.parseInt;

import android.widget.Toast;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;

import org.checkerframework.checker.units.qual.C;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private Integer quantity;
    private ArrayList<Photo> photos;
    private List<String> photoURLs;
    private String epoch;
    private int id;

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
        this.quantity = 1;
        this.photos = new ArrayList<Photo>();
        this.epoch = Long.toString(System.currentTimeMillis());
        this.id = Objects.hash(
                name,
                this.epoch,
                description,
                make,
                model,
                estimatedValue,
                comment
        );
        // this.photoURLs = new ArrayList<String>();
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
                Float estimatedValue, String comment, String serialNumber) {
        this.name = name;
        this.date = date;
        this.description = description;
        this.make = make;
        this.model = model;
        this.serialNumber = serialNumber;
        this.estimatedValue = estimatedValue;
        this.comment = comment;
        this.quantity = 1;
        this.photos = new ArrayList<>();
        this.epoch = Long.toString(System.currentTimeMillis());
        this.id = Objects.hash(
                name,
                this.epoch,
                description,
                make,
                model,
                serialNumber,
                estimatedValue,
                comment
        );
    }

    public Item(String name, Date date, String description, String make, String model,
                Float estimatedValue, String comment, String serialNumber, ArrayList<Photo> photos,
                String epoch, String id, Integer quantity, List<String> photoURLs) {
        this.name = name;
        this.date = date;
        this.description = description;
        this.make = make;
        this.model = model;
        this.serialNumber = serialNumber;
        this.estimatedValue = estimatedValue;
        this.comment = comment;
        this.photos = photos;
        this.epoch = epoch;
        this.id = parseInt(id);
        this.quantity = quantity;
        this.photoURLs = photoURLs;
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
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getEpoch() {return epoch;}

    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<Photo> photos) {
        this.photos = photos;
    }

    public List<String> getPhotoURLs() {
        return photoURLs;
    }
    public void setPhotoURLs(List<String> photoURLs) {
        this.photoURLs = photoURLs;
    }

    /**
     * add a Photo to the Item
     * @param p
     * Photo to be added
     */
    public  void addPhoto(Photo p){
        photos.add(p);
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    /**
     * Add the Item to the FireStore CollectionReference passed in as a parameter
     * @param collection Firestore CollectionReference the Item is being added to
     */
    public void addToDatabase(CollectionReference collection, TagManager manager){
        Map<String, Object> docData = new HashMap<>();
        docData.put("comment", this.getComment());
        docData.put("dateString", this.getDate().getString());
        docData.put("day", this.getDate().getDay());
        docData.put("month", this.getDate().getMonth());
        docData.put("year", this.getDate().getYear());
        docData.put("description", this.getDescription());
        docData.put("estimatedValue", this.getEstimatedValue());
        docData.put("make", this.getMake());
        docData.put("model", this.getModel());
        docData.put("name", this.getName());
        docData.put("photoURLs", this.getPhotoURLs());
        docData.put("photos", this.getPhotos());
        docData.put("quantity", this.getQuantity());
        docData.put("serialNumber", this.getSerialNumber());
        docData.put("tags", manager.getItemTags(this));
        docData.put("epoch", this.getEpoch());
        collection.document(Integer.toString(hashCode())).set(docData);
    }

    public void updateInDatabase(CollectionReference collection, TagManager manager){
        Map<String, Object> docData = new HashMap<>();
        docData.put("comment", this.getComment());
        docData.put("dateString", this.getDate().getString());
        docData.put("day", this.getDate().getDay());
        docData.put("month", this.getDate().getMonth());
        docData.put("year", this.getDate().getYear());
        docData.put("description", this.getDescription());
        docData.put("estimatedValue", this.getEstimatedValue());
        docData.put("make", this.getMake());
        docData.put("model", this.getModel());
        docData.put("name", this.getName());
        docData.put("photoURLs", this.getPhotoURLs());
        docData.put("photos", this.getPhotos());
        docData.put("quantity", this.getQuantity());
        docData.put("serialNumber", this.getSerialNumber());
        docData.put("tags", manager.getItemTags(this));
        collection.document(Integer.toString(hashCode())).update(docData);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item i = (Item) o;
        return i.hashCode() == o.hashCode();
    }
}


