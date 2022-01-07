package com.kandyba.mygeneration.di

import android.content.Context
import android.content.SharedPreferences
import com.kandyba.mygeneration.models.SHARED_PREFERENCES_NAME
import dagger.Module
import dagger.Provides

@Module
class SharedPreferencesModule {

    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

}