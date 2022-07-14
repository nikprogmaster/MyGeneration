package com.kandyba.mygeneration.di

import com.kandyba.mygeneration.presentation.utils.UserConverter
import dagger.Module
import dagger.Provides

@Module
class ConvertersModule {

    @Provides
    fun provideUserConverter() = UserConverter()
}