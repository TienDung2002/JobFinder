package com.example.jobfinder.UI.Admin.UserManagement

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.jobfinder.Datas.Model.AdminModel.BasicInfoAndRole
import com.example.jobfinder.R
import com.example.jobfinder.Utils.RetriveImg

class AdminUserManagementAdapter(private var userList: MutableList<BasicInfoAndRole>,
) :
    RecyclerView.Adapter<AdminUserManagementAdapter.UserManagementViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(userInfo: BasicInfoAndRole)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    class UserManagementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.UM_username)
        val email: TextView = itemView.findViewById(R.id.email)
        val phone_num: TextView = itemView.findViewById(R.id.phone_num)
        val address: TextView = itemView.findViewById(R.id.address)
        val role: TextView = itemView.findViewById(R.id.role)
        val accStatus: TextView = itemView.findViewById(R.id.acc_status)
        val accStatusTitle: TextView = itemView.findViewById(R.id.acc_status_title)
        val avt:ImageView = itemView.findViewById(R.id.UM_user_avt)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserManagementViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_user_management_model, parent, false)

        return UserManagementViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UserManagementViewHolder, position: Int) {
        val userInfo = userList[position]

        holder.userName.text = userInfo.userBasicInfo.name
        holder.email.text = "${holder.itemView.context.getString(R.string.email_title)} ${userInfo.userBasicInfo.email}"
        holder.phone_num.text = userInfo.userBasicInfo.phone_num
        holder.address.text = userInfo.userBasicInfo.address
        holder.accStatusTitle.text = "${holder.itemView.context.getString(R.string.status)}: "

        if(userInfo.userRole == "BUser"){
            holder.role.text = "${holder.itemView.context.getString(R.string.role_title)} ${holder.itemView.context.getString(R.string.buser)}"
        }
        if(userInfo.userRole == "NUser"){
            holder.role.text = "${holder.itemView.context.getString(R.string.role_title)} ${holder.itemView.context.getString(R.string.nuser)}"
        }
        if(userInfo.accountStatus == "active"){
            holder.accStatus.text = holder.itemView.context.getString(R.string.active)
            holder.accStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.income_color))
        }else{
            holder.accStatus.text = holder.itemView.context.getString(R.string.disable_acc)
            holder.accStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.expense_color))
        }

        RetriveImg.retrieveImage(userInfo.userBasicInfo.user_id.toString(), holder.avt)

        holder.itemView.setOnClickListener {
            listener?.onItemClick(userInfo)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: MutableList<BasicInfoAndRole>) {
        userList = newList
        notifyDataSetChanged()
    }

    override fun getItemCount() = userList.size

}