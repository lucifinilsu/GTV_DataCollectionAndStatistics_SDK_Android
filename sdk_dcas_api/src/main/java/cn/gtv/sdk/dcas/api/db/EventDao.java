package cn.gtv.sdk.dcas.api.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Event item);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Event... item);

    @Delete
    int delete(Event... items);

    @Query("DELETE FROM event WHERE updateTime < :updateTime")
    int deleteOldest(long updateTime);

    @Query("DELETE FROM event")
    int deleteAll();

    @Update
    int update(Event... items);

    @Query("SELECT * FROM event WHERE id = :id")
    Event getById(String id);

    @Query("SELECT * FROM event ORDER BY updateTime asc")
    List<Event> getAll();

    @Query("SELECT * FROM event ORDER BY updateTime asc")
    LiveData<List<Event>> getAllLiveData();

    @Query("SELECT * FROM event ORDER BY updateTime asc limit :limit")
    List<Event> getOldest(int limit);

    @Query("SELECT * FROM event ORDER BY updateTime asc limit :limit")
    LiveData<List<Event>> getOldestLiveData(int limit);
}
