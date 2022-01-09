package com.kandyba.mygeneration.di

import com.kandyba.mygeneration.models.presentation.user.UserConverter
import dagger.Module
import dagger.Provides

@Module
class ConvertersModule {

    @Provides
    fun provideUserConverter() = UserConverter()
}