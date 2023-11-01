package com.example.belongingsbuddy;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static
        androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import android.view.View;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.firebase.auth.FirebaseAuth;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginActivityTest {
    private View decorView;
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
     * check if
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

    @Test
    public void testLogin() {
        ActivityScenario<LoginActivity> rule = login_scenario.getScenario();
        ActivityScenario<MainActivity> rule2 = main_scenario.getScenario();
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
        onView(withId(R.id.username_input)).perform(clearText());
        onView(withId(R.id.password_input)).perform(clearText());
        onView(withId(R.id.login_confirm)).perform(click());
        rule.getState().isAtLeast(Lifecycle.State.CREATED);
        onView(withId(R.id.login_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.create_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.back_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.login_confirm)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.sign_up_confirm)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.username_input)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.password_input)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.username_input)).perform(clearText());
        onView(withId(R.id.password_input)).perform(clearText());
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
        onView(withId(R.id.username_input)).perform(clearText());
        onView(withId(R.id.password_input)).perform(clearText());
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
        onView(withId(R.id.username_input)).perform(clearText());
        onView(withId(R.id.password_input)).perform(clearText());
        onView(withId(R.id.username_input)).perform(typeText("test@gmail.com"));
        onView(withId(R.id.password_input)).perform(typeText("Test_password123"));
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword("test@gmail.com", "Test_password123");
        onView(withId(R.id.login_confirm)).perform(click());
        rule2.getState().isAtLeast(Lifecycle.State.CREATED);
    }

}
