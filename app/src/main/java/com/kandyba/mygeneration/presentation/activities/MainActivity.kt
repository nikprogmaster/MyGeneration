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
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kandyba.mygeneration.App
import com.kandyba.mygeneration.R
import com.kandyba.mygeneration.presentation.animation.AnimationHelper
import com.kandyba.mygeneration.presentation.animation.AnimationListener
import com.kandyba.mygeneration.presentation.fragments.MainFragment
import com.kandyba.mygeneration.presentation.viewmodel.AppViewModel
import com.kandyba.mygeneration.presentation.viewmodel.MainFragmentViewModel

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
    private lateinit var mainFragmentViewModel: MainFragmentViewModel
    private lateinit var animationHelper: AnimationHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        resolveDependencies()
        initViews()
        initObservers()
    }

    private fun resolveDependencies() {
        with((application as App).appComponent) {
            animationHelper = getAnimationHelper()
            appViewModel = ViewModelProvider(this@MainActivity, getAppViewModelFactory())
                .get(AppViewModel::class.java)
            mainFragmentViewModel =
                ViewModelProvider(this@MainActivity, getMainFragmentViewModelFactory())
                    .get(MainFragmentViewModel::class.java)
        }
    }

    private fun initObservers() {
        appViewModel.launchProfileLiveData.observe(this, ::launchProfile)
        appViewModel.openMainFragmentLiveData.observe(this, Observer {
            openFragment(MainFragment.newInstance())
        })
        mainFragmentViewModel.allDataLoaded.observe(this) {
            animatorsList = animationHelper.setAnimation(logo, animationListener)
            animationHelper.showAnimation(animatorsList, false)
        }
        mainFragmentViewModel.openBottomSheet.observe(this, ::openBottomSheetFragment)
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

        animatorsList = animationHelper.setAnimation(logo, animationListener)
        animationHelper.showAnimation(animatorsList, true)
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

    private fun openBottomSheetFragment(fragment: DialogFragment) {
        fragment.show(supportFragmentManager, null)
    }
}