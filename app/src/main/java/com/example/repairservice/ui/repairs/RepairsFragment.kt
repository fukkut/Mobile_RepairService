package com.example.repairservice.ui.repairs

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.repairservice.R
import com.example.repairservice.adapter.RepairAdapter
import com.example.repairservice.data.model.RepairStatus
import com.example.repairservice.data.repository.RepairRepository

class RepairsFragment : Fragment() {

    private lateinit var adapter: RepairAdapter
    private lateinit var recyclerView: RecyclerView
    private var currentFilter: RepairStatus? = null
    private var searchQuery: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_repairs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerRepairs)
        val editSearch = view.findViewById<EditText>(R.id.editSearch)
        val filtersContainer = view.findViewById<LinearLayout>(R.id.filtersContainer)
        val fabNewRepair = view.findViewById<TextView>(R.id.fabNewRepair)

        // Налаштування RecyclerView
        adapter = RepairAdapter(RepairRepository.getAllRepairs()) { repair ->
            val intent = Intent(requireContext(), RepairDetailActivity::class.java)
            intent.putExtra("repair_id", repair.id)
            startActivity(intent)
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Фільтри
        setupFilters(filtersContainer)

        // Пошук
        editSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                searchQuery = s.toString().trim()
                applyFilters()
            }
        })

        // FAB
        fabNewRepair.setOnClickListener {
            val intent = Intent(requireContext(), NewRepairActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        applyFilters()
    }

    private fun setupFilters(container: LinearLayout) {
        val filters = listOf(
            null to "Всі",
            RepairStatus.NEW to "Новий",
            RepairStatus.IN_PROGRESS to "В роботі",
            RepairStatus.DONE to "Готовий",
            RepairStatus.DELIVERED to "Видано"
        )

        for ((status, label) in filters) {
            val chip = TextView(requireContext()).apply {
                text = label
                textSize = 12f
                setPadding(36, 14, 36, 14)
                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                lp.marginEnd = 16
                layoutParams = lp

                if (status == currentFilter) {
                    setBackgroundResource(R.drawable.bg_chip_active)
                    setTextColor(resources.getColor(R.color.white, null))
                } else {
                    setBackgroundResource(R.drawable.bg_chip)
                    setTextColor(resources.getColor(R.color.text_secondary, null))
                }

                setOnClickListener {
                    currentFilter = status
                    applyFilters()
                    // Оновити вигляд фільтрів
                    container.removeAllViews()
                    setupFilters(container)
                }
            }
            container.addView(chip)
        }
    }

    private fun applyFilters() {
        var repairs = RepairRepository.getAllRepairs()

        // Фільтр за статусом
        if (currentFilter != null) {
            repairs = repairs.filter { it.status == currentFilter }
        }

        // Пошук
        if (searchQuery.isNotEmpty()) {
            val client = { id: Int -> RepairRepository.getClientById(id)?.name ?: "" }
            repairs = repairs.filter {
                it.deviceName.contains(searchQuery, ignoreCase = true) ||
                        client(it.clientId).contains(searchQuery, ignoreCase = true)
            }
        }

        adapter.updateList(repairs)
    }
}