package com.example.jobfinder.UI.FindNewJobs

import android.icu.text.NumberFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jobfinder.Datas.Model.newJobHomeData
import com.example.jobfinder.R
import java.util.Locale

class NewJobsAdapter(private var list: List<newJobHomeData>, private val noDataImage: ImageView) :
    RecyclerView.Adapter<NewJobsAdapter.NewJobViewHolder>(), Filterable {

    lateinit var mListener: onItemClickListener
    private var originalData: List<newJobHomeData> = list
    private var filteredData: List<newJobHomeData> = list

    interface onItemClickListener {
        fun onItemClicked(position: Int) {}
    }

    // Click vào từng item
    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    inner class NewJobViewHolder(view: View, listener: onItemClickListener) : RecyclerView.ViewHolder(view) {
        val avatar: ImageView
        val rec_nameNJ: TextView
        val jobTitle: TextView
        val numOfRecruits: TextView
        val numOfRecruited: TextView
        val postedTime: TextView
        val salary: TextView

        val bookmarkButton: ImageButton
        var isBookmarked = false


        init {
            avatar = view.findViewById(R.id.user_ava)
            rec_nameNJ = view.findViewById(R.id.rec_nameNJ)
            jobTitle = view.findViewById(R.id.JiPosition2)
            numOfRecruits = view.findViewById(R.id.NumOfRecruits)
            numOfRecruited = view.findViewById(R.id.NumOfRecruited)
            postedTime = view.findViewById(R.id.posttime)
            salary = view.findViewById(R.id.salary)
            bookmarkButton = view.findViewById(R.id.bookmark_btn)

            view.setOnClickListener {
                listener.onItemClicked(layoutPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewJobsAdapter.NewJobViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.new_jobs_item_home, parent, false)
        return NewJobViewHolder(view, mListener)
    }

    // gán data vào từng phần tử trong item (gán data trong Model.newJobHomeData vào textview)
    override fun onBindViewHolder(holder: NewJobViewHolder, position: Int) {
        val salaryValue = list[position].salary?.toDouble() ?: 0.0  //default salary là 0.0
        // Định dạng giá trị salary với dấu phẩy VNĐ
        val formattedSalary = NumberFormat.getNumberInstance(Locale("vi", "VN")).format(salaryValue)

        holder.avatar.setImageResource(list[position].avatar)
        holder.rec_nameNJ.text = list[position].recNameNJ
        holder.jobTitle.text = list[position].jobTitle
        holder.numOfRecruits.text = list[position].numOfRecruits
        holder.numOfRecruited.text = list[position].numOfRecruited
        holder.postedTime.text = list[position].postedTime
        holder.salary.text = formattedSalary.toString()

        // nút bookmark
        holder.bookmarkButton.setOnClickListener {
            if (holder.isBookmarked) {
                holder.bookmarkButton.setImageResource(R.drawable.ic_bookmark_grey30px)
            } else {
                holder.bookmarkButton.setImageResource(R.drawable.ic_bookmark_orange30px)
            }
            holder.isBookmarked = !holder.isBookmarked
        }
    }

    override fun getItemCount(): Int = list.size

    // Lọc data khi search
    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }


    fun resetOriginalList() {
        list = originalData
        hideNoDataFoundImg()
        notifyDataSetChanged()
    }

    fun showNoDataFoundImg() {
        noDataImage.visibility = View.VISIBLE
    }

    fun hideNoDataFoundImg() {
        noDataImage.visibility = View.GONE
    }
}