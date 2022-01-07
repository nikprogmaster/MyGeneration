package com.kandyba.mygeneration.data.repository

import com.kandyba.mygeneration.models.data.WallResponse
import io.reactivex.Single

interface WallRepository {

    fun getWallPosts(postCount: Int): Single<WallResponse>
}