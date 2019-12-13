package com.example.vspot4.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.vspot4.R;
import com.example.vspot4.adapters.PresentationAdapter;
import com.example.vspot4.database.SharedPref;
import com.example.vspot4.database.entity.Channel;
import com.example.vspot4.viewmodels.ChannelViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.shreyaspatil.MaterialDialog.MaterialDialog;


public class PresentationActivity extends FragmentActivity {

    private static final String TAG = "PresentationActivity";
    private static final Handler handler = new Handler();
    // --- update Failure tracker ---
    public static int updateFail = 0;
    static public Activity activity;
    static public View view;
    static int i = 0;
    // --- Channel specific variables ---
    static int display_time;
    static int refresh_time;
    static String presentation_name;
    static String description;
    private static Boolean isFirstUpdate = true;
    private static Runnable swipeRunnable;
    // --- Viewpager relatet ---
    private ChannelViewModel channelViewModel;
    private ViewPager2 mPager;
    private RecyclerView.Adapter pagerAdapter;
    TabLayout tabLayout;

    public static void setDisplay_time(String display_time) {
        if (display_time != null && Integer.parseInt(display_time) > 0) {
            PresentationActivity.display_time = Integer.parseInt(display_time);
        } else {
            Log.e(TAG, "alternative display time set");
            PresentationActivity.display_time = 5000;
        }
    }

    public static void setRefresh_time(String refresh_time) {
        if (refresh_time != null && Integer.parseInt(refresh_time) > 0) {
            PresentationActivity.refresh_time = Integer.parseInt(refresh_time) * 1000;
        } else {
            PresentationActivity.refresh_time = 5 * 1000;
        }
    }

    public static String getDescription() {
        return description;
    }

    public static void setDescription(String description) {
        if (description != null && !description.isEmpty()) {
            PresentationActivity.description = description;
        } else {
            PresentationActivity.description = "Keine Beschreibung vorhanden";
        }
    }

    // --- SETTER ---

    public static String getPresentation_name() {
        return presentation_name;
    }

    public static void setPresentation_name(String presentation_name) {
        if (presentation_name != null && !presentation_name.isEmpty()) {
            PresentationActivity.presentation_name = presentation_name;
        } else {
            PresentationActivity.presentation_name = "Kein Name angegeben";
        }
    }

    // double tap exit button to exit
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        activity = this;
        view = getWindow().getDecorView().getRootView();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);
        configureViewModel();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = findViewById(R.id.pager);
        pagerAdapter = new PresentationAdapter(this, getSupportFragmentManager(), getLifecycle());
        mPager.setAdapter(pagerAdapter);

        // Connect Tablayout with viewpager
        tabLayout = findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, mPager, true, (tab, position) -> {
            // TODO fill the tablayout
        }).attach();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    private void configureViewModel() {
        SharedPref sharedPref = SharedPref.getInstance(MainActivity.context);
        String API_KEY = sharedPref.getData(MainActivity.API_KEY);

        Log.e(TAG, "API KEY: " + API_KEY);

        channelViewModel = ViewModelProviders.of(this).get(ChannelViewModel.class);
        channelViewModel.init();
        channelViewModel.getChannel().observe(this, channel -> updateUI(channel));
    }


    // --- GETTER ---

    // GET THINGS OUT OF THE VIEWMODEL HERE!
    private void updateUI(@Nullable Channel channel) {
        if (channel != null) {
            Log.e(TAG, "UI UPDATED");
            // Set variables for channel
            Log.e(TAG, channel.getName());

            setPresentation_name(channel.getName());
            setDescription(channel.getDescription());

            setRefresh_time(channel.getRefresh_time());
            setDisplay_time(channel.getDisplay_time());

            //pagerAdapter.notifyItemRangeRemoved(0,-1);
            pagerAdapter.notifyDataSetChanged();

            // set the pager counter to invisible if less then 2 screens are present
            if (channel.getScreens().size() < 2) {
                tabLayout.setVisibility(View.GONE);
            } else {
                tabLayout.setVisibility(View.VISIBLE);
            }
            if (channel.getScreens().size() > 15) {
                tabLayout.setVisibility(View.GONE);
            } else {
                tabLayout.setVisibility(View.VISIBLE);
            }

            if (isFirstUpdate) {
                isFirstUpdate = false;
                // turn auto swipe on
                setAutoSwipe();
                showDialog();
            }
        }
    }

    private String getApiKey() {
        SharedPref sharedPref = new SharedPref(MainActivity.context);
        return sharedPref.getData(MainActivity.API_KEY);
    }


    // --- Dialog Window to delete or use presentation ---

    private void showDialog() {
        MaterialDialog mDialog = new MaterialDialog.Builder(this)
                .setTitle("Diesen API-Key verwenden?")
                .setMessage(getApiKey())
                .setCancelable(false)
                .setPositiveButton("Verwenden", R.drawable.ic_done_24px, (dialogInterface, which) -> dialogInterface.dismiss())
                .setNegativeButton("Neu scannen", R.drawable.ic_delete_24px, (dialogInterface, which) -> {
                    Intent intent = new Intent(MainActivity.context, FirstStartActivity.class);
                    startActivity(intent);
                    handler.removeCallbacks(swipeRunnable);
                    isFirstUpdate = true;
                    Log.e(TAG, "OnCancelListener");
                })
                .build();
        mDialog.show();
    }

    // --- turn autoswipe on  ---
    private void setAutoSwipe() {
        //auto swipe screen
        mPager.setUserInputEnabled(false);
        handler.post(swipeRunnable = new Runnable() {
            @Override
            public void run() {
                if (0 <= pagerAdapter.getItemCount()) {
                    mPager.setCurrentItem(i, true);
                    handler.postDelayed(this, PresentationActivity.display_time);
                    i++;
                    if (i >= pagerAdapter.getItemCount() + 1) {
                        i = 0;
                        mPager.setCurrentItem(i, false);
                        i++;
                    }
                }
            }
        });
    }
}
