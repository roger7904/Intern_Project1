package com.example.intern_project1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.intern_project1.databinding.ActivityMainBinding
import com.example.intern_project1.viewmodel.FactoryViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var mbinding: ActivityMainBinding

    private lateinit var mFactoryViewModel: FactoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mbinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mbinding.root)

        mFactoryViewModel = ViewModelProvider(this).get(FactoryViewModel::class.java)

        mFactoryViewModel.getFactoryInfoFromApi()

        factoryViewModelObserver()
    }

    private fun factoryViewModelObserver() {

        mFactoryViewModel.response.observe(
            this,
            Observer { response ->
                response?.let {
                    Log.i("FactoryInfo Response", "${response.data.data[0]}")
                }
            })

        mFactoryViewModel.loadingError.observe(
            this,
            Observer { dataError ->
                dataError?.let {
                    Log.i("API Error", "$dataError")
                }
            })

        mFactoryViewModel.loading.observe(this, Observer { loading ->
            loading?.let {
                Log.i("Loading", "$loading")
            }
        })
    }
}



