package com.android.flickphoto.util

import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.android.flickphoto.requests.responses.ApiResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// CacheObject: Type for the Resource data. (db cache)
// RequestObject: Type for the API response. (network request)
abstract class NetworkBoundResource<CacheObject, RequestObject>(private val coroutineScope: CoroutineScope) {
    companion object {
        private const val TAG = "NetworkBoundResource"
    }

    private val results = MediatorLiveData<Resource<CacheObject>>()

    init {
        Log.d(TAG, "NetworkBoundResource initialized: ")
        results.value = Resource.Loading(null)
        @Suppress("LeakingThis")
        val dbSource = loadFromDb()
        results.addSource(dbSource, Observer { cachedData ->
            Log.d(TAG,"BEFORE FETCH CONDITION")
            results.removeSource(dbSource)
            if (shouldFetch(cachedData)) {
                Log.d(TAG, "shouldfetch:true ")

                    fetchFromNetwork(dbSource)

            } else {
                Log.d(TAG, "shouldfetch:false ")
                results.addSource(dbSource, Observer { newCachedData ->
                    setValue(Resource.Success(newCachedData))
                })
            }

        })
    }

  fun fetchFromNetwork(dbSource: LiveData<CacheObject>) {
          val apiResponse=createCall()
          Log.d(TAG, "fetchFromNetwork:called. ")
          Log.d(TAG, "fetchFromNetwork: ${apiResponse.value}")
          results.addSource(dbSource, Observer { newData ->
              setValue(Resource.Loading(newData))
          })

          results.addSource(apiResponse, Observer { response ->
              results.removeSource(dbSource)
              results.removeSource(apiResponse)
              when (response) {
                  is ApiResponse.ApiSuccessResponse -> {
                      Log.d(TAG, "fetchFromNetwork: ApiSuccessResponse")
                      coroutineScope.launch {
                          withContext(Dispatchers.IO) {

                              saveCallResult(processResponse(response))
                          }
                          results.addSource(loadFromDb(), Observer { newData ->
                              setValue(Resource.Success(newData))
                          })
                      }

                  }
                  is ApiResponse.ApiEmptyResponse -> {
                      Log.d(TAG, "fetchFromNetwork:  ApiEmptyResource")
                      results.addSource(loadFromDb(), Observer { newData ->
                          setValue(Resource.Success(newData))
                      })

                  }
                  is ApiResponse.ApiErrorResponse -> {
                      onFetchFailed()
                      Log.d(TAG, "fetchFromNetwork: ApiErrorResponse")
                      results.addSource(dbSource, Observer { newData ->
                          setValue(Resource.Error(response.errorMessage, newData))

                      })

                  }
              }

          })


    }

    @WorkerThread
    protected open fun processResponse(response: ApiResponse.ApiSuccessResponse<RequestObject>) =
        response.body

    private fun setValue(newValue: Resource<CacheObject>) {
        if (results.value != newValue) {
            results.value = newValue
        }
    }

    // Called to save the result of the API response into the database
    @WorkerThread
    protected abstract fun saveCallResult(item: RequestObject)

    // Called with the data in the database to decide whether to fetch
    // potentially updated data from the network.
    @MainThread
    protected abstract fun shouldFetch(data: CacheObject?): Boolean

    // Called to get the cached data from the database.
    @MainThread
    protected abstract fun loadFromDb(): LiveData<CacheObject>

    // Called to create the API call.
    @MainThread
    protected  abstract  fun createCall(): LiveData<ApiResponse<RequestObject>>

    // Called when the fetch fails. The child class may want to reset components
    // like rate limiter.
    protected abstract fun onFetchFailed()

    // Returns a LiveData object that represents the resource that's implemented
    // in the base class.
    fun asLiveData()= results as LiveData<Resource<CacheObject>>
}
