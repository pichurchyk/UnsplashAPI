package com.example.pichurchyk_p3.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.pichurchyk_p3.databinding.FavoritesImagesItemBinding
import com.example.pichurchyk_p3.room.Photo

class FavoriteImagesAdapter(
    private val listener: OnItemClick,
    private val deleteListener: OnDeleteClick,
    private val isLandscape: Boolean
) :
    ListAdapter<Photo, FavoriteImagesAdapter.MViewHolder>(PHOTO_COMPARATOR) {

    var clickedItemId = ""

    inner class MViewHolder(private val binding: FavoritesImagesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val photoPosition = bindingAdapterPosition
                if (photoPosition != RecyclerView.NO_POSITION) {
                    val item = getItem(photoPosition)
                    if (item != null) {
                        clickedItemId = item.imgId
                        listener.onItemClick(item)
                    }
                }
            }
            binding.delete.setOnClickListener {
                val photoPosition = bindingAdapterPosition
                if (photoPosition != RecyclerView.NO_POSITION) {
                    val item = getItem(photoPosition)
                    if (item != null) {
                        deleteListener.onDeleteClick(item)
                    }
                }
            }
            if (isLandscape) {
                binding.cardView.visibility = View.GONE
                binding.cardViewLandscape.visibility = View.VISIBLE
            } else {
                binding.cardView.visibility = View.VISIBLE
                binding.cardViewLandscape.visibility = View.GONE
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(img: Photo) {
            Glide.with(itemView)
                .load(img.imgUrl)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.searchImgItem)
            if (binding.searchImgItemLandscape.visibility != View.GONE) {
                Glide.with(itemView)
                    .load(img.imgUrl)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(binding.searchImgItemLandscape)
            }
        }
    }

    interface OnItemClick {
        fun onItemClick(photo: Any)
    }

    interface OnDeleteClick {
        fun onDeleteClick(photo: Photo)
    }

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(oldItem: Photo, newItem: Photo) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Photo, newItem: Photo) =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MViewHolder {
        return MViewHolder(
            FavoritesImagesItemBinding.inflate(
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
