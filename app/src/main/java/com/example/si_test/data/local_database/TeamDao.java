package com.example.si_test.data.local_database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TeamDao {
    @Insert()
    void insert(TeamEntity team);

    @Update
    void update(TeamEntity team);

    @Delete
    void delete(TeamEntity team);

    @Query("DELETE FROM team_table")
    void deleteAll();

    @Query("SELECT * FROM team_table")
    LiveData<List<TeamEntity>> getAllTeams();

    @Query("SELECT * FROM team_table WHERE team_name LIKE :teamName LIMIT 1")
    TeamEntity getTeamByName(String teamName);
}
