package com.example.vspot4.api;

import com.example.vspot4.database.entity.Channel;
import com.example.vspot4.database.entity.ChannelTimestamp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ChannelWebservice {

    @GET()
    Call<Channel> getChannel(@Url String api_key);

    @GET()
    Call<ChannelTimestamp> getTimestamp(@Url String api_key, @Query("timestamp") String timestamp);
}
