package com.elli0tt.rpg_life.presentation.countdown_timer;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.databinding.FragmentCountdownTimerBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class CountDownFragment extends Fragment {

    private FloatingActionButton startFab;
    private FloatingActionButton pauseFab;
    private FloatingActionButton stopFab;
    private TextView timerTextView;
    private CountDownTimer timer;
    private NumberPicker hoursNumberPicker;
    private NumberPicker minutesNumberPicker;
    private NumberPicker secondsNumberPicker;
    private LinearLayout numberPickersLayout;
    private MaterialProgressBar progressBar;

    private CountDownViewModel viewModel;

    //Used to enable startTimerFab just 1 time - after it was disabled
    private boolean isStartFabEnabled = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentCountdownTimerBinding binding = FragmentCountdownTimerBinding.inflate(inflater,
                container, false);
        viewModel = ViewModelProviders.of(this).get(CountDownViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        subscribeToViewModel();

        startFab = view.findViewById(R.id.countdown_timer_start_fab);
        pauseFab = view.findViewById(R.id.countdown_timer_pause_fab);
        stopFab = view.findViewById(R.id.countdown_timer_stop_fab);
        hoursNumberPicker = view.findViewById(R.id.countdown_timer_hours_number_picker);
        minutesNumberPicker = view.findViewById(R.id.countdown_timer_minutes_number_picker);
        secondsNumberPicker = view.findViewById(R.id.countdown_timer_seconds_number_picker);
        numberPickersLayout = view.findViewById(R.id.countdown_timer_number_pickers_layout);
        timerTextView = view.findViewById(R.id.countdown_timer_text_view);
        progressBar = view.findViewById(R.id.countdown_timer_progress_bar);

        startFab.setOnClickListener(startFabOnClickListener);
        pauseFab.setOnClickListener(pauseFabOnClickListener);
        stopFab.setOnClickListener(resetFabOnClickListener);

        hoursNumberPicker.setMaxValue(Constants.HOURS_NUMBER_PICKER_MAX);
        minutesNumberPicker.setMaxValue(Constants.MINUTES_NUMBER_PICKER_MAX);
        secondsNumberPicker.setMaxValue(Constants.SECONDS_NUMBER_PICKER_MAX);

        hoursNumberPicker.setFormatter(numberPickersFormatter);
        minutesNumberPicker.setFormatter(numberPickersFormatter);
        secondsNumberPicker.setFormatter(numberPickersFormatter);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (viewModel.getTimerState().getValue() == CountDownViewModel.TimerState.RUNNING) {
            startTimer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel.getTimerState().getValue() == CountDownViewModel.TimerState.RUNNING) {
            startTimer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (viewModel.getTimerState().getValue() == CountDownViewModel.TimerState.RUNNING) {
            timer.cancel();
        }
        viewModel.saveData();
    }

    private void subscribeToViewModel() {
        viewModel.getTimeLeftMillis().observe(getViewLifecycleOwner(), aLong -> {
            timerTextView.setText(viewModel.getTimeLeft());
            progressBar.setProgress(viewModel.getProgress());
        });
        viewModel.getTimerState().observe(getViewLifecycleOwner(), timerState -> updateButtons());
        viewModel.isTimerNew().observe(getViewLifecycleOwner(), aBoolean -> {
            if (!aBoolean) {
                numberPickersLayout.setVisibility(View.INVISIBLE);
                timerTextView.setVisibility(View.VISIBLE);
                progressBar.setMax(viewModel.getTimeLeftMillis().getValue().intValue());
            } else {
                numberPickersLayout.setVisibility(View.VISIBLE);
                timerTextView.setVisibility(View.INVISIBLE);
                progressBar.setProgress(0);
            }
        });

        viewModel.getHours().observe(getViewLifecycleOwner(), integer -> {
            if (isStartFabEnabled ^ viewModel.isNeedToEnableStartFab()) {
                enableStartFab(viewModel.isNeedToEnableStartFab());
            }

        });
        viewModel.getMinutes().observe(getViewLifecycleOwner(), integer -> {
            if (isStartFabEnabled ^ viewModel.isNeedToEnableStartFab()) {
                enableStartFab(viewModel.isNeedToEnableStartFab());
            }

        });
        viewModel.getSeconds().observe(getViewLifecycleOwner(), integer -> {
            if (isStartFabEnabled ^ viewModel.isNeedToEnableStartFab()) {
                enableStartFab(viewModel.isNeedToEnableStartFab());
            }

        });
    }

    private View.OnClickListener startFabOnClickListener = v -> startTimer();

    private void startTimer() {
        viewModel.startTimer(System.currentTimeMillis());
        timer = new CountDownTimer(viewModel.getTimeLeftMillis().getValue(),
                Constants.COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                viewModel.updateTimeLeftInMillis(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                viewModel.pauseTimer();
                progressBar.setProgress(0);
            }
        }.start();
    }

    private View.OnClickListener pauseFabOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            timer.cancel();
            viewModel.pauseTimer();
        }
    };

    private View.OnClickListener resetFabOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            timer.cancel();
            viewModel.stopTimer();
        }
    };

    private NumberPicker.Formatter numberPickersFormatter =
            value -> String.format(Locale.getDefault(), "%02d", value);

    private void updateButtons() {
        switch (viewModel.getTimerState().getValue()) {
            case RUNNING:
                startFab.hide();
                pauseFab.show();
                stopFab.show();
                break;
            case PAUSED:
                startFab.show();
                pauseFab.hide();
                stopFab.show();
                break;
            case STOPPED:
                startFab.show();
                pauseFab.hide();
                stopFab.hide();
                break;
        }
    }

    //There is a bug in FloatingActionButton implementation, not fixed yet by Google:
    //If fab.hide() and fab.show() methods were called, you can't change fab icon - it won't be displayed
    //There is a workaround - you need to call fab.hide(), change icon and then fab.show()
    private void enableStartFab(boolean enabled) {
        startFab.hide();
        if (enabled) {
            startFab.setImageDrawable(ContextCompat.getDrawable(getContext(),
                    R.drawable.ic_play_arrow_white_24dp));
            isStartFabEnabled = true;
        } else {
            startFab.setImageDrawable(ContextCompat.getDrawable(getContext(),
                    R.drawable.ic_play_arrow_gray_24dp));
            isStartFabEnabled = false;
        }
        startFab.show();
        startFab.setEnabled(enabled);
    }
}
