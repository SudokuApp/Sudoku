package c.b.a.sudokuapp;

import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.facebook.login.Login;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class RegisterActivityTest {

    @Rule
    public ActivityTestRule<RegisterActivity> registerActivityActivityTestRule =
            new ActivityTestRule<>(RegisterActivity.class, true, false);

    @Rule
    public ActivityTestRule<LoginActivity> loginActivityActivityTestRule =
            new ActivityTestRule<>(LoginActivity.class, true, false);

    private RegisterActivity activity;
    private ViewInteraction email;
    private ViewInteraction password;
    private ViewInteraction btn;
    private ViewInteraction login;

    @Before
    public void setUp() {
        registerActivityActivityTestRule.launchActivity(new Intent());
        activity = registerActivityActivityTestRule.getActivity();

        email = onView(withId(R.id.emailreg));
        password = onView(withId(R.id.passwordred));
        btn = onView(withId(R.id.buttonregister));
        login = onView(withId(R.id.textgotolgoin));
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
    public void invalidEmailTest() {
        email.perform(typeText("abcdef"), closeSoftKeyboard());
        password.perform(typeText("testing"), closeSoftKeyboard());
        btn.perform(click());
        email.check(matches(hasFocus()));
        email.check(matches(hasErrorText(activity.getString(R.string.invalidEmail))));
    }

    @Test
    public void invalidPasswordTest() {
        email.perform(typeText("test@test.is"), closeSoftKeyboard());
        password.perform(typeText("12345"), closeSoftKeyboard());
        btn.perform(click());
        password.check(matches(hasErrorText(activity.getString(R.string.invalidPassword))));
    }

}