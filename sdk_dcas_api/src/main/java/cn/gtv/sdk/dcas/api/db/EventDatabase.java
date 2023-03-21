package cn.gtv.sdk.dcas.api.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(
        version = 1,
        entities = {Event.class}
)
public abstract class EventDatabase extends RoomDatabase {

    public abstract EventDao eventDao();
}
