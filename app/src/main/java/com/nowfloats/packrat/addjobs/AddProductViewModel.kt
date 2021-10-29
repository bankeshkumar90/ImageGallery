package com.nowfloats.packrat.addjobs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.nowfloats.packrat.clickInterface.ProdClickListener
import java.io.Serializable

class AddProductViewModel(application: Application) : AndroidViewModel(application), Serializable {
    var clickadd: MutableLiveData<Int> = MutableLiveData()

    init {

    }

    fun addViewOnClick() {
        clickadd.value = 1
    }
}