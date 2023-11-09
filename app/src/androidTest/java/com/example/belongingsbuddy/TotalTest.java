package com.example.belongingsbuddy;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

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

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TotalTest {
    @Rule public ActivityScenarioRule<MainActivity> scenario = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testIsTotalThere() {
        onView(withId(R.id.total)).check(matches(withText("$")));
    }
    public void testTotalUpdate() {
        onView(withId(R.id.add_item)).perform(click());
        onView(withId(R.id.add_name)).perform(ViewActions.typeText("TEST"));
        onView(withId(R.id.add_description)).perform(ViewActions.typeText("TEST"));
        onView(withId(R.id.add_value)).perform(ViewActions.typeText("22.0"));
        onView(withId(R.id.add_make)).perform(ViewActions.typeText("TEST"));
        onView(withId(R.id.add_model)).perform(ViewActions.typeText("TEST"));
        onView(withId(R.id.add_date)).perform(PickerActions.
    }
}