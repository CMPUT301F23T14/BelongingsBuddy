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
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
// CANNOT be run all at once for some reason (seems to be firestore problems)

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SortingTesting {
    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);

    // Check whether sort type is displayed
    @Test
    public void testSortDateDisplaysType() {
        ActivityScenario<MainActivity> scenario = rule.getScenario();
        scenario.recreate();
        // Enter the sort screen
        onView(withId(R.id.sort_button)).perform(click());
        // selection
        onView(withId(R.id.sort_date)).perform(click());
        onView(withId(R.id.sort_ok)).perform(click());
        // Check for correct string
        onView(withId(R.id.sort_type_textview)).check(matches(withText("Date")));
    }
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
}