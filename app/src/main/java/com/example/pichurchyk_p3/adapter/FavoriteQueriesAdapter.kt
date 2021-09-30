package com.example.pichurchyk_p3.adapter

import android.annotation.SuppressLint
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pichurchyk_p3.R
import com.example.pichurchyk_p3.databinding.FavoritesQueriesItemBinding
import com.example.pichurchyk_p3.room.Query

class FavoriteQueriesAdapter(
    private val listener: ChangeQueryState,
    private val onItemClick: OnItemClick,
    private val compareDates: (date: String) -> String
) :
    ListAdapter<Query, FavoriteQueriesAdapter.MViewHolder>(HISTORY_COMPARATOR) {

    private lateinit var res: Resources

    inner class MViewHolder(private val binding: FavoritesQueriesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            res = binding.root.resources
            binding.queryRemove.setOnClickListener {
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
            FavoritesQueriesItemBinding.inflate(
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
