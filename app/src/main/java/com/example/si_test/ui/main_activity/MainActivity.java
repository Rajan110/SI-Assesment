package com.example.si_test.ui.main_activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.si_test.data.local_database.TeamEntity;
import com.example.si_test.databinding.ActivityMainBinding;
import com.example.si_test.ui.add_team_activity.AddTeamActivity;
import com.example.si_test.ui.fixture_activity.FixtureActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainActivity_ViewModel mainActivity_viewModel;

    private TeamListAdapter listTeamAdapter;

    private List<TeamEntity> teamsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mainActivity_viewModel = new ViewModelProvider(MainActivity.this).get(MainActivity_ViewModel.class);

        // setting-up recyclerView
        listTeamAdapter = new TeamListAdapter(TeamListAdapter.TeamListDiffUtil());
        binding.recyclerViewListTeams.setAdapter(listTeamAdapter);

        loadTeams();

        initListeners();
    }

    private void loadTeams() {
        mainActivity_viewModel.mAllTeams.observe(MainActivity.this, teamEntities -> {
            // Update the copy of the teams in the adapter.
            this.teamsList = teamEntities;
            listTeamAdapter.submitList(teamsList);
        });
    }

    private void initListeners() {
        binding.floatingActionButtonAddTeam.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddTeamActivity.class);
            startActivity(intent);
        });

        binding.floatingActionButtonStartIPL.setOnClickListener(view -> {
            if (mainActivity_viewModel.checkTeamCountIsValid()) {
                Intent intent = new Intent(MainActivity.this, FixtureActivity.class);
                startActivity(intent);
            } else {
                Snackbar.make(view, "Invalid team count\nPlease add 1 more team to proceed.", Snackbar.LENGTH_LONG).show();
            }
        });
    }
}