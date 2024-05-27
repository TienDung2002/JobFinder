package com.example.jobfinder.UI.JobEmpList

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.jobfinder.Datas.Model.ApplicantsModel
import com.example.jobfinder.R
import com.example.jobfinder.Utils.RetriveImg

class JobEmpListAdapter(private var applicantList: MutableList<ApplicantsModel>,
                        private val context: Context
) :
    RecyclerView.Adapter<JobEmpListAdapter.empInJobViewHolder>() {

    class empInJobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.emp_in_job_username)
        val imgView :ImageView = itemView.findViewById(R.id.emp_in_job_user_avt)
        val checkBtn:Button = itemView.findViewById(R.id.check_in_btn)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): empInJobViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_emp_in_job_model, parent, false)

        return empInJobViewHolder(itemView)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: empInJobViewHolder, position: Int) {
        val currentItem = applicantList[position]
        holder.textViewName.text = currentItem.userName

        RetriveImg.retrieveImage(currentItem.userId.toString(), holder.imgView)

        holder.checkBtn.setOnClickListener{
            holder.checkBtn.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.gray))
            holder.checkBtn.setText(R.string.checked)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: MutableList<ApplicantsModel>) {
        applicantList = newList
        notifyDataSetChanged()
    }

    override fun getItemCount() = applicantList.size

}
