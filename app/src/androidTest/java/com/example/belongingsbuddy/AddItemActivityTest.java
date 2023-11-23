package com.example.belongingsbuddy;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasBackground;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.content.Intent;

import androidx.test.espresso.NoActivityResumedException;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests functionality of AddItemActivity
 * NOTE: all tests pass when run individually, but running all tests in ItemViewActivityTest in
 * sequence will cause issues with firestore.
 * This will be fixed for the final product
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddItemActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> addItemScenario = new ActivityScenarioRule<>(MainActivity.class);

    /**
     * All the tests run fine individually, but when they are done in sequence by running
     * AddItemActivityTest it causes errors with firestore |
     * A temporary solution is to do all the tests under one big test (runTests)
     */
    @Test
    public void runTests(){
        testCancel();
        testInputData();
        testInputDataWithSerialAndComment();
        testInvalidInput();
    }

    /**
     * Test that the CANCEL returns the correct result code
     */
    public void testCancel(){
        // start add item activity
        onView(withId(R.id.add_item)).perform(click());
        onView(withId(R.id.input_manually)).perform(click());
        //cancel the activity
        onView(withText("CANCEL")).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_cancel)).perform(click());
        Integer resultCode = MainActivity.lastResult;
        assertEquals(Activity.RESULT_CANCELED, (int) resultCode);
    }

    /**
     * give input in all required fields and then click the CONFIRM button.
     * Check that AddItemActivity returns the correct result code
     */
    public void testInputData(){
        // start add item activity
        onView(withId(R.id.add_item)).perform(click());
        onView(withId(R.id.input_manually)).perform(click());
        // input required information to create an Item
        // name
        onView(withId(R.id.add_name)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_name)).perform(typeText("test name"));
        // description
        onView(withId(R.id.add_description)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_description)).perform(typeText("test description"));
        // make
        onView(withId(R.id.add_make)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_make)).perform(typeText("test make"));
        // model
        onView(withId(R.id.add_model)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_model)).perform(typeText("test model"));
        // value
        onView(withId(R.id.add_value)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_value)).perform(typeText("1.99"));
        // date
        onView(withId(R.id.add_date)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_pick_date_button)).perform(click());
        onView(withText("OK")).perform(click());
        //click confirm and verify correct resultCode was given
        onView(withId(R.id.add_confirm)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_confirm)).perform(click());
        Integer resultCode = MainActivity.lastResult;
        assertEquals(Activity.RESULT_OK, (int) resultCode);
    }

    /**
     * fill out all required fields (including comment and serial number) and then click the CONFIRM button.
     * Check that AddItemActivity returns the correct result code
     */
    public void testInputDataWithSerialAndComment(){
        // start add item activity
        onView(withId(R.id.add_item)).perform(click());
        onView(withId(R.id.input_manually)).perform(click());
        // input required information to create an Item
        // name
        onView(withId(R.id.add_name)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_name)).perform(typeText("test name"));
        // description
        onView(withId(R.id.add_description)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_description)).perform(typeText("test description"));
        // make
        onView(withId(R.id.add_make)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_make)).perform(typeText("test make"));
        // model
        onView(withId(R.id.add_model)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_model)).perform(typeText("test model"));
        // value
        onView(withId(R.id.add_value)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_value)).perform(typeText("1.99"));
        // date
        onView(withId(R.id.add_date)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_pick_date_button)).perform(click());
        onView(withText("OK")).perform(click());
        // SERIAL NUMBER
        onView(withId(R.id.add_serial_number)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_serial_number)).perform(typeText("123456"));
        // COMMENT
        onView(withId(R.id.add_comment)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_comment)).perform(typeText("Even though it is optional, I am adding a comment :))"));
        //click confirm and verify correct resultCode was given
        onView(withId(R.id.add_confirm)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_confirm)).perform(click());
        Integer resultCode = MainActivity.lastResult;
        assertTrue(Activity.RESULT_OK == resultCode);
    }

    /**
     * test that clicking CONFIRM without giving input does not end the activity
     */
    public void testInvalidInput(){
        // start add item activity
        onView(withId(R.id.add_item)).perform(click());
        onView(withId(R.id.input_manually)).perform(click());
        // try clicking CONFIRM without filling out required input
        onView(withId(R.id.add_confirm)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_confirm)).perform(click());
        // the AddItemActivity should not have ended, because they user did not give required input
        onView(withId(R.id.add_master_view)).check(ViewAssertions.matches(isDisplayed()));
    }

}
