package cn.gtv.sdk.dcas.api;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.util.List;

import cn.gtv.sdk.dcas.api.db.Event;
import cn.gtv.sdk.dcas.api.db.EventDatabase;

public class EventsManager {

    private final EventDatabase database;

    public static final int MAX_COUNT = Integer.MAX_VALUE;

    private static  EventsManager ourInstance;

    private EventsManager(Context context) {
        database = Room.databaseBuilder(context,
                        EventDatabase.class, "dj_dcas_event.db")
                .enableMultiInstanceInvalidation()
                .allowMainThreadQueries()
                .build();
    }

    public static EventsManager getInstance(Context context) {
        if(ourInstance==null){
            ourInstance = new EventsManager(context);
        }
        return ourInstance;
    }

    public void insert(Event item) {
        database.eventDao().insert(item);
    }

    public void insert(String data) {
        Event item=new Event(data);
        database.eventDao().insert(item);
    }

    public void insertAll(Event... items) {
        database.eventDao().insertAll(items);
    }

    public List<Event> getAll(){
       return database.eventDao().getAll();
    }

    public LiveData<List<Event>> getAllLiveData(){
        return database.eventDao().getAllLiveData();
    }

    public List<Event> getOldest(int limit){
       return database.eventDao().getOldest(limit);
    }

    public LiveData<List<Event>> getOldestLiveData(int limit){
        return database.eventDao().getOldestLiveData(limit);
    }

    public int deleteOldest(long updateTime){
        return database.eventDao().deleteOldest(updateTime);
    }

    public int deleteAll(){
        return database.eventDao().deleteAll();
    }
}
