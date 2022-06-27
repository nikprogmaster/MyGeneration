package com.kandyba.mygeneration.models.presentation.user

class UserConverter {

    fun convertForSettings(userModel: User): Map<UserField, String?> {
        return mapOf(
            UserField.NAME to userModel.name,
            UserField.ID to userModel.id,
            UserField.ACCOUNT_TYPE to userModel.accountType?.title,
            UserField.BIRTHDAY to userModel.birthday,
            UserField.CITY to userModel.city,
            UserField.EMAIL to userModel.email,
            UserField.PHONE to userModel.phoneNumber,
            UserField.AUTH_TYPE to userModel.authType?.title,
            UserField.REGION_NAME to userModel.region?.regionName,
            UserField.REGION_CODE to userModel.region?.regionCode
        )
    }
}