package com.elli0tt.rpg_life.domain.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "skills_table")
public class Skill {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull private String name;
    private long timeSpentMillis;

    public Skill(@NonNull String name){
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public long getTimeSpentMillis() {
        return timeSpentMillis;
    }

    public void setTimeSpentMillis(long timeSpentMillis) {
        this.timeSpentMillis = timeSpentMillis;
    }
}
