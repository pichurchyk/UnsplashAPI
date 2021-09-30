package com.example.pichurchyk_p3.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pichurchyk_p3.databinding.RecyclerAdapterLoaderBinding

class RecyclerViewStateLoadAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<RecyclerViewStateLoadAdapter.MViewHolder>() {
    inner class MViewHolder(private val binding: RecyclerAdapterLoaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.loadingErrorButton.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            binding.progress.isVisible = loadState is LoadState.Loading
            binding.loadingErrorButton.isVisible = loadState !is LoadState.Loading
            binding.loadingErrorTitle.isVisible = loadState !is LoadState.Loading
        }
    }

    override fun onBindViewHolder(holder: MViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): MViewHolder {
        val binding = RecyclerAdapterLoaderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return MViewHolder(binding)
    }
}
