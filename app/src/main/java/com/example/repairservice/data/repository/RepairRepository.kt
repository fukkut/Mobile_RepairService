package com.example.repairservice.data.repository

import com.example.repairservice.data.model.Client
import com.example.repairservice.data.model.RepairOrder
import com.example.repairservice.data.model.RepairStatus

object RepairRepository {

    val clients = arrayListOf(
        Client(1, "Олександр Петренко", "+380 67 123 45 67"),
        Client(2, "Марія Коваленко", "+380 50 987 65 43"),
        Client(3, "Ігор Шевченко", "+380 63 456 78 90"),
        Client(4, "Анна Бондаренко", "+380 97 111 22 33")
    )

    val repairs = arrayListOf(
        RepairOrder(1, "MacBook Pro 14\"", "Ноутбук", "Не заряджається батарея, швидко розряджається", "Заміна батареї", 3200.0, RepairStatus.IN_PROGRESS, 1, "12.03.2026"),
        RepairOrder(2, "iPhone 15 Pro", "Смартфон", "Розбитий екран після падіння", "Заміна дисплейного модуля", 5800.0, RepairStatus.NEW, 2, "13.03.2026"),
        RepairOrder(3, "Samsung Galaxy S24", "Смартфон", "Не працює Face ID", "Діагностика модуля", 1500.0, RepairStatus.DONE, 3, "10.03.2026"),
        RepairOrder(4, "Dell XPS 15", "Ноутбук", "Перегрівається при навантаженні", "Чистка + заміна термопасти", 900.0, RepairStatus.DELIVERED, 1, "05.03.2026"),
        RepairOrder(5, "iPad Air 5", "Планшет", "Не працює сенсор у верхній частині", "", 0.0, RepairStatus.NEW, 4, "14.03.2026")
    )

    private var nextRepairId = 6
    private var nextClientId = 5

    // Отримати всі ремонти
    fun getAllRepairs(): List<RepairOrder> = repairs

    // Отримати всіх клієнтів
    fun getAllClients(): List<Client> = clients

    // Знайти ремонт за id
    fun getRepairById(id: Int): RepairOrder? = repairs.find { it.id == id }

    // Знайти клієнта за id
    fun getClientById(id: Int): Client? = clients.find { it.id == id }

    // Ремонти конкретного клієнта
    fun getRepairsByClientId(clientId: Int): List<RepairOrder> = repairs.filter { it.clientId == clientId }
}