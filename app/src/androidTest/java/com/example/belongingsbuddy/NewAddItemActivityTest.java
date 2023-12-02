package com.example.belongingsbuddy;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.app.Activity;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Rule;
import org.junit.Test;

public class NewAddItemActivityTest {
    @Rule
    public ActivityScenarioRule<AddItemActivity> addItemScenario = new ActivityScenarioRule<>(AddItemActivity.class);


    /**
     * Test that the CANCEL returns the correct result code
     */
    @Test
    public void TestCancel(){
        //cancel the activity
        onView(withId(R.id.add_cancel)).perform(click());
        Integer resultCode = MainActivity.lastResult;
        assertEquals(Activity.RESULT_CANCELED, (int) resultCode);
    }

    @Test
    /**
     * give input in all required fields and then click the CONFIRM button.
     * Check that AddItemActivity returns the correct result code
     */
    public void testInputData(){
        // input required information to create an Item
        // name
        //onView(withId(R.id.add_name)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_name)).perform(typeText("test name"));
        // description
        //onView(withId(R.id.add_description)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
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
        onView(withId(R.id.add_confirm)).perform(click());
        Integer resultCode = MainActivity.lastResult;
        assertEquals(Activity.RESULT_OK, (int) resultCode);
    }

    @Test
    /**
     * fill out all required fields (including comment and serial number) and then click the CONFIRM button.
     * Check that AddItemActivity returns the correct result code
     */
    public void testInputDataWithSerialAndComment(){

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
        onView(withId(R.id.add_confirm)).perform(click());
        Integer resultCode = MainActivity.lastResult;
        assertTrue(Activity.RESULT_OK == resultCode);
    }

    @Test
    /**
     * test that clicking CONFIRM without giving input does not end the activity
     */
    public void testInvalidInput(){
        // try clicking CONFIRM without filling out required input
        onView(withId(R.id.add_confirm)).perform(click());
        // the AddItemActivity should not have ended, because they user did not give required input
        onView(withId(R.id.add_master_view)).check(ViewAssertions.matches(isDisplayed()));
    }

}
