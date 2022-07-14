package com.kandyba.mygeneration.data.source

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.UploadTask
import com.kandyba.mygeneration.models.data.UserModel
import kotlinx.coroutines.flow.Flow
import java.io.File

interface UsersSource {

    fun getUserInfo(uid: String, userDatabaseEndpoint: String): Flow<UserModel?>

    suspend fun changeUserInfo(
        valueEndpoint: String,
        value: String,
        uid: String,
        userDatabaseEndpoint: String
    ): Boolean

    suspend fun createUser(
        authUser: FirebaseUser,
        providerType: String?,
        userDatabaseEndpoint: String
    ): Boolean

    fun uploadUserAvatar(file: File?, id: String?): UploadTask?
}