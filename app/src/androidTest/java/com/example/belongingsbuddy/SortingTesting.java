package com.example.belongingsbuddy;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.PickerActions.setDate;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

import android.view.KeyEvent;
import android.widget.DatePicker;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
//import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
// Must use testAll method otherwise firestore errors and designed to run that way
// often the first time you try to run a test youll have firestore sync errots on first run after building app
// run it again and it should not error out

/**
 * Runs a series of tests related to sorting items
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class SortingTesting {
    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testAllSorting() {
        testSortDateDisplaysType();
        testSortDescDisplaysType();
        testSortMakeDisplaysType();
        testSortValueDisplaysType();
        testSortRollback();
        testValueSortAsc();
        testMakeSortAsc();
        testDescSortAsc();
        testValueSortDesc();
        testMakeSortDesc();
        testDescSortDesc();
        testDateSortAsc();
        testDateSortDesc();
    }

    /**
     * Tests if sort type is displayed for date
     */
    // Check whether sort type is displayed
    @Test
    public void testSortDateDisplaysType() {
        // Enter the sort screen
        onView(withId(R.id.sort_button)).perform(click());
        // selection
        onView(withId(R.id.sort_date)).perform(click());
        onView(withId(R.id.sort_ok)).perform(click());
        // Check for correct string
        onView(withId(R.id.sort_type_textview)).check(matches(withText("Date")));
    }
    /**
     * Tests if sort type is displayed for desc
     */
    // Check whether sort type is displayed
    @Test
    public void testSortDescDisplaysType() {
        // Enter the sort screen
        onView(withId(R.id.sort_button)).perform(click());
        // selection
        onView(withId(R.id.sort_desc)).perform(click());
        onView(withId(R.id.sort_ok)).perform(click());
        // Check for correct string
        onView(withId(R.id.sort_type_textview)).check(matches(withText("Description")));
    }
    /**
     * Tests if sort type is displayed for make
     */
    // Check whether sort type is displayed
    @Test
    public void testSortMakeDisplaysType() {
        // Enter the sort screen
        onView(withId(R.id.sort_button)).perform(click());
        // selection
        onView(withId(R.id.sort_make)).perform(click());
        onView(withId(R.id.sort_ok)).perform(click());
        // Check for correct string
        onView(withId(R.id.sort_type_textview)).check(matches(withText("Make")));
    }
    /**
     * Tests if sort type is displayed for value
     */
    // Check whether sort type is displayed
    @Test
    public void testSortValueDisplaysType() {
        // Enter the sort screen
        onView(withId(R.id.sort_button)).perform(click());
        // selection
        onView(withId(R.id.sort_value)).perform(click());
        onView(withId(R.id.sort_ok)).perform(click());
        // Check for correct string
        onView(withId(R.id.sort_type_textview)).check(matches(withText("Estimated Value")));
    }

    /**
     * Tests if rollback ui works
     */
    // Check whether X works
    @Test
    public void testSortRollback() {
        // Rollback
        onView(withId(R.id.sort_type_rollback)).perform(click());
        // Check if Estimated Value still there
        onView(withText("Estimated")).check(doesNotExist());
    }

    /**
     * Tests value asc works
     */
    // ASC
    // Check wether value sort works
    @Test
    public void testValueSortAsc() {
        // Enter the sort screen
        onView(withId(R.id.sort_button)).perform(click());
        // selection
        onView(withId(R.id.sort_value)).perform(click());
        onView(withId(R.id.sort_ok)).perform(click());

        onData(is(instanceOf(Item.class))).atPosition(0).onChildView(withId(R.id.item_value)).check(matches(withText("50.0")));
        onData(is(instanceOf(Item.class))).atPosition(1).onChildView(withId(R.id.item_value)).check(matches(withText("200.0")));
        onData(is(instanceOf(Item.class))).atPosition(2).onChildView(withId(R.id.item_value)).check(matches(withText("400.0")));
    }
    /**
     * Tests make asc works
     */
    // Check wether make sort works
    @Test
    public void testMakeSortAsc() {
        // Enter the sort screen
        onView(withId(R.id.sort_button)).perform(click());
        // selection
        onView(withId(R.id.sort_make)).perform(click());
        onView(withId(R.id.sort_ok)).perform(click());

        onData(is(instanceOf(Item.class))).atPosition(0).onChildView(withId(R.id.item_name)).check(matches(withText("Lamp")));
        onData(is(instanceOf(Item.class))).atPosition(1).onChildView(withId(R.id.item_name)).check(matches(withText("Chair")));
        onData(is(instanceOf(Item.class))).atPosition(2).onChildView(withId(R.id.item_name)).check(matches(withText("Table")));
    }
    /**
     * Tests desc asc works
     */
    // Check wether desc sort works
    @Test
    public void testDescSortAsc() {
        // Enter the sort screen
        onView(withId(R.id.sort_button)).perform(click());
        // selection
        onView(withId(R.id.sort_desc)).perform(click());
        onView(withId(R.id.sort_ok)).perform(click());

        onData(is(instanceOf(Item.class))).atPosition(0).onChildView(withId(R.id.item_name)).check(matches(withText("Chair")));
        onData(is(instanceOf(Item.class))).atPosition(1).onChildView(withId(R.id.item_name)).check(matches(withText("Lamp")));
        onData(is(instanceOf(Item.class))).atPosition(2).onChildView(withId(R.id.item_name)).check(matches(withText("Table")));
    }
    /**
     * Tests value desc works
     */
    // DESC
    // Check wether value sort works
    @Test
    public void testValueSortDesc() {
        // Enter the sort screen
        onView(withId(R.id.sort_button)).perform(click());
        // selection
        onView(withId(R.id.sort_value)).perform(click());
        onView(withId(R.id.descending_rb)).perform(click());
        onView(withId(R.id.sort_ok)).perform(click());

        onData(is(instanceOf(Item.class))).atPosition(2).onChildView(withId(R.id.item_value)).check(matches(withText("50.0")));
        onData(is(instanceOf(Item.class))).atPosition(1).onChildView(withId(R.id.item_value)).check(matches(withText("200.0")));
        onData(is(instanceOf(Item.class))).atPosition(0).onChildView(withId(R.id.item_value)).check(matches(withText("400.0")));
    }
    /**
     * Tests make desc works
     */
    @Test
    public void testMakeSortDesc() {
        // Enter the sort screen
        onView(withId(R.id.sort_button)).perform(click());
        // selection
        onView(withId(R.id.sort_make)).perform(click());
        onView(withId(R.id.descending_rb)).perform(click());
        onView(withId(R.id.sort_ok)).perform(click());

        onData(is(instanceOf(Item.class))).atPosition(2).onChildView(withId(R.id.item_name)).check(matches(withText("Lamp")));
        onData(is(instanceOf(Item.class))).atPosition(1).onChildView(withId(R.id.item_name)).check(matches(withText("Chair")));
        onData(is(instanceOf(Item.class))).atPosition(0).onChildView(withId(R.id.item_name)).check(matches(withText("Table")));
    }
    /**
     * Tests desc desc works
     */
    @Test
    public void testDescSortDesc() {
        // Enter the sort screen
        onView(withId(R.id.sort_button)).perform(click());
        // selection
        onView(withId(R.id.sort_desc)).perform(click());
        onView(withId(R.id.descending_rb)).perform(click());
        onView(withId(R.id.sort_ok)).perform(click());

        onData(is(instanceOf(Item.class))).atPosition(2).onChildView(withId(R.id.item_name)).check(matches(withText("Chair")));
        onData(is(instanceOf(Item.class))).atPosition(1).onChildView(withId(R.id.item_name)).check(matches(withText("Lamp")));
        onData(is(instanceOf(Item.class))).atPosition(0).onChildView(withId(R.id.item_name)).check(matches(withText("Table")));
    }
    /**
     * Tests date asc works
     */

    // Test method for adding an item and checking if the total updates
    @Test
    public void testDateSortAsc() {
        // Enter the add screen
        onView(withId(R.id.add_item)).perform(click());
        onView(withId(R.id.input_manually)).perform(click());

        // Interact with the date picker and set a specific date
        onView(withId(R.id.add_pick_date_button)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(1989, 8, 25));
        onView(withText("OK")).perform(click());

        // Enter values for item details
        onView(withId(R.id.add_value)).perform(ViewActions.typeText("22"));
        onView(withId(R.id.add_description)).perform(ViewActions.typeText("TEST"));
        onView(withId(R.id.add_make)).perform(ViewActions.typeText("TEST"));
        onView(withId(R.id.add_model)).perform(ViewActions.typeText("TEST"));
        onView(withId(R.id.add_name)).perform(scrollTo());
        onView(withId(R.id.add_name)).perform(ViewActions.typeText("TEST"));

        // Close the keyboard and confirm the addition
        onView(withId(R.id.add_model)).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.add_confirm)).perform(scrollTo());
        onView(withId(R.id.add_confirm)).perform(click());

        // Enter the sort screen
        onView(withId(R.id.sort_button)).perform(click());
        // selection
        onView(withId(R.id.sort_date)).perform(click());
        onView(withId(R.id.ascending_rb)).perform(click());
        onView(withId(R.id.sort_ok)).perform(click());

        // Check if order is good
        onData(is(instanceOf(Item.class))).atPosition(0).onChildView(withId(R.id.item_name)).check(matches(withText("TEST")));
    }
    /**
     * Tests value dsc works
     */
    // Test method for adding an item and checking if the total updates
    @Test
    public void testDateSortDesc() {
        // Enter the add screen
        onView(withId(R.id.add_item)).perform(click());
        onView(withId(R.id.input_manually)).perform(click());

        // Interact with the date picker and set a specific date
        onView(withId(R.id.add_pick_date_button)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(1989, 8, 25));
        onView(withText("OK")).perform(click());

        // Enter values for item details
        onView(withId(R.id.add_value)).perform(ViewActions.typeText("22"));
        onView(withId(R.id.add_description)).perform(ViewActions.typeText("TEST"));
        onView(withId(R.id.add_make)).perform(ViewActions.typeText("TEST"));
        onView(withId(R.id.add_model)).perform(ViewActions.typeText("TEST"));
        onView(withId(R.id.add_name)).perform(scrollTo());
        onView(withId(R.id.add_name)).perform(ViewActions.typeText("TEST"));

        // Close the keyboard and confirm the addition
        onView(withId(R.id.add_model)).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.add_confirm)).perform(scrollTo());
        onView(withId(R.id.add_confirm)).perform(click());

        // Enter the sort screen
        onView(withId(R.id.sort_button)).perform(click());
        // selection
        onView(withId(R.id.sort_date)).perform(click());
        onView(withId(R.id.descending_rb)).perform(click());
        onView(withId(R.id.sort_ok)).perform(click());

        // Check if order is good
        onData(is(instanceOf(Item.class))).atPosition(3).onChildView(withId(R.id.item_name)).check(matches(withText("TEST")));
    }
}