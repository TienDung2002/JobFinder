package com.example.jobfinder.UI.Applicants

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jobfinder.Datas.Model.ApplicantsModel
import com.example.jobfinder.R
import com.example.jobfinder.UI.UsersProfile.ProfileViewModel
import com.example.jobfinder.Utils.RetriveImg

class ApplicantAdapter(private val applicantList: List<ApplicantsModel>) :
    RecyclerView.Adapter<ApplicantAdapter.ApplicantViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(applicant: ApplicantsModel)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    class ApplicantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.applicant_username)
        val textViewDescription: TextView = itemView.findViewById(R.id.applicant_des)
        val imgView :ImageView = itemView.findViewById(R.id.applicant_user_avt)
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

        RetriveImg.retrieveImage(currentItem.userId.toString(), holder.imgView)

        holder.itemView.setOnClickListener {
            listener?.onItemClick(currentItem)
        }
    }

    override fun getItemCount() = applicantList.size
}
