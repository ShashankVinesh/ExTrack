package com.shashank.extrack.ui.home.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shashank.extrack.R
import com.shashank.extrack.data.local.entity.TransactionEntity
import com.shashank.extrack.databinding.ItemTransactionBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionAdapter(
    private val showDeletedAt: Boolean = false
) : ListAdapter<TransactionEntity, TransactionAdapter.TransactionViewHolder>(TransactionViewHolder.DiffCallback()) {

    var onItemClick: ((TransactionEntity) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ItemTransactionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TransactionViewHolder(binding, showDeletedAt)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick)
    }

    class TransactionViewHolder(
        private val binding: ItemTransactionBinding,
        private val showDeletedAt: Boolean
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TransactionEntity, onItemClick: ((TransactionEntity) -> Unit)?) {
            binding.tvTitle.text = item.title

            binding.root.setOnClickListener {
                onItemClick?.invoke(item)
            }

            val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

            // HOME: show transaction date
            // HISTORY: show deletedAt (as "Deleted: ...")
            binding.tvDate.text = if (showDeletedAt && item.deletedAt != null) {
                "Deleted: ${sdf.format(Date(item.deletedAt))}"
            } else {
                sdf.format(Date(item.date))
            }

            val formattedAmount = com.shashank.extrack.util.formatINR(item.amount)

            if (item.isIncome) {
                binding.tvAmount.text = "+ $formattedAmount"
                binding.tvAmount.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.green)
                )
            } else {
                binding.tvAmount.text = "- $formattedAmount"
                binding.tvAmount.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.red)
                )
            }
        }

        class DiffCallback : DiffUtil.ItemCallback<TransactionEntity>() {
            override fun areItemsTheSame(oldItem: TransactionEntity, newItem: TransactionEntity) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: TransactionEntity,
                newItem: TransactionEntity
            ) =
                oldItem == newItem
        }
    }
}