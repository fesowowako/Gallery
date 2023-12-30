package app.android.gallery

import android.app.Application

class GalleryApplication : Application() {
    lateinit var container: ApplicationContainer

    override fun onCreate() {
        container = DefaultApplicationContainer(contentResolver, this)
        super.onCreate()
    }
}
