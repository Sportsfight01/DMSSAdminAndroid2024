package com.dmss.dmssadminmaintanance.pantry.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dmss.dmssadminmaintanance.databinding.TasksStatusListAdapterBinding
import com.dmss.dmssadminmaintanance.model.TaskData

class TasksListStatusAdapter  (private val dataSet: List<TaskData?>, val callBack: (TaskData) -> Unit) :
RecyclerView.Adapter<TasksListStatusAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: TasksStatusListAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(dataSet: TaskData) {
            val context = binding.root.context
            binding.title.text = dataSet.category
            binding.subTitle.text = dataSet.subCategory
            binding.date.text = dataSet.date
//            binding.taskImg.text = "Amount : " + dataSet.amount
            binding.status.text = dataSet.status
            binding.month.text = dataSet.month

            if(dataSet.status=="Completed"){
                binding.status.setTextColor(Color.parseColor("#FF018786"));
            }else if(dataSet.status=="Pending"){
                binding.status.setTextColor(Color.parseColor("#FF4081"));
            }else{
                binding.status.setTextColor(Color.parseColor("#FF03DAC5"));

            }

//            binding.tvDate.text =
//                dataSet.createdOn.convertDateFormat("dd MMM, yyyy, hh:mma")
            /* if (dataSet.status == "COMPLETED") {
                binding.tvRetry.visibility = View.GONE
                binding.tvStatus.setTextColor(Color.parseColor("#FF009688"))
            } else if(dataSet.status.equals("initiated",ignoreCase = true)){
                binding.tvStatus.setTextColor(Color.parseColor("#FFFF5722"))
                binding.tvRetry.visibility = View.VISIBLE
                binding.tvRetry.text = "Retry"
            }
            else {
                binding.tvStatus.setTextColor(Color.parseColor("#FFFF5722"))
                binding.tvRetry.visibility = View.VISIBLE

            }
            binding.tvRetry.setOnClickListener {
                callBack.invoke(dataSet)
            }*/
            binding.cvLayout.setOnClickListener {
                callBack.invoke(dataSet)
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = TasksStatusListAdapterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSet[position]!!)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}