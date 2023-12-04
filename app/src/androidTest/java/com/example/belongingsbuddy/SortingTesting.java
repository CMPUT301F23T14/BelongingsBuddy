package com.example.belongingsbuddy;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.PickerActions.setDate;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
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
import androidx.test.espresso.assertion.ViewAssertions;
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

    /**
     * Tests if sort type is displayed for date
     */

    /**
     * Tests if rollback ui works
     */
    // Check whether X works
    @Test
    public void testSortRollback() {
//        testsSetup("test 1", 2002, 1, 1, "a",  "a",  "12.00");
//        testsSetup("test 2", 2010, 1, 1, "x",  "x",  "2.00");
        // Enter the sort screen
        onView(withId(R.id.sort_button)).perform(click());
        // selection
        onView(withId(R.id.sort_value)).perform(click());
        onView(withId(R.id.sort_ok)).perform(click());

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
        testsSetup("1", 2002, 1, 1, "a",  "a",  "12.00");
        testsSetup("2", 2010, 1, 1, "x",  "x",  "2.00");
        // Enter the sort screen
        onView(withId(R.id.sort_button)).perform(click());
        // selection
        onView(withId(R.id.sort_value)).perform(click());
        onView(withId(R.id.sort_ok)).perform(click());

        onData(is(instanceOf(Item.class))).atPosition(0).onChildView(withId(R.id.item_name)).check(matches(withText("2")));
        onData(is(instanceOf(Item.class))).atPosition(1).onChildView(withId(R.id.item_name)).check(matches(withText("1")));
    }
    /**
     * Tests make asc works
     */
    // Check wether make sort works
    @Test
    public void testMakeSortAsc() {
        testsSetup("1", 2002, 1, 1, "a",  "a",  "12.00");
        testsSetup("2", 2010, 1, 1, "x",  "x",  "2.00");
        // Enter the sort screen
        onView(withId(R.id.sort_button)).perform(click());
        // selection
        onView(withId(R.id.sort_make)).perform(click());
        onView(withId(R.id.sort_ok)).perform(click());

        onData(is(instanceOf(Item.class))).atPosition(0).onChildView(withId(R.id.item_name)).check(matches(withText("1")));
        onData(is(instanceOf(Item.class))).atPosition(1).onChildView(withId(R.id.item_name)).check(matches(withText("2")));
    }
    /**
     * Tests desc asc works
     */
    // Check wether desc sort works
    @Test
    public void testDescSortAsc() {
        testsSetup("1", 2002, 1, 1, "a",  "a",  "12.00");
        testsSetup("2", 2010, 1, 1, "x",  "x",  "2.00");
        // Enter the sort screen
        onView(withId(R.id.sort_button)).perform(click());
        // selection
        onView(withId(R.id.sort_desc)).perform(click());
        onView(withId(R.id.sort_ok)).perform(click());

        onData(is(instanceOf(Item.class))).atPosition(0).onChildView(withId(R.id.item_name)).check(matches(withText("1")));
        onData(is(instanceOf(Item.class))).atPosition(1).onChildView(withId(R.id.item_name)).check(matches(withText("2")));
    }
    /**
     * Tests value desc works
     */
    // DESC
    // Check wether value sort works
    @Test
    public void testValueSortDesc() {
        testsSetup("1", 2002, 1, 1, "a",  "a",  "12.00");
        testsSetup("2", 2010, 1, 1, "x",  "x",  "2.00");
        // Enter the sort screen
        onView(withId(R.id.sort_button)).perform(click());
        // selection
        onView(withId(R.id.sort_value)).perform(click());
        onView(withId(R.id.descending_rb)).perform(click());
        onView(withId(R.id.sort_ok)).perform(click());

        onData(is(instanceOf(Item.class))).atPosition(0).onChildView(withId(R.id.item_name)).check(matches(withText("1")));
        onData(is(instanceOf(Item.class))).atPosition(1).onChildView(withId(R.id.item_name)).check(matches(withText("2")));
    }
    /**
     * Tests make desc works
     */
    @Test
    public void testMakeSortDesc() {
        testsSetup("1", 2002, 1, 1, "a",  "a",  "12.00");
        testsSetup("2", 2010, 1, 1, "x",  "x",  "2.00");
        // Enter the sort screen
        onView(withId(R.id.sort_button)).perform(click());
        // selection
        onView(withId(R.id.sort_make)).perform(click());
        onView(withId(R.id.descending_rb)).perform(click());
        onView(withId(R.id.sort_ok)).perform(click());

        onData(is(instanceOf(Item.class))).atPosition(0).onChildView(withId(R.id.item_name)).check(matches(withText("2")));
        onData(is(instanceOf(Item.class))).atPosition(1).onChildView(withId(R.id.item_name)).check(matches(withText("1")));
    }
    /**
     * Tests desc desc works
     */
    @Test
    public void testDescSortDesc() {
        testsSetup("1", 2002, 1, 1, "a",  "a",  "12.00");
        testsSetup("2", 2010, 1, 1, "x",  "x",  "2.00");
        // Enter the sort screen
        onView(withId(R.id.sort_button)).perform(click());
        // selection
        onView(withId(R.id.sort_desc)).perform(click());
        onView(withId(R.id.descending_rb)).perform(click());
        onView(withId(R.id.sort_ok)).perform(click());

        onData(is(instanceOf(Item.class))).atPosition(0).onChildView(withId(R.id.item_name)).check(matches(withText("2")));
        onData(is(instanceOf(Item.class))).atPosition(1).onChildView(withId(R.id.item_name)).check(matches(withText("1")));
    }
    /**
     * Tests date asc works
     */
    @Test
    public void testDateSortAsc() {
        testsSetup("1", 2002, 1, 1, "a",  "a",  "12.00");
        testsSetup("2", 2010, 1, 1, "x",  "x",  "2.00");
        // Enter the sort screen
        onView(withId(R.id.sort_button)).perform(click());
        // selection
        onView(withId(R.id.sort_date)).perform(click());
        onView(withId(R.id.ascending_rb)).perform(click());
        onView(withId(R.id.sort_ok)).perform(click());

        onData(is(instanceOf(Item.class))).atPosition(0).onChildView(withId(R.id.item_name)).check(matches(withText("1")));
        onData(is(instanceOf(Item.class))).atPosition(1).onChildView(withId(R.id.item_name)).check(matches(withText("2")));
    }
    @Test
    public void testDateSortDsc() {
        testsSetup("1", 2002, 1, 1, "a",  "a",  "12.00");
        testsSetup("2", 2010, 1, 1, "x",  "x",  "2.00");
        // Enter the sort screen
        onView(withId(R.id.sort_button)).perform(click());
        // selection
        onView(withId(R.id.sort_date)).perform(click());
        onView(withId(R.id.descending_rb)).perform(click());
        onView(withId(R.id.sort_ok)).perform(click());

        onData(is(instanceOf(Item.class))).atPosition(0).onChildView(withId(R.id.item_name)).check(matches(withText("2")));
        onData(is(instanceOf(Item.class))).atPosition(1).onChildView(withId(R.id.item_name)).check(matches(withText("1")));
    }

    public void testsSetup(String name, int y, int m, int d, String desc, String make, String value) {
        // start add item activity
        onView(withId(R.id.add_item)).perform(click());
        onView(withId(R.id.input_manually)).perform(click());
        // input required information to create an Item
        // name
        onView(withId(R.id.add_name)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_name)).perform(replaceText(name));
        // description
        onView(withId(R.id.add_description)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_description)).perform(replaceText(desc));
        // make
        onView(withId(R.id.add_make)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_make)).perform(replaceText(make));
        // model
        onView(withId(R.id.add_model)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_model)).perform(replaceText(name));
        // value
        onView(withId(R.id.add_value)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_value)).perform(replaceText(value));
        // date
        onView(withId(R.id.add_date)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        // Interact with the date picker and set a specific date
        onView(withId(R.id.add_pick_date_button)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(y, m, d));
        onView(withText("OK")).perform(click());
        //click confirm and verify correct resultCode was given
        //onView(withId(R.id.add_confirm)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_confirm)).perform(click());
    }
}