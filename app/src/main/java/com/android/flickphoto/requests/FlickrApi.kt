package com.android.flickphoto.requests

import androidx.lifecycle.LiveData
import com.android.flickphoto.requests.responses.ApiResponse
import com.android.flickphoto.requests.responses.FlickrResponse
import com.android.flickphoto.requests.responses.PhotoResponse
import com.android.flickphoto.util.API_KEY
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApi {
//search and getrecent requests return the same type of responses



    @GET("services/rest")
    fun searchPhotos(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("method") method: String = "flickr.photos.search",
        @Query("text") text: String="",
        @Query("nojsoncallback") noJsonCallBack: String = "1",
        @Query("format") responseFormat: String = "json",
        @Query("extras") extras: String = "url_s,date_taken,owner_name"
    ): LiveData<ApiResponse<FlickrResponse>> //setting retrofit to return Deferred which represents
    //a Job with a result

    //Call<> on the other hand represents a web request

    @GET("services/rest")
    fun getRecentPhotos(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("method") method: String = "flickr.photos.getRecent",
        @Query("nojsoncallback") noJsonCallBack: String = "1",
        @Query("format") responseFormat: String = "json",
        @Query("extras") extras: String = "url_s,date_taken,owner_name",
        @Query("page") page:Int=1
    ): LiveData<ApiResponse<FlickrResponse>> //setting retrofit to return Deferred which represents
    //a Job with a result

    //Call<> on the other hand represents a web request

}