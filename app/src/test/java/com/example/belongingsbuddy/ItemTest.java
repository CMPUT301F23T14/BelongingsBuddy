package com.example.belongingsbuddy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests the functionality of the Item class;
 * NOTE: currently Item class does not work with Tags and Photos, this will be implemented for the final product
 */
public class ItemTest {
    /**
     * Constructs an Item Object used for testing
     * @return Item for testing
     */
    private Item mockItem(){
        // constructs a mock item (without serial number)
        Item i = new Item("test item", new Date(), "this is a test item", "xx", "xx", (float)0.99, "");
        return i;
    }

    /**
     * Constructs another Item Object to be used for testing
     * @return Item for testing
     */
    private Item mockItem2(){
        // constructs a mock item (with a serial number)
        Date d = new Date(11,11,2011);
        Item i = new Item("chair", d, "this is a very cool chair", "make", "model", (float)123.45, "no comments", 101010);
        return i;
    }

    /**
     * Tests that item constructors (with and without serial numbers) work correctly
     */
    @Test
    public void testItemConstructors(){
        // construct the mock Items
        Item item1 = mockItem();
        Item item2 = mockItem2();
        // check the serial numbers for these items
        assertEquals(null, item1.getSerialNumber());
        assertEquals((Integer)101010, item2.getSerialNumber());
        // if this test works, then both constructors work as intended
    }

    /**
     * Tests the setter and getter methods in the Item class
     */
    @Test
    public void testSettersAndGetters(){
        Item item1 = mockItem();
        Item item2 = mockItem2();
        String name = mockItem().getName();
        // check that getName returned the correct name
        assertEquals("test item", name);
        // check the name of the second Item
        assertEquals("chair", item2.getName());
        // use setName() on item2
        item2.setName(name);
        assertEquals(name, item2.getName());
        assertEquals("test item", item2.getName());
        // both items should now have the same name
        assertEquals(item1.getName(), item2.getName());
        // test another getter method:
        assertEquals("no comments", item2.getComment());
        // check that item2 has greater value (123.45) than item1 (0.99)
        assertTrue(item2.getEstimatedValue() >  item1.getEstimatedValue());
    }
}
