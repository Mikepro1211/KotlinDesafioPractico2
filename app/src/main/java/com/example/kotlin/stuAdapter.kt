package com.example.kotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class stuAdapter (private val  stuList: ArrayList<StudentModel>): RecyclerView.Adapter<stuAdapter.Viewholder>() {
    private lateinit var mListener: onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(clickListener : onItemClickListener){
        mListener =clickListener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.emp_list_item,parent,false)
        return Viewholder(itemView,mListener)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val  currentStu = stuList[position]
        holder.tvEmpName.text =currentStu.stNombre
    }

    override fun getItemCount(): Int {
        return stuList.size
    }

    class Viewholder (itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
         val tvEmpName :TextView = itemView.findViewById(R.id.tvEmpName)

        init{
            itemView.setOnClickListener{
                clickListener.onItemClick(adapterPosition)
            }
        }

    }

}