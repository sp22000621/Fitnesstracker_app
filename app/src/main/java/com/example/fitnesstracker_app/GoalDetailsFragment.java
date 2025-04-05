package com.example.fitnesstracker_app;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class GoalDetailsFragment extends Fragment {

    private EditText goalNameInput;
    private TextView deadlineText;
    private Button pickDateBtn;
    private Button saveBtn;
    private String selectedDate = "";
    private GoalDatabaseHelper dbHelper;

    public GoalDetailsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_goal_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        goalNameInput = view.findViewById(R.id.edit_goal_name);
        deadlineText = view.findViewById(R.id.text_deadline);
        pickDateBtn = view.findViewById(R.id.btn_pick_date);
        saveBtn = view.findViewById(R.id.btn_save_goal);

        dbHelper = new GoalDatabaseHelper(getContext());

        pickDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = goalNameInput.getText().toString().trim();
                if (name.isEmpty() || selectedDate.isEmpty()) {
                    Toast.makeText(getContext(), "Enter name and date", Toast.LENGTH_SHORT).show();
                    return;
                }

                saveGoalToDatabase(name, selectedDate);

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new GoalListFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void saveGoalToDatabase(String name, String deadline) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if (db != null) {
            ContentValues values = new ContentValues();
            values.put(GoalDatabaseHelper.COLUMN_NAME, name);
            values.put(GoalDatabaseHelper.COLUMN_DEADLINE, deadline);

            long newRowId = db.insert(GoalDatabaseHelper.TABLE_GOALS, null, values);

            if (newRowId != -1) {
                Toast.makeText(getContext(), "Goal saved to database", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Error saving goal", Toast.LENGTH_SHORT).show();
            }

            db.close();
        } else {
            Toast.makeText(getContext(), "Could not open database", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        deadlineText.setText("Deadline: " + selectedDate);
                    }
                },
                y, m, d
        );
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}