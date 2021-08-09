package com.example.intern_project1.view.activities

import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intern_project1.base.BaseActivity
import com.example.intern_project1.databinding.ActivityFavoriteBinding
import com.example.intern_project1.model.entities.Favorite
import com.example.intern_project1.utils.Injection
import com.example.intern_project1.view.adapter.FavoriteAdapter
import com.example.intern_project1.viewmodel.FactoryViewModel

class FavoriteActivity : BaseActivity<FactoryViewModel, ActivityFavoriteBinding>() {

    private lateinit var adaptor: FavoriteAdapter

    override fun getViewModelFactory(): ViewModelProvider.Factory {
        return Injection.provideFactoryViewModel(this)
    }

    override fun getViewBinding(): ActivityFavoriteBinding {
        return ActivityFavoriteBinding.inflate(layoutInflater)
    }

    override fun getToolBar(): Toolbar {
        return binding.toolbar
    }

    override fun backButton(): Boolean {
        return true
    }

    override fun init() {
        initAdapter()

        favoriteViewModelObserver()
    }

    private fun favoriteViewModelObserver() {

        viewModel.allFavorite.observe(
            this,
            Observer {
                adaptor.submitList(it)
            }
        )

    }

    private fun initAdapter(){
        adaptor = FavoriteAdapter(viewModel)

        binding.rvFactoryInfo.adapter = adaptor
        binding.rvFactoryInfo.layoutManager =LinearLayoutManager(this)
    }

    fun addToFavorite(favorite: Favorite){
        viewModel.insert(favorite)
    }

}