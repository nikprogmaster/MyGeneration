package com.kandyba.mygeneration.di

import com.kandyba.mygeneration.data.EventsDatadaseSource
import com.kandyba.mygeneration.data.RegionFirestoreSource
import com.kandyba.mygeneration.data.UserDatabaseSource
import com.kandyba.mygeneration.data.WallApiMapper
import com.kandyba.mygeneration.data.repository.EventsRepositoryImpl
import com.kandyba.mygeneration.data.repository.RegionsRepositoryImpl
import com.kandyba.mygeneration.data.repository.UserRepositoryImpl
import com.kandyba.mygeneration.data.repository.WallRepositoryImpl
import com.kandyba.mygeneration.models.presentation.user.UserConverter
import com.kandyba.mygeneration.presentation.viewmodel.*
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule {

    @Provides
    fun provideAppViewModelFactory(): ViewModelFactory<AppViewModel> =
        ViewModelFactory { AppViewModel() }

    @Provides
    fun provideProfileViewModelFactory(userConverter: UserConverter): ViewModelFactory<ProfileViewModel> =
        ViewModelFactory {
            ProfileViewModel(
                userConverter,
                UserRepositoryImpl(UserDatabaseSource()),
                RegionsRepositoryImpl(RegionFirestoreSource())
            )
        }


    @Provides
    fun provideMainFragmentViewModelFactory(
        apiMapper: WallApiMapper
    ): ViewModelFactory<MainFragmentViewModel> =
        ViewModelFactory {
            MainFragmentViewModel(
                WallRepositoryImpl(apiMapper),
                EventsRepositoryImpl(EventsDatadaseSource()),
                RegionsRepositoryImpl(RegionFirestoreSource())
            )
        }

    @Provides
    fun provideCalendarDialogViewModelFactory(): ViewModelFactory<CalendarDialogViewModel> =
        ViewModelFactory { CalendarDialogViewModel() }
}