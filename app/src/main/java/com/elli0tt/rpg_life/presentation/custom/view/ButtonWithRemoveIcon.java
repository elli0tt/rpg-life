package com.elli0tt.rpg_life.presentation.custom.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

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

        removeButton = findViewById(R.id.remove_icon_remove_button);
        textView = findViewById(R.id.remove_icon_text_view);
        mainIconImageView = findViewById(R.id.remove_icon_main_icon_image_view);

        final TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.ButtonWithRemoveIcon, defStyleAttr, 0);
        mainIconImageView.setImageDrawable(ContextCompat.getDrawable(context,
                typedArray.getResourceId(R.styleable.ButtonWithRemoveIcon_icon, 0)));
        textView.setText(typedArray.getResourceId(R.styleable.ButtonWithRemoveIcon_text, 0));
        if (typedArray.getBoolean(R.styleable.ButtonWithRemoveIcon_showRemoveIcon, false)) {
            removeButton.setVisibility(View.VISIBLE);
        } else {
            removeButton.setVisibility(View.INVISIBLE);
        }
        typedArray.recycle();
    }

    public void setText(String text) {
        textView.setText(text);
    }

    public void setText(int resourceId) {
        textView.setText(resourceId);
    }

    public void setOnRemoveClickListener(@NonNull View.OnClickListener listener) {
        removeButton.setOnClickListener(v -> {
            listener.onClick(v);
            v.setVisibility(INVISIBLE);
        });
    }

    public void setRemoveIconVisibility(int visibility) {
        removeButton.setVisibility(visibility);
    }
}
