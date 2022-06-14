package com.example.si_test;

import android.app.Application;
import android.os.AsyncTask;

import com.example.si_test.data.local_database.TeamRepository;

public class MyApplication extends Application {

    private static TeamRepository mTeamRepository;

    public static TeamRepository getTeamRepository() {
        return mTeamRepository;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (mTeamRepository == null) {
            AsyncTask.execute(() -> mTeamRepository = new TeamRepository(this));
        }
    }
}
