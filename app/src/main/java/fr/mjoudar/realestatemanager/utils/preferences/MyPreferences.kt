package fr.mjoudar.realestatemanager.utils.preferences

import kotlinx.coroutines.flow.Flow

interface MyPreferences {
    fun isCurrencyEuro(): Flow<Boolean>
    suspend fun toggleCurrency()
}