package com.elli0tt.rpg_life.presentation.countdown_timer;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.elli0tt.rpg_life.R;

public class CountDownFragment extends Fragment {

    private Button startPauseButton;
    private Button resetButton;

    private TextView timerTextView;

    private CountDownTimer timer;

    private CountDownViewModel viewModel;

    private static final int COUNT_DOWN_INTERVAL = 1000;

    private static final String KEY_START_PAUSE_BUTTON_VISIBILITY = "start_pause_button_visibility";
    private static final String KEY_RESET_BUTTON_VISIBILITY = "reset_button_visibility";
    private static final String KEY_START_PAUSE_BUTTON_TEXT = "start_pause_button_text";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_countdown_timer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(CountDownViewModel.class);
        subscribeToViewModel();

        startPauseButton = view.findViewById(R.id.countdown_timer_start_pause_button);
        resetButton = view.findViewById(R.id.countdown_timer_reset_button);

        timerTextView = view.findViewById(R.id.countdown_timer_text_view);

        startPauseButton.setOnClickListener(startPauseButtonOnClickListener);
        resetButton.setOnClickListener(resetButtonOnClickListener);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            startPauseButton.setVisibility(savedInstanceState.getInt(KEY_START_PAUSE_BUTTON_VISIBILITY));
            resetButton.setVisibility(savedInstanceState.getInt(KEY_RESET_BUTTON_VISIBILITY));
            startPauseButton.setText(savedInstanceState.getCharSequence(KEY_START_PAUSE_BUTTON_TEXT));
        }

        if (viewModel.isTimerRunning()){
            startTimer();

        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_START_PAUSE_BUTTON_VISIBILITY, startPauseButton.getVisibility());
        outState.putInt(KEY_RESET_BUTTON_VISIBILITY, resetButton.getVisibility());
        outState.putCharSequence(KEY_START_PAUSE_BUTTON_TEXT, startPauseButton.getText());
    }



    @Override
    public void onPause() {
        super.onPause();
        if (viewModel.isTimerRunning()) {
            timer.cancel();
        }
    }


    private void subscribeToViewModel() {
        viewModel.getTimeLeftMillis().observe(getViewLifecycleOwner(),
                aLong -> timerTextView.setText(viewModel.getTimeLeft()));
    }

    private View.OnClickListener startPauseButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (viewModel.isTimerRunning()) {
                pauseTimer();
            } else {
                startTimer();
            }
        }
    };

    private View.OnClickListener resetButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            viewModel.resetTimer();
            startPauseButton.setText(R.string.countdown_timer_start);
            startPauseButton.setVisibility(View.VISIBLE);
            resetButton.setVisibility(View.INVISIBLE);
        }
    };

    private void startTimer() {
        viewModel.startTimer(System.currentTimeMillis());
        timer = new CountDownTimer(viewModel.getTimeLeftMillis().getValue(),
                COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                viewModel.updateTimeLeftInMillis(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                viewModel.pauseTimer();
                startPauseButton.setText(R.string.countdown_timer_start);
                startPauseButton.setVisibility(View.INVISIBLE);
                resetButton.setVisibility(View.VISIBLE);
            }
        }.start();


        startPauseButton.setText(R.string.countdown_timer_pause);
        resetButton.setVisibility(View.INVISIBLE);
    }

    private void pauseTimer() {
        timer.cancel();
        viewModel.pauseTimer();
        startPauseButton.setText(R.string.countdown_timer_start);
        resetButton.setVisibility(View.VISIBLE);
    }


}
