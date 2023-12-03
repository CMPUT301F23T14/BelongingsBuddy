package com.example.belongingsbuddy;

import java.util.ArrayList;

/**
 * The Listener interface provides callback methods for interacting with sorting and input operations
 * in the BelongingsBuddy application.
 */
public interface Listener {
    void onSortOKPressed(String sortType, Boolean isAscending);
    void onFilterOkPressed(String[] keywords, String[] makes, ArrayList<Tag> tags, Date starDate, Date endDate);
    void inputManually();
    void inputBarcode();
    TagManager getTagManager();
}

