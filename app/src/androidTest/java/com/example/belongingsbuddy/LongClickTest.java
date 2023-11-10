package com.example.belongingsbuddy;
import android.content.Intent;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.not;
import static java.util.regex.Pattern.matches;

import com.example.belongingsbuddy.MainActivity;

@RunWith(AndroidJUnit4.class)
public class LongClickTest {

    @Rule
    public ActivityScenarioRule<MainActivity> main_scenario = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testLongClickSelectAndDelete() {
        // Assuming the activity has loaded and there are items in the list

        // Perform long click on the first item
        Espresso.onView(withId(R.id.item_list)).perform(longClick());

        // Click on the first item
        Espresso.onView(withId(R.id.item_list)).perform(click());

        // Click on the delete button
        Espresso.onView(withId(R.id.delete_button_multiple)).perform(click());

        // Add a brief delay to wait for the delete action to complete
        try {
            Thread.sleep(10000); // Adjust the sleep duration as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if the item is no longer displayed in the list



        // Check if the expected intent to MainActivity is sent
        //Intents.intended(IntentMatchers.hasComponent(MainActivity.class.getName()));

        // You may want to add additional checks based on your specific requirements
    }


}





