package com.rabitech.ui.allHarvestsAdmin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.rabitech.network.NetworkState
import com.rabitech.repository.HarvestRequestsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

@ExperimentalCoroutinesApi
class AllHarvestsViewModel : ViewModel() {

    private val harvestRequestsRepository = HarvestRequestsRepository()

    fun getHarvestRequests() = liveData(Dispatchers.IO) {
        emit(NetworkState.loading())

        try {
            harvestRequestsRepository.getHarvestRequests().collect {
                emit(NetworkState.success(it))
            }

        } catch (e: Exception) {
            emit(NetworkState.failed(e.message.toString()))
            Log.e("ERROR:", e.message.toString())
        }
    }
}