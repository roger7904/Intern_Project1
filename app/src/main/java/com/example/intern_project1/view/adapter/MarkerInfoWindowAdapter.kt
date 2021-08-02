package com.example.intern_project1.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.example.intern_project1.R
import com.example.intern_project1.databinding.MarkerInfoContentsBinding
import com.example.intern_project1.model.entities.FactoryObject
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class MarkerInfoWindowAdapter(private val context: Context) : GoogleMap.InfoWindowAdapter {
    override fun getInfoContents(marker: Marker?): View? {

        return null
    }

    override fun getInfoWindow(marker: Marker?): View? {
        val factoryInfo = marker?.tag as? FactoryObject.DataX ?: return null

        val view = LayoutInflater.from(context).inflate(R.layout.marker_info_contents, null)
        val binding = MarkerInfoContentsBinding.bind(view)
        binding.tvTitle.text = factoryInfo.name
        binding.tvAddress.text = factoryInfo.address
        binding.tvPhone.text = factoryInfo.phone

        return view
    }
}