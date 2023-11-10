package com.example.belongingsbuddy;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.scrollTo;
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
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
// Must use testAll method otherwise firestore errors
// sometimes it errors with firestore sync issues on first run after building app
// run it again and it should not error out

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TotalTest {
    @Rule
    public ActivityScenarioRule<MainActivity> scenario1 = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testAllTotal() {
        testIsTotalThere();
        testTotalAdd();
        testTotalEdit();
        testTotalDelete();
        testTotalDeleteLongPress();
    }

    // Test method for checking if the initial total is displayed correctly
    @Test
    public void testIsTotalThere() {
        onView(withId(R.id.total)).check(matches(withText("$650.00")));
    }

    // Test method for adding an item and checking if the total updates
    @Test
    public void testTotalAdd() {
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

        // Check if the total updates correctly after adding the item
        onView(withId(R.id.total)).check(matches(withText("$672.00")));
    }

    // Test method for editing an item and checking if the total updates
    @Test
    public void testTotalEdit() {
        // Click on an item, click on the "Edit" button, edit the value, and confirm the changes
        onData(is(instanceOf(Item.class))).atPosition(0).perform(click());
        onView(withId(R.id.view_edit)).perform(scrollTo());
        onView(withId(R.id.view_edit)).perform(click());
        onView(withId(R.id.edit_value)).perform(scrollTo());
        onView(withId(R.id.edit_value)).perform(ViewActions.typeText("1"));
        onView(withId(R.id.edit_model)).perform(ViewActions.typeText("TEST"));
        onView(withId(R.id.edit_model)).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.edit_confirm)).perform(scrollTo());
        onView(withId(R.id.edit_confirm)).perform(click());

        // Check if the total updates correctly after editing the item
        onView(withId(R.id.total)).check(matches(withText("$672.01")));
    }

    // Test method for deleting an item and checking if the total updates
    @Test
    public void testTotalDelete() {
        // Click on an item in the list and then click on the "Delete" button
        onData(is(instanceOf(Item.class))).atPosition(0).perform(click());
        onView(withId(R.id.view_belete)).perform(scrollTo());
        onView(withId(R.id.view_belete)).perform(click());

        // Check if the total updates correctly after deleting the item
        onView(withId(R.id.total)).check(matches(withText("$472.00")));
    }

    // Test method for deleting multiple items and checking if the total updates
    @Test
    public void testTotalDeleteLongPress() {
        // Long press on an item, click on checkboxes, and then click on the "Delete" button
        onData(is(instanceOf(Item.class))).atPosition(0).perform(longClick());
        onData(is(instanceOf(Item.class))).atPosition(0).onChildView(withId(R.id.checkbox)).perform(click());
        onData(is(instanceOf(Item.class))).atPosition(1).onChildView(withId(R.id.checkbox)).perform(click());
        onView(withId(R.id.delete_button_multiple)).perform(click());

        // Check if the total updates correctly after deleting multiple items
        onView(withId(R.id.total)).check(matches(withText("$22.00")));
    }
}