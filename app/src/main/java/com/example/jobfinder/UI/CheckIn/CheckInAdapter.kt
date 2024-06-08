package com.example.jobfinder.UI.CheckIn

import android.annotation.SuppressLint
import android.content.Context
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
import com.example.jobfinder.Utils.CheckTime
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
        val confirmTxt:TextView = itemView.findViewById(R.id.check_in_confirm)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): empInJobViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_check_in_for_nuser, parent, false)

        return empInJobViewHolder(itemView)
    }

    @SuppressLint("ResourceAsColor", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: empInJobViewHolder, position: Int) {

        val currentItem = approvedJobList[position]

        holder.startTime.text = currentItem.startHr
        holder.endTime.text = currentItem.endHr
        holder.textViewJobTitle.text = currentItem.jobTitle
        holder.checkInTime.visibility = View.GONE
        holder.checkBtn.visibility = View.GONE
        holder.confirmTxt.visibility = View.GONE

        val uid = GetData.getCurrentUserId()

        val checkInDb = FirebaseDatabase.getInstance().getReference("NUserCheckIn").child(currentItem.jobId.toString())
        val bUserCheckInDb = FirebaseDatabase.getInstance().getReference("CheckInFromBUser").child(currentItem.jobId.toString())
        val today = GetData.getCurrentDateTime()
        val todayTime = GetData.getTimeFromString(today)
        val currentDayString = GetData.getDateFromString(today)
        val currentDay = GetData.formatDateForFirebase(currentDayString)
        // lấy dữ liệu từ fb về xem đã check in chưa
        checkInDb.child(currentDay).child(uid.toString()).get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {

                val checkInStatus =
                    dataSnapshot.child("status").getValue(String::class.java).toString()
                // kiểm tra buser đã xác nhận check in chưa
                if(checkInStatus != "uncomfirmed checked in") {
                    // hiện tại sau endTime thì sẽ mở check out
                    if(CheckTime.calculateMinuteDiff(currentItem.endHr.toString(), todayTime)>= 0) {

                        val checkOutTime =
                            dataSnapshot.child("checkOutTime").getValue(String::class.java).toString()
                        // kiểm tra check out time có rỗng không
                        if (checkOutTime == "") {
                            holder.checkBtn.setText(R.string.check_out)
                            holder.checkBtn.visibility = View.VISIBLE
                            holder.checkBtn.setOnClickListener {
                                // lấy thời gian lúc ấn nút đểm làm thời gian chấm công
                                val currentTime =
                                    GetData.getTimeFromString(GetData.getCurrentDateTime())
                                setCheckedOutBtn(holder.checkBtn)

                                val updateCheckOutTime = hashMapOf<String, Any>(
                                    "checkOutTime" to currentTime,
                                    "status" to "checked out"
                                )

                                checkInDb.child(currentDay).child(uid.toString())
                                    .updateChildren(updateCheckOutTime)

                                bUserCheckInDb.child(currentDay).child(uid.toString())
                                    .updateChildren(updateCheckOutTime)
                            }
                        } else {
                            holder.checkBtn.visibility = View.VISIBLE
                            setCheckedOutBtn(holder.checkBtn)
                        }
                    }else{
                        // có dữ liệu sẽ hiển thị nút check -> checked
                        val checkInTime =
                            dataSnapshot.child("checkInTime").getValue(String::class.java).toString()
                        setCheckInTime(checkInTime, currentItem.startHr.toString(), holder.checkInTime)
                        holder.confirmTxt.setText(R.string.confirmed)
                        holder.confirmTxt.visibility = View.VISIBLE
                        holder.checkBtn.visibility = View.VISIBLE
                        setCheckedInBtn(holder.checkBtn)
                    }
                }else{
                    // có dữ liệu sẽ hiển thị nút check -> checked nhưng chưa được xác nhận sẽ hiển thị unconfirm
                    val checkInTime =
                        dataSnapshot.child("checkInTime").getValue(String::class.java).toString()
                    setCheckInTime(checkInTime, currentItem.startHr.toString(), holder.checkInTime)
                    holder.confirmTxt.setText(R.string.unconfirmed)
                    holder.confirmTxt.visibility = View.VISIBLE
                    holder.checkBtn.visibility = View.VISIBLE
                    setCheckedInBtn(holder.checkBtn)
                }

            } else {
                // kiểm tra giờ hiện tại nêú trong khoảng trước startTime 15' đến sau startTime 30' thì sẽ hiên nút check in
                if(CheckTime.calculateMinuteDiff(currentItem.startHr.toString(), todayTime) in -15.. 30) {
                    // khi không có dữ liệu sẽ cho phép ấn nút hoạt động
                    holder.checkBtn.visibility = View.VISIBLE
                    holder.checkBtn.setOnClickListener {
                        // lấy thời gian lúc ấn nút đểm làm thời gian điểm danh
                        val currentTime = GetData.getTimeFromString(GetData.getCurrentDateTime())
                        setCheckedInBtn(holder.checkBtn)
                        setCheckInTime(currentTime, currentItem.startHr.toString(), holder.checkInTime)
                        holder.confirmTxt.setText(R.string.unconfirmed)
                        holder.confirmTxt.visibility = View.VISIBLE

                        val checkIn = CheckInFromBUserModel(uid.toString(), today, currentTime, "", "uncomfirmed checked in")

                        checkInDb.child(currentDay).child(uid.toString()).setValue(checkIn)
                    }
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

    private fun setCheckInTime(checkInTime:String, startTime:String, checkTimeTxt:TextView){
        if(CheckTime.checkTimeBefore(checkInTime, startTime)){
            checkTimeTxt.text = context.getText(R.string.in_time)
        }else{
            val lateMinute = CheckTime.calculateMinuteDiff(startTime, checkInTime)
            val lateMessage = "${context.getString(R.string.late)} $lateMinute ${context.getString(R.string.minute)}"
            checkTimeTxt.text = lateMessage
        }
        checkTimeTxt.visibility = View.VISIBLE
    }

    private fun setCheckedInBtn(checkBtn:Button){
        checkBtn.isClickable = false
        checkBtn.setBackgroundTintList(
            ContextCompat.getColorStateList(
                context,
                R.color.gray
            )
        )
        checkBtn.setText(R.string.checked_in)
        checkBtn.setTextColor(ContextCompat.getColor(context, R.color.white))
    }

    private fun setCheckedOutBtn(checkBtn:Button){
        checkBtn.isClickable = false
        checkBtn.setBackgroundTintList(
            ContextCompat.getColorStateList(
                context,
                R.color.gray
            )
        )
        checkBtn.setText(R.string.checked_out)
        checkBtn.setTextColor(ContextCompat.getColor(context, R.color.white))
    }

}
