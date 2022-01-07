package com.kandyba.mygeneration.models.presentation.user

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
}