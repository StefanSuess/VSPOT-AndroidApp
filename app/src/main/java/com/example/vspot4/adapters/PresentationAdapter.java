package com.example.vspot4.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.vspot4.database.entity.Screen;
import com.example.vspot4.fragments.PresentationFragment;
import com.example.vspot4.viewmodels.ChannelViewModel;

public class PresentationAdapter extends FragmentStateAdapter {

    private static final String TAG = "PresentationAdapter";
    ChannelViewModel channelViewModel;

    public PresentationAdapter(Context context, @NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
        channelViewModel = ViewModelProviders.of((FragmentActivity) context).get(ChannelViewModel.class);
    }

    @NonNull
    @Override // Choose which fragment to make depending on layout_name
    public Fragment createFragment(int position) {

        if (channelViewModel.getChannel().getValue() == null) { // empty fragment / loading fragment
            return PresentationFragment.newInstance(position);
        } else if (channelViewModel.getChannel().getValue().getScreens().get(position).getHtml_block() != null) { // html fragment
            // for easier coding set a new variable for every position
            Screen screens = channelViewModel.getChannel().getValue().getScreens().get(position);
            return PresentationFragment.newInstance(position, screens.getBackground_color()
                    , screens.getBg_img_cdn_link(), screens.getOverlay_color(), screens.getText_color()
                    , screens.getLayout_name(), screens.getHtml_block());
        } else { // basic fragment
            // for easier coding set a new variable for every position
            Screen screens = channelViewModel.getChannel().getValue().getScreens().get(position);
            return PresentationFragment.newInstance(position, screens.getBackground_color()
                    , screens.getBg_img_cdn_link(), screens.getOverlay_color(), screens.getText_color()
                    , screens.getHeading(), screens.getSubheading(), screens.getText_block()
                    , screens.getLayout_name());
        }
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
        /*if (channelViewModel.getChannel().getValue() != null) {
            return channelViewModel.getChannel().getValue().getScreens().get(position).hashCode();
        } else {
            return super.getItemId(position);
        }*/
    }

    @Override
    public int getItemCount() {
        if (channelViewModel.getChannel().getValue() == null) {
            return 1;
        }
        return channelViewModel.getChannel().getValue().getScreens().size();
    }
}

