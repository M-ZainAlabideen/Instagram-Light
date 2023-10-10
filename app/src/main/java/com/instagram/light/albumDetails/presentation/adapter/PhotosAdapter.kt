package com.instagram.light.albumDetails.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.instagram.light.albumDetails.data.model.Photo
import com.instagram.light.databinding.ItemPhotoBinding
import kotlin.properties.Delegates

class PhotosAdapter(
    list: List<Photo> = emptyList(),
    private val onItemClick: (List<Photo>, Int) -> Unit
) : RecyclerView.Adapter<PhotosAdapter.ViewHolder>() {

    private var list: List<Photo> by Delegates.observable(list) { _, old, new ->
        DiffUtil.calculateDiff(
            object : DiffUtil.Callback() {
                override fun getOldListSize() = old.size

                override fun getNewListSize() = new.size

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                    old[oldItemPosition].id == new[newItemPosition].id

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                    old[oldItemPosition] == new[newItemPosition]

            }
        ).also { result ->
            result.dispatchUpdatesTo(this@PhotosAdapter)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            ItemPhotoBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list, onItemClick)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateData(newList: List<Photo>) {
        list = newList
    }

    class ViewHolder(
        private val binding: ItemPhotoBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            photosList: List<Photo>,
            onItemClick: (List<Photo>, Int) -> Unit
        ) {
            binding.apply {
                Glide.with(thumbnail.context)
                    .asBitmap()
                    .load(photosList[bindingAdapterPosition].thumbnailUrl)
                    .into(thumbnail)
                thumbnail.setOnClickListener { onItemClick(photosList, bindingAdapterPosition) }
            }

        }
    }
}
