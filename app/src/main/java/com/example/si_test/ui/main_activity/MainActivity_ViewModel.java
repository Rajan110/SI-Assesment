package com.example.si_test.ui.main_activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.si_test.MyApplication;
import com.example.si_test.data.local_database.TeamEntity;
import com.example.si_test.data.local_database.TeamRepository;

import java.util.List;
import java.util.Objects;

public class MainActivity_ViewModel extends ViewModel {

    public final LiveData<List<TeamEntity>> mAllTeams;

    public MainActivity_ViewModel() {
        TeamRepository mTeamRepository = MyApplication.getTeamRepository();
        mAllTeams = mTeamRepository.getAllTeams();
    }

    public boolean checkTeamCountIsValid() {
        return (Objects.requireNonNull(mAllTeams.getValue()).size()) % 2 == 0;
    }
}
