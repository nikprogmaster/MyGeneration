package com.kandyba.mygeneration.di

import androidx.collection.LruCache
import com.kandyba.mygeneration.data.WallApiMapper
import com.kandyba.mygeneration.data.repository.*
import com.kandyba.mygeneration.data.source.EventsFirestoreSource
import com.kandyba.mygeneration.data.source.RegionsFirestoreSource
import com.kandyba.mygeneration.data.source.UserDatabaseSource
import com.kandyba.mygeneration.models.presentation.calendar.Event
import com.kandyba.mygeneration.presentation.utils.UserConverter
import com.kandyba.mygeneration.presentation.viewmodel.*
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule {

    private var eventsRepository: EventsRepository? = null

    @Provides
    fun provideAppViewModelFactory(): ViewModelFactory<AppViewModel> =
        ViewModelFactory { AppViewModel() }

    @Provides
    fun provideProfileViewModelFactory(
        userConverter: UserConverter,
        eventsRepository: EventsRepository
    ): ViewModelFactory<ProfileViewModel> =
        ViewModelFactory {
            ProfileViewModel(
                userConverter,
                UserRepositoryImpl(UserDatabaseSource()),
                RegionsRepositoryImpl(RegionsFirestoreSource()),
                eventsRepository
            )
        }

    @Provides
    fun provideMainFragmentViewModelFactory(
        apiMapper: WallApiMapper,
        eventsRepository: EventsRepository
    ): ViewModelFactory<MainFragmentViewModel> =
        ViewModelFactory {
            MainFragmentViewModel(
                WallRepositoryImpl(apiMapper),
                eventsRepository,
                RegionsRepositoryImpl(RegionsFirestoreSource())
            )
        }

    @Provides
    fun provideCalendarDialogViewModelFactory(): ViewModelFactory<CalendarDialogViewModel> =
        ViewModelFactory { CalendarDialogViewModel() }

    @Provides
    fun provideEventsRepository(): EventsRepository {
        if (eventsRepository == null) {
            eventsRepository = EventsRepositoryImpl(
                EventsFirestoreSource(),
                LruCache<String, List<Event>>((Runtime.getRuntime().maxMemory() / 1024).toInt())
            )
        }
        return eventsRepository as EventsRepository
    }


}