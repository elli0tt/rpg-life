package com.elli0tt.rpg_life.presentation.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;

import com.elli0tt.rpg_life.R;

public class ButtonWithRemoveIcon extends CardView {
    private Button removeButton;
    private TextView textView;
    private AppCompatImageView mainIconImageView;

    public ButtonWithRemoveIcon(Context context) {
        this(context, null);
    }

    public ButtonWithRemoveIcon(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ButtonWithRemoveIcon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.button_with_remove_icon, this);

        removeButton = findViewById(R.id.button_with_remove_icon_remove);
        textView = findViewById(R.id.button_with_remove_icon_text);
        mainIconImageView = findViewById(R.id.button_with_remove_icon_main_icon);

        final TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.ButtonWithRemoveIcon, defStyleAttr, 0);
        mainIconImageView.setImageDrawable(context.getDrawable(
                typedArray.getResourceId(R.styleable.ButtonWithRemoveIcon_icon, 0)));
        textView.setText(typedArray.getResourceId(R.styleable.ButtonWithRemoveIcon_text, 0));

    }

    public void setText(String text) {
        textView.setText(text);
    }

    public void setText(int resourceId) {
        textView.setText(resourceId);
    }

    public void setOnRemoveClickListener(@NonNull View.OnClickListener listener) {
        removeButton.setOnClickListener(listener);
    }

    public void setRemoveIconVisisbility(int visibility) {
        removeButton.setVisibility(visibility);
    }
}
