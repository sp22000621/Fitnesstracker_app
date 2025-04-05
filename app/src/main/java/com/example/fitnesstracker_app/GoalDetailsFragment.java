package com.example.fitnesstracker_app;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class GoalDetailsFragment extends Fragment {

    private EditText goalNameInput;
    private TextView deadlineText;
    private Button pickDateBtn;
    private Button saveBtn;
    private String selectedDate = "";

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

                GoalManager.getInstance().addGoal(new Goal(name, selectedDate));
                Toast.makeText(getContext(), "Goal saved", Toast.LENGTH_SHORT).show();

                // Go back to the list fragment
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new GoalListFragment())
                        .addToBackStack(null) // Optional: Add to back stack for navigation
                        .commit();
            }
        });
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
}