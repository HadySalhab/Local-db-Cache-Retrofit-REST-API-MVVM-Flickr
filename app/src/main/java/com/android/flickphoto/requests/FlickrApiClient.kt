package com.android.flickphoto.requests

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.flickphoto.models.Photo
import com.android.flickphoto.requests.responses.FlickrResponse
import com.android.flickphoto.util.NETWORK_TIMEOUT
import com.android.flickphoto.util.Resource
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import retrofit2.Response
import java.io.IOException


private const val TAG = "FlickrApiClient"

class FlickrApiClient {

    //creating an instance of the flickrAPI
    val flickrApi = ServiceGenerator.flickrApi

    private val _result = MutableLiveData<Resource<List<Photo>>>()
    val result :LiveData<Resource<List<Photo>>>
    get() = _result





    suspend fun getPhotos(page:Int=1){
         fetchPhotoMetaData(flickrApi.getRecentPhotos(page = page))
    }

    suspend fun searchPhotos(query:String){
         fetchPhotoMetaData(flickrApi.searchPhotos(text=query))
    }


    suspend fun fetchPhotoMetaData(flickrRequest:Deferred<Response<FlickrResponse?>>) {
        _result.value = Resource.Loading()


        try {
            withTimeout(NETWORK_TIMEOUT) {
                try {
                    //getting the response from the http request
                    val response = flickrRequest.await()

                    //if response was successful and OK (CODE=200)
                    if (response.code() == 200) {
                        val flickrResponse = response.body()
                        val photoResponse = flickrResponse?.photos
                        val listOfPhotos = photoResponse?.listOfPhoto ?: mutableListOf()
                        val listOfPhotosWithUrl = listOfPhotos.filterNot {
                            it.url.isNullOrBlank()
                        }
                        _result.value = Resource.Success(listOfPhotosWithUrl)

                    } else {
                        //if response was successful but not OK(CODE!=200)
                        _result.value= Resource.Error("Response successful but code different than 200")
                        try {
                            Log.d(TAG, "onResponse ${response.errorBody()}")
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
                // catching the exception if the http request is failed
                catch (e: Exception) {
                    _result.value= Resource.Error("Failed to fetch photos")

                }
            }

        }

        //catching the timeoutCancellation exception to inform the user
        catch (e:TimeoutCancellationException) {
            _result.value= Resource.Error("Network timed out")
        }


    }


}