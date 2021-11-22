package com.nowfloats.packrat.jobstatus

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nowfloats.packrat.R
import com.nowfloats.packrat.addjobs.ProductDataAdapter
import com.nowfloats.packrat.clickInterface.ClickListener
import com.nowfloats.packrat.clickInterface.ProdClickListener
import com.nowfloats.packrat.roomdatabase.EntityClass
import com.nowfloats.packrat.imageViewHolder.ViewHolder
import kotlinx.android.synthetic.main.product_data.view.*
import android.graphics.BitmapFactory

import android.graphics.Bitmap
import android.content.Intent

import android.content.pm.ResolveInfo

import android.content.pm.PackageManager
import android.database.Cursor

import android.provider.MediaStore

import com.nowfloats.packrat.BuildConfig

import androidx.core.content.FileProvider
import com.nowfloats.packrat.utils.AppConstant
import java.io.File


//adapter for our recycler view
class JobStatusAdapter(
    private val context:Context,
    private var imagePathList: ArrayList<JobModel>
) :
    RecyclerView.Adapter<JobStatusAdapter.JobViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.job_status_items, parent, false)
        return JobViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        //val bitmap = BitmapFactory.decodeStream(context!!.getContentResolver().openInputStream(Uri.parse(imagePathList[position].imagePath)))
        try {
            var jobItem = imagePathList[position]
            try {
                holder.imageView.setImageURI(jobItem.uri)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            //holder.imageView.setImageBitmap(bitmap);
            holder.jobStausView.setText(jobItem.jobStatus)
            holder.tvEnquedOn.setText(jobItem.savedTimeStamp)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return  if (imagePathList == null) 0 else imagePathList.size
    }



    //updates the latest data of the database
    fun updateList(imageList: List<JobModel>) {
        //this.imageList = imageList
        notifyDataSetChanged()
    }


    fun deleteImageFromPreview(position: Int): Boolean{
        var itemDeleted = true
        try {
            imagePathList.removeAt(position)
            notifyDataSetChanged()
        }catch (e:Exception){
            e.printStackTrace()
        }
        return itemDeleted
    }

    fun clearAll(){
        imagePathList.clear()
        notifyDataSetChanged()
    }




    class JobViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var jobStausView: TextView
        var imageView:ImageView
        var tvEnquedOn: TextView
        init {
            jobStausView = itemView.findViewById(R.id.itemStatusView)
            imageView = itemView.findViewById(R.id.itemImageView)
            tvEnquedOn = itemView.findViewById(R.id.tvEnquedOn)

        }
    }
}