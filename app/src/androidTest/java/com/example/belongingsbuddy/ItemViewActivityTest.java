package com.example.belongingsbuddy;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests functionality of ItemViewActivity.
 * NOTE: all tests pass when run individually, but running all tests in ItemViewActivityTest in
 * sequence will cause issues with firestore.
 * This will be fixed for the final product
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ItemViewActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> viewItemScenario = new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Tests the functionality of the BACK button in the ItemViewActivity class
     */
    @Test
    public void viewItemBackTest(){
        testsSetup();
        onView(withId(R.id.view_master_view)).check(matches(isDisplayed()));
        //onView(withId(R.id.view_back)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.view_back)).perform(click());
        Integer resultCode = MainActivity.lastResult;
        assertEquals(ItemViewActivity.REQUEST_CODE_BACK, (int) resultCode);
    }

    /**
     * Tests the functionality of the DELETE button in the ItemViewActivity class
     */
    @Test
    public void viewItemDeleteTest(){
        testsSetup();
        onView(withId(R.id.view_master_view)).check(matches(isDisplayed()));
        //onView(withId(R.id.view_back)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.view_belete)).perform(click());
        Integer resultCode = MainActivity.lastResult;
        assertEquals(ItemViewActivity.REQUEST_CODE_DELETE, (int) resultCode);
    }

    /**
     * Tests the functionality of the EDIT button in the ViewItemActivity class
     */
    @Test
    public void viewItemEditTest(){
        testsSetup();
        onView(withId(R.id.view_master_view)).check(matches(isDisplayed()));
        //onView(withId(R.id.view_edit)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.view_edit)).perform(click());
        Integer resultCode = MainActivity.lastResult;
        assertEquals(ItemViewActivity.REQUEST_CODE_EDIT, (int) resultCode);
    }


    /**
     * adds a MockItem to be viewed in tests
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
        // click the item to start ViewItemActivity
        onView(withText("test name")).perform(click());

    }
}
