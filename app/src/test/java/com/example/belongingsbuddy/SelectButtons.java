package com.example.belongingsbuddy;

import android.content.Context;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

public class SelectButtons {
    /**
     * creates mock CustomList of items for testing with 3 items in it
     * @return
     * returns type CustomList for testing
     */
    private CustomList mockList() {
        ArrayList<Item> mockData = new ArrayList<>();
        mockData.add(new Item("1", new Date(), "description", "make", "model", 1.00F, "hi"));
        mockData.add(new Item("2", new Date(), "description", "make", "model", 2.00F, "hi"));
        mockData.add(new Item("3", new Date(), "description", "make", "model", 3.00F, "hi"));
        CustomList mockList = new CustomList(mockList().getContext(), mockData);
        return mockList;
    }

    @Test
    public void testSelect() {
        CustomList mockData = mockList();
        assertEquals(0, mockData.getSelectedItems().size());
        mockData.setMultiSelectMode(true);
        assertEquals(0, mockData.getSelectedItems().size());
        assertTrue(mockData.isMultiSelectMode());
        mockData.selectAll();
        assertEquals(3, mockData.getSelectedItems().size());
    }
}
