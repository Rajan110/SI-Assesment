package com.example.si_test.ui.add_team_activity;

import android.os.Bundle;
import android.view.inputmethod.EditorInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.si_test.databinding.ActivityAddTeamBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class AddTeamActivity extends AppCompatActivity {

    private ActivityAddTeamBinding binding;
    private AddTeamActivity_ViewModel addTeamActivity_viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddTeamBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addTeamActivity_viewModel = new ViewModelProvider(AddTeamActivity.this).get(AddTeamActivity_ViewModel.class);

        //listening to IME actions
        binding.textInputEditTextTeamName.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addTeam();
                return true;
            }
            return false;
        });

        binding.buttonAddTeam.setOnClickListener(view -> addTeam());

        // observer to close activity once team is added
        addTeamActivity_viewModel.dataUpdated.observe(AddTeamActivity.this, aBoolean -> {
            if (aBoolean) {
                finish();
            }
        });
    }

    private void addTeam() {
        String teamName = Objects.requireNonNull(binding.textInputEditTextTeamName.getText()).toString().trim();
        if (!teamName.isEmpty()) {
            addTeamActivity_viewModel.addTeamButtonClicked(teamName);
        } else {
            Snackbar.make(binding.buttonAddTeam, "Team Name Cannot be Empty", Snackbar.LENGTH_LONG).show();
        }
    }
}