package com.example.assignment.roomdatabase.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class PainRecord {
    @PrimaryKey (autoGenerate = true)
    public int id;

    @ColumnInfo(name = "email")
    @NonNull
    public String email;

    @ColumnInfo(name="pain_level")
    @NonNull
    public int painLevel;

    @ColumnInfo(name="pain_location")
    @NonNull
    public String painLocation;

    @ColumnInfo(name="mood_level")
    @NonNull
    public String moodLevel;

    @ColumnInfo(name="step_taken")
    @NonNull
    public int stepTaken;

    @ColumnInfo(name="stepGoal")
    @NonNull
    public int stepGoal;

    @ColumnInfo(name="date")
    @NonNull
    public String date;

    @ColumnInfo(name="temperature")
    @NonNull
    public String temperature;

    @ColumnInfo(name="humidity")
    @NonNull
    public String humidity;

    @ColumnInfo(name="pressure")
    @NonNull
    public String pressure;

    public PainRecord(@NonNull String email, int painLevel, @NonNull String painLocation, @NonNull String moodLevel, @NonNull int stepTaken,@NonNull int stepGoal,@NonNull String date, @NonNull String temperature, @NonNull String humidity, @NonNull String pressure) {
        this.email = email;
        this.painLevel = painLevel;
        this.painLocation = painLocation;
        this.moodLevel = moodLevel;
        this.stepTaken = stepTaken;
        this.stepGoal = stepGoal;
        this.date = date;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
