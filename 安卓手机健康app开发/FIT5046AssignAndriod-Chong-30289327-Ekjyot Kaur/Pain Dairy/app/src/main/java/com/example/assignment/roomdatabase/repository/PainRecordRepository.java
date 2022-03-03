package com.example.assignment.roomdatabase.repository;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import com.example.assignment.roomdatabase.dao.PainRecordDAO;
import com.example.assignment.roomdatabase.database.PainRecordDatabase;
import com.example.assignment.roomdatabase.entity.PainRecord;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class PainRecordRepository {
    private PainRecordDAO painRecordDao;
    private LiveData<List<PainRecord>> allPainRecords;
    //private PainRecord painRecord;

    public PainRecordRepository(Application application){
        PainRecordDatabase db = PainRecordDatabase.getInstance(application);
        painRecordDao = db.PainRecordDAO();
        allPainRecords = painRecordDao.getAll();
    }
    public LiveData<List<PainRecord>> getAllPainRecords(){
        return allPainRecords;
    }

    public  void insert(final PainRecord painRecord){
        PainRecordDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                painRecordDao.insert(painRecord);
            }
        });
    }

    public void deleteAll(){
        PainRecordDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                painRecordDao.deleteAll();
            }
        });
    }
    public void delete(final PainRecord painRecord){
        PainRecordDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                painRecordDao.delete(painRecord);
            }
        });
    }
    public void updatePainRecord(final PainRecord painRecord){
        PainRecordDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                painRecordDao.updatePainRecord(painRecord);
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<PainRecord> findByIDFuture(final int recordid) {
        return CompletableFuture.supplyAsync(new Supplier<PainRecord>() {
            @Override
            public PainRecord get() {
                return painRecordDao.findByID(recordid);
            }
        }, PainRecordDatabase.databaseWriteExecutor);
    }
}