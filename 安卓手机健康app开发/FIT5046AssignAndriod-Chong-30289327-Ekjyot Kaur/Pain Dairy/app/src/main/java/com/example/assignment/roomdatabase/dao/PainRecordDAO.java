package com.example.assignment.roomdatabase.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.assignment.roomdatabase.entity.PainRecord;

import java.util.List;

@Dao
public interface PainRecordDAO {

    @Query("SELECT * FROM PainRecord")
    LiveData<List<PainRecord>> getAll();

    @Query("SELECT * FROM PainRecord WHERE id = :recordid LIMIT 1")
    PainRecord findByID(int recordid);

    @Insert
    void insert(PainRecord painRecord);

    @Delete
    void delete(PainRecord painRecord);

    @Update
    void updatePainRecord(PainRecord painRecord);

    @Query("DELETE FROM PainRecord")
    void deleteAll();
}
