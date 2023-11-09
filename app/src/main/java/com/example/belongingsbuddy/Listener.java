package com.example.belongingsbuddy;

/**
 * Interface implemented by MainActivity
 * Allows other classes to call methods from implemented in Listener;
 * specifies the onSortOKPressed and inputManually methods
 */
public interface Listener {
    void onSortOKPressed(String sortType, Boolean isAscending);
    void inputManually();
}
