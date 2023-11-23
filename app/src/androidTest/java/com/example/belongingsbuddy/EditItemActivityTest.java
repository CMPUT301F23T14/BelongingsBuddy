package com.example.belongingsbuddy;

/**
 * Test functionality of EditItemActivity
 * NOTE: all tests pass when run individually, but running all tests in ItemViewActivityTest in
 * sequence will cause issues with firestore.
 * This will be fixed for the final product
 */

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertEquals;

import android.app.Activity;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests functionality of AddItemActivity
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class EditItemActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> editItemScenario = new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Tests the functionality of the CANCEL button in the EditItemActivity class
     * and verifies correct resultCode is returned
     */
    @Test
    public void CancelButtonTest(){
        testsSetup();
        onView(withId(R.id.edit_cancel)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.edit_cancel)).perform(click());
        Integer resultCode = MainActivity.lastResult;
        assertEquals(Activity.RESULT_CANCELED, (int) resultCode);
    }

    /**
     * Tests the functionality of the CONFIRM button in the EditItemActivity class
     * and verifies correct resultCode is returned
     */
    @Test
    public void ConfirmButtonTest(){
        testsSetup();
        onView(withId(R.id.edit_confirm)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.edit_confirm)).perform(click());
        Integer resultCode = MainActivity.lastResult;
        assertEquals(Activity.RESULT_OK, (int) resultCode);
    }

    /**
     * Edits an Item and confirms that the changes were applied
     */
    @Test
    public void MakeChangesTest(){
        testsSetup();
        // edit the name
        onView(withId(R.id.edit_name)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.edit_name)).perform(clearText());
        onView(withId(R.id.edit_name)).perform(typeText("new name >:)"));
        // edit the value
        onView(withId(R.id.edit_value)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.edit_value)).perform(clearText());
        onView(withId(R.id.edit_value)).perform(typeText("500"));
        // confirm changes
        onView(withId(R.id.edit_confirm)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.edit_confirm)).perform(click());
        // item with name "new name >:)" should be in listView now
        onView(withText("new name >:)")).check(ViewAssertions.matches(isDisplayed()));
        // new total should be updated
        // new total should be 950 (650 default + 500 form edited item)
        onView(withId(R.id.total)).check(matches(withText("$1150.00")));
    }

    /**
     * Tries to edit and Item by removing its name. The confirm button should not work,
     * because invalid input was given
     */
    @Test
    public void testInvalidEdit(){
        testsSetup();
        // remove the name of the Item
        onView(withId(R.id.edit_name)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.edit_name)).perform(clearText());
        // try to click CONFIRM button
        onView(withId(R.id.edit_confirm)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.edit_confirm)).perform(click());
        // the EditItemActivity should not have ended, because they user did not give required input
        onView(withId(R.id.edit_master_view)).check(ViewAssertions.matches(isDisplayed()));
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
        onView(withId(R.id.add_confirm)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_confirm)).perform(click());
        // click the item to start get to item view screen
        onView(withText("test name")).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withText("test name")).perform(click());
        // click the edit button
        onView(withId(R.id.view_edit)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.view_edit)).perform(click());

    }
}
