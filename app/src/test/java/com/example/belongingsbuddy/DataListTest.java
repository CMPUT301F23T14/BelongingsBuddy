package com.example.belongingsbuddy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;


/**
 * Tests the functionality of the sumItems method and dataList
 */
public class DataListTest {
    @Test
    /**
     Testing the sumItems method
     */
    public void testSumItems() {
        // set up dataList
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

        // mockSumItems
        MockSumItems test = new MockSumItems();

        // testing
        float result = test.sumItems(dataList);
        assertEquals(result, 650, 0);

        dataList.remove(0);
        result = test.sumItems(dataList);
        assertEquals(result, 450, 0);

        dataList.clear();
        result = test.sumItems(dataList);
        assertEquals(result, 0, 0);
    }

    @Test
    /**
    Testing the type dataList holds is correct
     */
    public void testDataListType() {
        // set up dataList
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

        // testing
        assertTrue(testItem1 instanceof Item);
    }
}