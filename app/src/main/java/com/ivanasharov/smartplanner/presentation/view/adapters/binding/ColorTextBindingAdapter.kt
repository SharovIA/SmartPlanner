package com.ivanasharov.smartplanner.presentation.view.adapters.binding

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.ivanasharov.smartplanner.R
import org.jetbrains.annotations.NotNull

@BindingAdapter(value = ["colorText"])
fun setColorText(@NotNull textView: TextView, isNight: Boolean) {

    if (isNight) {
        textView.setTextColor(ContextCompat.getColor(textView.context, R.color.night_text))
    } else
        textView.setTextColor(ContextCompat.getColor(textView.context, R.color.secondary_text))
}