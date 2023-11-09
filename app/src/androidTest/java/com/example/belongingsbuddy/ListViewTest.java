package com.example.belongingsbuddy;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
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
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
// CANNOT be run all at once for some reason (seems to be firestore problems)

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ListViewTest {
    @Rule
    public ActivityScenarioRule<MainActivity> scenario1 = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testAddDisplays() {
        // enter add
        onView(withId(R.id.add_item)).perform(click());
        onView(withId(R.id.input_manually)).perform(click());
        // Interact with the date picker
        onView(withId(R.id.add_pick_date_button)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(1989, 8, 25));
        onView(withText("OK")).perform(click());
        // value
        onView(withId(R.id.add_value)).perform(ViewActions.typeText("22"));
        // strings
        onView(withId(R.id.add_description)).perform(ViewActions.typeText("TEST"));
        onView(withId(R.id.add_make)).perform(ViewActions.typeText("TEST"));
        onView(withId(R.id.add_model)).perform(ViewActions.typeText("TEST"));
        onView(withId(R.id.add_name)).perform(scrollTo());
        onView(withId(R.id.add_name)).perform(ViewActions.typeText("APPL*E"));
        // exit add
        onView(withId(R.id.add_model)).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.add_confirm)).perform(scrollTo());
        onView(withId(R.id.add_confirm)).perform(click());
        // check if anything says APPLE
        onView(withText("APPL*E"));
    }
    @Test
    public void testEditDiplays() {
        onData(is(instanceOf(Item.class))).atPosition(0).perform(click());
        onView(withId(R.id.view_edit)).perform(scrollTo());
        onView(withId(R.id.view_edit)).perform(click());
        onView(withId(R.id.edit_name)).perform(scrollTo());
//        for (int i = 0; i < 10; i++) {
//            onView(withId(R.id.edit_name)).perform(pressKey(KeyEvent.KEYCODE_DEL));
//        }
        onView(withId(R.id.edit_name)).perform(ViewActions.typeText("CHEESE"));
        onView(withId(R.id.edit_model)).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.edit_confirm)).perform(scrollTo());
        onView(withId(R.id.edit_confirm)).perform(click());
        // check if anything says CHEESE
        onView(withText("CHEESE"));

    }
    @Test
    public void testDeleteDiplays() {
        onData(is(instanceOf(Item.class))).atPosition(0).perform(click());
        onView(withId(R.id.view_belete)).perform(scrollTo());
        onView(withId(R.id.view_belete)).perform(click());
        // check if Chair still there
        onView(withText("Chair")).check(doesNotExist());

    }
    @Test
    public void testDeleteLongPressDiplays() {
        onData(is(instanceOf(Item.class))).atPosition(0).perform(longClick());
        onData(is(instanceOf(Item.class))).atPosition(0).onChildView(withId(R.id.checkbox)).perform(click());
        onView(withId(R.id.delete_button_multiple)).perform(click());
        // check if Chair still there
        onView(withText("Chair")).check(doesNotExist());
    }
}