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

import org.checkerframework.checker.units.qual.K;

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

    public HashMap<Item, Set<Tag>> getManagedItems() {
        return ManagedItems;
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
       ManagedItems.put(item, new HashSet<>(tags));
    }

    //Get Item tags
    public Set<Tag> getItemTags(Item item) {
        if (ManagedItems.containsKey(item)) {
            return ManagedItems.get(item);
        } else {
            return new HashSet<>();
        }
    }
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

    public void openTagSelector(TagListener listener, FragmentManager manager, ArrayList<Tag> selectedTags) {
        Bundle arg = new Bundle();
        arg.putSerializable("tagManager", this);
        arg.putSerializable("selectedTags", selectedTags);
        TagActivity TagFragment = new TagActivity();
        TagFragment.setArguments(arg);
        TagFragment.show(manager, "dialog");
    }

    public ArrayList<Item> filterByTags(Item i, Set<Tag> tagSet) {
        ArrayList<Item> subset = new ArrayList<>();
        for (Map.Entry<Item, Set<Tag>> entry : ManagedItems.entrySet()) {
            if (entry.getValue().containsAll(tagSet)) {
                subset.add(entry.getKey());
            }
        }
        return subset;
    }
}
