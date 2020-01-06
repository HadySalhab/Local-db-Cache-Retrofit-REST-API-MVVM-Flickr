package com.android.flickphoto.repositories

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import com.android.flickphoto.db.FlickrPhotoDao
import com.android.flickphoto.models.Photo
import com.android.flickphoto.requests.FlickrApi
import com.android.flickphoto.requests.responses.ApiResponse
import com.android.flickphoto.requests.responses.FlickrResponse
import com.android.flickphoto.util.NetworkBoundResource
import com.android.flickphoto.util.RateLimiter
import com.android.flickphoto.util.Resource
import kotlinx.coroutines.CoroutineScope
import java.util.concurrent.TimeUnit


class PhotoRepository (
    private val flickrApi: FlickrApi,
    private val flickrPhotoDao: FlickrPhotoDao
) {
    private val photoListRateLimit = RateLimiter<String>(10, TimeUnit.MINUTES)
    companion object {
        private const val TAG = "PhotoRepository"
    }

    fun getPhotos(query:String="",scope: CoroutineScope):LiveData<Resource<List<Photo>>> = if(TextUtils.isEmpty(query)) {
        searchPhotos(coroutineScope = scope)
    }else{
        Log.d(TAG, "getPhotos: searching")
        searchPhotos(query,scope)
    }


    private fun searchPhotos(query:String,coroutineScope: CoroutineScope):LiveData<Resource<List<Photo>>>{
        return object: NetworkBoundResource<List<Photo>, FlickrResponse>(coroutineScope){
            override fun saveCallResult(item: FlickrResponse) {
                val photos = item.photos
                Log.d(TAG, "saveCallResult: ${photos}")
                val listOfPhotos = photos.listOfPhoto
                Log.d(TAG, "saveCallResult: ${listOfPhotos}")
                flickrPhotoDao.insertListPhotos(listOfPhotos)
            }

            override fun shouldFetch(data: List<Photo>?): Boolean {
                Log.d(TAG, "shouldFetch: $query")
                val shouldFetch = data ==null || data.isEmpty() || photoListRateLimit.shouldFetch(query)
                Log.d(TAG, "shouldFetch: ${shouldFetch}")
                return  shouldFetch
            } 

            override fun loadFromDb(): LiveData<List<Photo>> = flickrPhotoDao.searchPhotos(query)

            override  fun createCall(): LiveData<ApiResponse<FlickrResponse>> = flickrApi.searchPhotos(text = query)

            override fun onFetchFailed() {
                photoListRateLimit.reset(query)
            }

        }.asLiveData()
    }




    private fun searchPhotos(coroutineScope: CoroutineScope):LiveData<Resource<List<Photo>>>{
        return object: NetworkBoundResource<List<Photo>, FlickrResponse>(coroutineScope){
            override fun saveCallResult(item: FlickrResponse) {
                val photos = item.photos
                Log.d(TAG, "saveCallResult: ${photos}")
                val listOfPhotos = photos.listOfPhoto
                Log.d(TAG, "saveCallResult: ${listOfPhotos}")
                flickrPhotoDao.insertListPhotos(listOfPhotos)
            }

            override fun shouldFetch(data: List<Photo>?): Boolean {
                Log.d(TAG, "shouldFetch: \"\" ")
                val shouldFetch = data ==null || data.isEmpty() || photoListRateLimit.shouldFetch("")
                Log.d(TAG, "shouldFetch: ${shouldFetch}")
                return  shouldFetch
            }
            override fun loadFromDb(): LiveData<List<Photo>> = flickrPhotoDao.getPhotos()

            override  fun createCall(): LiveData<ApiResponse<FlickrResponse>> = flickrApi.getRecentPhotos()

            override fun onFetchFailed() {
                photoListRateLimit.reset("")
            }

        }.asLiveData()
    }

}