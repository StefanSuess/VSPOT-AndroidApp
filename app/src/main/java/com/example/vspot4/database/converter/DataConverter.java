package com.example.vspot4.database.converter;

import androidx.room.TypeConverter;

import com.example.vspot4.database.entity.Screen;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class DataConverter {

    @TypeConverter
    public String toJson(List<Screen> datalist) {
        if (datalist == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Screen>>() {
        }.getType();
        String json = gson.toJson(datalist, type);
        return json;
    }

    @TypeConverter
    public List<Screen> toDataList(String list) {
        if (list == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Screen>>() {
        }.getType();
        List<Screen> datalist = gson.fromJson(list, type);
        return datalist;
    }
}