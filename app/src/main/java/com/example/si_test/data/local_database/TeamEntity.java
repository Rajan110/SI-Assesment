package com.example.si_test.data.local_database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "team_table")
public class TeamEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "team_id")
    public int teamId;

    @ColumnInfo(name = "team_name")
    public String teamName;

    public TeamEntity(String teamName) {
        this.teamName = teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
