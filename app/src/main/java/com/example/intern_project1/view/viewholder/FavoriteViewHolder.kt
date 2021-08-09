package com.example.intern_project1.view.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.intern_project1.R
import com.example.intern_project1.databinding.ItemFactoryListLayoutBinding
import com.example.intern_project1.model.entities.Favorite
import com.example.intern_project1.utils.Constants
import com.example.intern_project1.viewmodel.FactoryViewModel

class FavoriteViewHolder(private val mbinding: ItemFactoryListLayoutBinding) : RecyclerView.ViewHolder(mbinding.root) {
    private var favorite: Favorite? = null

    fun bind(favorite: Favorite,viewModel: FactoryViewModel) {
        this.favorite=favorite

        mbinding.tvCity.text = favorite.city
        mbinding.tvTitle.text = favorite.name
        mbinding.tvAddress.text = if (favorite.address.isEmpty()) "未提供地址" else favorite.address
        mbinding.tvPhone.text = if (favorite.phone.isEmpty()) "未提供電話" else favorite.phone
        mbinding.tvBrandname.text = if (favorite.tags.isEmpty()) "全車系" else favorite.tags
        mbinding.ivFavorite.background =
            itemView.context.resources.getDrawable(R.drawable.ic_favorite_selected)


        val imageResult = if(favorite.maintenance_plant_photo.isEmpty()) R.drawable.test_picture
        else Constants.IMAGE_API_URL+favorite.maintenance_plant_photo
        Glide.with(itemView)
            .load(imageResult)
            .into(mbinding.ivFactoryImage)

        mbinding.ivFavorite.setOnClickListener {
            viewModel.deleteById(
                favorite.id,
            )

            Toast.makeText(
                it.context,
                "已刪除收藏",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    companion object {
        fun create(parent: ViewGroup): FavoriteViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_factory_list_layout,  parent,false)

            val binding = ItemFactoryListLayoutBinding.bind(view)

            return FavoriteViewHolder(
                binding
            )
        }
    }
}