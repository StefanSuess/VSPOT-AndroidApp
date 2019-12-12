package com.example.vspot4.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.vspot4.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.graphics.Color.rgb;


public class PresentationFragment extends Fragment {

    private static final String TAG = "PresentationFragment";

    Boolean progressbar = false;
    TextView textViewHeading, textViewSubheading, textViewTextblock;
    int number;
    private String position, background_color, bg_img_cdn_link, overlay_color, text_color, heading, subheading, text_block, layout_name;

    // Minimal fragment
    public static PresentationFragment newInstance(int position) {
        PresentationFragment presentationFragment = new PresentationFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("NUMBER", position);
        bundle.putBoolean("PROGRESSBAR", true);
        presentationFragment.setArguments(bundle);
        return presentationFragment;
    }

    // All IN ONE FRAGMENT
    public static PresentationFragment newInstance(int position, String background_color
            , String bg_img_cdn_link, String overlay_color, String text_color, String heading
            , String subheading, String text_block, String layout_name) {
        PresentationFragment presentationFragment = new PresentationFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("number", position);
        //Log.e(TAG,background_color);
        bundle.putString("background_color", background_color);
        bundle.putString("bg_img_cdn_link", bg_img_cdn_link);
        bundle.putString("overlay_color", overlay_color);
        bundle.putString("text_color", text_color);
        bundle.putString("heading", heading);
        bundle.putString("subheading", subheading);
        bundle.putString("text_block", text_block);
        bundle.putString("layout_name", layout_name);
        presentationFragment.setArguments(bundle);
        return presentationFragment;
    }

    private static int getColor(String input) {
        if (input != null) {
            String newPattern = "rgba? *\\( *([0-9]+), *([0-9]+), *([0-9]+)(, *([0-9]+)){0,1} *\\)";
            //String oldPattern = "rgb *\\( *([0-9]+), *([0-9]+), *([0-9]+) *\\)";
            Pattern c = Pattern.compile(newPattern);
            Matcher m = c.matcher(input);
            if (m.matches()) {
                int red = Integer.parseInt((m.group(1)));
                int green = Integer.parseInt((m.group(2)));
                int blue = Integer.parseInt((m.group(3)));
                //int alpha = Integer.parseInt((m.group(4)));
                return rgb(red, green, blue);
            }
        }
        return rgb(0, 0, 0);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_slide, container, false);

        // Get all arguments
        number = getArguments().getInt("NUMBER", 0);
        background_color = getArguments().getString("background_color", null);
        bg_img_cdn_link = getArguments().getString("bg_img_cdn_link", null);
        overlay_color = getArguments().getString("overlay_color", null);
        text_color = getArguments().getString("text_color", null);
        heading = getArguments().getString("heading", null);
        subheading = getArguments().getString("subheading", null);
        text_block = getArguments().getString("text_block", null);
        layout_name = getArguments().getString("layout_name", null);
        progressbar = getArguments().getBoolean("PROGRESSBAR", false);

        // Loading animation
        ProgressBar progressBar = rootView.findViewById(R.id.progressbar);
        if (progressbar) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }

        // Heading and subeheading
        textViewHeading = rootView.findViewById(R.id.heading);
        textViewSubheading = rootView.findViewById(R.id.subheading);
        textViewTextblock = rootView.findViewById(R.id.textblock);
        textViewHeading.setText(heading);
        textViewHeading.setTextColor(getColor(text_color));
        textViewSubheading.setText(subheading);
        textViewSubheading.setTextColor(getColor(text_color));
        textViewTextblock.setText(text_block);
        textViewTextblock.setTextColor(getColor(text_color));

        // Show Pictures
        setFrameBackGroundImage(bg_img_cdn_link, rootView);

        // set background color
        rootView.setBackgroundColor(getColor(background_color));

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    // Show Pictures , set background
    private void setFrameBackGroundImage(String bg_img_cdn_link, View rootView) {
        //TODO replace depricated code
        if (bg_img_cdn_link != null) {
            Glide.with(this).load(bg_img_cdn_link).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                    rootView.setBackground(resource);
                }
            });
        }
    }


}
