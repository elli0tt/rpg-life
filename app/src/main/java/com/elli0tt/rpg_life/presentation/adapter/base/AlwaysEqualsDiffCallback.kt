package com.elliot.patientapp.presentation.adapter.base

import androidx.recyclerview.widget.DiffUtil

class AlwaysEqualsDiffCallback<Model> : DiffUtil.ItemCallback<Model>() {
    override fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean {
        return true
    }

    override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean {
        return true
    }
}