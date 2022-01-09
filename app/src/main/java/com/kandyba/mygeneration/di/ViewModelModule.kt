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
import io.reactivex.subjects.ReplaySubject
import javax.inject.Named
import javax.inject.Singleton

@Module
class ViewModelModule {

    @Provides
    fun provideAppViewModelFactory(
        @Named("EventsSubject") eventsSubject: ReplaySubject<Unit>,
        @Named("VkPostsSubject") vkPostsSubject: ReplaySubject<Unit>
    ): AppViewModelFactory {
        return AppViewModelFactory { AppViewModel(vkPostsSubject, eventsSubject) }
    }

    @Provides
    fun provideProfileViewModelFactory(userConverter: UserConverter): ProfileViewModelFactory {
        return ProfileViewModelFactory { ProfileViewModel(userConverter) }
    }

    @Provides
    @Named("EventsSubject")
    fun provideEventsSubject(): ReplaySubject<Unit> {
        return eventsSubject
    }

    @Provides
    @Named("VkPostsSubject")
    fun provideVkPostsSubject(): ReplaySubject<Unit> {
        return vkPostsSubject
    }

    @Provides
    fun provideMainFragmentViewModelFactory(
        apiMapper: WallApiMapper,
        @Named("EventsSubject") eventsSubject: ReplaySubject<Unit>,
        @Named("VkPostsSubject") vkPostsSubject: ReplaySubject<Unit>
    ): MainFragmentViewModelFactory {
        return MainFragmentViewModelFactory {
            MainFragmentViewModel(
                WallInteractorImpl(WallRepositoryImpl(apiMapper)),
                eventsSubject,
                vkPostsSubject
            )
        }
    }

    companion object {
        private val eventsSubject: ReplaySubject<Unit> = ReplaySubject.create()
        private val vkPostsSubject: ReplaySubject<Unit> = ReplaySubject.create()
    }
}