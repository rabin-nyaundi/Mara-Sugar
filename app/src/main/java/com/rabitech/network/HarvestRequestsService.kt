package com.rabitech.network

import com.google.firebase.firestore.DocumentReference
import com.rabitech.dataModels.HarvestRequest
import kotlinx.coroutines.flow.Flow

interface HarvestRequestsService {

    suspend fun getHarvestRequests(userId: String): Flow<List<HarvestRequest>>

    suspend fun getHarvestRequests(): Flow<List<HarvestRequest>>

    suspend fun updateHarvestRequestStatus(request: HarvestRequest): Flow<NetworkState<DocumentReference>>
}