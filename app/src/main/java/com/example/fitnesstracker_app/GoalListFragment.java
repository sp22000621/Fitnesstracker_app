package com.example.fitnesstracker_app;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

public class GoalListFragment extends Fragment {

    private ListView goalListView;
    private GoalCursorAdapter goalAdapter;
    private GoalDatabaseHelper dbHelper;

    public GoalListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_goal_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        goalListView = view.findViewById(R.id.goal_list_view);
        dbHelper = new GoalDatabaseHelper(getContext());
        populateGoalList();
    }

    @Override
    public void onResume() {
        super.onResume();
        populateGoalList(); // Refresh the list when the fragment is resumed
    }

    private void populateGoalList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                GoalDatabaseHelper.COLUMN_ID,
                GoalDatabaseHelper.COLUMN_NAME,
                GoalDatabaseHelper.COLUMN_DEADLINE
        };

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sortOrder = prefs.getString(MainActivity.PREF_SORT_ORDER, "name"); // Default to sorting by name

        String orderBy;
        if ("name".equals(sortOrder)) {
            orderBy = GoalDatabaseHelper.COLUMN_NAME;
        } else if ("deadline".equals(sortOrder)) {
            orderBy = GoalDatabaseHelper.COLUMN_DEADLINE;
        } else {
            orderBy = GoalDatabaseHelper.COLUMN_NAME; // Default fallback
        }

        Cursor cursor = db.query(
                GoalDatabaseHelper.TABLE_GOALS,
                projection,
                null,
                null,
                null,
                null,
                orderBy // Set the sort order
        );

        if (goalAdapter == null) {
            goalAdapter = new GoalCursorAdapter(getContext(), cursor, 0);
            goalListView.setAdapter(goalAdapter);
        } else {
            goalAdapter.swapCursor(cursor);
        }

        db.close();
    }

    private static class GoalCursorAdapter extends CursorAdapter {

        private final LayoutInflater inflater;

        public GoalCursorAdapter(android.content.Context context, Cursor c, int flags) {
            super(context, c, flags);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View newView(android.content.Context context, Cursor cursor, ViewGroup parent) {
            return inflater.inflate(R.layout.item_goal, parent, false);
        }

        @Override
        public void bindView(View view, android.content.Context context, Cursor cursor) {
            TextView goalNameTextView = view.findViewById(R.id.goal_name);
            TextView goalDeadlineTextView = view.findViewById(R.id.goal_deadline);

            int nameColumnIndex = cursor.getColumnIndexOrThrow(GoalDatabaseHelper.COLUMN_NAME);
            int deadlineColumnIndex = cursor.getColumnIndexOrThrow(GoalDatabaseHelper.COLUMN_DEADLINE);

            String goalName = cursor.getString(nameColumnIndex);
            String goalDeadline = cursor.getString(deadlineColumnIndex);

            goalNameTextView.setText(goalName);
            goalDeadlineTextView.setText("Deadline: " + goalDeadline);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (goalAdapter != null) {
            goalAdapter.changeCursor(null); // Close the cursor
        }
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}