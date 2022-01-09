package com.kandyba.mygeneration.models.presentation.user

import android.icu.text.UFormat
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthProvider
import com.kandyba.mygeneration.models.data.User

class UserConverter {

    fun convert(firebaseUser: FirebaseUser): User {
        val authType =
            when (firebaseUser.providerId) {
                GoogleAuthProvider.PROVIDER_ID -> GoogleAuthProvider.GOOGLE_SIGN_IN_METHOD
                PhoneAuthProvider.PROVIDER_ID -> PhoneAuthProvider.PHONE_SIGN_IN_METHOD
                else -> EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD
            }
        return User(
            firebaseUser.uid,
            firebaseUser.displayName,
            authType,
            firebaseUser.email,
            null,
            firebaseUser.phoneNumber
        )
    }

    fun convertForSettings(user: User): Map<UserField, String?> {
        return mapOf(
            UserField.NAME to user.name,
            UserField.ID to user.id,
            UserField.ACCOUNT_TYPE to user.accountType,
            UserField.BIRTHDAY to user.birthday,
            UserField.CITY to user.city,
            UserField.EMAIL to user.email,
            UserField.PHONE to user.phoneNumber,
            UserField.AUTH_TYPE to user.authType
        )
    }
}