package com.example.repairservice.ui.repairs

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.repairservice.R
import com.example.repairservice.data.model.RepairStatus
import com.example.repairservice.data.repository.RepairRepository

class RepairDetailActivity : AppCompatActivity() {

    private var repairId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repair_detail)

        repairId = intent.getIntExtra("repair_id", -1)

        findViewById<ImageView>(R.id.btnBack).setOnClickListener { finish() }
        findViewById<ImageView>(R.id.btnEdit).setOnClickListener {
            val intent = Intent(this, NewRepairActivity::class.java)
            intent.putExtra("repair_id", repairId)
            startActivity(intent)
        }

        loadRepairData()
    }

    override fun onResume() {
        super.onResume()
        loadRepairData()
    }

    private fun loadRepairData() {
        val repair = RepairRepository.getRepairById(repairId) ?: return
        val client = RepairRepository.getClientById(repair.clientId)

        findViewById<TextView>(R.id.textDeviceName).text = repair.deviceName
        findViewById<TextView>(R.id.textDeviceType).text = repair.deviceType
        findViewById<TextView>(R.id.textDate).text = repair.createdDate
        findViewById<TextView>(R.id.textProblem).text = repair.problemDescription.ifEmpty { "—" }
        findViewById<TextView>(R.id.textWorkDone).text = repair.workDone.ifEmpty { "—" }
        findViewById<TextView>(R.id.textPrice).text = if (repair.price > 0) "${repair.price.toInt()} ₴" else "—"

        // Статус бейдж
        val textStatus = findViewById<TextView>(R.id.textStatus)
        textStatus.text = repair.status.label
        applyStatusStyle(textStatus, repair.status)

        // Клієнт
        if (client != null) {
            findViewById<TextView>(R.id.textClientName).text = client.name
            findViewById<TextView>(R.id.textClientPhone).text = client.phone
            findViewById<TextView>(R.id.textClientInitial).text = client.name.first().toString()

            findViewById<LinearLayout>(R.id.clientCard).setOnClickListener {
                val intent = Intent(this, ClientDetailActivity::class.java)
                intent.putExtra("client_id", client.id)
                startActivity(intent)
            }
        }

        // Кнопки зміни статусу
        setupStatusButtons(repair.status)
    }

    private fun setupStatusButtons(currentStatus: RepairStatus) {
        val container = findViewById<LinearLayout>(R.id.statusContainer)
        container.removeAllViews()

        for (status in RepairStatus.values()) {
            val btn = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                val lp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                lp.marginEnd = if (status != RepairStatus.DELIVERED) 6 else 0
                layoutParams = lp
                setPadding(8, 18, 8, 18)

                if (status == currentStatus) {
                    setBackgroundResource(getStatusBg(status))
                } else {
                    setBackgroundResource(R.drawable.bg_status_button)
                }
            }

            // Кружечок кольору
            val dot = View(this).apply {
                val dotLp = LinearLayout.LayoutParams(8.dpToPx(), 8.dpToPx())
                dotLp.bottomMargin = 8
                layoutParams = dotLp
                setBackgroundColor(getStatusColor(status))
            }

            val label = TextView(this).apply {
                text = status.label
                textSize = 9f
                gravity = Gravity.CENTER
                setTextColor(
                    if (status == currentStatus) getStatusColor(status)
                    else getColor(R.color.text_muted)
                )
            }

            btn.addView(dot)
            btn.addView(label)

            btn.setOnClickListener {
                RepairRepository.updateRepairStatus(repairId, status)
                Toast.makeText(this, "Статус змінено на \"${status.label}\"", Toast.LENGTH_SHORT).show()
                loadRepairData()
            }

            container.addView(btn)
        }
    }

    private fun applyStatusStyle(view: TextView, status: RepairStatus) {
        when (status) {
            RepairStatus.NEW -> {
                view.setBackgroundResource(R.drawable.bg_status_new)
                view.setTextColor(getColor(R.color.status_new))
            }
            RepairStatus.IN_PROGRESS -> {
                view.setBackgroundResource(R.drawable.bg_status_in_progress)
                view.setTextColor(getColor(R.color.status_in_progress))
            }
            RepairStatus.DONE -> {
                view.setBackgroundResource(R.drawable.bg_status_done)
                view.setTextColor(getColor(R.color.status_done))
            }
            RepairStatus.DELIVERED -> {
                view.setBackgroundResource(R.drawable.bg_status_delivered)
                view.setTextColor(getColor(R.color.status_delivered))
            }
        }
    }

    private fun getStatusBg(status: RepairStatus): Int = when (status) {
        RepairStatus.NEW -> R.drawable.bg_status_new
        RepairStatus.IN_PROGRESS -> R.drawable.bg_status_in_progress
        RepairStatus.DONE -> R.drawable.bg_status_done
        RepairStatus.DELIVERED -> R.drawable.bg_status_delivered
    }

    private fun getStatusColor(status: RepairStatus): Int = when (status) {
        RepairStatus.NEW -> getColor(R.color.status_new)
        RepairStatus.IN_PROGRESS -> getColor(R.color.status_in_progress)
        RepairStatus.DONE -> getColor(R.color.status_done)
        RepairStatus.DELIVERED -> getColor(R.color.status_delivered)
    }

    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()
}