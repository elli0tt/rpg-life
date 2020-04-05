package com.elli0tt.rpg_life.presentation.stopwatch;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.elli0tt.rpg_life.R;

public class StopwatchFragment extends Fragment {

    private Button startButton;
    private Button pauseButton;
    private Button resetButton;

    private Chronometer chronometer;

    private long pauseOffSet;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stopwatch, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        startButton = view.findViewById(R.id.button_start_pause);
        pauseButton = view.findViewById(R.id.button_pause);
        resetButton = view.findViewById(R.id.button_reset);

        chronometer = view.findViewById(R.id.chronometer);

        startButton.setOnClickListener(startButtonOnClickListener);
        pauseButton.setOnClickListener(pauseButtonOnClickListener);
        resetButton.setOnClickListener(resetButtonOnClickListener);
    }

    private View.OnClickListener startButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffSet);
            chronometer.start();
        }
    };

    private View.OnClickListener pauseButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            chronometer.stop();
            pauseOffSet = SystemClock.elapsedRealtime() - chronometer.getBase();
        }
    };

    private View.OnClickListener resetButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            chronometer.stop();
            chronometer.setBase(SystemClock.elapsedRealtime());
            pauseOffSet = 0;
        }
    };


}
