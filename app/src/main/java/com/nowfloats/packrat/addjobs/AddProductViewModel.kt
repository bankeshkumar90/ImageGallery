package com.nowfloats.packrat.addjobs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.nowfloats.packrat.bottomsheetdialog.Item
import com.nowfloats.packrat.clickInterface.ProdClickListener
import com.nowfloats.packrat.roomdatabase.ProductFormData
import java.io.Serializable
import java.lang.Exception

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

    fun deletFragmentData(deletedTabPos:Int, totalRemaining:Int): Int{
        var calculateHeaderMap:HashMap<Int, ArrayList<metaDataBeanItem>> = HashMap<Int, ArrayList<metaDataBeanItem>>()
        try {
            fragmentMapObj.get(deletedTabPos)?.clear()
            if(fragmentMapObj.size>0){
                    if(deletedTabPos==0){
                        for ((key, value) in fragmentMapObj) {
                            if(key!=0) {
                                calculateHeaderMap.put(key -1 , value)
                            }
                        }
                    }else if(deletedTabPos == totalRemaining){
                        for ((key, value) in fragmentMapObj) {
                            if(key!=totalRemaining) {
                                if(key > deletedTabPos)
                                    calculateHeaderMap.put(key -1 , value)
                                else
                                    calculateHeaderMap.put(key , value)
                            }
                        }
                    }else if(deletedTabPos < totalRemaining){
                        for ((key, value) in fragmentMapObj) {
                            if(key!= deletedTabPos) {
                                if(key > deletedTabPos)
                                    calculateHeaderMap.put(key -1 , value)
                                else
                                    calculateHeaderMap.put(key , value)
                            }
                        }
                    }


            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        finally {
            fragmentMapObj = calculateHeaderMap
        }

        return fragmentMapObj.size
    }
    fun clearFragmentData(){
        fragmentMapObj.clear()
    }

    fun deleteFragmentObjectItem(selcetedPosition: Int, itemDeletPosition: Int){
        try {
            if(fragmentMapObj.size>0){
                fragmentMapObj.get(selcetedPosition)?.removeAt(itemDeletPosition)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}