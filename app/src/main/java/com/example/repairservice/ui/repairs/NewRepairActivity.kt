package com.example.repairservice.ui.repairs

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.repairservice.R
import com.example.repairservice.data.model.Client
import com.example.repairservice.data.model.RepairOrder
import com.example.repairservice.data.model.RepairStatus
import com.example.repairservice.data.repository.RepairRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NewRepairActivity : AppCompatActivity() {

    private var selectedClient: Client? = null
    private var editingRepairId: Int = -1
    private var isEditing: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_repair)

        // Відступ для статус бару
        val rootView = findViewById<android.view.View>(android.R.id.content)
        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            val systemBars = insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemBars.top, 0, 0)
            insets
        }

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val editDeviceName = findViewById<EditText>(R.id.editDeviceName)
        val editDeviceType = findViewById<EditText>(R.id.editDeviceType)
        val editClient = findViewById<AutoCompleteTextView>(R.id.editClient)
        val editPhone = findViewById<EditText>(R.id.editPhone)
        val editProblem = findViewById<EditText>(R.id.editProblem)
        val editPrice = findViewById<EditText>(R.id.editPrice)
        val btnSave = findViewById<TextView>(R.id.btnSave)
        val titleText = findViewById<TextView>(R.id.titleText)

        btnBack.setOnClickListener { finish() }

        // Перевіряємо чи це редагування
        editingRepairId = intent.getIntExtra("repair_id", -1)
        isEditing = editingRepairId != -1

        if (isEditing) {
            // Режим редагування — заповнюємо поля
            titleText.text = "Редагувати ордер"
            btnSave.text = "Зберегти зміни"

            val repair = RepairRepository.getRepairById(editingRepairId)
            if (repair != null) {
                editDeviceName.setText(repair.deviceName)
                editDeviceType.setText(repair.deviceType)
                editProblem.setText(repair.problemDescription)
                if (repair.price > 0) editPrice.setText(repair.price.toInt().toString())

                val client = RepairRepository.getClientById(repair.clientId)
                if (client != null) {
                    selectedClient = client
                    editClient.setText(client.name)
                    editPhone.setText(client.phone)
                }
            }
        }

        // Автодоповнення клієнтів
        val clientNames = RepairRepository.getAllClients().map { it.name }
        val clientAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, clientNames)
        editClient.setAdapter(clientAdapter)

        editClient.setOnItemClickListener { _, _, position, _ ->
            val name = clientAdapter.getItem(position) ?: return@setOnItemClickListener
            selectedClient = RepairRepository.getAllClients().find { it.name == name }
            selectedClient?.let {
                editPhone.setText(it.phone)
            }
        }

        // Збереження
        btnSave.setOnClickListener {
            val deviceName = editDeviceName.text.toString().trim()
            val deviceType = editDeviceType.text.toString().trim()
            val clientName = editClient.text.toString().trim()
            val phone = editPhone.text.toString().trim()
            val problem = editProblem.text.toString().trim()
            val priceText = editPrice.text.toString().trim()

            if (deviceName.isEmpty()) {
                Toast.makeText(this, "Введіть назву пристрою", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (clientName.isEmpty()) {
                Toast.makeText(this, "Введіть ім'я клієнта", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Знайти або створити клієнта
            var client = RepairRepository.getAllClients().find {
                it.name.equals(clientName, ignoreCase = true)
            }
            if (client == null) {
                if (phone.isEmpty()) {
                    Toast.makeText(this, "Для нового клієнта введіть телефон", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                client = RepairRepository.addClient(Client(0, clientName, phone))
            }

            val price = priceText.toDoubleOrNull() ?: 0.0

            if (isEditing) {
                // Оновити існуючий ордер
                val existingRepair = RepairRepository.getRepairById(editingRepairId)
                if (existingRepair != null) {
                    val updated = existingRepair.copy(
                        deviceName = deviceName,
                        deviceType = deviceType,
                        problemDescription = problem,
                        price = price,
                        clientId = client.id
                    )
                    RepairRepository.updateRepair(editingRepairId, updated)
                    Toast.makeText(this, "Зміни збережено!", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Створити новий ордер
                val date = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())
                RepairRepository.addRepair(
                    RepairOrder(
                        id = 0,
                        deviceName = deviceName,
                        deviceType = deviceType,
                        problemDescription = problem,
                        workDone = "",
                        price = price,
                        status = RepairStatus.NEW,
                        clientId = client.id,
                        createdDate = date
                    )
                )
                Toast.makeText(this, "Ордер успішно створено!", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }
}