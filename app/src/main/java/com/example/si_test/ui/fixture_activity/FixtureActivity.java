package com.example.si_test.ui.fixture_activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.si_test.R;
import com.example.si_test.data.local_database.TeamEntity;
import com.example.si_test.databinding.ActivityFixtureBinding;
import com.example.si_test.ui.LeaderboardActivity;

import java.util.ArrayList;
import java.util.Objects;

public class FixtureActivity extends AppCompatActivity {

    private ActivityFixtureBinding binding;
    private FixtureActivity_ViewModel fixtureActivity_viewModel;

    private String current_teams = "";
    private String qualified_teams = "";

    private String body = qualified_teams + "\n\n" + current_teams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFixtureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fixtureActivity_viewModel = new ViewModelProvider(FixtureActivity.this).get(FixtureActivity_ViewModel.class);

        setupObservers();

        binding.buttonSimulate.setOnClickListener(view -> {
            if (Boolean.FALSE.equals(fixtureActivity_viewModel.allMatchesFinished.getValue())) {
                fixtureActivity_viewModel.simulateOnClick();
            }
        });
    }

    @SuppressWarnings("StringConcatenationInLoop")
    @SuppressLint("DefaultLocale")
    private void setupObservers() {
        fixtureActivity_viewModel.qualified_match_pairs.observe(FixtureActivity.this, matchPairs -> {
            qualified_teams = matchPairs.size() > 0 ? String.format("Qualified Teams : %d\n\n", matchPairs.size() * 2) : "";
            for (MatchPair pair : matchPairs) {
                qualified_teams += String.format("%s\nvs\n%s\n\n", pair.team1.teamName, pair.team2.teamName);
            }
            body = qualified_teams + "\n\n" + current_teams;
            binding.textView.setText(String.format("%s\nbyes: %d", body, matchPairs.size() * 2));
        });

        fixtureActivity_viewModel.current_match_pairs.observe(FixtureActivity.this, matchPairs -> {
            current_teams = matchPairs.size() > 0 ? String.format("\nTeams In Current Round : %d\n\n", matchPairs.size() * 2) : "";
            for (MatchPair pair : matchPairs) {
                //current_teams += String.format("%s - %s\nWinner : %s\nLoser : %s\n\n", pair.team1.teamName, pair.team2.teamName, pair.getWinner().teamName, pair.getLoser().teamName);
                current_teams += String.format("%s\nvs\n%s\n\n", pair.team1.teamName, pair.team2.teamName);
            }
            body = qualified_teams + "\n\n" + current_teams;

            binding.textView.setText(body);

            if (Objects.requireNonNull(fixtureActivity_viewModel.qualified_match_pairs.getValue()).size() == 0 && matchPairs.size() == 1) {
                binding.buttonSimulate.setText(R.string.simulate_and_end);
            }
        });

        binding.textView.setText(body);

        fixtureActivity_viewModel.allMatchesFinished.observe(FixtureActivity.this, aBoolean -> {
            if (aBoolean) {
                ArrayList<TeamEntity> topTeams = fixtureActivity_viewModel.getTopTeams();
                ArrayList<String> topTeamNames = new ArrayList<>();
                for (int i = 0, topTeamsSize = topTeams.size(); i < topTeamsSize; i++) {
                    topTeamNames.add(topTeams.get(i).teamName);
                }
                Intent intent = new Intent(FixtureActivity.this, LeaderboardActivity.class);
                intent.putStringArrayListExtra(LeaderboardActivity.EXTRA_TOP_TEAMS, topTeamNames);
                startActivity(intent);
                // closing current activity
                finish();
            }
        });
    }
}