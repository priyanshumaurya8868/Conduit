package com.priyanshumaurya8868.realworld.io.conduit.extentions

import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadImageInCircleView(uri: String?, placeholder: Int ) {
    Log.d("image" ,"loading -> $uri  -> palceholder $placeholder ")
    if (uri != null)
    Glide.with(this).load(uri).placeholder(placeholder).circleCrop().into(this)
    else
        Glide.with(this).load(placeholder).circleCrop().into(this)
}

fun ImageView.loadImageInCircleView(uri: String) {
    Glide.with(this).load(uri).circleCrop().into(this)
}

fun ImageView.loadImage(uri: String) {
    Glide.with(this).load(uri).into(this)
}