package com.kandyba.mygeneration

import android.app.Application
import com.kandyba.mygeneration.di.DaggerAppComponent

class App: Application() {

    val appComponent = DaggerAppComponent
        .factory()
        .create(this)

}
