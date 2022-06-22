package com.kandyba.mygeneration.models.presentation.user

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthProvider
import com.kandyba.mygeneration.models.EMPTY_STRING

data class User(
    val id: String? = null,
    val name: String? = null,
    val authType: AuthType? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val birthday: String? = null,
    val accountType: AccountType? = null,
    val city: String? = null,
    val region: Region? = null,
    var photoUri: String? = null
)

enum class AccountType(val title: String) {
    MEMBER("Участник"),
    TEAMER("Тимер"),
    MODERATOR("Модератор")
}

enum class Region(val regionCode: String, val regionName: String) {
    MOSCOW("msk", "Москва"),
    PITER("spb", "Санкт-Петербург")
}

enum class AuthType(val title: String) {
    GOOGLE(GoogleAuthProvider.GOOGLE_SIGN_IN_METHOD),
    EMAIL(EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD),
    PHONE(PhoneAuthProvider.PHONE_SIGN_IN_METHOD)
}

fun String.toAuthType() =
    when (this) {
        GoogleAuthProvider.PROVIDER_ID -> AuthType.GOOGLE
        PhoneAuthProvider.PROVIDER_ID -> AuthType.PHONE
        EmailAuthProvider.PROVIDER_ID -> AuthType.EMAIL
        else -> null
    }

fun String.toAccountType() =
    when (this) {
        AccountType.MEMBER.title -> AccountType.MEMBER
        AccountType.TEAMER.title -> AccountType.TEAMER
        AccountType.MODERATOR.title -> AccountType.MODERATOR
        else -> null
    }

fun String.toRegion() =
    when (this) {
        Region.MOSCOW.regionCode -> Region.MOSCOW
        Region.PITER.regionCode -> Region.PITER
        else -> null
    }

fun String.toRegionCode() =
    when (this) {
        Region.MOSCOW.regionName -> Region.MOSCOW.regionCode
        Region.PITER.regionName -> Region.PITER.regionCode
        else -> EMPTY_STRING
    }