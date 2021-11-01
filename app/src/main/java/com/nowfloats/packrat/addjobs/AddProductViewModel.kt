package com.nowfloats.packrat.addjobs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.nowfloats.packrat.bottomsheetdialog.Item
import com.nowfloats.packrat.clickInterface.ProdClickListener
import com.nowfloats.packrat.roomdatabase.ProductFormData
import java.io.Serializable

class AddProductViewModel(application: Application) : AndroidViewModel(application), Serializable {
    var clickadd: MutableLiveData<Int> = MutableLiveData()
    var addBottomClick: MutableLiveData<Item> = MutableLiveData()
    var clickdeleteview: MutableLiveData<Int> = MutableLiveData()
    var deleteFormViews: MutableLiveData<Int> = MutableLiveData()
    var getProductData: MutableLiveData<ProductFormData> = MutableLiveData()
    var getData: MutableLiveData<Boolean> = MutableLiveData()

    init {

    }

    fun deleteViewOnClick(position: Int) {
        clickdeleteview.value = position
    }

    fun addViewOnClick() {
        clickadd.value = 1
    }

    fun bottomDialogClick(item: Item) {
        addBottomClick.value = item
    }

    fun deleteAddFormViews() {
        deleteFormViews.value
    }

    fun getProductFormData(productFormData: ProductFormData) {
        getProductData.value = productFormData
    }

    fun getDataForm() {
        getData.value = true
    }
}