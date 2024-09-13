package com.dmss.dmssadminmaintanance.pantry.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dmss.dmssadminmaintanance.R
import com.dmss.dmssadminmaintanance.model.CheckBoxModel
import java.lang.String

class PantryTasksAdapter(val callBack: (List<CheckBoxModel>) -> Unit)  : RecyclerView.Adapter<PantryTasksAdapter.ViewHolder>() {
    var items: List<CheckBoxModel>? = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context: Context = parent.context
        val layoutForItem: Int = R.layout.pantry_items
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(layoutForItem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return if (items == null) {
            0
        } else items!!.size
    }

    fun loadItems(tournaments: List<CheckBoxModel>?) {
        items = tournaments
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var mCheckedTextView: CheckedTextView
        var tvHheader: TextView
        var tvDate: TextView

        init {
            mCheckedTextView = itemView.findViewById(R.id.checked_text_view)
            tvHheader = itemView.findViewById(R.id.tv_header)
            tvDate = itemView.findViewById(R.id.tv_date)
            itemView.setOnClickListener(this)
        }

        fun bind(position: Int) {
            // check the state of the model
            if (!items!![position].checked) {
                mCheckedTextView.isChecked = false
            } else {
                mCheckedTextView.isChecked = true
            }
//            mCheckedTextView.setText(String.valueOf(items!![position].text))
            tvHheader.setText(String.valueOf(items!![position].text))
//            tvDate.setText(String.valueOf(items!![position].date))
            tvDate.setText("12-09-2024")

            println("tvHheader:: "+items!![position].text)

        }

        override fun onClick(v: View?) {
            val adapterPosition = adapterPosition
            if (!items!![adapterPosition].checked) {
                mCheckedTextView.isChecked = true
                items!![adapterPosition].checked = true
            } else {
                mCheckedTextView.isChecked = false
                items!![adapterPosition].checked = false
            }
            var filterItems=items!!.filter { it.checked }
            callBack.invoke(filterItems)

        }
    }
}