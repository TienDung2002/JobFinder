package com.example.jobfinder.UI.JobHistory

import com.example.jobfinder.Datas.Model.JobHistoryModel

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jobfinder.R
import com.example.jobfinder.Utils.RetriveImg

class BUserJobHIstoryChildAdapter(private var jobList: List<JobHistoryModel>) :
    RecyclerView.Adapter<BUserJobHIstoryChildAdapter.BUserJobHistoryChildViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<JobHistoryModel>) {
        jobList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BUserJobHistoryChildViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_buser_job_history_model, parent, false)
        return BUserJobHistoryChildViewHolder(view)
    }

    override fun onBindViewHolder(holder: BUserJobHistoryChildViewHolder, position: Int) {
        val job = jobList[position]
        RetriveImg.retrieveImage(job.nUserId.toString(), holder.nUserAvt)
        holder.nUserNameTxt.text = job.nUserName
        if(job.review.toString() == ""){
            holder.reviewStatusTxt.text = holder.itemView.context.getString(R.string.not_review)
        }else{
            holder.reviewStatusTxt.text = job.review
        }



    }

    override fun getItemCount(): Int {
        return jobList.size
    }

    inner class BUserJobHistoryChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nUserNameTxt: TextView = itemView.findViewById(R.id.jh_nusername)
        val reviewStatusTxt: TextView = itemView.findViewById(R.id.rv_status)
        val nUserAvt: ImageView =itemView.findViewById(R.id.nuser_ava)
    }

}