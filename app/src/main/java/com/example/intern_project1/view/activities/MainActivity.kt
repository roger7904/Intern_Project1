package com.example.intern_project1.view.activities

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingSource
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intern_project1.R
import com.example.intern_project1.databinding.ActivityMainBinding
import com.example.intern_project1.model.network.FactoryPagingSource
import com.example.intern_project1.model.network.entities.entities.FactoryObject
import com.example.intern_project1.model.repository.FactoryInfoRepository
import com.example.intern_project1.utils.Injection
import com.example.intern_project1.view.adapter.FactoryInfoAdapter
import com.example.intern_project1.view.adapter.FactoryLoadStateAdapter
import com.example.intern_project1.viewmodel.FactoryViewModel
import com.example.intern_project1.viewmodel.FactoryViewModelFactory
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var mbinding: ActivityMainBinding
    private lateinit var mFactoryViewModel: FactoryViewModel
    private lateinit var pagingDataAdaptor: FactoryInfoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mbinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mbinding.root)


        mFactoryViewModel = ViewModelProvider(this, Injection.provideFactoryViewModel(this)).get(
            FactoryViewModel::class.java)

        pagingDataAdaptor = FactoryInfoAdapter()

        mFactoryViewModel.getFactoryInfoPagingData()



        mbinding.rvFactorInfo.apply {
            layoutManager= LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = pagingDataAdaptor

        }

        mbinding.rvFactorInfo.adapter =
            pagingDataAdaptor.withLoadStateFooter(
                footer = FactoryLoadStateAdapter{
                    pagingDataAdaptor.retry()
                })


        pagingDataAdaptor.addLoadStateListener { loadState ->
            //show progress bar when the load state is Loading
            mbinding.swipeRefresh.isRefreshing = loadState.source.refresh is LoadState.Loading

            //load state for error and show the msg on UI
            mbinding.llError.isVisible = loadState.source.refresh is LoadState.Error

            mbinding.rvFactorInfo.isVisible =  !mbinding.llError.isVisible

            if (loadState.source.refresh is LoadState.Error){
                mbinding.btnRetry.setOnClickListener {
                    pagingDataAdaptor.retry()
                }

                mbinding.llError.isVisible = loadState.source.refresh is LoadState.Error

                val errorMessage =(loadState.source.refresh as LoadState.Error).error.message
                mbinding.tvErrorMessage.text = errorMessage
            }

            mbinding.swipeRefresh.setOnRefreshListener {
                mFactoryViewModel.getFactoryInfoPagingData()
            }
        }


        factoryViewModelObserver()
    }

    private fun factoryViewModelObserver() {

        mFactoryViewModel.factoryInfoPagingData.observe(
            this,
            Observer {
                pagingDataAdaptor.submitData(lifecycle, it)
            }
        )

        mFactoryViewModel.loading.observe(this, Observer { loading ->
            loading?.let {
                mbinding.swipeRefresh.isRefreshing=loading
                Log.i("Loading", "$loading")
            }
        })
    }
}


