package com.behraz.fastermixer.batch.utils.general

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.respository.apiservice.ApiService
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions


@BindingAdapter("profileUrl")
fun loadProfilePic(mProfileImage: ImageView, picUrl: String?) {
    println("debug: glideUrl:$picUrl")
    val requestOptions = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .placeholder(R.drawable.ic_worker)
    Glide.with(mProfileImage.context)
        .setDefaultRequestOptions(requestOptions)
        .load(if (picUrl != null) ApiService.domain + picUrl else "") //.transition(new DrawableTransitionOptions().crossFade()) //transition baraye circleImageView nemishe
        .into(mProfileImage)
}

@BindingAdapter("imageUrl")
fun loadPic(iv: ImageView, picUrl: String?) {
    //val _picUrl = picUrl?.toInt() //TODO in khat hatman bayad hazf shavad //test purpose
    val requestOptions = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .placeholder(R.drawable.ic_worker)
    Glide.with(iv.context)
        .setDefaultRequestOptions(requestOptions)
        .load(
            ApiService.domain + (picUrl ?: "")
        ) //.transition(new DrawableTransitionOptions().crossFade()) //transition baraye circleImageView nemishe
        .into(iv)
}


@BindingAdapter("weatherIcon")
fun loadWeatherIcon(iv: ImageView, picUrl: String?) {
    //val _picUrl = picUrl?.toInt() //TODO in khat hatman bayad hazf shavad //test purpose
    val requestOptions = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        //.placeholder(R.drawable.ic_sun) //TODO add place holder
    Glide.with(iv.context)
        .setDefaultRequestOptions(requestOptions)
        .load(picUrl)
        .transition(DrawableTransitionOptions().crossFade()) //transition baraye circleImageView nemishe
        .into(iv)
}

@BindingAdapter("imageUrl")
fun loadPic(mProfileImage: ImageView, resId: Int) {
    val requestOptions = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .placeholder(R.drawable.ic_worker)
    Glide.with(mProfileImage.context)
        .setDefaultRequestOptions(requestOptions)
        .load(resId) //.transition(new DrawableTransitionOptions().crossFade()) //transition baraye circleImageView nemishe
        .into(mProfileImage)
}