package com.example.belongingsbuddy;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.HashMap;

import com.example.belongingsbuddy.Item;
import com.example.belongingsbuddy.Tag;

public class TagManager implements Serializable {
    //Manager Class keeps track of all possible tags and the item->tags relationships via HashMaps
    private Set<Tag> tags;
    private HashMap<Item, Set<Tag>> ManagedItems;
    public TagManager(List<Item> Items) {
        tags = new HashSet<>();
        ManagedItems = new HashMap<Item, Set<Tag>>();
        for (Item item : Items) {
            ManagedItems.put(item, new HashSet<>());
        }
    }

    //Constructor
    public TagManager() {
        tags = new HashSet<>();
        ManagedItems = new HashMap<Item, Set<Tag>>();
    }

    //Add a tag to the pool of possible tags
    public void addTag(Tag tag) {
        tags.add(tag);
    }

    //Remove tag from pool of possible tags
    public void removeTag(Tag tag) {
        tags.remove(tag);
        this.updateTags();
    }

    //Set the possible tags
    public void setTags(Set<Tag> tagSet) {
        tags = tagSet;
    }

    //Search a tag based on string (Not implemented yet)
    public Set<Tag> searchTags(String keyword) {
        return null;
    }

    //Get all the managed items
    public Set<Item> getItems() {
        return ManagedItems.keySet();
    }

    //Initialize an item to the list of managed items with no tags
    public void AddItem(Item element) {
        ManagedItems.put(element, new HashSet<>());
    }

    //Update the tags on all the items if tag removed from the possible pool of tags
    public void updateTags() {
        for(Set<Tag> Tags : ManagedItems.values()) {
            Tags.removeIf(i -> (!tags.contains(i)));
        }
    }

    //Get all the possible tags
    public Set<Tag> getTags() {
        return tags;
    }

    //Set the tags of an item
    public void setItemTags(Item item, ArrayList<Tag> tags) {
       // ManagedItems.put(item, new HashSet<>());
    }
    public String printItemTags(Item i) {
        String returnString = "";
        if (ManagedItems.containsKey(i)) {
            Set<Tag> itemTags = ManagedItems.get(i);
            for (Tag tag : itemTags) {
                returnString += tag.toString() + " ";
            }
        }
        return returnString;
    }

}
