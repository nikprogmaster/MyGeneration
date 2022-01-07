package com.kandyba.mygeneration.di

import android.content.Context
import com.kandyba.mygeneration.presentation.activities.MainActivity
import com.kandyba.mygeneration.presentation.activities.ProfileActivity
import com.kandyba.mygeneration.presentation.fragments.MainFragment
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
interface AppComponent {

    fun injectMainActivity(mainActivity: MainActivity)

    fun injectProfileActivity(profileActivity: ProfileActivity)

    fun injectMainFragment(mainFragment: MainFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): AppComponent
    }
}