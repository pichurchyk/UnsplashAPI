package com.example.pichurchyk_p3.adapter

import android.annotation.SuppressLint
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pichurchyk_p3.R
import com.example.pichurchyk_p3.databinding.HistoryItemBinding
import com.example.pichurchyk_p3.room.Query


class HistoryFragmentAdapter(
    private val listener: ChangeQueryState,
    private val onItemClick: OnItemClick,
    private val compareDates: (date: String) -> String,
) :
    ListAdapter<Query, HistoryFragmentAdapter.MViewHolder>(HISTORY_COMPARATOR) {

    private lateinit var res: Resources

    inner class MViewHolder(private val binding: HistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            res = binding.root.resources
            binding.queryLike.setOnClickListener {
                val photoPosition = bindingAdapterPosition
                if (photoPosition != RecyclerView.NO_POSITION) {
                    val item = getItem(photoPosition)
                    if (item != null) {
                        listener.changeQueryState(item)
                    }
                }
            }
            binding.root.setOnClickListener {
                val photoPosition = bindingAdapterPosition
                if (photoPosition != RecyclerView.NO_POSITION) {
                    val item = getItem(photoPosition)
                    if (item != null) {
                        onItemClick.openSearchPage(item.queryText)
                    }
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(query: Query) {
            binding.queryTitle.text = query.queryText
            binding.queryTotal.text =
                String.format(res.getString(R.string.query_results), query.total)
            binding.queryDate.text = compareDates(query.date)
            if (query.isLiked) {
                DrawableCompat.setTint(
                    binding.queryLike.drawable,
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.blue
                    )
                )
            } else {
                DrawableCompat.setTint(
                    binding.queryLike.drawable,
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.gray
                    )
                )
            }
        }
    }

    companion object {
        private val HISTORY_COMPARATOR = object : DiffUtil.ItemCallback<Query>() {
            override fun areItemsTheSame(oldItem: Query, newItem: Query) =
                oldItem.queryText == newItem.queryText

            override fun areContentsTheSame(oldItem: Query, newItem: Query) =
                oldItem == newItem
        }
    }

    interface ChangeQueryState {
        fun changeQueryState(query: Query)
    }

    interface OnItemClick {
        fun openSearchPage(query: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MViewHolder {
        return MViewHolder(
            HistoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }
}
