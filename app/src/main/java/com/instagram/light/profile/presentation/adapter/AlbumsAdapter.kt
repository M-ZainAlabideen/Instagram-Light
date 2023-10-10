package com.instagram.light.profile.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.instagram.light.databinding.ItemChildAlbumBinding
import com.instagram.light.profile.data.model.Album
import kotlin.properties.Delegates

class AlbumsAdapter(
    list: List<Album> = emptyList(),
    private val onSubItemClick: (Album) -> Unit
) : RecyclerView.Adapter<AlbumsAdapter.ViewHolder>() {

    private var list: List<Album> by Delegates.observable(list) { _, old, new ->
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
            result.dispatchUpdatesTo(this@AlbumsAdapter)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            ItemChildAlbumBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], onSubItemClick)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateData(newList: List<Album>) {
        list = newList
    }

    class ViewHolder(
        private val binding: ItemChildAlbumBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            album: Album,
            onSubItemClick: (Album) -> Unit
        ) {
            binding.apply {
                albumName.text = album.title
                container.setOnClickListener { onSubItemClick(album) }
            }

        }
    }
}
