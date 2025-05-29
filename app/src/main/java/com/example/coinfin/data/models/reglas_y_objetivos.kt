package com.example.coinfin.data.models

class reglas_y_objetivos {
    data class SavingGoal(val amount: Double, val resetDay: Int)
    data class UserRule(val category: String, val limit: Double)
    data class UserPreferences(val goal: SavingGoal, val rules: List<UserRule>)
}