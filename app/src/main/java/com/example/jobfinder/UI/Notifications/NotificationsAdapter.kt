package com.example.jobfinder.UI.Notifications

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.jobfinder.Datas.Model.NotificationsRowModel
import com.example.jobfinder.databinding.RowNotificationsBinding

class NotificationsAdapter(
  var list: MutableList<NotificationsRowModel>,
  private val context: Context,
  private val noNotiLayout: ConstraintLayout
) : RecyclerView.Adapter<NotificationsAdapter.RowNotificationsVH>() {
  private var clickListener: OnItemClickListener? = null
  init {
    checkEmptyAdapter()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowNotificationsVH {
    val binding = RowNotificationsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return RowNotificationsVH(binding)
  }

  override fun onBindViewHolder(holder: RowNotificationsVH, position: Int) {
    val notificationsRowModel = list[position]
    holder.bind(notificationsRowModel)

    holder.itemView.setOnClickListener {
      selectedNotification = notificationsRowModel
      val dialog = NotificationDetailDialog(context, selectedNotification)
      dialog.show()
    }

  }

  private var selectedNotification: NotificationsRowModel? = null

  override fun getItemCount(): Int = list.size

  fun updateData(newData: List<NotificationsRowModel>) {
    list.clear()
    list.addAll(newData)
    notifyDataSetChanged()
    checkEmptyAdapter()
  }

  fun removeItem(position: Int) {
    if (position in 0 until list.size) {
      list.removeAt(position)
      notifyItemRemoved(position)
      notifyItemRangeChanged(position, list.size - position)
      checkEmptyAdapter()
    }
  }


  private fun checkEmptyAdapter() {
    if (list.isEmpty()) {
      noNotiLayout.visibility = View.VISIBLE
    } else {
      noNotiLayout.visibility = View.GONE
    }
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
  ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    init {
      binding.txtDelete.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
      if (view == binding.txtDelete) {
        val position = adapterPosition
        if (position != RecyclerView.NO_POSITION) {
          // Gọi phương thức xóa mục trong danh sách
          clickListener?.onItemClick(view, position, list[position])
        }
      }
    }

    fun bind(item: NotificationsRowModel) {
      binding.apply {
        txtApplicationsen.text = item.from
        txtApplicationsfo.text = item.detail
        txtDate.text = item.date
      }
    }
  }

}
