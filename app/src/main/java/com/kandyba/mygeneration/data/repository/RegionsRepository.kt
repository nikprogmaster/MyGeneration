package com.kandyba.mygeneration.data.repository

import com.kandyba.mygeneration.models.data.RegionModel

interface RegionsRepository {

    suspend fun getRegions(): List<RegionModel>
}