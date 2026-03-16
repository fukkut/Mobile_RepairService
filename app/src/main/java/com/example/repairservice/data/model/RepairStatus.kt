package com.example.repairservice.data.model

enum class RepairStatus(val label: String) {
    NEW("Новий"),
    IN_PROGRESS("В роботі"),
    DONE("Готовий"),
    DELIVERED("Видано")
}