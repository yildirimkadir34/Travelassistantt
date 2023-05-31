package com.example.travelassistant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter(private val postList: ArrayList<Location>): RecyclerView.Adapter<Adapter.locationholder>(){

    private lateinit var mListener: onItemClickListener
    interface onItemClickListener{
        fun onItemclick(position: CharSequence)
    }
    fun setOnItemClickListener(listener: onItemClickListener){

        mListener = listener
    }





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): locationholder {
        val binding= LayoutInflater.from(parent.context).inflate(R.layout.recycler_row,parent,false)
        return locationholder(binding, mListener)

    }

    override fun getItemCount(): Int {
        return postList.size

    }

    override fun onBindViewHolder(holder: locationholder, position: Int) {
        val currentitem= postList[position]
        holder.not.text=currentitem.not
        holder.latitude.text=currentitem.latitude.toString()
        holder.Longitude.text=currentitem.longitude.toString()
        holder.sira.text=currentitem.sira.toString()




    }
    class locationholder (itemView: View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView){
        val not : TextView = itemView.findViewById(R.id.recyclerNot)
        val latitude : TextView = itemView.findViewById(R.id.recyclerLatitude)
        val Longitude: TextView = itemView.findViewById(R.id.recyclerLongitude)
        val sira : TextView = itemView.findViewById(R.id.recyclerSira)

        init {
            itemView.setOnClickListener {
                var aa= "${latitude.text}+${Longitude.text}"
                listener.onItemclick(aa)
                //listener.onItemclick(latitude.text)
            }

        }

    }


}