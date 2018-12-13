package c.b.a.sudokuapp;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.app.FragmentManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class MenuFragmentTest {

    /*private ViewInteraction newGame;
    private MenuActivity menuActivity;

    @Rule
    public ActivityTestRule<MenuActivity> menuActivityActivityTestRule =
            new ActivityTestRule<>(MenuActivity.class);

    @Before
    public void setUp() {
        menuActivityActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction();

        menuActivity = menuActivityActivityTestRule.getActivity();
        newGame = onView(withId(R.id.new_game_btn));
    }

    @Test
    public void newGameButtonTest() {
        //newGame.perform(click());

        FragmentManager fragmentManager = menuActivity.getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_frag, new DifficultyFragment()).commit();
    }*/
}