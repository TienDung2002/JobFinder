package com.example.jobfinder.UI.CheckIn

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.jobfinder.Datas.Model.AppliedJobModel
import com.example.jobfinder.Datas.Model.CheckInFromBUserModel
import com.example.jobfinder.R
import com.example.jobfinder.Utils.GetData
import com.google.firebase.database.FirebaseDatabase

class CheckInAdapter(private var approvedJobList: MutableList<AppliedJobModel>,
                        private val context: Context
) :
    RecyclerView.Adapter<CheckInAdapter.empInJobViewHolder>() {

    class empInJobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewJobTitle: TextView = itemView.findViewById(R.id.check_in_job_title)
        val checkInTime: TextView = itemView.findViewById(R.id.check_in_time)
        val startTime:TextView =itemView.findViewById(R.id.check_in_timeStart)
        val endTime:TextView =itemView.findViewById(R.id.check_in_timeEnd)
        val checkBtn:Button = itemView.findViewById(R.id.check_in_btn)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): empInJobViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_check_in_for_nuser, parent, false)

        return empInJobViewHolder(itemView)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: empInJobViewHolder, position: Int) {

        val currentItem = approvedJobList[position]

        holder.startTime.text = currentItem.startHr
        holder.endTime.text = currentItem.endHr
        holder.textViewJobTitle.text = currentItem.jobTitle
        holder.checkInTime.visibility = View.GONE

        val uid = GetData.getCurrentUserId()

        val checkInDb = FirebaseDatabase.getInstance().getReference("NUserCheckIn").child(currentItem.jobId.toString())
        val today = GetData.getCurrentDateTime()
        val currentDayString = GetData.getDateFromString(today)
        val currentDay = GetData.formatDateForFirebase(currentDayString.toString())
        checkInDb.child(currentDay).child(uid.toString()).get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                holder.checkInTime.text = dataSnapshot.child("checkInDate").getValue(String::class.java).toString()
                holder.checkInTime.visibility = View.VISIBLE
                holder.checkBtn.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.gray))
                holder.checkBtn.setText(R.string.checked)
                holder.checkBtn.setTextColor(ContextCompat.getColor(context, R.color.white))
            } else {
                holder.checkBtn.setOnClickListener{
                    holder.checkBtn.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.gray))
                    holder.checkBtn.setText(R.string.checked)
                    holder.checkBtn.setTextColor(ContextCompat.getColor(context, R.color.white))

                    val checkIn = CheckInFromBUserModel(uid.toString(),today,"checked")

                    checkInDb.child(currentDay).child(uid.toString()).setValue(checkIn)
                }
            }
        }.addOnFailureListener{
            // Handle failure here if necessary
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: MutableList<AppliedJobModel>) {
        approvedJobList = newList
        notifyDataSetChanged()
    }

    override fun getItemCount() = approvedJobList.size

}
