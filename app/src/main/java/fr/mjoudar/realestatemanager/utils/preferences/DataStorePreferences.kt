package fr.mjoudar.realestatemanager.utils.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import fr.mjoudar.realestatemanager.utils.preferences.DataStorePreferences.PreferenceKeys.IS_CURRENCY_EURO
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject


private const val PREFERENCES_NAME = "my_preferences"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(PREFERENCES_NAME)

class DataStorePreferences @Inject constructor(@ApplicationContext context: Context) : MyPreferences {
    private val preferences = context.dataStore

    override fun isCurrencyEuro() = preferences.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        preferences[IS_CURRENCY_EURO] ?: false
    }

    override suspend fun toggleCurrency() {
        preferences.edit {
            it[IS_CURRENCY_EURO] = !(it[IS_CURRENCY_EURO] ?: false)
        }
    }

    private object PreferenceKeys {
        val IS_CURRENCY_EURO = booleanPreferencesKey("is_currency_euro")
    }
}