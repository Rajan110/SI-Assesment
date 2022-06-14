package com.example.si_test.ui.add_team_activity;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.si_test.MyApplication;
import com.example.si_test.data.local_database.TeamEntity;
import com.example.si_test.data.local_database.TeamRepository;

public class AddTeamActivity_ViewModel extends ViewModel {

    private final TeamRepository mTeamRepository;
    public MutableLiveData<Boolean> dataUpdated = new MutableLiveData<>(false);

    public AddTeamActivity_ViewModel() {
        mTeamRepository = MyApplication.getTeamRepository();
    }

    public void insertOrUpdate(String teamName) {
        TeamEntity teamByName = mTeamRepository.getTeamByName(teamName);
        if (teamByName != null) {
            teamByName.setTeamName(teamName);
            mTeamRepository.update(teamByName);
        } else {
            mTeamRepository.insert(new TeamEntity(teamName));
        }
    }

    public void addTeamButtonClicked(String teamName) {
        insertOrUpdate(teamName);
        // once data is stored we set updated flag to true
        dataUpdated.postValue(true);
    }
}
