package com.example.si_test.data.local_database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {TeamEntity.class}, version = 1, exportSchema = false)
public abstract class TeamDatabase extends RoomDatabase {

    public abstract TeamDao TeamDao();

    //static instance of database
    private static volatile TeamDatabase DB_INSTANCE;

    //defining no. of threads for database
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static TeamDatabase getDatabase(final Context context) {
        if (DB_INSTANCE == null) {
            synchronized (TeamDatabase.class) {
                if (DB_INSTANCE == null) {
                    DB_INSTANCE = Room
                            .databaseBuilder(context.getApplicationContext(), TeamDatabase.class, "team_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return DB_INSTANCE;
    }

    /**
     * Override the onCreate method to populate the database.
     * For this sample, we clear & populate the database every time it is created.
     */
    private static final Callback sRoomDatabaseCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with some teams, just add them.
                TeamDao dao = DB_INSTANCE.TeamDao();
                dao.deleteAll();

                populateTeams(dao);
            });
        }
    };

    private static void populateTeams(TeamDao dao) {
        String[] teams = new String[]{"Mumbai Indians", "Sunrisers Hyderabad", "Royal Challengers Bangalore", "Rajasthan Royals",
                "Chennai Super Kings", "Kolkata Knight Riders", "Delhi Capitals", "Kings XI Punjab"};
        for (String teamName : teams) {
            dao.insert(new TeamEntity(teamName));
        }
    }
}
