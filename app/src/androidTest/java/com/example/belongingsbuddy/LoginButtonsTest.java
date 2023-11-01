package com.example.belongingsbuddy;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;

import android.view.View;

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

/**
 * Tests the functionality of all the buttons in the LoginActivity
 * Tests user input verification
 * TEST ALL FUNCTIONS INDIVIDUALLY AS testSignUp and testLogin need to be tested individually
 * with testSignUp being tested first and the account on the Firebase Authentication
 * Console with the email test@gmail.com must be deleted before hand
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginButtonsTest {
    @Rule
    public ActivityScenarioRule<MainActivity> main_scenario = new ActivityScenarioRule<>(MainActivity.class);
    @Rule
    public ActivityScenarioRule<LoginActivity> login_scenario = new ActivityScenarioRule<>(LoginActivity.class);

    /**
     *  check if the login button on the initial login activity changes screen visibility and clears inputs properly
     */
    @Test
    public void testLoginButton() {
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.login_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.create_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.back_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.login_confirm)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.sign_up_confirm)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.username_input)).check(matches(withText("")));
        onView(withId(R.id.password_input)).check(matches(withText("")));
    }

    /**
     *  check if the sign up button on the initial login activity changes screen visibility and clears inputs properly
     */
    @Test
    public void testSignUpButton() {
        onView(withId(R.id.create_button)).perform(click());
        onView(withId(R.id.login_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.create_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.back_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.login_confirm)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.sign_up_confirm)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.username_input)).check(matches(withText("")));
        onView(withId(R.id.password_input)).check(matches(withText("")));
    }

    /**
     *  check if the back button on the login activity where the user inputs the username (email) and password
     *  changes screen visibility and clears inputs properly
     */
    @Test
    public void testBackButton() {
        onView(withId(R.id.create_button)).perform(click());
        for (int i = 0; i < 1; i++) {
            onView(withId(R.id.username_input)).perform(ViewActions.typeText("test@gmail.com"));
            onView(withId(R.id.password_input)).perform(typeText("test_password"));
            onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.back_button)).perform(click());
            onView(withId(R.id.login_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
            onView(withId(R.id.create_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
            onView(withId(R.id.back_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
            onView(withId(R.id.login_confirm)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
            onView(withId(R.id.sign_up_confirm)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
            onView(withId(R.id.username_input)).check(matches(withText("")));
            onView(withId(R.id.password_input)).check(matches(withText("")));
            onView(withId(R.id.username_input)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
            onView(withId(R.id.password_input)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
            onView(withId(R.id.login_button)).perform(click());
        }

    }


    /**
     * checks if program correctly handles the user not inputting an email when
     * signing up
     */
    @Test
    public void testSignUpUsernameEmpty() {
        ActivityScenario<LoginActivity> rule = login_scenario.getScenario();
        onView(withId(R.id.create_button)).perform(click());
        onView(withId(R.id.username_input)).perform(typeText(""));
        onView(withId(R.id.password_input)).perform(typeText("test_password"));
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.sign_up_confirm)).perform(click());
        rule.getState().isAtLeast(Lifecycle.State.CREATED);
        onView(withId(R.id.login_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.create_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.back_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.login_confirm)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.sign_up_confirm)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.username_input)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.password_input)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    /**
     * checks if program correctly handles the user not inputting a password when
     * signing up
     */
    @Test
    public void testSignUpPasswordEmpty() {
        ActivityScenario<LoginActivity> rule = login_scenario.getScenario();
        onView(withId(R.id.create_button)).perform(click());
        onView(withId(R.id.username_input)).perform(typeText("test@gmail.com"));
        onView(withId(R.id.password_input)).perform(typeText(""));
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.sign_up_confirm)).perform(click());
        rule.getState().isAtLeast(Lifecycle.State.CREATED);
        onView(withId(R.id.login_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.create_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.back_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.login_confirm)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.sign_up_confirm)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.username_input)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.password_input)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    /**
    * checks if program correctly handles the user not inputting an email and passwordwhen
     * signing up
     */
    @Test
    public void testSignUpUsernameAndPasswordEmpty() {
        ActivityScenario<LoginActivity> rule = login_scenario.getScenario();
        onView(withId(R.id.create_button)).perform(click());
        onView(withId(R.id.sign_up_confirm)).perform(click());
        rule.getState().isAtLeast(Lifecycle.State.CREATED);
        onView(withId(R.id.login_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.create_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.back_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.login_confirm)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.sign_up_confirm)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.username_input)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.password_input)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    /**
     * checks if user inputted an email that fits the email format when signing up
     */
    @Test
    public void testSignUpEmailFormat() {
        ActivityScenario<LoginActivity> rule = login_scenario.getScenario();
        onView(withId(R.id.create_button)).perform(click());
        onView(withId(R.id.username_input)).perform(ViewActions.typeText("testgmail.com"));
        onView(withId(R.id.password_input)).perform(typeText("test_password"));
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.sign_up_confirm)).perform(click());
        rule.getState().isAtLeast(Lifecycle.State.CREATED);
        onView(withId(R.id.login_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.create_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.back_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.login_confirm)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.sign_up_confirm)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.username_input)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.password_input)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    /**
     * checks if program correctly handles the user not inputting an email when
     * logging in
     */
    @Test
    public void testLoginEmailEmpty() {
        ActivityScenario<LoginActivity> rule = login_scenario.getScenario();
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.username_input)).perform(typeText(""));
        onView(withId(R.id.password_input)).perform(typeText("test_password"));
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.login_confirm)).perform(click());
        rule.getState().isAtLeast(Lifecycle.State.CREATED);
        onView(withId(R.id.login_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.create_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.back_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.login_confirm)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.sign_up_confirm)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.username_input)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.password_input)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    /**
     * checks if program correctly handles the user not inputting a password when
     * logging in
     */
    @Test
    public void testLoginPasswordEmpty() {
        ActivityScenario<LoginActivity> rule = login_scenario.getScenario();
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.username_input)).perform(typeText("test@gmail.com"));
        onView(withId(R.id.password_input)).perform(typeText(""));
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.login_confirm)).perform(click());
        rule.getState().isAtLeast(Lifecycle.State.CREATED);
        onView(withId(R.id.login_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.create_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.back_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.login_confirm)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.sign_up_confirm)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.username_input)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.password_input)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    /**
     * checks if program correctly handles the user not inputting an email and password when
     * logging in
     */
    @Test
    public void testLoginEmailAndPasswordEmpty() {
        ActivityScenario<LoginActivity> rule = login_scenario.getScenario();
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.login_confirm)).perform(click());
        rule.getState().isAtLeast(Lifecycle.State.CREATED);
        onView(withId(R.id.login_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.create_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.back_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.login_confirm)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.sign_up_confirm)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.username_input)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.password_input)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    /**
     * checks if the email inputted by the user conforms to the email format
     * while logging in
     */
    @Test
    public void testLoginEmailFormat() {
        ActivityScenario<LoginActivity> rule = login_scenario.getScenario();
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.username_input)).perform(ViewActions.typeText("testgmail.com"));
        onView(withId(R.id.password_input)).perform(typeText("test_password"));
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.login_confirm)).perform(click());
        rule.getState().isAtLeast(Lifecycle.State.CREATED);
        onView(withId(R.id.login_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.create_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.back_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.login_confirm)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.sign_up_confirm)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.username_input)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.password_input)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    /**
     * THESE NEXT 3 MUST BE RUN INDIVIDUALLY IN THE ORDER THEY APPEAR (testSignUp, then testLogin, then testFailedLogin)
     * BEFORE RUNNING testSignUp, ENSURE THE ACCOUNT IN FIREBASE AUTH CONSOLE
     * WITH THE EMAIL "test@gmail.com" IS DELETED
     * IF THE testSignUp FAILS TO AUTHORIZE SIGN UP, RESTART ANDROID STUDIO
     * FIREBASE AUTH CAN GET CONFUSED
     *
     * checks if signing up functions properly
     */
    @Test
    public void testSignUp() {
        ActivityScenario<MainActivity> rule2 = main_scenario.getScenario();
        onView(withId(R.id.create_button)).perform(click());
        onView(withId(R.id.username_input)).perform(typeText("test@gmail.com"));
        onView(withId(R.id.password_input)).perform(typeText("Test_password123"));
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.sign_up_confirm)).perform(click());
        rule2.getState().isAtLeast(Lifecycle.State.CREATED);
    }

    /**
     * checks if logging in functions properly
     */
    @Test
    public void testLogin() {
        ActivityScenario<MainActivity> rule2 = main_scenario.getScenario();
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.username_input)).perform(typeText("test@gmail.com"));
        onView(withId(R.id.password_input)).perform(typeText("Test_password123"));
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.login_confirm)).perform(click());
        rule2.getState().isAtLeast(Lifecycle.State.CREATED);
    }

    /**
     * tests if FirebaseAuth prevents users from logging into different accounts
     * with the incorrect email, user, or both
     */
    @Test
    public void failedLogin() {
        // test incorrect email
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.username_input)).perform(typeText("tes@gmail.com"));
        onView(withId(R.id.password_input)).perform(typeText("Test_password123"));
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.login_confirm)).perform(click());
        onView(withId(R.id.username_input)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.password_input)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.login_confirm)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.back_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        // test incorrect password
        onView(withId(R.id.username_input)).perform(replaceText("test@gmail.com"));
        onView(withId(R.id.password_input)).perform(replaceText("Test_password124"));
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.login_confirm)).perform(click());
        onView(withId(R.id.username_input)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.password_input)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.login_confirm)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.back_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

    }
}
