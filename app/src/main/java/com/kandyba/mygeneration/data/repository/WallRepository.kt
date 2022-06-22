package com.kandyba.mygeneration.data.repository

import com.kandyba.mygeneration.models.presentation.VkPost

interface WallRepository {

    suspend fun getWallPosts(postCount: Int): List<VkPost>
}