package com.android.flickphoto.requests.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FlickrResponse{
    @SerializedName("photos")
    @Expose()
    lateinit var photos:PhotoResponse
}