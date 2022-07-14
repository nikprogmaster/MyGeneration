package com.kandyba.mygeneration.di

import android.content.SharedPreferences
import com.kandyba.mygeneration.data.WallApiMapper
import com.kandyba.mygeneration.presentation.animation.AnimationHelper
import com.kandyba.mygeneration.presentation.animation.ProfileAnimation
import com.kandyba.mygeneration.presentation.utils.UserConverter
import com.kandyba.mygeneration.presentation.viewmodel.*

interface AppApi {

    fun getAnimationHelper(): AnimationHelper

    fun getProfileAnimation(): ProfileAnimation

    fun getUserConverter(): UserConverter

    fun getWallApiMapper(): WallApiMapper

    fun getSharedPreferences(): SharedPreferences

    fun getAppViewModelFactory(): ViewModelFactory<AppViewModel>

    fun getProfileViewModelFactory(): ViewModelFactory<ProfileViewModel>

    fun getMainFragmentViewModelFactory(): ViewModelFactory<MainFragmentViewModel>

    fun getCalendarDialogViewModelFactory(): ViewModelFactory<CalendarDialogViewModel>
}