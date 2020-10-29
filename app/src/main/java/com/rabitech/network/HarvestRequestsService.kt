package com.rabitech.network

import com.rabitech.dataModels.HarvestRequest
import kotlinx.coroutines.flow.Flow

interface HarvestRequestsService {

    suspend fun getHarvestRequests(caseStatus: String): Flow<List<HarvestRequest>>
}