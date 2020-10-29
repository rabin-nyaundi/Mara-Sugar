package com.rabitech.ui.harvestHistory

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.rabitech.network.NetworkState
import com.rabitech.repository.HarvestRequestsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

@ExperimentalCoroutinesApi
class HarvestHistoryViewModel : ViewModel() {

    private val harvestRequestsRepository = HarvestRequestsRepository()

    fun getCases(caseStatus: String) = liveData(Dispatchers.IO) {
        emit(NetworkState.loading())

        try {
            harvestRequestsRepository.getHarvestRequests(caseStatus).collect {
                emit(NetworkState.success(it))
            }

        } catch (e: Exception) {
            emit(NetworkState.failed(e.message.toString()))
            Log.e("ERROR:", e.message.toString())
        }
    }

}