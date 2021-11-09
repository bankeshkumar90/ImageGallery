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
    var addBottomClickPosition: MutableLiveData<Int> = MutableLiveData()
    var clickdeleteview: MutableLiveData<Int> = MutableLiveData()
    var deleteFormViews: MutableLiveData<Int> = MutableLiveData()
    var getProductData: MutableLiveData<ProductFormData> = MutableLiveData()
    var saveMetaData: MutableLiveData<Boolean> = MutableLiveData()
    var fragmentMapObj:HashMap<Int, ArrayList<metaDataBeanItem>> = HashMap<Int, ArrayList<metaDataBeanItem>>()

    init {
        print("meesaage1212")
    }

    fun deleteViewOnClick(position: Int) {
        clickdeleteview.value = position
    }

    fun addViewOnClick() {
        clickadd.value = 1
    }

    fun bottomDialogClick(item: Item, position: Int) {
        addBottomClick.value = item
        addBottomClickPosition.value = position
    }

    fun deleteAddFormViews() {
        deleteFormViews.value
    }

    fun getProductFormData(productFormData: ProductFormData) {
        getProductData.value = productFormData
    }

    fun saveFormMetaData() {
        saveMetaData.value = true
    }

    fun updateFragmentIndex(tabPosition: Int, fragmentObj: ArrayList<metaDataBeanItem>){
        fragmentMapObj.put(tabPosition, fragmentObj)
    }
}