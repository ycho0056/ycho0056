package com.example.assignment.roomdatabase.viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.assignment.roomdatabase.entity.PainRecord;
import com.example.assignment.roomdatabase.repository.PainRecordRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PainRecordViewModel extends AndroidViewModel {
    private PainRecordRepository painRepository;
    private LiveData<List<PainRecord>> allPainRecord;

    public void  initail(Application application){
        painRepository = new PainRecordRepository(application);
    }
    public PainRecordViewModel (Application application){
        super(application);
         painRepository = new PainRecordRepository(application);
         allPainRecord = painRepository.getAllPainRecords();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<PainRecord> findByIDFuture(final int recordid){
        return painRepository.findByIDFuture(recordid);
    }
    public LiveData<List<PainRecord>> getAllPainRecord() {
        return allPainRecord;
    }
    public void insert(PainRecord painRecord) {
        painRepository.insert(painRecord);
    }

    public void deleteAll() {
        painRepository.deleteAll();
    }
    public void update(PainRecord painRecord) {
        painRepository.updatePainRecord(painRecord);
    }
}
