package com.ivanasharov.smartplanner.presentation.view.adapters.binding

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

@BindingAdapter(value = ["app:url"])
fun loadImage(imageView: ImageView, uri: String?){

    if (uri.isNullOrEmpty()) {
        return
    }
    try {
        Glide.with(imageView)
            .load(uri)
            .transition(DrawableTransitionOptions.withCrossFade(300))
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(imageView)
    }catch (e: Exception){
        e.printStackTrace()
    }
}