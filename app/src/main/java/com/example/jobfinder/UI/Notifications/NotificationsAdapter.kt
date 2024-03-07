package com.example.jobfinder.UI.Notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jobfinder.Datas.Model.NotificationsRowModel
import com.example.jobfinder.databinding.RowNotificationsBinding

class NotificationsAdapter(
  var list: List<NotificationsRowModel>
) : RecyclerView.Adapter<NotificationsAdapter.RowNotificationsVH>() {
  private var clickListener: OnItemClickListener? = null

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowNotificationsVH {
    val binding = RowNotificationsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return RowNotificationsVH(binding)
  }

  override fun onBindViewHolder(holder: RowNotificationsVH, position: Int) {
    val notificationsRowModel = list[position]
    holder.bind(notificationsRowModel)
  }

  override fun getItemCount(): Int = list.size

  fun updateData(newData: List<NotificationsRowModel>) {
    list = newData
    notifyDataSetChanged()
  }

  fun setOnItemClickListener(clickListener: OnItemClickListener) {
    this.clickListener = clickListener
  }

  interface OnItemClickListener {
    fun onItemClick(
      view: View,
      position: Int,
      item: NotificationsRowModel
    ) {
    }
  }

  inner class RowNotificationsVH(
    private val binding: RowNotificationsBinding
  ) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: NotificationsRowModel) {
      binding.apply {
        txtApplicationsen.text = item.txtApplicationsen
        txtApplicationsfo.text = item.txtApplicationsfo
        txtDuration.text = item.txtDuration
        txtDelete.text = item.txtDelete
      }
    }
  }
}
