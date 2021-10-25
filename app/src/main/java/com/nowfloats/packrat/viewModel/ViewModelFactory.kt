package com.nowfloats.packrat.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nowfloats.packrat.repository.MyRepository

class ViewModelFactory(private val myRepository: MyRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MyViewModel(myRepository) as T
    }
}