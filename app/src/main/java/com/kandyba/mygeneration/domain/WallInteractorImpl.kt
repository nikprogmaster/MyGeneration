package com.kandyba.mygeneration.domain

import com.kandyba.mygeneration.data.repository.WallRepository
import com.kandyba.mygeneration.models.data.WallResponse
import io.reactivex.Single

class WallInteractorImpl(
    private val wallRepository: WallRepository
): WallInteractor {

    override fun getWallPosts(postCount: Int): Single<WallResponse> {
        return wallRepository.getWallPosts(postCount)
    }
}