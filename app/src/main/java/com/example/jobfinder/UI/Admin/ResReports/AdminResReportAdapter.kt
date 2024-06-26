package com.example.jobfinder.UI.Admin.ResReports

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jobfinder.Datas.Model.SupportUser
import com.example.jobfinder.R

class AdminResReportAdapter(private var reportList: List<SupportUser>) :
    RecyclerView.Adapter<AdminResReportAdapter.ReportViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(report: SupportUser)
    }

    private var listener: OnItemClickListener? = null

    // Phương thức để thiết lập listener
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<SupportUser>) {
        reportList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.row_report_res_model, parent, false)
        return ReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val report = reportList[position]

        // Bind data to views
        holder.reportTitleTextView.text = report.status
        holder.reportDesTextView.text = report.description
    }

    override fun getItemCount(): Int {
        return reportList.size
    }

    inner class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reportTitleTextView: TextView = itemView.findViewById(R.id.report_res_report_title)
        val reportDesTextView: TextView = itemView.findViewById(R.id.report_des)
    }
}

