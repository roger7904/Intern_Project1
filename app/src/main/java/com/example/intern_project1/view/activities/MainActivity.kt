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

    private val mDisposable = CompositeDisposable()

    private lateinit var mbinding: ActivityMainBinding
    private lateinit var mFactoryViewModel: FactoryViewModel
//    private lateinit var repository: FactoryInfoRepository
//    private lateinit var pagingSource: FactoryPagingSource
    private lateinit var pagingDataAdaptor: FactoryInfoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mbinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mbinding.root)


        mFactoryViewModel = ViewModelProvider(this, Injection.provideFactoryViewModel(this)).get(
            FactoryViewModel::class.java)

        pagingDataAdaptor = FactoryInfoAdapter()

        mFactoryViewModel.getFactoryInfoPagingData()

        //mFactoryViewModel.getFactoryInfoFromApi()

        mbinding.swipeRefresh.setOnRefreshListener {
            mFactoryViewModel.getFactoryInfoPagingData()
        }

        mbinding.rvFactorInfo.apply {
            layoutManager= LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = pagingDataAdaptor

        }

//        mbinding.rvFactorInfo.adapter =
//            pagingDataAdaptor.withLoadStateFooter(
//                footer = FactoryLoadStateAdapter{
//                    pagingDataAdaptor.retry()
//                })

//        pagingDataAdaptor.addLoadStateListener { loadState ->
//            //show progress bar when the load state is Loading
//            mbinding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
//
//            //load state for error and show the msg on UI
//            val errorState = loadState.source.append as? LoadState.Error
//                ?: loadState.source.prepend as? LoadState.Error
//                ?: loadState.append as? LoadState.Error
//                ?: loadState.prepend as? LoadState.Error
//
//            errorState?.let {
//                AlertDialog.Builder(this)
//                    .setTitle(R.string.error)
//                    .setMessage(it.error.localizedMessage)
//                    .setNegativeButton(R.string.cancel) { dialog, _ ->
//                        dialog.dismiss()
//                    }
//                    .setPositiveButton(R.string.retry) { _, _ ->
//                        pagingDataAdaptor.retry()
//                    }
//                    .show()
//            }
//        }

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

//        mFactoryViewModel.response.observe(
//            this,
//            Observer { response ->
//                response?.let {
//                    setResponseToAdapter(response)
//
//                    Log.i("FactoryInfo Response", "${response.data.data[0]}")
//                }
//            })

//        mFactoryViewModel.loadingError.observe(
//            this,
//            Observer { dataError ->
//                dataError?.let {
//                    mbinding.swipeRefresh.isRefreshing=!dataError //沒處理loadingError
//                    Log.i("API Error", "$dataError")
//                }
//            })
//

//    }

//    private fun setResponseToAdapter(response : FactoryObject.FactoryInfo){
//        mbinding.rvFactorInfo.layoutManager = LinearLayoutManager(this)
//
//        val factoryInfoAdapter = FactoryInfoAdapter(this,
//            response.data.data as ArrayList<FactoryObject.DataX>
//        )
//
//        mbinding.rvFactorInfo.adapter = factoryInfoAdapter
//    }

}


