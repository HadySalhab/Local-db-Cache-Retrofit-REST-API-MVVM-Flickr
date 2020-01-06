package com.android.flickphoto.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.flickphoto.models.Photo

@Database(entities = [Photo::class],version = 1,exportSchema = false)
abstract class FlickrPhotoDatabase : RoomDatabase() {
    abstract  val flickrPhotoDao:FlickrPhotoDao

    companion object{
        val DATABASE_NAME = "flickrPhotos.db"

        fun getInstance(context: Context):FlickrPhotoDatabase {
            synchronized(this) {
                return Room.databaseBuilder(
                    context.applicationContext, FlickrPhotoDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
        }
    }
}
