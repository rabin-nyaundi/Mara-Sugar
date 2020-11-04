package com.rabitech.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.rabitech.dataModels.HarvestRequest
import com.rabitech.network.HarvestRequestsService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
class HarvestRequestsRepository : HarvestRequestsService {

    override suspend fun getHarvestRequests(userId: String): Flow<List<HarvestRequest>> =
        callbackFlow {
            val caseDocument = FirebaseFirestore.getInstance()
                .collection("harvestRequests")
                .whereEqualTo("userId",userId)

            val subscription = caseDocument.addSnapshotListener { value, _ ->
                value?.let {
                    val harvestRequests = it.toObjects(HarvestRequest::class.java)
                    offer(harvestRequests)
                }
            }

            //Finally if collect is not in use or collecting any data we cancel this channel to prevent any leak and remove the subscription listener to the database
            awaitClose {
                subscription.remove()
            }
        }

    override suspend fun getHarvestRequests(): Flow<List<HarvestRequest>> =
        callbackFlow {
            val caseDocument = FirebaseFirestore.getInstance()
                .collection("harvestRequests")


            val subscription = caseDocument.addSnapshotListener { value, _ ->
                value?.let {
                    val harvestRequests = it.toObjects(HarvestRequest::class.java)
                    offer(harvestRequests)
                }
            }

            //Finally if collect is not in use or collecting any data we cancel this channel to prevent any leak and remove the subscription listener to the database
            awaitClose {
                subscription.remove()
            }
        }

}