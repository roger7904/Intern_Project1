package com.example.intern_project1.view.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.intern_project1.R
import com.example.intern_project1.databinding.ItemLoadingStateBinding


class FactoryLoadStateViewHolder(
    private val mbinding: ItemLoadingStateBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(mbinding.root) {

    init {
        mbinding.loadStateRetry.setOnClickListener {
            retry.invoke()
        }
    }

    fun bind(loadState: LoadState) {

        val progress = mbinding.loadStateProgress
        val btnRetry = mbinding.loadStateRetry
        val txtErrorMessage = mbinding.loadStateErrorMessage

        btnRetry.isVisible = loadState !is LoadState.Loading
        txtErrorMessage.isVisible = loadState !is LoadState.Loading
        progress.isVisible = loadState is LoadState.Loading

        if (loadState is LoadState.Error){
            txtErrorMessage.text = loadState.error.localizedMessage
        }

    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): FactoryLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_loading_state,  parent,false)

            val binding = ItemLoadingStateBinding.bind(view)

            return FactoryLoadStateViewHolder(
                binding,retry
            )
        }
    }
}