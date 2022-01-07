package com.kandyba.mygeneration.data

import com.kandyba.mygeneration.models.data.WallResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WallApiMapper {

    @GET(WALL_ENDPOINT)
    fun getWallPosts(
        @Query(OWNERS_ID_QUERY) ownersId: String,
        @Query(POSTS_NUMBER_QUERY) postCount: Int,
        @Query(VERSION_QUERY) version: String,
        @Query(ACCESS_TOKEN_QUERY) accessToken: String
    ): Single<WallResponse>

    companion object {
        private const val WALL_ENDPOINT = "wall.get/"
        private const val OWNERS_ID_QUERY = "owner_id"
        private const val POSTS_NUMBER_QUERY = "count"
        private const val VERSION_QUERY = "v"
        private const val ACCESS_TOKEN_QUERY = "access_token"
    }
}