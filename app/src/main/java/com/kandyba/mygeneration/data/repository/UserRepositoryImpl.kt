package com.kandyba.mygeneration.data.repository

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.UploadTask
import com.kandyba.mygeneration.data.UserDatabaseSource
import com.kandyba.mygeneration.models.data.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.File

class UserRepositoryImpl(private val userDatabaseSource: UserDatabaseSource) : UserRepository {

    override fun getUserInfo(uid: String): Flow<UserModel?> {
        return userDatabaseSource.getUserInfo(uid, USER_DATABASE_ENDPOINT)
            .flowOn(Dispatchers.IO)
    }

    override suspend fun createUser(authUser: FirebaseUser, providerType: String?) =
        withContext(Dispatchers.IO) {
            userDatabaseSource.createUser(authUser, providerType, USER_DATABASE_ENDPOINT)
        }

    override suspend fun changeUserInfo(value: String, valueEndpoint: String, uid: String) =
        withContext(Dispatchers.IO) {
            userDatabaseSource.changeUserInfo(valueEndpoint, value, uid, USER_DATABASE_ENDPOINT)
        }

    override suspend fun uploadUserAvatar(file: File?, id: String?): UploadTask? =
        withContext(Dispatchers.IO) {
            userDatabaseSource.uploadUserAvatar(file, id)
        }

    companion object {
        private const val USER_DATABASE_ENDPOINT = "users"
    }
}