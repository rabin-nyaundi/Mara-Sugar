package com.rabitech.ui.allHarvestsAdmin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rabitech.dataModels.HarvestRequest
import com.rabitech.databinding.ItemHarvestHistoryAdminBinding
import com.rabitech.ui.allHarvestsAdmin.AllHarvestAdapter.AllHarvestViewHolder.Companion.from
import com.rabitech.ui.harvestHistory.HarvestHistoryAdapter

class AllHarvestAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<HarvestRequest, AllHarvestAdapter.AllHarvestViewHolder>(
        HarvestHistoryAdapter.HarvestHistoryDiffUtil()
    ) {

    private var isExpanded = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllHarvestViewHolder {
        return from(parent)
    }

    override fun onBindViewHolder(holder: AllHarvestViewHolder, position: Int) {
        val harvestRequest = getItem(position)
        holder.bind(harvestRequest)
        holder.binding.buttonApprove.setOnClickListener {
            onClickListener.onClick(harvestRequest)
        }
        holder.binding.imageExpandDetails.setOnClickListener {
            if (isExpanded) {
                holder.binding.cardViewHarvestDetails.visibility = View.GONE
                isExpanded = false
            } else {
                holder.binding.cardViewHarvestDetails.visibility = View.VISIBLE
                isExpanded = true
            }

        }
    }


    class OnClickListener(val onClickListener: (harvestRequest: HarvestRequest) -> Unit) {
        fun onClick(harvestRequest: HarvestRequest) = onClickListener(harvestRequest)
    }

    class AllHarvestViewHolder(val binding: ItemHarvestHistoryAdminBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(harvestRequest: HarvestRequest) {
            binding.harvestRequest = harvestRequest
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): AllHarvestViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val itemHarvestHistoryAdminBinding =
                    ItemHarvestHistoryAdminBinding.inflate(layoutInflater, parent, false)

                return AllHarvestViewHolder(itemHarvestHistoryAdminBinding)
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