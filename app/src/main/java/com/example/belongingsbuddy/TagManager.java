package com.example.belongingsbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;


import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.example.belongingsbuddy.Item;
import com.example.belongingsbuddy.Tag;
import com.google.firebase.firestore.CollectionReference;

import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.K;

/**
 * Manager class for all things tag related
 */
public class TagManager implements Serializable {
    //Manager Class keeps track of all possible tags and the item->tags relationships via HashMaps
    private Set<Tag> tags;
    private HashMap<Item, Set<Tag>> ManagedItems;

    /**
     * Constructor for the TagManager class which initializes with a list of Item objects
     * @param Items List of items to initialize ManagedItems within the manager
     */
    public TagManager(List<Item> Items) {
        tags = new HashSet<>();
        ManagedItems = new HashMap<Item, Set<Tag>>();
        for (Item item : Items) {
            ManagedItems.put(item, new HashSet<>());
        }
    }

    /**
     * Base Constructor method for TagManager Class which doesn't take in an initial list of Items objects
     */
    public TagManager() {
        this.tags = new HashSet<>();
        this.ManagedItems = new HashMap<Item, Set<Tag>>();
    }

    /**
     * Constructor for the TagManager Class which initializes with a set of created Tag objects
     * @param initTags Set of Tags for initialization within tags
     */
    public TagManager(Set<Tag> initTags) {
        this.tags = initTags;
        this.ManagedItems = new HashMap<Item, Set<Tag>>();
    }

    /**
     * Getter method for getting the ManagedItems Hashmap which maps each item to a set of tags
     * @return ManagedItems
     */
    public HashMap<Item, Set<Tag>> getManagedItems() {
        return ManagedItems;
    }

    /**
     * Add a tag to the pool of possible tags
     * @param tag Tag object
     */
    public void addTag(Tag tag) {
        tags.add(tag);
    }

    /**
     * Remove tag from pool of possible tags
     * @param tag Tag object
     */
    public void removeTag(Tag tag) {
        tags.remove(tag);
        this.updateTags();
    }

    /**
     * Set the possible tags
     * @param tagSet Set of Tag Objects
     */
    public void setTags(Set<Tag> tagSet) {
        tags = tagSet;
        this.updateTags();
    }

    /**
     * Get all the Item objects which are managed within the TagManager
     * @return Set of managed Item Objects
     */
    public Set<Item> getItems() {
        return ManagedItems.keySet();
    }

    /**
     * Initialize an item to the map of ManagedItems with no initial tags
     * @param element Item Object
     */
    public void AddItem(Item element) {
        ManagedItems.put(element, new HashSet<>());
    }

    /**
     * Update the tags on all the items if tag removed from the possible pool of tags
     */
    public void updateTags() {
        for(Set<Tag> Tags : ManagedItems.values()) {
            Tags.removeIf(i -> (!tags.contains(i)));
        }
    }

    /**
     * Get all the possible tags
     * @return Set of Tag Objects
     */
    public Set<Tag> getTags() {
        return tags;
    }

    /**
     * Set the tags of an item where tags are in a list
     * @param item Item object to set tags to
     * @param tags List of Tag objects to be set to the item
     */
    public void setItemTags(Item item, ArrayList<Tag> tags) {
       ManagedItems.put(item, new HashSet<>(tags));
    }

    /**
     * Set the tags of an item where tags are in a set
     * @param item Item object to set tags to
     * @param tags Set of Tag objects to be set to the item
     */
    public void setItemTags(Item item, Set<Tag> tags) {
        ManagedItems.put(item, tags);
    }

    /**
     * Get Item tags
     * @param item The Item object to get the tags of
     * @return Returns empty set if item is not managed and returns a set of managed items if it is managed
     */
    public Set<Tag> getItemTags(Item item) {
        if (ManagedItems.containsKey(item)) {
            return ManagedItems.get(item);
        } else {
            return new HashSet<>();
        }
    }

    /**
     * Prints the tags of an item to a string format in ascending or descending order
     * @param i Item object to print tags of
     * @param descending Select if the printing should be in descending order (default ascending)
     * @return returns a String of tags
     */
    public String printItemTags(Item i, Boolean descending) {
        String returnString = "";
        if (ManagedItems.containsKey(i)) {
            ArrayList<Tag> itemTags = new ArrayList<>(ManagedItems.get(i));
            if (descending) {
                itemTags.sort(Comparator.comparing(Tag::getTagName).reversed());
            } else {
                itemTags.sort(Comparator.comparing(Tag::getTagName));
            }
            for (int a = 0; a < itemTags.size(); a++) {
                returnString += itemTags.get(a).toString() + " ";
            }
        }
        return returnString;
    }

    /**
     * Opens the tag selection interface to select tags from possible tags into an array
     * @param listener The calling activity class of the tag selector which must implement the TagListener interface
     * @param manager The fragment manager of the the calling activity
     * @param selectedTags An array of selected tags to initialize the tag selector with
     */
    public void openTagSelector(TagListener listener, FragmentManager manager, ArrayList<Tag> selectedTags) {
        Bundle arg = new Bundle();
        arg.putSerializable("tagManager", this);
        arg.putSerializable("selectedTags", selectedTags);
        arg.putSerializable("mainActivity", listener);
        TagActivity TagFragment = new TagActivity();
        TagFragment.setArguments(arg);
        TagFragment.show(manager, "dialog");
    }

    /**
     * Filter managed items based on a subset of tags
     * @param tagSet The set of tags to be used for filtering
     * @return List of items which include the subset of tags
     */
    public ArrayList<Item> filterByTags(Set<Tag> tagSet) {
        ArrayList<Item> subset = new ArrayList<>();
        for (Map.Entry<Item, Set<Tag>> entry : ManagedItems.entrySet()) {
            if (entry.getValue().containsAll(tagSet)) {
                subset.add(entry.getKey());
            }
        }
        return subset;
    }

    /**
     * Updates the database collection with the new user tags
     * @param collection the collection to be updated
     */
    public void updateDatabaseTags(CollectionReference collection){
        List<Map<String, Object>> map = returnTagDatamap(tags);

        // Create a data map to store the list of tags
        Map<String, Object> data = new HashMap<>();
        data.put("tags", map);

        // Set the data to Firestore document
        collection.document("userTags").set(data);
    }

    /**
     * Returns a ready to insert map of a set of tags which can be stored in a collection document
     * @param tagSet The set of tags to be converted for storage
     * @return The list of maps mapping to each tag in the given set
     */
    public List<Map<String, Object>> returnTagDatamap(Set<Tag> tagSet){
        List<Map<String, Object>> tagMaps = new ArrayList<>();
        for (Tag tag : tagSet) {
            Map<String, Object> tagMap = new HashMap<>();
            tagMap.put("tagName", tag.getTagName());
            tagMaps.add(tagMap);
        }
        return tagMaps;
    }

    /**
     * Converts a data map gotten from a collection or in general back to a set of Tag objects
     * @param dataMap The data map of a set of tags
     * @return The set of mapped tags
     */
    public Set<Tag> convertTagDatamap(List<Map<String, Object>> dataMap) {
        HashSet<Tag> retrievedTags = new HashSet<>();
        for (Map<String, Object> tagMap : dataMap) {
            String tagName = (String) tagMap.get("tagName");
            retrievedTags.add(new Tag(tagName));
        }
        return retrievedTags;
    }
}
