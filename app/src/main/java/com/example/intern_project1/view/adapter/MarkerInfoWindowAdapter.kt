// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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