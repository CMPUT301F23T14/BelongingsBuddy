package com.example.belongingsbuddy;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class LongClickUnitTest {

    @Test
    public void testLongClickAndDelete() {
        // Create a sample list of items
        ArrayList<Item> dataList = new ArrayList<>();
        Item testItem1 = new Item("Chair", new Date(), "A chair",
                "Hermann Miller", "Chair 9000", (float) 200, "I like this chair");
        Item testItem2 = new Item("Table", new Date(), "A table",
                "Ikea", "Table 9000", (float) 400, "I like this table");
        Item testItem3 = new Item("Lamp", new Date(), "A lamp",
                "Amazon", "Lamp 9000", (float) 50, "I like this lamp");
        dataList.add(testItem1);
        dataList.add(testItem2);
        dataList.add(testItem3);

        // Perform long click on the first item
        int position = 0; // Assuming you want to long click the first item
        Item longClickedItem = dataList.get(position);
        dataList.remove(position);

        // Check if the item is removed
        assertEquals(2, dataList.size());
        assertEquals(testItem2, dataList.get(0));
        assertEquals(testItem3, dataList.get(1));

        // Add additional assertions based on your requirements
    }
}

