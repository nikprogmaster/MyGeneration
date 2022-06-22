package com.kandyba.mygeneration.models.presentation.user

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthProvider
import com.kandyba.mygeneration.models.data.UserModel

class UserConverter {

    fun convert(firebaseUser: FirebaseUser): UserModel {
        val authType =
            when (firebaseUser.providerId) {
                GoogleAuthProvider.PROVIDER_ID -> GoogleAuthProvider.GOOGLE_SIGN_IN_METHOD
                PhoneAuthProvider.PROVIDER_ID -> PhoneAuthProvider.PHONE_SIGN_IN_METHOD
                else -> EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD
            }
        return UserModel(
            firebaseUser.uid,
            firebaseUser.displayName,
            authType,
            firebaseUser.email,
            null,
            firebaseUser.phoneNumber
        )
    }

    fun convertForSettings(userModel: UserModel): Map<UserField, String?> {
        return mapOf(
            UserField.NAME to userModel.name,
            UserField.ID to userModel.id,
            UserField.ACCOUNT_TYPE to userModel.accountType,
            UserField.BIRTHDAY to userModel.birthday,
            UserField.CITY to userModel.city,
            UserField.EMAIL to userModel.email,
            UserField.PHONE to userModel.phoneNumber,
            UserField.AUTH_TYPE to userModel.authType
        )
    }
}