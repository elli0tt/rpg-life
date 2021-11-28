package com.elliot.patientapp.presentation.adapter.base

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseListAdapter<Model, ViewHolder : RecyclerView.ViewHolder>(
    diffCallback: DiffUtil.ItemCallback<Model> = AlwaysEqualsDiffCallback()
) :
    ListAdapter<Model, ViewHolder>(diffCallback) {

    fun interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    var onItemClickListener: OnItemClickListener? = null
}