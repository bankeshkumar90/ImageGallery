package com.nowfloats.packrat.jobstatus

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nowfloats.packrat.R

import android.os.Build

import com.bumptech.glide.Glide
import com.nowfloats.packrat.utils.AppConstant


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
                Glide.with(context)
                    .load( jobItem.imagePath)
                    .override(100, 100)
                    .centerInside() // scale to fill the ImageView and crop any extra
                    .into( holder.imageView)
                //holder.imageView.setImageURI(jobItem.uri)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            //holder.imageView.setImageBitmap(bitmap);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(jobItem.jobStatus.equals(AppConstant.STATUS_SUCCESS)){
                    holder.jobStausView.setTextColor(context!!.getColor(R.color.green_success))
                }else {
                    holder.jobStausView.setTextColor(context!!.getColor(R.color.blue_enqued))
                }
            }
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