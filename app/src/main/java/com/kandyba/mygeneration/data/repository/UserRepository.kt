package com.kandyba.mygeneration.data.repository

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.UploadTask
import com.kandyba.mygeneration.models.data.UserModel
import kotlinx.coroutines.flow.Flow
import java.io.File

interface UserRepository {

    fun getUserInfo(uid: String): Flow<UserModel?>

    suspend fun createUser(authUser: FirebaseUser, providerType: String?)

    suspend fun changeUserInfo(value: String, valueEndpoint: String, uid: String)

    suspend fun uploadUserAvatar(file: File?, id: String?): UploadTask?

}