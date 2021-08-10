package com.example.intern_project1.view.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.intern_project1.R
import com.example.intern_project1.databinding.ItemFactoryListLayoutBinding
import com.example.intern_project1.model.entities.FactoryObject
import com.example.intern_project1.model.entities.Favorite
import com.example.intern_project1.utils.Constants
import com.example.intern_project1.viewmodel.FactoryViewModel
import kotlinx.coroutines.Job


class FactoryViewHolder(private val mbinding: ItemFactoryListLayoutBinding) : RecyclerView.ViewHolder(mbinding.root) {

    private var factoryInfo: FactoryObject.DataX? = null

    fun bind(
        factoryInfo: FactoryObject.DataX,
        viewModel: FactoryViewModel,
    ) {
        this.factoryInfo=factoryInfo

        mbinding.tvCity.text = factoryInfo.city
        mbinding.tvTitle.text = factoryInfo.name
        mbinding.tvAddress.text = if (factoryInfo.address.isEmpty()) "未提供地址" else factoryInfo.address
        mbinding.tvPhone.text = if (factoryInfo.phone.isEmpty()) "未提供電話" else factoryInfo.phone
        mbinding.tvBrandname.text = if (factoryInfo.tags.isEmpty()) "全車系" else factoryInfo.tags
        if (factoryInfo.isFavorite == true){
            mbinding.ivFavorite.background =
                itemView.context.resources.getDrawable(R.drawable.ic_favorite_selected)
        }else {
            mbinding.ivFavorite.background =
                itemView.context.resources.getDrawable(R.drawable.ic_favorite_unselect)
        }
        val imageResult = if(factoryInfo.maintenance_plant_photo.isEmpty()) R.drawable.test_picture
        else Constants.IMAGE_API_URL+factoryInfo.maintenance_plant_photo[0].filename
        Glide.with(itemView)
            .load(imageResult)
            .into(mbinding.ivFactoryImage)

        mbinding.ivFavorite.setOnClickListener {
            if (factoryInfo.isFavorite == true) {
                factoryInfo.isFavorite = false
                mbinding.ivFavorite.background =
                    itemView.context.resources.getDrawable(R.drawable.ic_favorite_unselect)
                viewModel.deleteById(factoryInfo.id)
                Toast.makeText(
                    it.context,
                    "已刪除收藏",
                    Toast.LENGTH_SHORT
                ).show()
            }else {
                factoryInfo.isFavorite = true
                mbinding.ivFavorite.background =
                    itemView.context.resources.getDrawable(R.drawable.ic_favorite_selected)
                viewModel.insert(
                    Favorite(
                        factoryInfo.name,
                        if (factoryInfo.address.isEmpty()) "未提供地址" else factoryInfo.address,
                        factoryInfo.city,
                        if (factoryInfo.phone.isEmpty()) "未提供電話" else factoryInfo.phone,
                        if (factoryInfo.tags.isEmpty()) "全車系" else factoryInfo.tags,
                        if(factoryInfo.maintenance_plant_photo.isEmpty()) ""
                        else factoryInfo.maintenance_plant_photo[0].filename,
                        factoryInfo.id,
                    )
                )
                Toast.makeText(
                    it.context,
                    "已加入收藏",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

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