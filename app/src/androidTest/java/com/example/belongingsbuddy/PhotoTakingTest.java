package com.example.belongingsbuddy;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.Manifest;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * UI tests for photo taking
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class PhotoTakingTest {

    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);
    @Rule
    public GrantPermissionRule permissionCamera = GrantPermissionRule.grant(Manifest.permission.CAMERA);

    /**
     * open taking photos from the add menu
     */
    @Test
    public void testOpeningPhotosAdd() {
        onView(withId(R.id.add_item)).perform(click());
        onView(withId(R.id.input_manually)).perform(click());

        onView(withId(R.id.add_photo)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_photo)).perform(click());

        onView(withId(R.id.take_photo_button)).perform(click());
    }

    /**
     * open taking photos from the edit menu
     */
    @Test
    public void testOpeningPhotosEdit() {
        testsSetup();

        onView(withId(R.id.edit_photo)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.edit_photo)).perform(click());

        onView(withId(R.id.take_photo_button)).perform(click());
    }

    /**
     * Clicks the photo related buttons
     */
    @Test
    public void testButtons() {
        onView(withId(R.id.add_item)).perform(click());
        onView(withId(R.id.input_manually)).perform(click());

        onView(withId(R.id.add_photo)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_photo)).perform(click());

        onView(withId(R.id.take_photo_button)).perform(click());
        onView(withId(R.id.swap_camera_button)).perform(click());
        onView(withId(R.id.back_button_photo)).perform(click());
    }

    /**
     * adds a MockItem to be edited in tests
     */
    public void testsSetup() {
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
        //onView(withId(R.id.add_confirm)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_confirm)).perform(click());
        // click the item to start get to item view screen
        onView(withText("test name")).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withText("test name")).perform(click());
        // click the edit button
        //onView(withId(R.id.view_edit)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.view_edit)).perform(click());

    }
}
