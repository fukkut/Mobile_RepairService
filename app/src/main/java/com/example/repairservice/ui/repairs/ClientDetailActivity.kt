package com.example.repairservice.ui.repairs

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.repairservice.R
import com.example.repairservice.adapter.RepairAdapter
import com.example.repairservice.data.repository.RepairRepository

class ClientDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_detail)

        val clientId = intent.getIntExtra("client_id", -1)
        val client = RepairRepository.getClientById(clientId) ?: return

        findViewById<ImageView>(R.id.btnBack).setOnClickListener { finish() }

        findViewById<TextView>(R.id.textInitial).text = client.name.first().toString()
        findViewById<TextView>(R.id.textName).text = client.name
        findViewById<TextView>(R.id.textPhone).text = client.phone

        val clientRepairs = RepairRepository.getRepairsByClientId(clientId)
        val totalPrice = clientRepairs.sumOf { it.price }

        findViewById<TextView>(R.id.textRepairsCount).text = clientRepairs.size.toString()
        findViewById<TextView>(R.id.textTotalPrice).text = "${totalPrice.toInt()} ₴"

        // Список ремонтів клієнта
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerRepairs)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RepairAdapter(clientRepairs) { repair ->
            val intent = Intent(this, RepairDetailActivity::class.java)
            intent.putExtra("repair_id", repair.id)
            startActivity(intent)
        }
    }
}