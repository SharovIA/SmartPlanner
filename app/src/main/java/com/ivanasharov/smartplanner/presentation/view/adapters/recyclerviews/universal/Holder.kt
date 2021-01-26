package com.ivanasharov.smartplanner.presentation.view.adapters.recyclerviews.universal

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class Holder<T : ViewDataBinding>(bind: T) : RecyclerView.ViewHolder(bind.root) {
    val binding = bind
}