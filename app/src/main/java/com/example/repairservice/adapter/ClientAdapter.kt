package com.example.repairservice.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.repairservice.R
import com.example.repairservice.data.model.Client
import com.example.repairservice.data.repository.RepairRepository

class ClientAdapter(
    private var clients: List<Client>,
    private val onItemClick: (Client) -> Unit
) : RecyclerView.Adapter<ClientAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textInitial: TextView = view.findViewById(R.id.textInitial)
        val textName: TextView = view.findViewById(R.id.textName)
        val textPhone: TextView = view.findViewById(R.id.textPhone)
        val textRepairsCount: TextView = view.findViewById(R.id.textRepairsCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_client, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val client = clients[position]

        holder.textInitial.text = client.name.first().toString()
        holder.textName.text = client.name
        holder.textPhone.text = client.phone
        holder.textRepairsCount.text = RepairRepository.getRepairsByClientId(client.id).size.toString()

        holder.itemView.setOnClickListener { onItemClick(client) }
    }

    override fun getItemCount(): Int = clients.size

    fun updateList(newClients: List<Client>) {
        clients = newClients
        notifyDataSetChanged()
    }
}