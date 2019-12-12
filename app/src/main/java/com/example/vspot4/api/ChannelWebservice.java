package com.example.vspot4.api;

import com.example.vspot4.database.entity.Channel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ChannelWebservice {

    @GET()
    Call<Channel> getChannel(@Url String api_key);
}
