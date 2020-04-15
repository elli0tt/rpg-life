package com.elli0tt.rpg_life.presentation.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.elli0tt.rpg_life.R

class UpDownArrowsView(context: Context, attributeSet: AttributeSet?)
    : ConstraintLayout(context, attributeSet) {
    private val arrowImageView: AppCompatImageView
    private val textView: TextView
    private val removeImageView: AppCompatImageView

    var onViewClickListener: OnClickListener? = null

    init {
        inflate(context, R.layout.up_down_arrows_view, this)

        arrowImageView = findViewById(R.id.arrow_icon_image_view)
        textView = findViewById(R.id.text_view)
        removeImageView = findViewById(R.id.remove_image_view)

        setOnClickListener {
            onViewClickListener?.onClick(it)
        }
    }

    fun setText(text: String) {
        textView.text = text
    }

    fun setText(textResId: Int) {
        textView.setText(textResId)
    }

    fun setArrowUp(){
        arrowImageView.setImageDrawable(context.getDrawable(R.drawable.ic_keyboard_arrow_up_black_20dp))
    }

    fun setArrowDown(){
        arrowImageView.setImageDrawable(context.getDrawable(R.drawable.ic_keyboard_arrow_down_black_20dp))
    }
}