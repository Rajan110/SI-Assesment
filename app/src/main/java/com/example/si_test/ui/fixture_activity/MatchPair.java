package com.example.si_test.ui.fixture_activity;

import com.example.si_test.data.local_database.TeamEntity;

import java.util.concurrent.ThreadLocalRandom;

public class MatchPair {
    public final TeamEntity team1;
    public final TeamEntity team2;
    private TeamEntity winner;
    private TeamEntity loser;

    public MatchPair(TeamEntity team1, TeamEntity team2) {
        this.team1 = team1;
        this.team2 = team2;
    }

    public void simulateMatch() {
        //boolean bool = new Random().nextBoolean();
        int bias_percentage = 50;
        boolean bool = ThreadLocalRandom.current().nextInt(100) <= bias_percentage;
        winner = bool ? team1 : team2;
        loser = !bool ? team1 : team2;
    }

    public TeamEntity getWinner() {
        if (winner == null) {
            simulateMatch();
        }
        return winner;
    }

    public TeamEntity getLoser() {
        if (loser == null) {
            simulateMatch();
        }
        return loser;
    }
}
