package com.example.si_test.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.si_test.databinding.ActivityLeaderboardBinding;
import com.example.si_test.ui.fixture_activity.FixtureActivity;

import java.util.ArrayList;

public class LeaderboardActivity extends AppCompatActivity {

    public static final String EXTRA_TOP_TEAMS = "top_teams";
    private ActivityLeaderboardBinding binding;
    private ArrayList<String> topTeams;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLeaderboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle extras = getIntent().getExtras();
        if (extras != null && !extras.isEmpty()) {
            topTeams = extras.getStringArrayList(EXTRA_TOP_TEAMS);
        }

        displayTopTeams();

        binding.buttonRestart.setOnClickListener(view -> {
            Intent intent = new Intent(LeaderboardActivity.this, FixtureActivity.class);
            startActivity(intent);
            // closing current activity
            finish();
        });
    }

    @SuppressLint("DefaultLocale")
    private void displayTopTeams() {
        StringBuilder stringBuilder = new StringBuilder().append("Top 3 Teams\n");

        for (int i = 0; i < topTeams.size(); i++) {
            String teamName = topTeams.get(i);
            stringBuilder.append(String.format("\n%d. %s\n", i + 1, teamName));
        }

        binding.textViewLeaderBoard.setText(stringBuilder);
    }
}