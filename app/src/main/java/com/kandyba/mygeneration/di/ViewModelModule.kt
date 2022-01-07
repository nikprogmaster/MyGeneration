package com.kandyba.mygeneration.di

import com.kandyba.mygeneration.data.WallApiMapper
import com.kandyba.mygeneration.data.repository.WallRepositoryImpl
import com.kandyba.mygeneration.domain.WallInteractorImpl
import com.kandyba.mygeneration.models.presentation.user.UserConverter
import com.kandyba.mygeneration.presentation.viewmodel.AppViewModel
import com.kandyba.mygeneration.presentation.viewmodel.MainFragmentViewModel
import com.kandyba.mygeneration.presentation.viewmodel.ProfileViewModel
import com.kandyba.mygeneration.presentation.viewmodel.factories.AppViewModelFactory
import com.kandyba.mygeneration.presentation.viewmodel.factories.MainFragmentViewModelFactory
import com.kandyba.mygeneration.presentation.viewmodel.factories.ProfileViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule {

    @Provides
    fun provideAppViewModelFactory(): AppViewModelFactory {
        return AppViewModelFactory { AppViewModel() }
    }

    @Provides
    fun provideProfileViewModelFactory(userConverter: UserConverter): ProfileViewModelFactory {
        return ProfileViewModelFactory { ProfileViewModel(userConverter) }
    }

    @Provides
    fun provideMainFragmentViewModelFactory(apiMapper: WallApiMapper): MainFragmentViewModelFactory {
        return MainFragmentViewModelFactory {
            MainFragmentViewModel(
                WallInteractorImpl(
                    WallRepositoryImpl(apiMapper)
                )
            )
        }
    }
}