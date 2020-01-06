package com.android.flickphoto.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo(
    val id: String,
    @SerializedName("ownername") val ownerName: String,
    @SerializedName("url_s") val url: String,
    val title: String,
    @SerializedName("datetaken") val dateTaken: String
):Parcelable {

    override fun toString(): String {
        return "Photo(id='$id', ownerName='$ownerName', url='$url', title='$title', dateTaken='$dateTaken')"
    }

}

