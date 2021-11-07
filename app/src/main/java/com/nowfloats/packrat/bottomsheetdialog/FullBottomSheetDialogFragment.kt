package com.nowfloats.packrat.bottomsheetdialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.annotation.NonNull
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nowfloats.packrat.R
import com.nowfloats.packrat.addjobs.AddProductViewModel
import java.text.FieldPosition
import java.util.*

class FullBottomSheetDialogFragment(var recyclerViewPosition:Int) : BottomSheetDialogFragment(), ItemAdapter.ItemListener {
    private var mBehavior: BottomSheetBehavior<View>? = null
    private lateinit var dataDialogModel: AddProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataDialogModel =
            ViewModelProviders.of(requireActivity()).get(AddProductViewModel::class.java)
    }

    @NonNull
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: BottomSheetDialog =
            super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view = View.inflate(getContext(), R.layout.sheet, null)
        view.findViewById<View>(R.id.fakeShadow).visibility = View.GONE
        val recyclerView: RecyclerView = view.findViewById<View>(R.id.recyclerView) as RecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.setLayoutManager(LinearLayoutManager(getContext()))
        val itemAdapter = ItemAdapter(createItems(), this, recyclerViewPosition)
        recyclerView.setAdapter(itemAdapter)
        dialog.setContentView(view)
        mBehavior = BottomSheetBehavior.from(view.parent as View)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        mBehavior?.setState(BottomSheetBehavior.STATE_EXPANDED)
    }

    fun createItems(): List<Item> {
        val items = ArrayList<Item>()
        items.add(Item(R.drawable.ic_preview_24dp, context!!.getString(R.string.product)))
        items.add(Item(R.drawable.ic_share_24dp, context!!.getString(R.string.price)))
        items.add(Item(R.drawable.ic_link_24dp, context!!.getString(R.string.barcode)))
        items.add(Item(R.drawable.ic_content_copy_24dp, context!!.getString(R.string.quantity)))
        items.add(Item(R.drawable.ic_content_copy_24dp, context!!.getString(R.string.other)))
        return items
    }

    override fun onItemClick(item: Item?, position: Int) {
        mBehavior?.setState(BottomSheetBehavior.STATE_HIDDEN)
        dataDialogModel.bottomDialogClick(item!!, position)
    }
}