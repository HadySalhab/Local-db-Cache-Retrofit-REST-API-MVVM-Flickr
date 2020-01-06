package com.android.flickphoto

import android.app.Application
import com.android.flickphoto.db.FlickrPhotoDatabase
import com.android.flickphoto.models.Photo
import com.android.flickphoto.repositories.PhotoRepository
import com.android.flickphoto.requests.ServiceGenerator
import com.android.flickphoto.ui.display.DisplayPhotoViewModel
import com.android.flickphoto.ui.li.PhotoListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application() {
    private val koinModule = module {

        single {
            FlickrPhotoDatabase.getInstance(get())
        }

        viewModel {
            val photoRepository:PhotoRepository = get()
            PhotoListViewModel(photoRepository,this@App)
        }

        viewModel {
            (photo:Photo)->DisplayPhotoViewModel(photo,this@App)
        }


        single {
            val db :FlickrPhotoDatabase = get()
            PhotoRepository(ServiceGenerator.flickrApi,db.flickrPhotoDao)
        }

    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(koinModule)
        }
    }
}