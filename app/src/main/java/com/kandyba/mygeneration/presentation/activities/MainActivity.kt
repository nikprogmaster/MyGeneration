package com.kandyba.mygeneration.presentation.activities

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kandyba.mygeneration.App
import com.kandyba.mygeneration.R
import com.kandyba.mygeneration.models.presentation.calendar.Event
import com.kandyba.mygeneration.presentation.fragments.MainFragment
import com.kandyba.mygeneration.presentation.animation.AnimationHelper
import com.kandyba.mygeneration.presentation.animation.AnimationListener
import com.kandyba.mygeneration.presentation.fragments.BottomCalendarDialogFragment
import com.kandyba.mygeneration.presentation.viewmodel.AppViewModel
import com.kandyba.mygeneration.presentation.viewmodel.MainFragmentViewModel
import com.kandyba.mygeneration.presentation.viewmodel.factories.AppViewModelFactory
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var loadLayout: LinearLayout
    private lateinit var logo: ImageView
    private lateinit var divider: View
    private lateinit var navigation: BottomNavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var profileButton: ImageView

    private lateinit var animatorsList: List<Animator>
    private lateinit var animationListener: AnimationListener
    private lateinit var appViewModel: AppViewModel

    @Inject
    lateinit var animationHelper: AnimationHelper

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as App).appComponent.injectMainActivity(this)
        appViewModel = ViewModelProvider(this, viewModelFactory).get(AppViewModel::class.java)
        initViews()
        initObservers()
    }

    private fun initObservers() {
        appViewModel.init()
        appViewModel.showAnimationLiveData.observe(this, Observer { show ->
            animatorsList = animationHelper.setAnimation(logo, animationListener)
            animationHelper.showAnimation(animatorsList, show)
        })
        appViewModel.launchProfileLiveData.observe(this, Observer { launchProfile(Unit) })
        appViewModel.openMainFragmentLiveData.observe(this, Observer {
            openFragment(MainFragment.newInstance()) })
        }

    private fun initViews() {
        loadLayout = findViewById(R.id.loading)
        logo = findViewById(R.id.logo)
        divider = findViewById(R.id.divider)
        navigation = findViewById(R.id.navigation)
        appBarLayout = findViewById(R.id.app_bar_layout)
        profileButton = findViewById(R.id.profile)
        setListeners()

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    private fun navigateFragment(id: Int): Boolean {
        when (id) {
            R.id.main -> openFragment(MainFragment.newInstance())
            //R.id.diary -> Open Diary Fragment
            //R.id.dreams -> Open Dreams Fragment
            //R.id.messenger -> Open Messenger Fragment
            //R.id.ideas -> Open Ideas Fragment
        }
        return true
    }

    private fun setListeners() {
        profileButton.setOnClickListener {
            appViewModel.launchProfileActivity()
        }
        navigation.setOnNavigationItemSelectedListener { item: MenuItem ->
            navigateFragment(item.itemId)
        }
        navigation.selectedItemId = R.id.main
        animationListener = object : AnimationListener {
            override fun onAnimationEnd() {
                loadLayout.visibility = View.GONE
                divider.visibility = View.VISIBLE
                navigation.visibility = View.VISIBLE
                appBarLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.root, fragment)
            .commit()
    }

    private fun launchProfile(unit: Unit) {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    fun openBottomSheetFragment(event: List<Event>) {
        BottomCalendarDialogFragment.newInstance(event)
            .show(supportFragmentManager, null)
    }
}