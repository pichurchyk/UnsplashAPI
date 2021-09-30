package com.example.pichurchyk_p3.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.pichurchyk_p3.databinding.ImgItemBinding
import com.example.pichurchyk_p3.model.UnsplashPhoto

class SearchFragmentAdapter(private val listener: OnItemClick, private val isLandscape: Boolean) :
    PagingDataAdapter<UnsplashPhoto, SearchFragmentAdapter.MViewHolder>(PHOTO_COMPARATOR) {
    inner class MViewHolder(private val binding: ImgItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val photoPosition = bindingAdapterPosition
                if (photoPosition != RecyclerView.NO_POSITION) {
                    val item = getItem(photoPosition)
                    if (item != null) {
                        listener.onItemClick(item)
                    }
                }
            }
            if (isLandscape) {
                binding.cardView.visibility = View.GONE
                binding.cardViewLandscape.visibility = View.VISIBLE
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(img: UnsplashPhoto) {
            Glide.with(itemView)
                .load(img.urls.regular)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.searchImgItem)
            Glide.with(itemView)
                .load(img.urls.regular)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.searchImgItemLandscape)
        }
    }

    interface OnItemClick {
        fun onItemClick(photo: UnsplashPhoto)
    }

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<UnsplashPhoto>() {
            override fun areItemsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto) =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MViewHolder {
        return MViewHolder(
            ImgItemBinding.inflate(
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
