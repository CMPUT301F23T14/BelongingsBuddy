package com.example.belongingsbuddy;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;

import android.view.View;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests the functionality of all the buttons in TagActivity
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class TagTests {
    @Rule
    public ActivityScenarioRule<LoginActivity> tag_scenario = new ActivityScenarioRule<>(TagActivity.class);

    /**
     *  check that the buttons in TagActivity work properly
     */
    @Test
    public void TagActivityConfirm() {
        onView(withId(R.id.btnConfirm)).perform(click());
        onView(withId(R.id.searchView)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.elementListView)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.linearLayout2)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

    }

    @Test
    public void TagActivityCancel() {
        onView(withId(R.id.btnCancel)).perform(click());
        onView(withId(R.id.searchView)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.elementListView)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.linearLayout2)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

    }
}

