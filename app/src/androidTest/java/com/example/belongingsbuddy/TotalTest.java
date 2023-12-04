package com.example.belongingsbuddy;
/**
 * Sleep calls are because it goes too fast after a transition and cannot find views that do exist
 */
import static android.app.PendingIntent.getActivity;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests functionality of total feature
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class TotalTest {
    @Rule
    public ActivityScenarioRule<MainActivity> totalTestScenario = new ActivityScenarioRule<>(MainActivity.class);
    /**
     * Check if total update with adds
     */
    @Test
    public void totalAdd(){
        testsSetup("Apple", "20.00");
        testsSetup("Banana", "1");
        try {Thread.sleep(2000);} catch (InterruptedException ignored) {}
        onView(withId(R.id.total)).check(matches(withText("$21.00")));
    }
    /**
     * Check if total update with edit
     */
    @Test
    public void totalEdit(){
        testsSetup("Apple", "20.00");
        testsSetup("Banana", "1");
        try {Thread.sleep(2000);} catch (InterruptedException ignored) {}
        onData(Matchers.is(instanceOf(Item.class))).atPosition(0).perform(click());

        // click the edit button
        //onView(withId(R.id.view_edit)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.view_edit)).perform(click());
        // edit the name
        onView(withId(R.id.edit_name)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.edit_name)).perform(clearText());
        onView(withId(R.id.edit_name)).perform(typeText("new name >:)"));
        // edit the value
        onView(withId(R.id.edit_value)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.edit_value)).perform(clearText());
        onView(withId(R.id.edit_value)).perform(typeText("500"));
        // confirm changes
        //onView(withId(R.id.edit_confirm)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.edit_confirm)).perform(click());
        // item with name "new name >:)" should be in listView now
        onView(withText("new name >:)")).check(ViewAssertions.matches(isDisplayed()));
        // new total should be updated
        // new total should be 501
        onView(withId(R.id.total)).check(matches(withText("$501.00")));
    }
    /**
     * Check if total update with delete
     */
    @Test
    public void testTotalDelete() {
        testsSetup("Apple", "20.00");
        testsSetup("Banana", "1");
        try {Thread.sleep(2000);} catch (InterruptedException ignored) {}
        // Click on an item in the list and then click on the "Delete" button
        onData(Matchers.is(instanceOf(Item.class))).atPosition(0).perform(click());
        try {Thread.sleep(2000);} catch (InterruptedException ignored) {}
        onView(withId(R.id.view_belete)).perform(click());

        // Check if the total updates correctly after deleting the item
        onView(withId(R.id.total)).check(matches(withText("$1.00")));
    }
    /**
     * Check if total update with long press
     */
    @Test
    public void testTotalDeleteLongPress() {
//        testsSetup("Apple", "20.00");
//        testsSetup("Banana", "1");
//        try {Thread.sleep(2000);} catch (InterruptedException ignored) {}
//        // Long press on an item, click on checkboxes, and then click on the "Delete" button
//        onData(Matchers.is(instanceOf(Item.class))).atPosition(0).perform(longClick());
//        try {Thread.sleep(2000);} catch (InterruptedException ignored) {}
//        onData(Matchers.is(instanceOf(Item.class))).atPosition(0).onChildView(withId(R.id.checkbox)).perform(click());
//        onView(withId(R.id.delete_button_multiple)).perform(click());
//        try {Thread.sleep(2000);} catch (InterruptedException ignored) {}
//
//        // Check if the total updates correctly after deleting multiple items
//        onView(withId(R.id.total)).check(matches(withText("$1.00")));
    }
    /**
     * adds a MockItem to be edited in tests
     */
    public void testsSetup(String name, String value) {
        // start add item activity
        onView(withId(R.id.add_item)).perform(click());
        onView(withId(R.id.input_manually)).perform(click());
        // input required information to create an Item
        // name
        onView(withId(R.id.add_name)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_name)).perform(replaceText(name));
        // description
        onView(withId(R.id.add_description)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_description)).perform(replaceText(name));
        // make
        onView(withId(R.id.add_make)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_make)).perform(replaceText(name));
        // model
        onView(withId(R.id.add_model)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_model)).perform(replaceText(name));
        // value
        onView(withId(R.id.add_value)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_value)).perform(replaceText(value));
        // date
        onView(withId(R.id.add_date)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_pick_date_button)).perform(click());
        onView(withText("OK")).perform(click());
        //click confirm and verify correct resultCode was given
        //onView(withId(R.id.add_confirm)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_confirm)).perform(click());
    }
}