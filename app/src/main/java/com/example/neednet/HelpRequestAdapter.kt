package com.example.neednet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HelpRequestAdapter(private val helpRequests: List<HelpRequest>) :
    RecyclerView.Adapter<HelpRequestAdapter.HelpRequestViewHolder>() {

    inner class HelpRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        val tvDistance: TextView = itemView.findViewById(R.id.tvDistance)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HelpRequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_help_request, parent, false)
        return HelpRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: HelpRequestViewHolder, position: Int) {
        val request = helpRequests[position]
        holder.tvTitle.text = request.title
        holder.tvCategory.text = request.category
        holder.tvDistance.text = "${request.distance} km away"
    }

    override fun getItemCount(): Int = helpRequests.size
}
