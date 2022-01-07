package com.kandyba.mygeneration.di

import com.kandyba.mygeneration.presentation.animation.AnimationHelper
import com.kandyba.mygeneration.presentation.animation.ProfileAnimation
import dagger.Module
import dagger.Provides

@Module
class AnimationModule {

    @Provides
    fun provideAnimationHelper() =
        AnimationHelper()

    @Provides
    fun provideProfileAnimation() =
        ProfileAnimation()
}