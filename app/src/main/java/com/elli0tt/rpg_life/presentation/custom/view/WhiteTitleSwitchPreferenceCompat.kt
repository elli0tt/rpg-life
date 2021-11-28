package com.elli0tt.rpg_life.presentation.custom.view

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceViewHolder
import androidx.preference.SwitchPreferenceCompat
import com.elli0tt.rpg_life.R


class WhiteTitleSwitchPreferenceCompat(context: Context, attrs: AttributeSet?) :
    SwitchPreferenceCompat(context, attrs) {

    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        super.onBindViewHolder(holder)
        val titleTextView = holder?.findViewById(android.R.id.title) as TextView
        titleTextView.setTextColor(ContextCompat.getColor(context, R.color.colorTextWhite))
    }
}