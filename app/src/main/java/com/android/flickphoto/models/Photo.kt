package com.android.flickphoto.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "photos")
data class Photo(
    @PrimaryKey
    val id: String,
    @field:SerializedName("ownername")
    val ownerName:String?="Unknown Author",
    @SerializedName("url_s")
    val url: String?,
    @field:SerializedName("title")
    val title: String?="No Title",
    @SerializedName("datetaken")
    val dateTaken: String?="Unknown Date"
):Parcelable {

    override fun toString(): String {
        return "Photo(id='$id', ownerName='$ownerName', url='$url', title='$title', dateTaken='$dateTaken')"
    }

}

