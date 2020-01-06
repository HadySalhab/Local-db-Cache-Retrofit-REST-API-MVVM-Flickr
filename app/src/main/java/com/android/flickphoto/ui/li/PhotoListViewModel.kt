package com.android.flickphoto.ui.li

import android.app.Application
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.*
import com.android.flickphoto.models.Photo
import com.android.flickphoto.repositories.PhotoRepository
import com.android.flickphoto.util.PreferencesStorage
import com.android.flickphoto.util.Resource
import kotlinx.coroutines.cancel


class PhotoListViewModel(private val photoRepository: PhotoRepository,private val app: Application) : AndroidViewModel(app) {
    companion object {
        private const val TAG = "PhotoListViewModel"
    }
    init {
        Log.d(TAG, "photoListViewModel initialized: ")
    }






    private val _query = MutableLiveData<String>(PreferencesStorage.getStoredQuery(app))
    val query:LiveData<String>
        get() = _query

    var isUserSearching = false


    // http request response status
    val results: LiveData<Resource<List<Photo>>> = Transformations.switchMap(_query){query->
        isUserSearching = !TextUtils.isEmpty(query)
        photoRepository.getPhotos(query,viewModelScope)
    }


    fun refresh(query:String) {
        _query.value = query
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