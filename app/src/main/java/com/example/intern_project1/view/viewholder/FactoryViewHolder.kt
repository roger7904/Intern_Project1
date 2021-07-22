package com.example.intern_project1.view.viewholder
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.intern_project1.R
import com.example.intern_project1.databinding.ItemFactoryListLayoutBinding
import com.example.intern_project1.model.network.entities.entities.FactoryObject
import com.example.intern_project1.utils.Constants

class FactoryViewHolder(private val mbinding: ItemFactoryListLayoutBinding) : RecyclerView.ViewHolder(mbinding.root) {

    private var factoryInfo: FactoryObject.DataX? = null

    fun bind(factoryInfo: FactoryObject.DataX) {
        this.factoryInfo=factoryInfo

        mbinding.tvCity.text = factoryInfo.city
        mbinding.tvTitle.text = factoryInfo.name
        mbinding.tvAddress.text = if (factoryInfo.address.isEmpty()) "未提供地址" else factoryInfo.address
        mbinding.tvPhone.text = if (factoryInfo.phone.isEmpty()) "未提供電話" else factoryInfo.phone
        mbinding.tvBrandname.text = if (factoryInfo.tags.isEmpty()) "全車系" else factoryInfo.tags

        val imageResult = if(factoryInfo.maintenance_plant_photo.isEmpty()) R.drawable.test_picture
                        else Constants.IMAGE_API_URL+factoryInfo.maintenance_plant_photo[0].filename
        Glide.with(itemView)
            .load(imageResult)
            .into(mbinding.ivFactoryImage)


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