package com.example.studentmanager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qlsvapp.R
import com.example.qlsvapp.Student

class StudentAdapter(
    private var studentList: MutableList<Student>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    interface OnItemClickListener {
        fun onItemLongClick(position: Int, student: Student)
    }

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvHoTen: TextView = itemView.findViewById(R.id.name)
        val tvMssv: TextView = itemView.findViewById(R.id.mssv)

        init {
            itemView.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener.onItemLongClick(position, studentList[position])
                    true
                } else {
                    false
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val currentStudent = studentList[position]
        holder.tvHoTen.text = currentStudent.name
        holder.tvMssv.text = currentStudent.mssv
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    fun updateList(newList: List<Student>) {
        studentList.clear()
        studentList.addAll(newList)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        studentList.removeAt(position)
        notifyItemRemoved(position)
    }
}