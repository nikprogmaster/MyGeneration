package com.kandyba.mygeneration.presentation.activities


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.kandyba.mygeneration.App
import com.kandyba.mygeneration.R
import com.kandyba.mygeneration.models.EMPTY_STRING
import com.kandyba.mygeneration.models.data.RegionModel
import com.kandyba.mygeneration.models.presentation.FileUtils
import com.kandyba.mygeneration.models.presentation.user.AuthType
import com.kandyba.mygeneration.models.presentation.user.User
import com.kandyba.mygeneration.models.presentation.user.UserField
import com.kandyba.mygeneration.models.presentation.user.toRegionCode
import com.kandyba.mygeneration.presentation.animation.AnimationListener
import com.kandyba.mygeneration.presentation.animation.ProfileAnimation
import com.kandyba.mygeneration.presentation.animation.show
import com.kandyba.mygeneration.presentation.viewmodel.ProfileViewModel
import java.io.File


class ProfileActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var avatar: ImageView
    private lateinit var editButton: ImageView
    private lateinit var exitButton: ImageView
    private lateinit var fio: TextInputEditText
    private lateinit var login: TextInputEditText
    private lateinit var accountType: TextInputEditText
    private lateinit var phone: TextInputEditText
    private lateinit var birthday: TextInputEditText
    private lateinit var city: TextInputEditText
    private lateinit var region: TextInputEditText
    private lateinit var signInButton: Button
    private lateinit var loggedUserLayout: NestedScrollView
    private lateinit var unloggedUserLayout: LinearLayout
    private lateinit var fioLayout: TextInputLayout
    private lateinit var saveButton: Button
    private lateinit var logged: ConstraintLayout
    private lateinit var progressLayout: LinearLayout

    private lateinit var viewModel: ProfileViewModel
    private lateinit var settings: SharedPreferences
    private lateinit var profileAnimation: ProfileAnimation

    private var regionMenu: PopupMenu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_activity)
        resolveDependencies()
        viewModel.init(settings)
        initViews()
        initObservers()
        initListeners()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                val providerType = response?.providerType ?: EMPTY_STRING
                user?.let {
                    viewModel.successfullySigned(it, providerType)
                }

            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
        if (requestCode == PICK_IMAGE_AVATAR) {
            val selectedImage: Uri? = data?.data
            avatar.setImageURI(selectedImage)

            val file = FileUtils.getPath(this, selectedImage)
            Log.i("selectedImage", data?.dataString.toString())
            viewModel.changeUserAvatar(
                File(file),
                settings.getString(UserField.ID.preferencesKey, null)
            )
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_PERMISSION_READ_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    launchImageChooser()
                } else {
                    Log.i(TAG, "Пользователь не дал разрешение!")
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun resolveDependencies() {
        with((application as App).appComponent) {
            viewModel = ViewModelProvider(this@ProfileActivity, getProfileViewModelFactory())
                .get(ProfileViewModel::class.java)
            settings = getSharedPreferences()
            profileAnimation = getProfileAnimation()
        }
    }

    private fun initViews() {
        avatar = findViewById(R.id.avatar)
        editButton = findViewById(R.id.edit)
        exitButton = findViewById(R.id.exit)
        fio = findViewById(R.id.user_name)
        login = findViewById(R.id.login)
        accountType = findViewById(R.id.account_type)
        phone = findViewById(R.id.phone)
        birthday = findViewById(R.id.birthday)
        city = findViewById(R.id.city)
        region = findViewById(R.id.region)
        signInButton = findViewById(R.id.sign_in)
        loggedUserLayout = findViewById(R.id.logged_user_layout)
        unloggedUserLayout = findViewById(R.id.unlogged_user_layout)
        toolbar = findViewById(R.id.profile_toolbar)
        fioLayout = findViewById(R.id.fio)
        saveButton = findViewById(R.id.save_changes)
        logged = findViewById(R.id.logged)
        progressLayout = findViewById(R.id.loading)
        setSupportActionBar(toolbar)
    }

    private fun initListeners() {
        signInButton.setOnClickListener { viewModel.signInUser() }
        val startListener = object : AnimationListener {
            override fun onAnimationEnd() {
                makeFocusable(true)
            }
        }
        editButton.setOnClickListener {
            profileAnimation.setStartAnimation(editButton, avatar, loggedUserLayout, startListener)
                .show()
            saveButton.visibility = View.VISIBLE
        }

        fio.setOnFocusChangeListener { _, _ ->
            fioLayout.isHintEnabled = false
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .showSoftInput(fio, InputMethodManager.SHOW_FORCED)
        }
        saveButton.setOnClickListener {
            val finishListener = object : AnimationListener {
                override fun onAnimationEnd() {
                    makeFocusable(false)
                }
            }
            profileAnimation.setReverseAnimation(
                editButton,
                avatar,
                loggedUserLayout,
                finishListener
            ).show()
            saveButton.visibility = View.GONE
            viewModel.changeUserInfo()
        }
        exitButton.setOnClickListener {
            showProgressBar(true)
            signOut()
        }
        avatar.setOnClickListener {
            requestAccessToPhotoPermission()
        }
        region.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(region.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                regionMenu?.show()
                region.clearFocus()
            }
        }
        setTextWatchers()
    }

    private fun setMenuToRegion(regions: List<RegionModel>) {
        regionMenu = PopupMenu(this, region)
        regionMenu?.let {
            for (r in regions) {
                it.menu.add(r.name)
            }
            it.setOnMenuItemClickListener { item ->
                region.setText(item.title)
                true
            }
        }
    }

    private fun launchImageChooser() {
        val intent = Intent()
        with(intent) {
            type = IMAGE_INTENT_TYPE
            action = Intent.ACTION_PICK
        }
        startActivityForResult(
            Intent.createChooser(intent, getString(R.string.image_chooser_title)),
            PICK_IMAGE_AVATAR
        )
    }

    private fun setTextWatchers() {
        city.addTextChangedListener { viewModel.addChangedField(UserField.CITY, it.toString()) }
        fio.addTextChangedListener { viewModel.addChangedField(UserField.NAME, it.toString()) }
        login.addTextChangedListener { viewModel.addChangedField(UserField.EMAIL, it.toString()) }
        phone.addTextChangedListener { viewModel.addChangedField(UserField.PHONE, it.toString()) }
        accountType.addTextChangedListener {
            viewModel.addChangedField(UserField.ACCOUNT_TYPE, it.toString())
        }
        birthday.addTextChangedListener {
            viewModel.addChangedField(UserField.BIRTHDAY, it.toString())
        }
        region.addTextChangedListener {
            viewModel.addChangedField(UserField.REGION, it.toString().toRegionCode())
        }
    }

    private fun showProgressBar(show: Boolean) {
        progressLayout.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun initObservers() {
        viewModel.showLoggedUserLayoutLiveData.observe(this) { show ->
            showLoggedUserLayout(show)
        }
        viewModel.signInUserLiveData.observe(this) { createSignInIntent() }
        viewModel.userModelInfoLiveData.observe(this) { user -> setFields(user) }
        viewModel.sharedPreferencesUserInfoLiveData.observe(this) { fields ->
            updateSettings(fields)
        }
        viewModel.showReservedUserInfoLiveData.observe(this) { setReservedFields() }
        viewModel.showProgressBarLiveData.observe(this) { show -> showProgressBar(show) }
        viewModel.regionsLiveData.observe(this) { setMenuToRegion(it) }
    }

    private fun createSignInIntent() {
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder()
                .setRequireName(true)
                .build(),
            AuthUI.IdpConfig.PhoneBuilder()
                .setWhitelistedCountries(mutableListOf("ru"))
                .build(),
            AuthUI.IdpConfig.GoogleBuilder()
                .build()
        )

        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.logo_white)
                .setTheme(R.style.FirebaseLoginTheme)
                .setTosAndPrivacyPolicyUrls(
                    "https://example.com/terms.html",
                    "https://example.com/privacy.html"
                )
                .build(),
            RC_SIGN_IN
        )
    }

    private fun requestAccessToPhotoPermission() {
        val permissionStatus =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            launchImageChooser()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE_PERMISSION_READ_STORAGE
            )
        }
    }

    private fun signOut() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                showProgressBar(false)
                showLoggedUserLayout(false)
                clearSharedPreferences()
            }
    }

    private fun delete() {
        AuthUI.getInstance()
            .delete(this)
            .addOnCompleteListener {
                // ...
            }
    }

    private fun makeFocusable(flag: Boolean) {
        fio.isEnabled = flag
        login.isEnabled = flag
        phone.isEnabled = flag
        birthday.isEnabled = flag
        city.isEnabled = flag
        region.isEnabled = flag
        if (flag) {
            when (settings.getString(UserField.AUTH_TYPE.preferencesKey, EMPTY_STRING)
                ?: EMPTY_STRING) {
                AuthType.GOOGLE.title, AuthType.EMAIL.title -> login.isEnabled = false
                AuthType.PHONE.title -> phone.isEnabled = false
            }
        }
    }

    private fun updateSettings(fields: Map<UserField, String?>) {
        val editor = settings.edit()
        for (key in fields.keys) {
            if (settings.getString(key.preferencesKey, EMPTY_STRING) != fields[key]) {
                editor.putString(key.preferencesKey, fields[key])
            }
        }
        editor.apply()
    }

    private fun setFields(userModel: User) {
        if (userModel.name != null) {
            fioLayout.isHintEnabled = false
        }
        fio.setText(userModel.name)
        login.setText(userModel.email)
        phone.setText(userModel.phoneNumber)
        birthday.setText(userModel.birthday)
        accountType.setText(userModel.accountType?.title)
        city.setText(userModel.city)
        region.setText(userModel.region?.regionName)

        //TODO: Нужно понять, как по uri фотку сетать!!!
        //avatar.setImageResource(user.photoUrl)
    }

    private fun clearSharedPreferences() {
        settings.edit().clear().apply()
    }

    private fun setReservedFields() {
        if (settings.getString(UserField.NAME.preferencesKey, EMPTY_STRING) != EMPTY_STRING) {
            fioLayout.isHintEnabled = false
        }
        fio.setText(settings.getString(UserField.NAME.preferencesKey, EMPTY_STRING))
        login.setText(settings.getString(UserField.EMAIL.preferencesKey, EMPTY_STRING))
        phone.setText(settings.getString(UserField.PHONE.preferencesKey, EMPTY_STRING))
        birthday.setText(settings.getString(UserField.BIRTHDAY.preferencesKey, EMPTY_STRING))
        city.setText(settings.getString(UserField.CITY.preferencesKey, EMPTY_STRING))
        accountType.setText(settings.getString(UserField.ACCOUNT_TYPE.preferencesKey, EMPTY_STRING))
        region.setText(settings.getString(UserField.REGION.preferencesKey, EMPTY_STRING))
    }

    private fun showLoggedUserLayout(flag: Boolean) {
        if (flag) {
            logged.visibility = View.VISIBLE
            unloggedUserLayout.visibility = View.GONE
        } else {
            logged.visibility = View.GONE
            unloggedUserLayout.visibility = View.VISIBLE
            clearSharedPreferences()
        }
    }

    companion object {
        private const val TAG = "ProfileActivity"
        private const val IMAGE_INTENT_TYPE = "image/*"

        private const val RC_SIGN_IN = 123
        private const val PICK_IMAGE_AVATAR = 1
        private const val REQUEST_CODE_PERMISSION_READ_STORAGE = 2
    }
}