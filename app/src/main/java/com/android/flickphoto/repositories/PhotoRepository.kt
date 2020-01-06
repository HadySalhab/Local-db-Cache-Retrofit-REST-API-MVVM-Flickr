package com.android.flickphoto.repositories

import androidx.lifecycle.LiveData
import com.android.flickphoto.models.Photo
import com.android.flickphoto.requests.FlickrApiClient
import com.android.flickphoto.util.Resource


class PhotoRepository (private val flickrApiClient: FlickrApiClient) {

    val result :LiveData<Resource<List<Photo>>> = flickrApiClient.result


    suspend fun fetchPhotos(){
      flickrApiClient.getPhotos()
    }

    suspend fun searchPhotos(query:String){
        flickrApiClient.searchPhotos(query)
    }

}