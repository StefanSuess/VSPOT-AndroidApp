package com.example.vspot4.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.vspot4.database.converter.DataConverter;
import com.example.vspot4.database.converter.DateConverter;
import com.example.vspot4.database.dao.ChannelDao;
import com.example.vspot4.database.entity.Channel;

@Database(entities = Channel.class, version = 1)
@TypeConverters({DateConverter.class, DataConverter.class})

public abstract class DB extends RoomDatabase {
    // --- SINGLETON ---
    private static volatile DB INSTANCE;

    // --- DAO ---
    public abstract ChannelDao channelDao();
}
