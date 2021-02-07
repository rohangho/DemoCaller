package com.example.democaller.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.democaller.R
import com.example.democaller.adapter.PhoneDisplayerAdapter.MyViewHolder
import com.example.democaller.model.DisplayModel
import com.example.democaller.utility.OnSelectItem

class PhoneDisplayerAdapter(var allContact: List<DisplayModel>, val onSelectItem: OnSelectItem) :
    RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.display_container, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.contactNumber.text = allContact[position].number
        holder.contactName.text = allContact[position].name
    }

    override fun getItemCount(): Int {
        return allContact.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var contactName: TextView = itemView.findViewById(R.id.name)
        var contactNumber: TextView = itemView.findViewById(R.id.number)

        init {
            contactName.setOnClickListener(this)
            contactNumber.setOnClickListener(this)
        }


        override fun onClick(v: View?) {
            onSelectItem.onSelected(adapterPosition)
        }


    }
}