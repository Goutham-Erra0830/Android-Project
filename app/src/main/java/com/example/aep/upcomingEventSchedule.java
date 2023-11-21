package com.example.aep;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class upcomingEventSchedule extends Fragment {

    private ViewPager eventViewPager;
    private Handler handler;
    private Runnable runnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate your fragment's layout and initialize eventViewPager
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //eventViewPager = view.findViewById(R.id.eventViewPager);

        // Initialize handler and runnable for auto-scrolling
        handler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = eventViewPager.getCurrentItem();
                int totalItems = eventViewPager.getAdapter().getCount();

                if (currentItem < totalItems - 1) {
                    eventViewPager.setCurrentItem(currentItem + 1);
                } else {
                    eventViewPager.setCurrentItem(0);
                }
            }
        };

        // Start auto-scrolling
        startAutoScroll();

        return view;
    }

    @Override
    public void onDestroy() {
        // Stop auto-scrolling when the fragment is destroyed
        stopAutoScroll();
        super.onDestroy();
    }

    private void startAutoScroll() {
        // Auto-scroll every 3 seconds (adjust the interval as needed)
        handler.postDelayed(runnable, 3000);
    }

    private void stopAutoScroll() {
        handler.removeCallbacks(runnable);
    }
}
