package com.example.belongingsbuddy;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;
import android.Manifest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * UI tests for barcode scanning
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ScannerTest {
    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);
    @Rule
    public GrantPermissionRule permissionCamera = GrantPermissionRule.grant(Manifest.permission.CAMERA);

    /**
     * opens the scanner
     */
    @Test
    public void testOpenScanner() {
        onView(withId(R.id.add_item)).perform(click());
        onView(withId(R.id.scan_bar)).perform(click());

        onView(withId(R.id.camera_view)).check(matches(isDescendantOfA(withId(android.R.id.content))));
    }

    /**
     * opens the scanner and uses the back button
     */
    @Test
    public void testBackButton() {
        onView(withId(R.id.add_item)).perform(click());
        onView(withId(R.id.scan_bar)).perform(click());

        onView(withId(R.id.camera_view)).check(matches(isDescendantOfA(withId(android.R.id.content))));
        onView(withId(R.id.scannerBackButton)).perform(click());
    }
}