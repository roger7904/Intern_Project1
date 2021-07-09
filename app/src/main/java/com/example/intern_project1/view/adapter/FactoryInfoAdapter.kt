package com.example.intern_project1.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.intern_project1.model.network.entities.entities.FactoryObject
import com.example.intern_project1.databinding.ItemFactoryListLayoutBinding
import com.example.intern_project1.view.viewholder.FactoryViewHolder

class FactoryInfoAdapter : PagingDataAdapter<FactoryObject.DataX, FactoryViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FactoryViewHolder {
        return FactoryViewHolder.create(
            parent
        )
    }

    override fun onBindViewHolder(holder: FactoryViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
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
