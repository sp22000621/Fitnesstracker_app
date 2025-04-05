package com.example.fitnesstracker_app;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;

public class MainActivity extends AppCompatActivity {

    public static final String PREF_SORT_ORDER = "sort_order";

    // Load the Goal List fragment on start
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(new GoalListFragment());
    }

    // Load a fragment into the main container
    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    // Inflate the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // Handle menu clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_add) {
            loadFragment(new GoalDetailsFragment());
            return true;
        } else if (id == R.id.menu_clear) {
            GoalDatabaseHelper dbHelper = new GoalDatabaseHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(GoalDatabaseHelper.TABLE_GOALS, null, null);
            db.close();
            dbHelper.close();
            loadFragment(new GoalListFragment());
            Toast.makeText(this, "All goals cleared", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menu_sort_order) {
            showSortOrderDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSortOrderDialog() {
        final CharSequence[] sortOptions = {"Sort by Name", "Sort by Deadline"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Sort Order")
                .setItems(sortOptions, (dialog, which) -> {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = prefs.edit();
                    if (which == 0) {
                        editor.putString(PREF_SORT_ORDER, "name");
                        Toast.makeText(this, "Sorting by Name", Toast.LENGTH_SHORT).show();
                    } else if (which == 1) {
                        editor.putString(PREF_SORT_ORDER, "deadline");
                        Toast.makeText(this, "Sorting by Deadline", Toast.LENGTH_SHORT).show();
                    }
                    editor.apply(); // Apply the changes asynchronously
                    // Reload the GoalListFragment to apply the sorting
                    loadFragment(new GoalListFragment());
                })
                .show();
    }
}