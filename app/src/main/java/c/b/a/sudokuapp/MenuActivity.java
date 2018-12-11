package c.b.a.sudokuapp;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Activity with two fragments. First fragment letting user choose between
 * "resume game" and "new game". Second fragment letting user choose between
 * difficulty levels; easy, medium and hard.
 */
public class MenuActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    /**
     * Activity is created and starts the Main menu fragment
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.main_frag, new MenuFragment()).commit();
        fragmentManager.executePendingTransactions();
    }
}
