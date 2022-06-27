package com.kandyba.mygeneration.models.presentation.user

enum class UserField(val preferencesKey: String) {
    NAME("name"),
    EMAIL("email"),
    CITY("city"),
    BIRTHDAY("birthday"),
    ACCOUNT_TYPE("accountType"),
    PHONE("phoneNumber"),
    ID("id"),
    AUTH_TYPE("authType"),
    REGION_NAME("region_name"),
    REGION_CODE("region")
}