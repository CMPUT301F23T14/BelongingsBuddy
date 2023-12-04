package com.example.belongingsbuddy;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test class that tests the functionality of the user button and signout functionality
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityUserButtonTest {
    @Rule
    public ActivityScenarioRule<MainActivity> main_scenario = new ActivityScenarioRule<>(MainActivity.class);

    /**
     * test functionality of the user sign out button
     */
    @Test
    public void testUserButton() {
        onView(withId(R.id.username)).perform(click());
        onView(withText("CANCEL")).perform(click());
        onView(withId(R.id.username)).perform(click());
        onView(withText("SIGN OUT")).perform(click());
    }

    /**
     * test functionality of select all buttons
     */
    @Test
    public void testSelectAll() {
        onView(withId(R.id.select_all_button)).perform(click());
        onView(withId(R.id.checkbox)).perform(click());
        onView(withId(R.id.cancel_button)).perform(click());
        onView(withId(R.id.select_all_button)).perform(click());
        onView(withId(R.id.delete_button_multiple)).perform(click());
    }
}
