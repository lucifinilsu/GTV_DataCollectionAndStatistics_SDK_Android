package cn.gtv.sdk.dcas.api.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Event {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id;
    @NonNull
    private long updateTime;
    private String data;

    public Event(String data) {
        this.data = data;
        updateTime = new Date().getTime();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", time=" + updateTime +
                ", data='" + data + '\'' +
                '}';
    }
}
