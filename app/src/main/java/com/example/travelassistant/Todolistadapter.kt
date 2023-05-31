package com.example.travelassistant

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Todolistadapter (private val data: ArrayList<Taskinfo>): RecyclerView.Adapter<Todolistadapter.listholder>(){

    private lateinit var mListener: onItemClickListenerr
    interface onItemClickListenerr{
        fun onItemclickk(position: CharSequence)
    }
    fun setOnItemClickListenerr(listener: onItemClickListenerr){

        mListener = listener
    }





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): listholder {
        val binding= LayoutInflater.from(parent.context).inflate(R.layout.todolistview,parent,false)
        return listholder(binding, mListener)

    }

    override fun getItemCount(): Int {
        return data.size

    }

    override fun onBindViewHolder(holder: listholder, position: Int) {
        when (data[position].important.toString()){
            "0" -> holder.layout?.setBackgroundColor(Color.parseColor("#71f054"))
            "1" -> holder.layout?.setBackgroundColor(Color.parseColor("#FFA500"))
            "2" -> holder.layout?.setBackgroundColor(Color.parseColor("#F05454"))

        }

        val currentitem= data[position]
        holder.tasktext.text=currentitem.tasktext.toString()
        holder.time.text= currentitem.time.toString()
        holder.sira.text=currentitem.sira.toString()
        holder.important.text=currentitem.important.toString()






    }
    class listholder (itemView: View, listener: onItemClickListenerr): RecyclerView.ViewHolder(itemView){
        val tasktext : TextView = itemView.findViewById(R.id.title)
        val time : TextView = itemView.findViewById(R.id.time)
        val sira : TextView =itemView.findViewById(R.id.todolistsira)
        var layout: View? =itemView.findViewById(R.id.mylayout)
        val important: TextView = itemView.findViewById(R.id.important)



        init {
            itemView.setOnClickListener {
                var aa= "${sira.text}"
                listener.onItemclickk(aa)
                //listener.onItemclick(latitude.text)
            }

        }

    }


}