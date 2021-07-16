package com.example.intern_project1.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intern_project1.databinding.ActivityMainBinding
import com.example.intern_project1.utils.Injection
import com.example.intern_project1.view.adapter.FactoryInfoAdapter
import com.example.intern_project1.view.adapter.FactoryLoadStateAdapter
import com.example.intern_project1.viewmodel.FactoryViewModel

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

        mFactoryViewModel.getFactoryInfoPagingData()

        initAdapter()

        factoryViewModelObserver()
    }

    private fun factoryViewModelObserver() {

        mFactoryViewModel.factoryInfoPagingData.observe(
            this,
            Observer {
                pagingDataAdaptor.submitData(lifecycle, it)
            }
        )
    }

    private fun initAdapter(){
        pagingDataAdaptor = FactoryInfoAdapter()

        mbinding.rvFactorInfo.apply {
            layoutManager= LinearLayoutManager(context)
            setHasFixedSize(true) // item改變不會影響rv寬高，避免重新計算，耗資源
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
                    pagingDataAdaptor.retry() //paging library 會觸發 pagingSource.load()
                }

                mbinding.llError.isVisible = loadState.source.refresh is LoadState.Error

                val errorMessage =(loadState.source.refresh as LoadState.Error).error.message
                mbinding.tvErrorMessage.text = errorMessage
            }

            mbinding.swipeRefresh.setOnRefreshListener {
                mFactoryViewModel.getFactoryInfoPagingData()
            }
        }
    }

}


