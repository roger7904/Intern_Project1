package com.example.intern_project1.view.adapter

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.example.intern_project1.view.viewholder.FactoryLoadStateViewHolder

class FactoryLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<FactoryLoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): FactoryLoadStateViewHolder {
        return FactoryLoadStateViewHolder.create(
            parent
        )
    }

    override fun onBindViewHolder(holder: FactoryLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState,retry)
    }

}