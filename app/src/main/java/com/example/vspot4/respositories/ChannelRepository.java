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
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    private static int REFRESH_TIMEOUT_IN_MINUTES = 1;
    private final Snackbar snackbar = makeSnackBar(PresentationActivity.activity);
    private final ChannelWebservice channelWebservice;
    private final ChannelDao channelDao;
    private final Executor executor;
    Runnable runnable;

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
                //if a recent Channel exists
                boolean channelExists = channelDao.hasChannel() != null;
                //Log.e(TAG, "CHANNEL EXISTS = " + channelExists);
                channelWebservice.getChannel(API_KEY).enqueue(new Callback<Channel>() {
                    @Override
                    public void onResponse(Call<Channel> call, final Response<Channel> response) {
                        executor.execute(() -> {
                            //Log.e(TAG, "Data refreshed from network");
                            Channel channel = response.body();
                            channel.setLastRefresh(new Date());

                            if (!channelExists) {
                                channelDao.save(channel);
                            } else if (!channel.getName().equals(channelDao.hasChannel().getName())) {
                                Log.e(TAG, "SAVED NEW CHANNEL");
                                channelDao.delete(channelDao.hasChannel().getName());
                                channelDao.save(channel);
                            } else {
                                Log.e(TAG, "CHANNEL UP TO DATE");
                            }

                            // set fail timer to zero
                            PresentationActivity.updateFail = 0;

                            // make the notification go away
                            if (snackbar != null) {
                                snackbar.dismiss();
                            }
                            // recurring update
                            updateContinously(API_KEY);
                        });
                    }

                    @Override
                    public void onFailure(Call<Channel> call, Throwable t) {
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
                });
            }
        });
    }

    private Date getMaxRefreshTime(Date currentDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.MINUTE, -REFRESH_TIMEOUT_IN_MINUTES);
        return cal.getTime();
    }

    private void updateContinously(String API_KEY) {
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                refreshChannel(API_KEY);
            }
        }, 5000);
    }
}