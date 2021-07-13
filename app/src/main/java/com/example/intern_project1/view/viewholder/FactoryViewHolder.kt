package com.example.intern_project1.view.viewholder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.intern_project1.R
import com.example.intern_project1.databinding.ItemFactoryListLayoutBinding
import com.example.intern_project1.model.network.entities.entities.FactoryObject

class FactoryViewHolder(private val mbinding: ItemFactoryListLayoutBinding) : RecyclerView.ViewHolder(mbinding.root) {

    private var factoryInfo: FactoryObject.DataX? = null

    fun bind(factoryInfo: FactoryObject.DataX) {
        this.factoryInfo=factoryInfo

        mbinding.tvCity.text = factoryInfo.city
        mbinding.tvTitle.text = factoryInfo.name
        mbinding.tvAddress.text = factoryInfo.address
        mbinding.tvPhone.text = factoryInfo.phone
        mbinding.tvBrandname.text = factoryInfo.tags
    }

    companion object {
        fun create(parent: ViewGroup): FactoryViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_factory_list_layout,  parent,false)

            val binding = ItemFactoryListLayoutBinding.bind(view)

            return FactoryViewHolder(
                binding
            )
        }
    }
}