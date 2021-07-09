package com.example.intern_project1.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intern_project1.databinding.ActivityMainBinding
import com.example.intern_project1.model.network.entities.entities.FactoryObject
import com.example.intern_project1.view.adapter.FactoryInfoAdapter
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

        mbinding.swipeRefresh.setOnRefreshListener {
            mFactoryViewModel.getFactoryInfoFromApi()
        }
    }

    private fun factoryViewModelObserver() {

        mFactoryViewModel.response.observe(
            this,
            Observer { response ->
                response?.let {
                    setResponseToAdapter(response)

                    Log.i("FactoryInfo Response", "${response.data.data[0]}")
                }
            })

        mFactoryViewModel.loadingError.observe(
            this,
            Observer { dataError ->
                dataError?.let {
                    mbinding.swipeRefresh.isRefreshing=!dataError //沒處理loadingError
                    Log.i("API Error", "$dataError")
                }
            })

        mFactoryViewModel.loading.observe(this, Observer { loading ->
            loading?.let {
                mbinding.swipeRefresh.isRefreshing=loading
                Log.i("Loading", "$loading")
            }
        })
    }

    private fun setResponseToAdapter(response : FactoryObject.FactoryInfo){
        mbinding.rvFactorInfo.layoutManager = LinearLayoutManager(this)

        val factoryInfoAdapter = FactoryInfoAdapter(this,
            response.data.data as ArrayList<FactoryObject.DataX>
        )

        mbinding.rvFactorInfo.adapter = factoryInfoAdapter
    }

}



