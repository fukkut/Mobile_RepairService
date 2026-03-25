package com.example.repairservice.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.repairservice.R
import com.example.repairservice.data.model.RepairOrder
import com.example.repairservice.data.model.RepairStatus
import com.example.repairservice.data.repository.RepairRepository

class RepairAdapter(
    private var repairs: List<RepairOrder>,
    private val onItemClick: (RepairOrder) -> Unit
) : RecyclerView.Adapter<RepairAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textDeviceName: TextView = view.findViewById(R.id.textDeviceName)
        val textStatus: TextView = view.findViewById(R.id.textStatus)
        val textClientName: TextView = view.findViewById(R.id.textClientName)
        val textDate: TextView = view.findViewById(R.id.textDate)
        val textPrice: TextView = view.findViewById(R.id.textPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_repair, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repair = repairs[position]
        val client = RepairRepository.getClientById(repair.clientId)

        holder.textDeviceName.text = repair.deviceName
        holder.textClientName.text = client?.name ?: "Невідомий клієнт"
        holder.textDate.text = repair.createdDate
        holder.textPrice.text = if (repair.price > 0) "${repair.price.toInt()} ₴" else "—"

        // Статус бейдж
        holder.textStatus.text = repair.status.label
        when (repair.status) {
            RepairStatus.NEW -> {
                holder.textStatus.setBackgroundResource(R.drawable.bg_status_new)
                holder.textStatus.setTextColor(holder.itemView.context.getColor(R.color.status_new))
            }
            RepairStatus.IN_PROGRESS -> {
                holder.textStatus.setBackgroundResource(R.drawable.bg_status_in_progress)
                holder.textStatus.setTextColor(holder.itemView.context.getColor(R.color.status_in_progress))
            }
            RepairStatus.DONE -> {
                holder.textStatus.setBackgroundResource(R.drawable.bg_status_done)
                holder.textStatus.setTextColor(holder.itemView.context.getColor(R.color.status_done))
            }
            RepairStatus.DELIVERED -> {
                holder.textStatus.setBackgroundResource(R.drawable.bg_status_delivered)
                holder.textStatus.setTextColor(holder.itemView.context.getColor(R.color.status_delivered))
            }
        }

        holder.itemView.setOnClickListener { onItemClick(repair) }
    }

    override fun getItemCount(): Int = repairs.size

    fun updateList(newRepairs: List<RepairOrder>) {
        repairs = newRepairs
        notifyDataSetChanged()
    }
}