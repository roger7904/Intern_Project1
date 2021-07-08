package com.example.intern_project1.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.intern_project1.model.network.entities.entities.FactoryObject
import com.example.intern_project1.databinding.ItemFactoryListLayoutBinding

class FactoryInfoAdapter(val context: Context, val items: ArrayList<FactoryObject.DataX>) :
    RecyclerView.Adapter<FactoryInfoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mbinding: ItemFactoryListLayoutBinding =
            ItemFactoryListLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(mbinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items.get(position)

        holder.tvCity.text = item.city
        holder.tvTitle.text = item.name
        holder.tvAddress.text = item.address
        holder.tvPhone.text = item.phone
        holder.tvBrandName.text = item.tags

    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view: ItemFactoryListLayoutBinding) : RecyclerView.ViewHolder(view.root) {

        val ivFactory = view.ivFactoryImage
        val tvCity = view.tvCity
        val tvTitle = view.tvTitle
        val tvAddress = view.tvAddress
        val tvPhone = view.tvPhone
        val tvBrandName = view.tvBrandname

    }

}