package com.example.intern_project1.view.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.intern_project1.model.entities.Favorite
import com.example.intern_project1.view.viewholder.FavoriteViewHolder
import com.example.intern_project1.viewmodel.FactoryViewModel

class FavoriteAdapter(private val viewModel: FactoryViewModel) : ListAdapter<Favorite, FavoriteViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder.create(
            parent
        )
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it,viewModel)
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Favorite>() {
            override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
                return oldItem == newItem
            }
        }
    }
}