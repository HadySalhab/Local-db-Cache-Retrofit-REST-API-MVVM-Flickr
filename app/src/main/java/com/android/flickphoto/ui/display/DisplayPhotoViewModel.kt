package com.android.flickphoto.ui.display

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.palette.graphics.Palette
import com.android.flickphoto.models.Photo
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class DisplayPhotoViewModel(val photo: Photo, val app: Application) : AndroidViewModel(app) {

    private val _color = MutableLiveData<Drawable>()
    val color: LiveData<Drawable>
        get() = _color


    init {
        generateColor()
    }

 private fun generateColor() {
        Glide.with(app.applicationContext)
            .asBitmap()
            .load(photo.url?.toUri()?.buildUpon()?.scheme("https")?.build())
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Palette.from(resource)
                        .generate{palette ->
                            _color.value = ColorDrawable(palette?.lightVibrantSwatch?.rgb?:0)
                        }
                }

            })
    }
}