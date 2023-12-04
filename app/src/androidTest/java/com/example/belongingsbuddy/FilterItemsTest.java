package com.example.belongingsbuddy;
/**
 * Sleep calls are because it goes too fast after a transition and cannot find views that do exist
 */
import static android.app.PendingIntent.getActivity;
import static androidx.core.util.Predicate.not;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.google.common.base.CharMatcher.is;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import android.app.Activity;
import android.widget.DatePicker;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.google.android.material.datepicker.MaterialDatePicker;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

/**
 * Tests functionality of AddItemActivity
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class FilterItemsTest {
    @Rule
    public ActivityScenarioRule<MainActivity> filterItemsScenario = new ActivityScenarioRule<>(MainActivity.class);
    /**
     * Check if filtering without anything shows dismiss layout (it shouldn't)
     */
    @Test
    public void filterByNothing(){
        testsSetup("Apple");
        testsSetup("Banana");
        onView(withId(R.id.filter_button)).perform(click());
        onView(withId(R.id.filter_ok)).perform(click());
        try {Thread.sleep(2000);} catch (InterruptedException ignored) {}
        onView(withId(R.id.filter_type_layout)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }
    @Test
    public void filterByNoMatches(){
        testsSetup("Apple");
        testsSetup("Banana");
        // filter no item has
        onView(withId(R.id.filter_button)).perform(click());
        onView(withId(R.id.filter_selected_keywords)).perform(replaceText("XXX"));
        onView(withId(R.id.filter_ok)).perform(click());
        try {Thread.sleep(2000);} catch (InterruptedException ignored) {}
        // Check if the ListView is empty
        onView(withText("Apple")).check(doesNotExist());
        onView(withText("Banana")).check(doesNotExist());
    }
    @Test
    public void filterDismiss(){
        testsSetup("Apple");
        testsSetup("Banana");
        // filter no item has
        onView(withId(R.id.filter_button)).perform(click());
        onView(withId(R.id.filter_selected_keywords)).perform(replaceText("Apple"));
        onView(withId(R.id.filter_ok)).perform(click());
        try {Thread.sleep(2000);} catch (InterruptedException ignored) {}
        // Check if dismiss button avaible
        onView(withId(R.id.filter_type_layout)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }
    @Test
    public void filterByDesc(){
        testsSetup("Apple");
        testsSetup("Banana");
        // filter no item has
        onView(withId(R.id.filter_button)).perform(click());
        onView(withId(R.id.filter_selected_keywords)).perform(replaceText("Apple"));
        onView(withId(R.id.filter_ok)).perform(click());
        try {Thread.sleep(2000);} catch (InterruptedException ignored) {}
        // Check if the ListView is just apple
        onData(Matchers.is(instanceOf(Item.class))).atPosition(0).onChildView(withId(R.id.item_name)).check(matches(withText("Apple")));
        onView(withText("Banana")).check(doesNotExist());
    }
    @Test
    public void filterByMake(){
        testsSetup("Apple");
        testsSetup("Banana");
        // filter no item has
        onView(withId(R.id.filter_button)).perform(click());
        onView(withId(R.id.filter_selected_makes)).perform(replaceText("Apple"));
        onView(withId(R.id.filter_ok)).perform(click());
        try {Thread.sleep(2000);} catch (InterruptedException ignored) {}
        // Check if the ListView is just apple
        onData(Matchers.is(instanceOf(Item.class))).atPosition(0).onChildView(withId(R.id.item_name)).check(matches(withText("Apple")));
        onView(withText("Banana")).check(doesNotExist());
    }

    @Test
    public void filterByDate(){
        testsSetupDate("Apple", 1989, 2, 2);
        testsSetupDate("Banana", 1989, 3, 2);
        // filter no item has
        onView(withId(R.id.filter_button)).perform(click());
//        onView(withId(R.id.filter_pick_date_button)).perform(click());
        onView(withId(R.id.filter_ok)).perform(click());
        try {Thread.sleep(2000);} catch (InterruptedException ignored) {}

        // check if just apple
//        onData(Matchers.is(instanceOf(Item.class))).atPosition(0).onChildView(withId(R.id.item_name)).check(matches(withText("Apple")));
//        onView(withText("Banana")).check(doesNotExist());

    }
    /**
     * adds a MockItem to be edited in tests
     */
    public void testsSetup(String name) {
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
        onView(withId(R.id.add_value)).perform(replaceText("1.99"));
        // date
        onView(withId(R.id.add_date)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_pick_date_button)).perform(click());
        onView(withText("OK")).perform(click());
        //click confirm and verify correct resultCode was given
        //onView(withId(R.id.add_confirm)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_confirm)).perform(click());
    }
    public void testsSetupDate(String name, int y, int m, int d) {
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
        onView(withId(R.id.add_value)).perform(replaceText("1.99"));
        // date
        onView(withId(R.id.add_date)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        // Interact with the date picker and set a specific date
        onView(withId(R.id.add_pick_date_button)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(y, m, d));
        onView(withText("OK")).perform(click());
        //click confirm and verify correct resultCode was given
        //onView(withId(R.id.add_confirm)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.add_confirm)).perform(click());
    }
}