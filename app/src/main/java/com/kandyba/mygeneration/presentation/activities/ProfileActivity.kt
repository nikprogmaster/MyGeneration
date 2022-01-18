package com.kandyba.mygeneration.presentation.activities


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.kandyba.mygeneration.App
import com.kandyba.mygeneration.R
import com.kandyba.mygeneration.models.EMPTY_STRING
import com.kandyba.mygeneration.models.data.AuthType
import com.kandyba.mygeneration.models.data.User
import com.kandyba.mygeneration.models.presentation.FileUtils
import com.kandyba.mygeneration.models.presentation.user.UserField
import com.kandyba.mygeneration.presentation.animation.AnimationListener
import com.kandyba.mygeneration.presentation.animation.ProfileAnimation
import com.kandyba.mygeneration.presentation.animation.show
import com.kandyba.mygeneration.presentation.viewmodel.ProfileViewModel
import com.kandyba.mygeneration.presentation.viewmodel.factories.ProfileViewModelFactory
import java.io.File
import javax.inject.Inject

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
    private lateinit var signInButton: Button
    private lateinit var loggedUserLayout: NestedScrollView
    private lateinit var unloggedUserLayout: LinearLayout
    private lateinit var fioLayout: TextInputLayout
    private lateinit var saveButton: Button
    private lateinit var logged: ConstraintLayout
    private lateinit var progressLayout: LinearLayout

    private lateinit var viewModel: ProfileViewModel
    private lateinit var fieldsValues: Map<UserField, String>

    @Inject
    lateinit var viewModelFactory: ProfileViewModelFactory

    @Inject
    lateinit var settings: SharedPreferences

    @Inject
    lateinit var profileAnimation: ProfileAnimation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_activity)
        (application as App).appComponent.injectProfileActivity(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]
        viewModel.init(settings)
        initViews()
        initObservers()
        initListeners()
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
            fieldsValues = mapOf(
                Pair(UserField.NAME, fio.text.toString()),
                Pair(UserField.ACCOUNT_TYPE, accountType.text.toString()),
                Pair(UserField.BIRTHDAY, birthday.text.toString()),
                Pair(UserField.CITY, city.text.toString()),
                Pair(UserField.EMAIL, login.text.toString()),
                Pair(UserField.PHONE, phone.text.toString())
            )
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
            editButton.isEnabled = false
            viewModel.changeUserInfo(checkIfFieldsWereChanged())
        }
        exitButton.setOnClickListener {
            showProgressBar(true)
            signOut()
        }
        avatar.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_PICK
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE_AVATAR
            )
        }
    }

    private fun showProgressBar(show: Boolean) {
        progressLayout.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun initObservers() {
        viewModel.showLoggedUserLayoutLiveData.observe(this, Observer { show ->
            showLoggedUserLayout(show)
        })
        viewModel.signInUserLiveData.observe(this, Observer { createSignInIntent() })
        viewModel.userInfoLiveData.observe(this, Observer { user -> setFields(user) })
        viewModel.sharedPreferencesUserInfoLiveData.observe(this, Observer { fields ->
            updateSettings(fields)
        })
        viewModel.showReservedUserInfoLiveData.observe(this, Observer { setReservedFields() })
        viewModel.showProgressBarLiveData.observe(this, Observer { show ->
            showProgressBar(show)
        })
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
                .setLogo(R.drawable.logomin)
                .setTheme(R.style.MyFirebaseTheme)
                .build(),
            RC_SIGN_IN
        )

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
            //viewModel.uploadUserAvatar(File(file), settings.getString(UserField.ID.preferencesKey, null))
        }
        super.onActivityResult(requestCode, resultCode, data)
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

    private fun themeAndLogo() {
        val providers = emptyList<AuthUI.IdpConfig>()

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                //.setLogo(R.drawable.my_great_logo) // Set logo drawable
                //.setTheme(R.style.MySuperAppTheme) // Set theme
                .build(),
            RC_SIGN_IN
        )
    }

    private fun privacyAndTerms() {
        val providers = emptyList<AuthUI.IdpConfig>()
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTosAndPrivacyPolicyUrls(
                    "https://example.com/terms.html",
                    "https://example.com/privacy.html"
                )
                .build(),
            RC_SIGN_IN
        )
    }

    private fun checkIfFieldsWereChanged(): Map<UserField, String> {
        val changedFieldsList = mutableMapOf<UserField, String>()
        if (fio.text.toString() != fieldsValues[UserField.NAME]) {
            changedFieldsList[UserField.NAME] = fio.text.toString()
        }
        if (login.text.toString() != fieldsValues[UserField.EMAIL]) {
            changedFieldsList[UserField.EMAIL] = login.text.toString()
        }
        if (accountType.text.toString() != fieldsValues[UserField.ACCOUNT_TYPE]) {
            changedFieldsList[UserField.ACCOUNT_TYPE] = accountType.text.toString()
        }
        if (birthday.text.toString() != fieldsValues[UserField.BIRTHDAY]) {
            changedFieldsList[UserField.BIRTHDAY] = birthday.text.toString()
        }
        if (city.text.toString() != fieldsValues[UserField.CITY]) {
            changedFieldsList[UserField.CITY] = city.text.toString()
        }
        if (phone.text.toString() != fieldsValues[UserField.PHONE]) {
            changedFieldsList[UserField.PHONE] = phone.text.toString()
        }
        return changedFieldsList.toMap()
    }

    private fun makeFocusable(flag: Boolean) {
        fio.isEnabled = flag
        login.isEnabled = flag
        phone.isEnabled = flag
        birthday.isEnabled = flag
        city.isEnabled = flag
        if (flag) {
            when (settings.getString(UserField.AUTH_TYPE.preferencesKey, EMPTY_STRING) ?: EMPTY_STRING) {
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

    private fun setFields(user: User) {
        if (user.name != null) {
            fioLayout.isHintEnabled = false
        }
        fio.setText(user.name)
        login.setText(user.email)
        phone.setText(user.phoneNumber)
        birthday.setText(user.birthday)
        accountType.setText(user.accountType)
        city.setText(user.city)

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
        private const val RC_SIGN_IN = 123
        private const val PICK_IMAGE_AVATAR = 1
    }
}