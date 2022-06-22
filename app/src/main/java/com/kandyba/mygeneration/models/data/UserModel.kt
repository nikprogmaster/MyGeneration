package com.kandyba.mygeneration.models.data

import com.kandyba.mygeneration.models.presentation.user.User
import com.kandyba.mygeneration.models.presentation.user.toAccountType
import com.kandyba.mygeneration.models.presentation.user.toAuthType
import com.kandyba.mygeneration.models.presentation.user.toRegion

data class UserModel(
    val id: String? = null,
    val name: String? = null,
    val authType: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val birthday: String? = null,
    val accountType: String? = null,
    val city: String? = null,
    val region: String? = null,
    var photoUri: String? = null
)

fun UserModel.toUser() = User(
    id,
    name,
    authType?.toAuthType(),
    email,
    phoneNumber,
    birthday,
    accountType?.toAccountType(),
    city,
    region?.toRegion(),
    photoUri
)
