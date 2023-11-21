package com.example.aep.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.example.aep.Event;
import com.example.aep.R;
import com.example.aep.databinding.FragmentHomeBinding;
import com.example.aep.databinding.FragmentStudenthomeBinding;

import java.util.ArrayList;
import java.util.List;

public class StudentHomeFragment extends Fragment {

    private @NonNull FragmentStudenthomeBinding binding;
    private CardView SportsRegistration;

    private ViewPager eventViewPager;
    private List<Event> events = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentStudenthomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Initialize imageView from the layout
        SportsRegistration = binding.sportsRegistration;



        // Set a click listener for imageView1
        SportsRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use the Navigation component to navigate to the new destination
                Navigation.findNavController(v).navigate(R.id.action_homeStudentFragment_to_SportsRegistrationActivity);
            }
        });

        return root;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}