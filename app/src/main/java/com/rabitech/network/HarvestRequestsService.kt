package com.rabitech.network

import com.rabitech.dataModels.HarvestRequest
import kotlinx.coroutines.flow.Flow

interface HarvestRequestsService {

    suspend fun getHarvestRequests(userId: String): Flow<List<HarvestRequest>>

    suspend fun getHarvestRequests(): Flow<List<HarvestRequest>>
}