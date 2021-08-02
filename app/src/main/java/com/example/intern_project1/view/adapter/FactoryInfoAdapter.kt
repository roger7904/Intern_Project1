package com.example.intern_project1.view.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.intern_project1.model.entities.FactoryObject
import com.example.intern_project1.view.activities.FavoriteActivity
import com.example.intern_project1.view.viewholder.FactoryViewHolder
import com.example.intern_project1.viewmodel.FavoriteViewModel

class FactoryInfoAdapter(private val viewModel: FavoriteViewModel): PagingDataAdapter<FactoryObject.DataX, FactoryViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FactoryViewHolder {
        return FactoryViewHolder.create(
            parent
        )
    }

    override fun onBindViewHolder(holder: FactoryViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it,viewModel)
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<FactoryObject.DataX>() {
            override fun areItemsTheSame(oldItem: FactoryObject.DataX, newItem: FactoryObject.DataX): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: FactoryObject.DataX, newItem: FactoryObject.DataX): Boolean {
                return oldItem == newItem
            }
        }
    }
}
