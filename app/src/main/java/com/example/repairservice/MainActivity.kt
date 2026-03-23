package com.example.repairservice

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.repairservice.ui.clients.ClientsFragment
import com.example.repairservice.ui.repairs.RepairsFragment
import com.example.repairservice.ui.settings.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Відступ для статус бару
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        if (savedInstanceState == null) {
            replaceFragment(RepairsFragment())
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_repairs -> replaceFragment(RepairsFragment())
                R.id.nav_clients -> replaceFragment(ClientsFragment())
                R.id.nav_settings -> replaceFragment(SettingsFragment())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}