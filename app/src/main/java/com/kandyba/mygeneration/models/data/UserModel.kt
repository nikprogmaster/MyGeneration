package com.kandyba.mygeneration.models.data

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthProvider

data class User(
    val id: String? = null,
    val name: String? = null,
    val authType: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val birthday: String? = null,
    val accountType: String? = null,
    val city: String? = null,
    var photoUri: String? = null
)

enum class AccountType(val title: String) {
    TEAMER("Тимер"),
    MODERATOR("Модератор")
}

enum class AuthType(val title: String) {
    GOOGLE(GoogleAuthProvider.GOOGLE_SIGN_IN_METHOD),
    EMAIL(EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD),
    PHONE(PhoneAuthProvider.PHONE_SIGN_IN_METHOD)
}
