package com.example.intern_project1.view.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.intern_project1.R
import com.example.intern_project1.databinding.ItemLoadingStateBinding
import com.example.intern_project1.model.network.entities.entities.FactoryObject

class FactoryLoadStateViewHolder(private val mbinding: ItemLoadingStateBinding) : RecyclerView.ViewHolder(mbinding.root) {

    private var loadState: LoadState? = null

    fun bind(loadState: LoadState,retry: () -> Unit) {
        this.loadState=loadState

        val progress = mbinding.loadStateProgress
        val btnRetry = mbinding.loadStateRetry
        val txtErrorMessage = mbinding.loadStateErrorMessage

        btnRetry.isVisible = loadState !is LoadState.Loading
        txtErrorMessage.isVisible = loadState !is LoadState.Loading
        progress.isVisible = loadState is LoadState.Loading

        if (loadState is LoadState.Error){
            txtErrorMessage.text = loadState.error.localizedMessage
        }

        btnRetry.setOnClickListener {
            retry.invoke()
        }
    }

    companion object {
        fun create(parent: ViewGroup): FactoryLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_loading_state,  parent,false)

            val binding = ItemLoadingStateBinding.bind(view)

            return FactoryLoadStateViewHolder(
                binding
            )
        }
    }
}