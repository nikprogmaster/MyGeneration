package com.kandyba.mygeneration.data.repository

import com.kandyba.mygeneration.data.RegionFirestoreSource
import com.kandyba.mygeneration.models.data.RegionModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RegionsRepositoryImpl(private val regionFirestoreSource: RegionFirestoreSource) :
    RegionsRepository {

    override suspend fun getRegions(): List<RegionModel> = withContext(Dispatchers.IO) {
        regionFirestoreSource.getRegions(REGIONS_ENDPOINT)
    }

    companion object {
        private const val REGIONS_ENDPOINT = "regions"
    }
}