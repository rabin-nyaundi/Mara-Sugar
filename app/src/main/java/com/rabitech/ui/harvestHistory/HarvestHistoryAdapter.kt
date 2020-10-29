package com.rabitech.ui.harvestHistory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rabitech.dataModels.HarvestRequest
import com.rabitech.databinding.ItemHarvestHistoryBinding
import com.rabitech.ui.harvestHistory.HarvestHistoryAdapter.HarvestHistoryViewHolder.Companion.from

class HarvestHistoryAdapter :
    ListAdapter<HarvestRequest, HarvestHistoryAdapter.HarvestHistoryViewHolder>(
        HarvestHistoryDiffUtil()
    ) {

    private var isExpanded = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HarvestHistoryViewHolder {
        return from(parent)
    }

    override fun onBindViewHolder(holder: HarvestHistoryViewHolder, position: Int) {
        val harvestRequest = getItem(position)
        holder.bind(harvestRequest)
        holder.binding.imageExpandDetails.setOnClickListener {
            if(isExpanded){
                holder.binding.cardViewHarvestDetails.visibility = View.GONE
                isExpanded = false
            }else{
                holder.binding.cardViewHarvestDetails.visibility = View.VISIBLE
                isExpanded = true
            }

        }
    }


    class HarvestHistoryViewHolder(val binding: ItemHarvestHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(harvestRequest: HarvestRequest) {
            binding.harvestRequest = harvestRequest
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): HarvestHistoryViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val itemHarvestHistoryBinding =
                    ItemHarvestHistoryBinding.inflate(layoutInflater, parent, false)

                return HarvestHistoryViewHolder(itemHarvestHistoryBinding)
            }
        }

    }

    class HarvestHistoryDiffUtil : DiffUtil.ItemCallback<HarvestRequest>() {
        override fun areItemsTheSame(oldItem: HarvestRequest, newItem: HarvestRequest): Boolean {
            return oldItem.requestId == newItem.requestId
        }

        override fun areContentsTheSame(oldItem: HarvestRequest, newItem: HarvestRequest): Boolean {
            return oldItem == newItem
        }

    }
}