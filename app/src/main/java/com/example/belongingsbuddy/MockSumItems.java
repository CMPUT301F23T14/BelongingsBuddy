package com.example.belongingsbuddy;

import java.util.ArrayList;

/**
 * Calculates the sum of estimated values for a list of items FOR UNIT TESTING
 */
public class MockSumItems {
    public static float sumItems(ArrayList<Item> dataList) {
        float sum = 0f;
        for (Item item: dataList) {
            sum += item.getEstimatedValue();
        }
        return sum;
    }
}
