package com.kandyba.mygeneration.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [
        ViewModelModule::class,
        AnimationModule::class,
        SharedPreferencesModule::class,
        ConvertersModule::class,
        NetworkModule::class
    ]
)
interface AppComponent : AppApi {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}