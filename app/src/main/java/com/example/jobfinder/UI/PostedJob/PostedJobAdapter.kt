package com.example.jobfinder.UI.PostedJob

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jobfinder.Datas.Model.JobModel
import com.example.jobfinder.R
import com.example.jobfinder.Utils.GetData

class PostedJobAdapter(private val context: Context, private val jobList: List<JobModel>) :
    RecyclerView.Adapter<PostedJobAdapter.PostedJobViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(job: JobModel)
    }

    private var listener: OnItemClickListener? = null

    // Phương thức để thiết lập listener
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostedJobViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_posted_job, parent, false)
        return PostedJobViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostedJobViewHolder, position: Int) {
        val job = jobList[position]

        // Bind data to views
        holder.jobTitleTextView.text = job.jobTitle
        holder.numOfRecruitsTextView.text = job.empAmount
        holder.numOfRecruitedTxtView.text= job.numOfRecruited
        holder.salaryTextView.text = job.salaryPerEmp
        holder.postTimeTextView.text = GetData.getDateFromString(job.postDate.toString())

        holder.itemView.setOnClickListener {
            listener?.onItemClick(job)
        }
    }

    override fun getItemCount(): Int {
        return jobList.size
    }

    inner class PostedJobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val jobTitleTextView: TextView = itemView.findViewById(R.id.posted_job_job_title)
        val numOfRecruitsTextView: TextView = itemView.findViewById(R.id.NumOfRecruits)
        val numOfRecruitedTxtView: TextView = itemView.findViewById(R.id.NumOfRecruited)
        val salaryTextView: TextView = itemView.findViewById(R.id.salary)
        val postTimeTextView: TextView = itemView.findViewById(R.id.posttime)
    }
}