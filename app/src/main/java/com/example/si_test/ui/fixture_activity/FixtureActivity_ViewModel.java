package com.example.si_test.ui.fixture_activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.si_test.MyApplication;
import com.example.si_test.data.local_database.TeamEntity;
import com.example.si_test.data.local_database.TeamRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FixtureActivity_ViewModel extends ViewModel {

    private final LiveData<List<TeamEntity>> mAllTeams;

    public final MutableLiveData<ArrayList<MatchPair>> qualified_match_pairs = new MutableLiveData<>(new ArrayList<>());
    public final MutableLiveData<ArrayList<MatchPair>> current_match_pairs = new MutableLiveData<>(new ArrayList<>());

    public MutableLiveData<Boolean> allMatchesFinished = new MutableLiveData<>(false);

    //array list to store top 3 teams
    private final ArrayList<TeamEntity> topTeams = new ArrayList<>(3);

    public FixtureActivity_ViewModel() {
        TeamRepository mTeamRepository = MyApplication.getTeamRepository();

        mAllTeams = mTeamRepository.getAllTeams();

        // comment/remove this line to make sequential pairs
        Collections.shuffle(Objects.requireNonNull(mAllTeams.getValue()));
        generateFixtures();
    }

    public void generateFixtures() {
        List<TeamEntity> teamEntities = mAllTeams.getValue();
        int byes = calculateByes(Objects.requireNonNull(teamEntities).size());

        // add/qualify top seeded teams to next round
        ArrayList<MatchPair> qmp = new ArrayList<>();
        for (int i = 0; i < byes; i += 2) {
            int j = i + 1;
            qmp.add(new MatchPair(teamEntities.get(i), teamEntities.get(j)));
        }
        qualified_match_pairs.postValue(qmp);

        // generate current round fixtures
        ArrayList<MatchPair> cmp = new ArrayList<>();
        for (int i = byes; i < teamEntities.size(); i += 2) {
            int j = i + 1;
            cmp.add(new MatchPair(teamEntities.get(i), teamEntities.get(j)));
        }
        current_match_pairs.postValue(cmp);
    }

    private int calculateByes(int teamSize) {
        int bye = 0;
        for (int i = 0; i < 100; i++) {
            if (teamSize <= Math.pow(2, i)) {
                bye = (int) (Math.pow(2, i) - teamSize);
                break;
            }
        }
        return bye;
    }

    public void simulateOnClick() {
        // if more than 1 match is available we deduce till final
        if (Objects.requireNonNull(qualified_match_pairs.getValue()).size() == 0
                && Objects.requireNonNull(current_match_pairs.getValue()).size() == 2) {
            // gets losers of semi-finals and then finding third place
            ArrayList<TeamEntity> q_match_pairs_lose = new ArrayList<>();
            for (MatchPair pair : Objects.requireNonNull(current_match_pairs.getValue())) {
                q_match_pairs_lose.add(pair.getLoser());
            }
            thirdPlace(q_match_pairs_lose);
        } else if (Objects.requireNonNull(qualified_match_pairs.getValue()).size() == 0
                && Objects.requireNonNull(current_match_pairs.getValue()).size() == 1) {
            // here current_match_pairs has only one match-up/fixture
            // so we find winner and loser and add to leaderboard variables
            topTeams.add(current_match_pairs.getValue().get(0).getLoser());
            topTeams.add(current_match_pairs.getValue().get(0).getWinner());
            allMatchesFinished.postValue(true);
        }

        if (Objects.requireNonNull(current_match_pairs.getValue()).size() > 1) {
            generateFixtures();

            // gets winner of current matches and then added to qualifying/next round teams
            ArrayList<TeamEntity> q_match_pairs = new ArrayList<>();
            for (MatchPair pair : Objects.requireNonNull(current_match_pairs.getValue())) {
                q_match_pairs.add(pair.getWinner());
            }
            updateMatchPairs(q_match_pairs);
        }
    }

    private void updateMatchPairs(ArrayList<TeamEntity> teamEntities) {
        if (teamEntities == null || teamEntities.isEmpty()) {
            return;
        }

        ArrayList<MatchPair> aqt;
        //for (int i = 0; i < teamEntities.size(); i += 2) {
        if (!Objects.requireNonNull(qualified_match_pairs.getValue()).isEmpty()) {
            aqt = qualified_match_pairs.getValue();
        } else {
            aqt = new ArrayList<>();
        }

        for (int i = 0; i < teamEntities.size(); i += 2) {
            int j = i + 1;
            if (j < teamEntities.size())
                aqt.add(new MatchPair(teamEntities.get(i), teamEntities.get(j)));
        }
        current_match_pairs.postValue(aqt);
        qualified_match_pairs.postValue(new ArrayList<>());
    }

    private void thirdPlace(ArrayList<TeamEntity> teamEntities) {
        if (teamEntities == null || teamEntities.isEmpty()) {
            return;
        }

        if (teamEntities.size() > 1) {
            MatchPair matchPair = new MatchPair(teamEntities.get(0), teamEntities.get(1));
            topTeams.add(matchPair.getWinner());
        }
    }

    public ArrayList<TeamEntity> getTopTeams() {
        Collections.reverse(topTeams);
        return topTeams;
    }
}
