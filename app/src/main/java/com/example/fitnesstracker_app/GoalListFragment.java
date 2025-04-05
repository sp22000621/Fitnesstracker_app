package com.example.fitnesstracker_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class GoalListFragment extends Fragment {

    private ListView goalListView;
    private GoalAdapter goalAdapter;
    private GoalManager goalManager;

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
        goalManager = GoalManager.getInstance();
        List<Goal> goals = goalManager.getGoals();

        goalAdapter = new GoalAdapter(getActivity(), R.layout.item_goal, goals);
        goalListView.setAdapter(goalAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the list when the fragment is resumed (e.g., after adding a goal)
        if (goalManager != null && goalAdapter != null) {
            goalAdapter.clear();
            goalAdapter.addAll(goalManager.getGoals());
            goalAdapter.notifyDataSetChanged();
        }
    }

    private static class GoalAdapter extends ArrayAdapter<Goal> {

        private final LayoutInflater inflater;
        private final int layoutResource;

        public GoalAdapter(@NonNull android.content.Context context, int resource, @NonNull List<Goal> objects) {
            super(context, resource, objects);
            this.inflater = LayoutInflater.from(context);
            this.layoutResource = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = inflater.inflate(layoutResource, parent, false);
                holder = new ViewHolder();
                holder.goalNameTextView = convertView.findViewById(R.id.goal_name);
                holder.goalDeadlineTextView = convertView.findViewById(R.id.goal_deadline);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Goal goal = getItem(position);
            if (goal != null) {
                holder.goalNameTextView.setText(goal.getName());
                holder.goalDeadlineTextView.setText("Deadline: " + goal.getDeadline());
            }

            return convertView;
        }

        private static class ViewHolder {
            TextView goalNameTextView;
            TextView goalDeadlineTextView;
        }
    }
}