package com.example.vspot4.viewmodels;

import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;

import com.example.vspot4.activities.MainActivity;
import com.example.vspot4.api.ChannelWebservice;
import com.example.vspot4.database.DB;
import com.example.vspot4.database.SharedPref;
import com.example.vspot4.database.dao.ChannelDao;
import com.example.vspot4.database.entity.Channel;
import com.example.vspot4.respositories.ChannelRepository;
import com.google.gson.Gson;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ChannelViewModel extends ViewModel {

    private static final String TAG = "ChannelViewModel";
    private static final Handler handler = new Handler();
    Runnable runnable;
    DB db = Room.databaseBuilder(MainActivity.context, DB.class, "VSPOT.db").build();
    private LiveData<Channel> channelLiveData;
    private ChannelRepository channelRepository;
    private ChannelDao channelDao = db.channelDao();

    public ChannelViewModel() {

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .baseUrl("https://vspot.eu")
                .build();
        ChannelWebservice channelWebservice = retrofit.create(ChannelWebservice.class);


        Executor executor = Executors.newSingleThreadExecutor();

        channelRepository = new ChannelRepository(channelWebservice, channelDao, executor);

    }

    public void init() {
        SharedPref sharedPref = SharedPref.getInstance(MainActivity.context);
        String API_KEY = sharedPref.getData("API_KEY");
        if (this.channelLiveData == null) {
            channelLiveData = channelRepository.getChannel(API_KEY);
        } else {
            //TODO
        }
    }

    public LiveData<Channel> getChannel() {
        return this.channelLiveData;
    }

    private void updateContinously(String API_KEY) {
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "HEARTBEAT");
            }
        }, 5000);
    }

}
