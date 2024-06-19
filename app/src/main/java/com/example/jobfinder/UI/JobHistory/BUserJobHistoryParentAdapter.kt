package com.example.jobfinder.UI.JobHistory

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobfinder.Datas.Model.JobHistoryParentModel
import com.example.jobfinder.R

class BUserJobHistoryParentAdapter(private var jobList: List<JobHistoryParentModel>) :
    RecyclerView.Adapter<BUserJobHistoryParentAdapter.BUserJobHistoryParentViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<JobHistoryParentModel>) {
        jobList = newList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BUserJobHistoryParentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_buser_job_history_job_id_model, parent, false)
        return BUserJobHistoryParentViewHolder(view)
    }

    override fun onBindViewHolder(holder: BUserJobHistoryParentViewHolder, position: Int) {
        val job = jobList[position]


        holder.jobTitleTextView.text = job.jobTitle
        val adapter = BUserJobHIstoryChildAdapter(job.childernList)
        holder.childRecyclerView.adapter= adapter
        holder.childRecyclerView.layoutManager= LinearLayoutManager(holder.itemView.context)

    }

    override fun getItemCount(): Int {
        return jobList.size
    }

    inner class BUserJobHistoryParentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val jobTitleTextView: TextView = itemView.findViewById(R.id.jH_job_title)
        val childRecyclerView :RecyclerView= itemView.findViewById(R.id.buser_job_history_recycler)
    }

}