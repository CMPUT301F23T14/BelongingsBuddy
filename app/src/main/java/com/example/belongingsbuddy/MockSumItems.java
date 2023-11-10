package com.example.belongingsbuddy;

import java.util.ArrayList;
public class MockSumItems {
    public static float sumItems(ArrayList<Item> dataList) {
        float sum = 0f;
        for (Item item: dataList) {
            sum += item.getEstimatedValue();
        }
        return sum;
    }
}
