package com.example.repairservice.ui.clients

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.repairservice.R
import com.example.repairservice.adapter.ClientAdapter
import com.example.repairservice.data.repository.RepairRepository
import com.example.repairservice.ui.repairs.ClientDetailActivity

class ClientsFragment : Fragment() {

    private lateinit var adapter: ClientAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_clients, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerClients)
        val editSearch = view.findViewById<EditText>(R.id.editSearchClient)

        adapter = ClientAdapter(RepairRepository.getAllClients()) { client ->
            val intent = Intent(requireContext(), ClientDetailActivity::class.java)
            intent.putExtra("client_id", client.id)
            startActivity(intent)
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        editSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim()
                if (query.isEmpty()) {
                    adapter.updateList(RepairRepository.getAllClients())
                } else {
                    adapter.updateList(RepairRepository.searchClients(query))
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        adapter.updateList(RepairRepository.getAllClients())
    }
}