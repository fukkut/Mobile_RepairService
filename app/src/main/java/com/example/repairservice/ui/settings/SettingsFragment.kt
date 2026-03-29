package com.example.repairservice.ui.settings

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.repairservice.R
import com.example.repairservice.data.repository.RepairRepository
import java.io.File

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnExport = view.findViewById<LinearLayout>(R.id.btnExport)
        val btnClearData = view.findViewById<LinearLayout>(R.id.btnClearData)

        // Експорт
        btnExport.setOnClickListener {
            exportData()
        }

        // Очистка
        btnClearData.setOnClickListener {
            showClearDialog()
        }
    }

    private fun exportData() {
        try {
            val repairs = RepairRepository.getAllRepairs()
            val clients = RepairRepository.getAllClients()

            val json = buildString {
                append("{\n")
                append("  \"clients\": [\n")
                clients.forEachIndexed { i, c ->
                    append("    {\"id\": ${c.id}, \"name\": \"${c.name}\", \"phone\": \"${c.phone}\"}")
                    if (i < clients.size - 1) append(",")
                    append("\n")
                }
                append("  ],\n")
                append("  \"repairs\": [\n")
                repairs.forEachIndexed { i, r ->
                    append("    {\"id\": ${r.id}, \"device\": \"${r.deviceName}\", \"type\": \"${r.deviceType}\", ")
                    append("\"problem\": \"${r.problemDescription}\", \"work\": \"${r.workDone}\", ")
                    append("\"price\": ${r.price}, \"status\": \"${r.status.name}\", ")
                    append("\"clientId\": ${r.clientId}, \"date\": \"${r.createdDate}\"}")
                    if (i < repairs.size - 1) append(",")
                    append("\n")
                }
                append("  ]\n")
                append("}")
            }

            val dir = requireContext().getExternalFilesDir(null)
            val file = File(dir, "kontaktservice_backup.json")
            file.writeText(json)

            AlertDialog.Builder(requireContext())
                .setTitle("Дані збережено!")
                .setMessage("Файл збережено як:\nkontaktservice_backup.json")
                .setPositiveButton("Ок", null)
                .show()

        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Помилка експорту: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showClearDialog() {
        val input = android.widget.EditText(requireContext()).apply {
            hint = "Введіть ВИДАЛИТИ ДАНІ"
            textSize = 14f
            setPadding(40, 30, 40, 30)
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Очистити всі дані?")
            .setMessage("Всі ремонти та клієнти будуть видалені без можливості відновлення.\n\nДля підтвердження введіть: ВИДАЛИТИ ДАНІ")
            .setView(input)
            .setNegativeButton("Скасувати", null)
            .setPositiveButton("Видалити") { _, _ ->
                val typed = input.text.toString().trim()
                if (typed == "ВИДАЛИТИ ДАНІ") {
                    RepairRepository.repairs.clear()
                    RepairRepository.clients.clear()
                    Toast.makeText(requireContext(), "Всі дані видалено", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Невірне підтвердження. Дані не видалено.", Toast.LENGTH_SHORT).show()
                }
            }
            .show()
    }
}