package c.b.a.sudokuapp;

import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> loginActivityActivityTestRule =
            new ActivityTestRule<>(LoginActivity.class, true, false);

    @Rule
    public ActivityTestRule<MenuActivity> menuActivityActivityTestRule =
            new ActivityTestRule<>(MenuActivity.class, true, false);

    private LoginActivity activity;
    private ViewInteraction email;
    private ViewInteraction btn;
    private ViewInteraction password;

    @Before
    public void setUp() {
        loginActivityActivityTestRule.launchActivity(new Intent());
        activity = loginActivityActivityTestRule.getActivity();

        email = onView(withId(R.id.login_email));
        password = onView(withId(R.id.login_password));
        btn = onView(withId(R.id.login_btn));
    }

    @After
    public void tearDown() {}

    @Test
    public void noInputTest() {
        btn.perform(click());
        email.check(matches(hasFocus()));
        email.check(matches(hasErrorText(activity.getString(R.string.required))));
    }

    @Test
    public void noPasswordInputTest() {
        email.perform(typeText("test@test.is"), closeSoftKeyboard());
        btn.perform(click());
        password.check(matches(hasFocus()));
        password.check(matches(hasErrorText(activity.getString(R.string.required))));
    }

    @Test
    public void notExistingAccountTest() {
        email.perform(typeText("test@test.is"), closeSoftKeyboard());
        password.perform(typeText("123"), closeSoftKeyboard());
        btn.perform(click());
        email.check(matches(hasFocus()));
        email.check(matches(hasErrorText(activity.getString(R.string.email_password_incorrect))));
    }
}