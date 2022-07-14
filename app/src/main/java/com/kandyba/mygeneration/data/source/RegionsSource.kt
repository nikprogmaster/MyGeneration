package com.kandyba.mygeneration.data.source

import com.kandyba.mygeneration.models.data.RegionModel

interface RegionsSource {

    suspend fun getRegions(regionEndpoint: String): List<RegionModel>
}