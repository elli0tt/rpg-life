package com.elli0tt.rpg_life.presentation.screen.stopwatch;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.presentation.core.fragment.BaseFragment;
import com.elli0tt.rpg_life.presentation.screen.stopwatch.di.StopwatchComponent;

import javax.inject.Inject;

public class StopwatchFragment extends BaseFragment {

    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    private StopwatchComponent stopwatchComponent;

    private StopwatchViewModel viewModel;

    private Button startButton;
    private Button pauseButton;
    private Button resetButton;

    private Chronometer chronometer;

    private long pauseOffSet;

    private final View.OnClickListener startButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffSet);
            chronometer.start();
        }
    };
    private final View.OnClickListener pauseButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            chronometer.stop();
            pauseOffSet = SystemClock.elapsedRealtime() - chronometer.getBase();
        }
    };
    private final View.OnClickListener resetButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            chronometer.stop();
            chronometer.setBase(SystemClock.elapsedRealtime());
            pauseOffSet = 0;
        }
    };

    public StopwatchFragment() {
        super(R.layout.fragment_stopwatch);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initDagger();
        initViews(view);
        setListeners();
    }

    private void initDagger() {
        stopwatchComponent = getAppComponent().stopwatchComponentFactory().create();
        stopwatchComponent.inject(this);

        viewModel = new ViewModelProvider(this, viewModelFactory).get(StopwatchViewModel.class);
    }

    private void initViews(@NonNull View view) {
        startButton = view.findViewById(R.id.button_start_pause);
        pauseButton = view.findViewById(R.id.button_pause);
        resetButton = view.findViewById(R.id.button_reset);

        chronometer = view.findViewById(R.id.chronometer);
    }

    private void setListeners() {
        startButton.setOnClickListener(startButtonOnClickListener);
        pauseButton.setOnClickListener(pauseButtonOnClickListener);
        resetButton.setOnClickListener(resetButtonOnClickListener);
    }
}