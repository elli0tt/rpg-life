package com.elli0tt.rpg_life.presentation.screen.countdown_timer;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.databinding.FragmentCountdownTimerBinding;

import java.util.Locale;

public class CountDownFragment extends Fragment {

    private static final String TIME_LEFT_TAG = "time left";
    private FragmentCountdownTimerBinding binding;
    private CountDownViewModel viewModel;
    private CountDownTimer timer;
    //Used to enable startTimerFab just 1 time - after it was disabled
    private boolean isStartFabEnabled = true;
    private Observer<Integer> numberPickersValuesObserver = integer -> {
        if (binding.numberPickersLayout.getVisibility() == View.VISIBLE &&
                (isStartFabEnabled ^ viewModel.isNeedToEnableStartFab())) {
            enableStartFab(viewModel.isNeedToEnableStartFab());
        }
    };
    private View.OnClickListener startFabOnClickListener = v -> startTimer();
    private View.OnClickListener pauseFabOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            timer.cancel();
            viewModel.pauseTimer();
        }
    };
    private View.OnClickListener stopFabOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (viewModel.getTimerState().getValue() == TimerState.RUNNING) {
                timer.cancel();
            }
            viewModel.stopTimer();
        }
    };
    private NumberPicker.Formatter numberPickersFormatter =
            value -> String.format(Locale.getDefault(), "%02d", value);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCountdownTimerBinding.inflate(inflater,
                container, false);
        viewModel = new ViewModelProvider(this).get(CountDownViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        subscribeToViewModel();

        binding.startFab.setOnClickListener(startFabOnClickListener);
        binding.pauseFab.setOnClickListener(pauseFabOnClickListener);
        binding.stopFab.setOnClickListener(stopFabOnClickListener);

        binding.hoursNumberPicker.setMaxValue(Constants.HOURS_NUMBER_PICKER_MAX);
        binding.minutesNumberPicker.setMaxValue(Constants.MINUTES_NUMBER_PICKER_MAX);
        binding.secondsNumberPicker.setMaxValue(Constants.SECONDS_NUMBER_PICKER_MAX);

        binding.hoursNumberPicker.setFormatter(numberPickersFormatter);
        binding.minutesNumberPicker.setFormatter(numberPickersFormatter);
        binding.secondsNumberPicker.setFormatter(numberPickersFormatter);
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.progressBar.setMax(viewModel.getMaxProgress());
        //Log.d(MAX_PROGRESS_TAG, Integer.toString(viewModel.getMaxProgress()));
        if (viewModel.getTimerState().getValue() == TimerState.RUNNING) {
            startTimer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (viewModel.getTimerState().getValue() == TimerState.RUNNING) {
            timer.cancel();
        }
        viewModel.saveData();
    }

    private void subscribeToViewModel() {
        viewModel.isTimerNew().observe(getViewLifecycleOwner(), aBoolean -> {
            if (!aBoolean) {
                binding.numberPickersLayout.setVisibility(View.INVISIBLE);
                binding.timeLeftTextView.setVisibility(View.VISIBLE);
            } else {
                binding.numberPickersLayout.setVisibility(View.VISIBLE);
                enableStartFab(false);
                binding.timeLeftTextView.setVisibility(View.INVISIBLE);
                //progressBar.setProgress(0);
            }
        });
        viewModel.getTimeLeftSeconds().observe(getViewLifecycleOwner(), aLong -> {
            binding.timeLeftTextView.setText(viewModel.getTimeLeft());
            binding.progressBar.setProgress(viewModel.getProgress());
            Log.d(TIME_LEFT_TAG,
                    viewModel.getTimeLeft() + " " + viewModel.getProgress());
        });
        viewModel.getTimerState().observe(getViewLifecycleOwner(), timerState -> updateButtons());


        viewModel.getHours().observe(getViewLifecycleOwner(), numberPickersValuesObserver);
        viewModel.getMinutes().observe(getViewLifecycleOwner(), numberPickersValuesObserver);
        viewModel.getSeconds().observe(getViewLifecycleOwner(), numberPickersValuesObserver);
    }

    private void startTimer() {
        viewModel.startTimer(System.currentTimeMillis());
        binding.progressBar.setMax(viewModel.getMaxProgress());
        timer = new CountDownTimer(viewModel.getTimeLeftMillis(), Constants.COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                viewModel.updateTimeLeftSeconds();
            }

            @Override
            public void onFinish() {
                viewModel.stopTimer();
                binding.progressBar.setProgress(0);

            }
        }.start();
    }

    private void updateButtons() {
        TimerState timerState = viewModel.getTimerState().getValue();
        if (timerState == null) {
            timerState = TimerState.STOPPED;
        }
        switch (timerState) {
            case RUNNING:
                binding.startFab.hide();
                binding.pauseFab.show();
                binding.stopFab.show();
                break;
            case PAUSED:
                binding.startFab.show();
                binding.pauseFab.hide();
                binding.stopFab.show();
                break;
            case STOPPED:
                binding.startFab.show();
                binding.pauseFab.hide();
                binding.stopFab.hide();
                break;
        }
    }

    //There is a bug in FloatingActionButton implementation, which has not been fixed yet by Google:
    //If fab.hide() and fab.show() methods were called, you can't change fab icon - it won't be
    // displayed
    //There is a workaround - you need to call fab.hide(), change icon and then fab.show()
    private void enableStartFab(boolean enabled) {
        binding.startFab.hide();
        if (enabled) {
            binding.startFab.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                    R.drawable.ic_round_play_arrow_white_24));
            isStartFabEnabled = true;
        } else {
            binding.startFab.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                    R.drawable.ic_round_play_arrow_grey_24));
            isStartFabEnabled = false;
        }
        binding.startFab.show();
        binding.startFab.setEnabled(enabled);
    }
}
