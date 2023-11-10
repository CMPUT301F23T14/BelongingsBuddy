package com.example.belongingsbuddy;
/**
 * The Listener interface provides callback methods for interacting with sorting and input operations
 * in the BelongingsBuddy application.
 */
public interface Listener {
    void onSortOKPressed(String sortType, Boolean isAscending);
    void inputManually();
    void inputBarcode();
}

