package com.kandyba.mygeneration.data.repository

import com.kandyba.mygeneration.data.WallApiMapper
import com.kandyba.mygeneration.models.data.WallResponse
import io.reactivex.Single

class WallRepositoryImpl(
    private val wallApiMapper: WallApiMapper
) : WallRepository {

    override fun getWallPosts(
        postCount: Int
    ): Single<WallResponse> {
        return wallApiMapper.getWallPosts(OWNERS_ID, postCount, VERSION, ACCESS_TOKEN)
    }

    companion object {
        private const val VERSION = "5.131"
        private const val ACCESS_TOKEN =
            "37cc781b37cc781b37cc781b9337b5ca1e337cc37cc781b56a434886fa4c1593b30a4d3"
        private const val OWNERS_ID = "-511139"
    }
}