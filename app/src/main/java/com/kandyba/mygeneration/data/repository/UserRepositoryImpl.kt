package com.kandyba.mygeneration.data.repository

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.UploadTask
import com.kandyba.mygeneration.data.source.UsersSource
import com.kandyba.mygeneration.models.data.toUser
import com.kandyba.mygeneration.models.presentation.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File

class UserRepositoryImpl(private val usersSource: UsersSource) : UserRepository {

    override fun getUserInfo(uid: String): Flow<User?> {
        return usersSource.getUserInfo(uid, USER_DATABASE_ENDPOINT)
            .map { it?.toUser() }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun createUser(authUser: FirebaseUser, providerType: String?) =
        withContext(Dispatchers.IO) {
            usersSource.createUser(authUser, providerType, USER_DATABASE_ENDPOINT)
        }

    override suspend fun changeUserInfo(value: String, valueEndpoint: String, uid: String) =
        withContext(Dispatchers.IO) {
            usersSource.changeUserInfo(valueEndpoint, value, uid, USER_DATABASE_ENDPOINT)
        }

    override suspend fun uploadUserAvatar(file: File?, id: String?): UploadTask? =
        withContext(Dispatchers.IO) {
            usersSource.uploadUserAvatar(file, id)
        }

    companion object {
        private const val USER_DATABASE_ENDPOINT = "users"
    }
}