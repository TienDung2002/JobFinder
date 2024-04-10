package com.example.jobfinder.UI.Applicants

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jobfinder.Datas.Model.applicantModel
import com.example.jobfinder.R

class ApplicantAdapter(private val applicantList: List<applicantModel>) :
    RecyclerView.Adapter<ApplicantAdapter.ApplicantViewHolder>() {

    class ApplicantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.applicant_username)
        val textViewDescription: TextView = itemView.findViewById(R.id.applicant_des)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicantViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_applicant_model, parent, false)
        return ApplicantViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ApplicantViewHolder, position: Int) {
        val currentItem = applicantList[position]
        holder.textViewName.text = currentItem.userName
        holder.textViewDescription.text = currentItem.applicantDes
    }

    override fun getItemCount() = applicantList.size
}
