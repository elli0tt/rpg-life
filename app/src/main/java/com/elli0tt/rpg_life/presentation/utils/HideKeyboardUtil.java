package com.elli0tt.rpg_life.presentation.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class HideKeyboardUtil {
    public static void hideKeyboard(View view, Activity activity) {
        if (activity != null) {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
