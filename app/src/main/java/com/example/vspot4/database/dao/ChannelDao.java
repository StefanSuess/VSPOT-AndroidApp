package com.example.vspot4.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.vspot4.database.entity.Channel;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ChannelDao {

    // what to do on  if db is inconsistent
    @Insert(onConflict = REPLACE)
    void save(Channel channel);

    // load channel
    @Query("SELECT * FROM Channel LIMIT 1")
    LiveData<Channel> load();

    // check if  channel is recent enough
    @Query("SELECT * FROM Channel LIMIT 1")
    Channel hasChannel();

    @Query("DELETE FROM Channel WHERE name=:name")
    void delete(String name);

}
