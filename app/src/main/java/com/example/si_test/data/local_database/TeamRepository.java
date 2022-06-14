package com.example.si_test.data.local_database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class TeamRepository {
    private final TeamDao mTeamDao;
    private final LiveData<List<TeamEntity>> mAllTeams;

    // Note that in order to unit test the TeamRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public TeamRepository(Application application) {
        TeamDatabase db = TeamDatabase.getDatabase(application);
        mTeamDao = db.TeamDao();
        mAllTeams = mTeamDao.getAllTeams();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<TeamEntity>> getAllTeams() {
        return mAllTeams;
    }

    public TeamEntity getTeamByName(String teamName) {
        Callable<TeamEntity> callable = () -> mTeamDao.getTeamByName(teamName);
        try {
            return TeamDatabase.databaseWriteExecutor.submit(callable).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.

    public void insert(TeamEntity team) {
        TeamDatabase.databaseWriteExecutor.execute(() -> mTeamDao.insert(team));
    }

    public void update(TeamEntity team) {
        TeamDatabase.databaseWriteExecutor.execute(() -> mTeamDao.update(team));
    }

    public void delete(TeamEntity team) {
        TeamDatabase.databaseWriteExecutor.execute(() -> mTeamDao.delete(team));
    }
}
