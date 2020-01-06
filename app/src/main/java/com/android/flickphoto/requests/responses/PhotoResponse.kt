package com.android.flickphoto.requests.responses

import com.android.flickphoto.models.Photo
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



//Both Search & Recent
class PhotoResponse {
    @SerializedName("photo")
    @Expose()
   lateinit var listOfPhoto:List<Photo>


}