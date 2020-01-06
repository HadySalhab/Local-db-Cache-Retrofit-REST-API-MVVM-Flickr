package com.android.flickphoto.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.flickphoto.models.Photo

@Dao
interface FlickrPhotoDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhotos(vararg photos: Photo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhoto(photo: Photo)

    @Update
    fun update(photo: Photo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertListPhotos(photos:List<Photo>)


    @Query("SELECT * FROM photos WHERE title LIKE '%' || :query || '%'")
    fun searchPhotos(query:String): LiveData<List<Photo>>

    @Query("SELECT * FROM photos")
    fun getPhotos(): LiveData<List<Photo>>







}