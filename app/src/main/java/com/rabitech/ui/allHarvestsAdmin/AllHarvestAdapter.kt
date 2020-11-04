package com.rabitech.ui.allHarvestsAdmin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rabitech.dataModels.HarvestRequest
import com.rabitech.databinding.ItemHarvestHistoryBinding
import com.rabitech.ui.allHarvestsAdmin.AllHarvestAdapter.AllHarvestViewHolder.Companion.from
import com.rabitech.ui.harvestHistory.HarvestHistoryAdapter

class AllHarvestAdapter :
    ListAdapter<HarvestRequest, AllHarvestAdapter.AllHarvestViewHolder>(
        HarvestHistoryAdapter.HarvestHistoryDiffUtil()
    )
{

    private var isExpanded = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllHarvestViewHolder {
        return from(parent)
    }

    override fun onBindViewHolder(holder: AllHarvestViewHolder, position: Int) {
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


    class AllHarvestViewHolder(val binding: ItemHarvestHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(harvestRequest: HarvestRequest) {
            binding.harvestRequest = harvestRequest
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): AllHarvestViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val itemHarvestHistoryBinding =
                    ItemHarvestHistoryBinding.inflate(layoutInflater, parent, false)

                return AllHarvestViewHolder(itemHarvestHistoryBinding)
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