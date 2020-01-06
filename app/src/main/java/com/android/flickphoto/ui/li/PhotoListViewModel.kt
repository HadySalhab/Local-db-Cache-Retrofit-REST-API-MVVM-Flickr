package com.android.flickphoto.ui.li

import android.app.Application
import android.content.Context
import android.text.TextUtils
import androidx.lifecycle.*
import com.android.flickphoto.models.Photo
import com.android.flickphoto.repositories.PhotoRepository
import com.android.flickphoto.util.PreferencesStorage
import com.android.flickphoto.util.Resource
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

enum class FlickrApiStatus { LOADING, ERROR, DONE }

class PhotoListViewModel(private val photoRepository: PhotoRepository,private val app: Application) : AndroidViewModel(app) {



    private val _photos = MutableLiveData<List<Photo>>()
    val photos: LiveData<List<Photo>>
        get() = _photos


    private val _query = MutableLiveData<String>(PreferencesStorage.getStoredQuery(app))
    val query:LiveData<String>
        get() = _query

    var isUserSearching = false





    // http request response status
    val status: LiveData<FlickrApiStatus> = Transformations.map(photoRepository.result){ result->
        when(result){
            is Resource.Loading->{
                FlickrApiStatus.LOADING
            }
            is Resource.Success->{
                _photos.value = result.data
                FlickrApiStatus.DONE
            }
            is Resource.Error->{
                FlickrApiStatus.ERROR
            }
        }
    }






    fun getFlickrPhotos(query:String) {
        if(TextUtils.isEmpty(query)){
            isUserSearching  = false
        viewModelScope.launch {
             photoRepository.fetchPhotos()

        }
        }else{
            isUserSearching = true
            viewModelScope.launch {
                photoRepository.searchPhotos(query)
            }
        }

    }



    fun changeQueryValue(query:String){
        PreferencesStorage.setStoredQuery(app,query)
        _query.value = query
    }


    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }


}