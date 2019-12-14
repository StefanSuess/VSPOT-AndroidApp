package com.example.vspot4.respositories;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;

import com.example.vspot4.R;
import com.example.vspot4.activities.PresentationActivity;
import com.example.vspot4.api.ChannelWebservice;
import com.example.vspot4.database.dao.ChannelDao;
import com.example.vspot4.database.entity.Channel;
import com.example.vspot4.database.entity.ChannelTimestamp;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;

import de.mateware.snacky.Snacky;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChannelRepository {

    private static final String TAG = "ChannelRepository";
    private static final Handler handler = new Handler();
    //private final Snackbar snackbar = MakeSnackBar();
    private final Snackbar snackbar = makeSnackBar(PresentationActivity.activity);
    private final ChannelWebservice channelWebservice;
    private final ChannelDao channelDao;
    private final Executor executor;
    Runnable runnable;

    private static Date timestamp = new Date(00000000); // initalize with the oldest possible value

    public ChannelRepository(ChannelWebservice channelWebservice, ChannelDao channelDao, Executor executor) {
        this.channelWebservice = channelWebservice;
        this.channelDao = channelDao;
        this.executor = executor;
    }

    // --- Show Snackbar ---
    private static Snackbar makeSnackBar(Activity activity) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        return Snacky.builder()
                .setActivity(activity)
                .setText("Stand vom " + dateFormat.format(date) + ", " + timeFormat.format(date) + " Uhr")
                .centerText()
                .setDuration(Snacky.LENGTH_INDEFINITE)
                .info();
        //.addCallback();
    }

    public LiveData<Channel> getChannel(String name) {
        refreshChannel(name);
        return channelDao.load();
    }


    private void refreshChannel(final String API_KEY) {
        executor.execute(() -> {
            {
                // check if a Channel exists
                boolean channelExists = channelDao.hasChannel() != null;
                // get the latest channel
                getTimestamp(API_KEY);

                // check if the online timestamp is more recent
                if (timestamp != null && channelExists && !timestamp.before(channelDao.hasChannel().getLastRefresh())) {
                    getLatestChannel(API_KEY); // get the latest chanell + initalize update every n seconds
                } else if (!channelExists) {
                    getLatestChannel(API_KEY);
                } else {
                    Log.e(TAG, "Most recent timestamp");
                    updateContinously(API_KEY);  // initialize update every n seconds
                }
            }
        });
    }


    private void updateContinously(String API_KEY) {
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                refreshChannel(API_KEY);
            }
        }, 5000);
    }


    private void handleFailure(String API_KEY, Throwable t) {
        // show what went wrong
        Log.e(TAG, "onFailure called: " + t.getMessage());
        // keep trying to update
        updateContinously(API_KEY);
        // count failure variable up for every failure
        if (PresentationActivity.updateFail == 3) {
            snackbar.getView().findViewById(R.id.snackbar_action).setBackground(null);
            snackbar.setAction("Verstecken", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }
        PresentationActivity.updateFail++;
        Log.e(TAG, "upadteFail = " + PresentationActivity.updateFail);
    }


    private void getTimestamp(String API_KEY) {
        // Check the timestamp
        channelWebservice.getTimestamp(API_KEY, "null").enqueue(new Callback<ChannelTimestamp>() {
            @Override
            public void onResponse(Call<ChannelTimestamp> call, Response<ChannelTimestamp> response) {
                executor.execute(() -> {
                    ChannelTimestamp channelTimestamp = response.body();
                    // +3 zeros because the timestamp from the json has only 10 digits and required are 13
                    ChannelRepository.timestamp.setTime(Long.parseLong(channelTimestamp.getLastupdate() + "000"));
                    // set fail timer to zero
                    PresentationActivity.updateFail = 0;
                    // TODO Fix too many updates
                    // make the notification go away
                    if (snackbar != null) {
                        snackbar.dismiss();
                    }
                });
            }

            @Override
            public void onFailure(Call<ChannelTimestamp> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                handleFailure(API_KEY, t);
            }
        });
    }

    private void getLatestChannel(String API_KEY) {
        // get the latest channel
        channelWebservice.getChannel(API_KEY).enqueue(new Callback<Channel>() {
            @Override
            public void onResponse(Call<Channel> call, final Response<Channel> response) {
                executor.execute(() -> {
                    //Log.e(TAG, "Data refreshed from network");
                    Channel channel = response.body();
                    channel.setLastRefresh(new Date());


                    Log.e(TAG, "SAVED NEW CHANNEL");
                    try {
                        channelDao.delete(channelDao.hasChannel().getName());
                    } catch (Exception e) {
                        // do nothing
                    }
                    channelDao.save(channel);

                    // recurring update
                    updateContinously(API_KEY);
                });
            }

            @Override
            public void onFailure(Call<Channel> call, Throwable t) {
                Log.e(TAG, "onFailure Called: " + t.getMessage());
                //handleFailure(API_KEY, t);
            }
        });
    }

}