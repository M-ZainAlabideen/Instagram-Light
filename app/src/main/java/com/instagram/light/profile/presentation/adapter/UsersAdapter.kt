package com.instagram.light.profile.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.instagram.light.databinding.ItemHeaderUserBinding
import com.instagram.light.profile.data.model.User
import com.todkars.shimmer.ShimmerRecyclerView
import kotlin.properties.Delegates

class UsersAdapter(
    list: List<User> = emptyList(),
    private val onItemClick: (User, ShimmerRecyclerView) -> Unit
) : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    private var list: List<User> by Delegates.observable(list) { _, old, new ->
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
            result.dispatchUpdatesTo(this@UsersAdapter)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            ItemHeaderUserBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], list, onItemClick)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateData(newList: List<User>) {
        list = newList
    }

    class ViewHolder(
        private val binding: ItemHeaderUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(
            user: User,
            list: List<User>,
            onItemClick: (User, ShimmerRecyclerView) -> Unit
        ) {
            binding.apply {
                user.apply {
                    userName.text = name
                    userAddress.text =
                        "${address.city}, ${address.suite}, ${address.street}"
                    if (expandable) {
                        albumsRv.visibility = View.VISIBLE
                    } else {
                        albumsRv.visibility = View.GONE
                    }

                    container.setOnClickListener {
                        for (index in list.indices) {
                            if (list[index].expandable) {
                                list[index].expandable = false
                                bindingAdapter?.notifyItemChanged(index, null)
                            }
                        }
                        expandable = true
                        albumsRv.visibility = View.VISIBLE
                        onItemClick(user, albumsRv)
                    }
                }
            }

        }
    }
}
