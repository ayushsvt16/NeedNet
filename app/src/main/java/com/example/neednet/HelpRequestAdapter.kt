package com.example.neednet

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class HelpRequestAdapter(
    private val context: Context,
    private val requestList: List<HelpRequest>
) : RecyclerView.Adapter<HelpRequestAdapter.HelpRequestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HelpRequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_help_request, parent, false)
        return HelpRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: HelpRequestViewHolder, position: Int) {
        val request = requestList[position]

        holder.name.text = request.title
        holder.description.text = request.category
        holder.time.text = request.distance

        holder.card.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(context, R.anim.click_animation)
            holder.card.startAnimation(animation)

            // TODO: Handle click, navigate or show details
        }
    }

    override fun getItemCount(): Int = requestList.size

    class HelpRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card: MaterialCardView = itemView.findViewById(R.id.cardHelpRequest)
        val name: TextView = itemView.findViewById(R.id.textName)
        val description: TextView = itemView.findViewById(R.id.textDescription)
        val time: TextView = itemView.findViewById(R.id.textTime)
    }
}
