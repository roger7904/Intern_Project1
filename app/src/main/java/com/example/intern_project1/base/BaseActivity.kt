package com.example.intern_project1.base

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<VM : ViewModel,B : ViewBinding> : AppCompatActivity() {

    lateinit var factory: ViewModelProvider.Factory
    lateinit var viewModel: VM
    lateinit var binding: B
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        factory = getViewModelFactory()
        binding = getViewBinding()
        toolbar = getToolBar()
        viewModel = ViewModelProvider(this, factory).get(getViewModelClass())
        setContentView(binding.root)
        setSupportActionBar(toolbar)
        init()
    }

    private fun getViewModelClass(): Class<VM> {
        val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
        return type as Class<VM>
    }

    abstract fun getViewModelFactory(): ViewModelProvider.Factory

    abstract fun getViewBinding(): B
    
    abstract fun getToolBar(): Toolbar

    abstract fun init()

}