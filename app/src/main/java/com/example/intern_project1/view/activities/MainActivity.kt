package com.example.intern_project1.view.activities

import android.content.Intent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intern_project1.R
import com.example.intern_project1.base.BaseActivity
import com.example.intern_project1.databinding.ActivityMainBinding
import com.example.intern_project1.utils.Injection
import com.example.intern_project1.view.adapter.FactoryInfoAdapter
import com.example.intern_project1.view.adapter.FactoryLoadStateAdapter
import com.example.intern_project1.viewmodel.FactoryViewModel


class MainActivity : BaseActivity<FactoryViewModel,ActivityMainBinding>() {

    private lateinit var pagingDataAdaptor: FactoryInfoAdapter

    override fun getViewModelFactory(): ViewModelProvider.Factory {
        return Injection.provideFactoryViewModel(this)
    }

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun getToolBar(): Toolbar {
        return binding.toolbar
    }

    override fun init() {
        viewModel.getFactoryInfoPagingData()

        initAdapter()

        factoryViewModelObserver()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.back -> {
                val intent = Intent(this, MapActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun factoryViewModelObserver() {

        viewModel.factoryInfoPagingData.observe(
            this,
            Observer {
                pagingDataAdaptor.submitData(lifecycle, it)
            }
        )
    }

    private fun initAdapter(){

        pagingDataAdaptor = FactoryInfoAdapter()

        binding.rvFactorInfo.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true) // item改變不會影響rv寬高，避免重新計算，耗資源
            adapter = pagingDataAdaptor
            adapter = pagingDataAdaptor.withLoadStateFooter(
                footer = FactoryLoadStateAdapter{
                    pagingDataAdaptor.retry()
                })
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }

        pagingDataAdaptor.addLoadStateListener { loadState ->
            binding.run {
                //show progress bar when the load state is Loading
                swipeRefresh.isRefreshing = loadState.source.refresh is LoadState.Loading
                //load state for error and show the msg on UI
                llError.isVisible = loadState.source.refresh is LoadState.Error

                rvFactorInfo.isVisible =  !llError.isVisible

                if (loadState.source.refresh is LoadState.Error){
                    btnRetry.setOnClickListener {
                        pagingDataAdaptor.retry() //paging library 會觸發 pagingSource.load()
                    }

                    llError.isVisible = loadState.source.refresh is LoadState.Error

                    val errorMessage =(loadState.source.refresh as LoadState.Error).error.message
                    tvErrorMessage.text = errorMessage
                }

                swipeRefresh.setOnRefreshListener {
                    viewModel.getFactoryInfoPagingData()
                }
            }
        }
    }



}


