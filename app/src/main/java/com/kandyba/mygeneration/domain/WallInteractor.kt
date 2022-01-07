package com.kandyba.mygeneration.domain

import com.kandyba.mygeneration.models.data.WallResponse
import io.reactivex.Single

interface WallInteractor {

    fun getWallPosts(postCount: Int): Single<WallResponse>
}