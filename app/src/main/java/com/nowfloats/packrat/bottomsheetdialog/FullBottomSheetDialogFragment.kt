package com.nowfloats.packrat.bottomsheetdialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.annotation.NonNull
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nowfloats.packrat.R
import java.util.*

class FullBottomSheetDialogFragment : BottomSheetDialogFragment(), ItemAdapter.ItemListener {
    private var mBehavior: BottomSheetBehavior<View>? = null

    @NonNull
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: BottomSheetDialog =
            super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view = View.inflate(getContext(), R.layout.sheet, null)
        view.findViewById<View>(R.id.fakeShadow).visibility = View.GONE
        val recyclerView: RecyclerView = view.findViewById<View>(R.id.recyclerView) as RecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.setLayoutManager(LinearLayoutManager(getContext()))
        val itemAdapter = ItemAdapter(createItems(),  this)
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
        items.add(Item(R.drawable.ic_preview_24dp, "Product"))
        items.add(Item(R.drawable.ic_share_24dp, "Price"))
        items.add(Item(R.drawable.ic_link_24dp, "Barcode"))
        items.add(Item(R.drawable.ic_content_copy_24dp, "Quantity"))
        items.add(Item(R.drawable.ic_content_copy_24dp, "Others"))
        return items
    }

    override fun onItemClick(item: Item?) {
        mBehavior?.setState(BottomSheetBehavior.STATE_HIDDEN)
    }
}