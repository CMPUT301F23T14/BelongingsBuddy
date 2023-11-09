package com.example.belongingsbuddy;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import com.example.belongingsbuddy.Item;
import com.example.belongingsbuddy.Tag;

public class TagManager {
    private Set<Tag> tags;
    private HashMap<Item, Set<Tag>> ManagedItems;
    public TagManager(Set<Item> Items) {
        tags = new HashSet<>();
        ManagedItems = new HashMap<Item, Set<Tag>>();

        for (Item item : Items) {
            ManagedItems.put(item, new HashSet<>());
        }
    }

    public TagManager() {
        tags = new HashSet<>();
        ManagedItems = new HashMap<Item, Set<Tag>>();
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
        this.updateTags();
    }

    public void setTags(Set<Tag> tagSet) {
        tags = tagSet;
    }

    public Set<Tag> searchTags(String keyword) {
        return null;
    }

    public Set<Item> getItems() {
        return ManagedItems.keySet();
    }

    public void AddItem(Item element) {
        ManagedItems.put(element, new HashSet<>());
    }

    public void updateTags() {
        for(Set<Tag> Tags : ManagedItems.values()) {
            Tags.removeIf(i -> (!tags.contains(i)));
        }
    }

    public Set<Tag> getTags() {
        return tags;
    }

}
