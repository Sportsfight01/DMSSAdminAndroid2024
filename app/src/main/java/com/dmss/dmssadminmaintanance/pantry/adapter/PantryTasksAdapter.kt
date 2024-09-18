package com.dmss.dmssadminmaintanance.pantry.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dmss.dmssadminmaintanance.R
import com.dmss.dmssadminmaintanance.model.CheckBoxModel

class PantryTasksAdapter(val fragmentFrom:String,val callBack: (List<CheckBoxModel>) -> Unit)  : RecyclerView.Adapter<PantryTasksAdapter.ViewHolder>() {
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
       return items!!.size
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
        var imIcon: ImageView

        init {
            mCheckedTextView = itemView.findViewById(R.id.checked_text_view)
            tvHheader = itemView.findViewById(R.id.tv_header)
            tvDate = itemView.findViewById(R.id.tv_date)
            imIcon = itemView.findViewById(R.id.icon)

            itemView.setOnClickListener(this)
        }

        fun bind(position: Int) {
            // check the state of the model
            if (!items!![position].checked) {
                mCheckedTextView.isChecked = false
            } else {
                mCheckedTextView.isChecked = true
            }
            if(fragmentFrom.equals( "Pantry")){
                imIcon.setImageResource(R.drawable.pantry)
            }else if(fragmentFrom.equals("Rest Room")){
                imIcon.setImageResource(R.drawable.toilet)

            }
//            mCheckedTextView.setText(String.valueOf(items!![position].text))
            tvHheader.text = items!![position].text.replace("_"," ").replaceFirstChar ( Char::uppercase)
//            tvDate.setText(String.valueOf(items!![position].date))
            if(items!![position].assignTo!="") {
                tvDate.setText("Assigned To : " + items!![position].assignTo)
            }else{
                tvDate.setText("Open ")

            }

//            println("tvHheader:: "+items!![position].text)

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