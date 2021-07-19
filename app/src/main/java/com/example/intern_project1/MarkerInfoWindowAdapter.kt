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

package com.example.intern_project1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.example.intern_project1.model.network.entities.entities.FactoryObject
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class MarkerInfoWindowAdapter(private val context: Context) : GoogleMap.InfoWindowAdapter {
    override fun getInfoContents(marker: Marker?): View? {
        // 1. Get tag
        val factoryInfo = marker?.tag as? FactoryObject.DataX ?: return null

        // 2. Inflate view and set title, address and rating
        val view = LayoutInflater.from(context).inflate(R.layout.marker_info_contents, null)
        view.findViewById<TextView>(R.id.tv_title).text = factoryInfo.name
        view.findViewById<TextView>(R.id.tv_address).text = factoryInfo.address
        view.findViewById<TextView>(R.id.tv_phone).text = factoryInfo.phone

        return view
    }

    override fun getInfoWindow(marker: Marker?): View? {
        // Return null to indicate that the default window (white bubble) should be used
        return null
    }
}