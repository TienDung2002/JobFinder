package com.example.jobfinder.UI.Applicants

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.RecyclerView
import com.example.jobfinder.Datas.Model.ApplicantsModel
import com.example.jobfinder.Datas.Model.JobModel
import com.example.jobfinder.Datas.Model.NotificationsRowModel
import com.example.jobfinder.R
import com.example.jobfinder.Utils.GetData
import com.example.jobfinder.Utils.RetriveImg
import com.google.firebase.database.FirebaseDatabase

class ApplicantAdapter(private var applicantList: MutableList<ApplicantsModel>,
                       private val job:JobModel,
                       private val context: android.content.Context,
                       private val viewModel: ApplicantViewModel) :
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
        val approveBtn :Button = itemView.findViewById(R.id.approve_btn)
        val rejectBtn : Button= itemView.findViewById(R.id.reject_btn)
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

        val notiRef = FirebaseDatabase.getInstance().getReference("Notifications").child(currentItem.userId.toString())
        val curTime = GetData.getCurrentDateTime()

        RetriveImg.retrieveImage(currentItem.userId.toString(), holder.imgView)

        holder.itemView.setOnClickListener {
            listener?.onItemClick(currentItem)
        }

        holder.approveBtn.setOnClickListener {
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                viewModel.deleteApplicant(job.jobId.toString() ,currentItem.userId.toString())
                applicantList.removeAt(position)
                notifyItemRemoved(position)

                // add recruitedEmp
                val jobRef = FirebaseDatabase.getInstance().getReference("Job").child(job.BUserId.toString()).child(job.jobId.toString())
                jobRef.get().addOnSuccessListener { data ->
                    var recruitedAmount = data.child("numOfRecruited").getValue(String::class.java).toString()
                    val empAmount = data.child("empAmount").getValue(String::class.java).toString()
                    if(recruitedAmount.toInt() <= empAmount.toInt()){


                        // notification
                        val notiId = notiRef.push().key.toString()
                        val notification = NotificationsRowModel(notiId, job.BUserName.toString(),
                            getString(context,R.string.approve_from) + " ${job.jobTitle.toString()}"
                            ,curTime)
                        notiRef.child(notiId).setValue(notification)

                        Toast.makeText(
                            context,
                            getString( context,R.string.approve_success),
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{
                        Toast.makeText(
                            context,
                            getString( context,R.string.enough_Emp),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

            }
        }

        holder.rejectBtn.setOnClickListener {
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                viewModel.deleteApplicant(job.jobId.toString() ,currentItem.userId.toString())
                applicantList.removeAt(position)
                notifyItemRemoved(position)

                // notification
                val notiId = notiRef.push().key.toString()
                val notification = NotificationsRowModel(notiId, job.BUserName.toString(),
                    getString(context,R.string.reject_from) + " ${job.jobTitle.toString()}"
                    ,curTime)
                notiRef.child(notiId).setValue(notification)

                Toast.makeText(
                    context,
                    getString( context,R.string.reject_success),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: MutableList<ApplicantsModel>) {
        applicantList = newList
        notifyDataSetChanged()
    }

    override fun getItemCount() = applicantList.size

}
