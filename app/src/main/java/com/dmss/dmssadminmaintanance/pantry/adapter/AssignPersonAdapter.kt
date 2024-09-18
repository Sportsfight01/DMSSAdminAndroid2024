package com.dmss.dmssadminmaintanance.pantry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dmss.dmssadminmaintanance.databinding.AssignedItemLayoutBinding
import com.dmss.dmssadminmaintanance.databinding.ListTieamBinding
import com.dmss.dmssadminmaintanance.db.AssignTaskToPersonEntityData
import com.dmss.dmssadminmaintanance.model.CheckBoxModel
import com.dmss.dmssadminmaintanance.model.TaskData
import com.dmss.dmssadminmaintanance.pantry.adapter.TasksListStatusAdapter.ViewHolder

class AssignPersonAdapter(val callback:(AssignTaskToPersonEntityData)->Unit) :
    RecyclerView.Adapter<AssignPersonAdapter.ViewHolder> () {
    var items: List<AssignTaskToPersonEntityData>? = ArrayList()
    fun loadItems(tournaments: List<AssignTaskToPersonEntityData>?) {
        items = tournaments
        notifyDataSetChanged()
    }
    inner class ViewHolder(val binding:AssignedItemLayoutBinding): RecyclerView.ViewHolder(binding.root),View.OnClickListener{
        override fun onClick(p0: View?) {

        }
        fun bind(item: AssignTaskToPersonEntityData) {
            binding.title.text= item.name
            binding.close.setOnClickListener {
                println()
                callback.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = AssignedItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        println("items!!.size:: "+items!!.size)
        return if (items == null) {
            0
        } else items!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items?.get(position)!!)

    }

}