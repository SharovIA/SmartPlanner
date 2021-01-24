package com.ivanasharov.smartplanner.presentation.view.adapters.binding

import android.annotation.SuppressLint
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ivanasharov.smartplanner.R

@SuppressLint("ResourceAsColor")
@BindingAdapter(value = ["app:colorText"])
fun setColorText(textView: TextView, isNight: Boolean){

    if(isNight)
        textView.setTextColor(R.color.primary_light)
    else
        textView.setTextColor(R.color.secondary_text)
}