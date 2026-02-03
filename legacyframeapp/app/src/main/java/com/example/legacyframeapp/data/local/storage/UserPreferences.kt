package com.example.legacyframeapp.data.local.storage

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

// Extension to create the DataStore (preferences database).
private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    // Storage keys
    companion object {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val AUTH_TOKEN = stringPreferencesKey("auth_token")
        val USER_EMAIL = stringPreferencesKey("user_email")

        // UI configuration
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val ACCENT_COLOR = stringPreferencesKey("accent_color")
        val FONT_SCALE = floatPreferencesKey("font_scale")

        // Notification configuration
        val NOTIF_OFFERS = booleanPreferencesKey("notif_offers")
        val NOTIF_TRACKING = booleanPreferencesKey("notif_tracking")
        val NOTIF_CART = booleanPreferencesKey("notif_cart")
    }

    // --- FLOWS (Real-time reading for the UI) ---
    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { it[IS_LOGGED_IN] ?: false }
    val authToken: Flow<String?> = context.dataStore.data.map { it[AUTH_TOKEN] }
    val userEmail: Flow<String?> = context.dataStore.data.map { it[USER_EMAIL] }

    val isDarkMode: Flow<Boolean> = context.dataStore.data.map { it[IS_DARK_MODE] ?: false }
    val themeMode: Flow<String> = context.dataStore.data.map { it[THEME_MODE] ?: "system" }
    val accentColor: Flow<String> = context.dataStore.data.map { it[ACCENT_COLOR] ?: "#FF8B5C2A" }
    val fontScale: Flow<Float> = context.dataStore.data.map { it[FONT_SCALE] ?: 1.0f }

    val notifOffers: Flow<Boolean> = context.dataStore.data.map { it[NOTIF_OFFERS] ?: true }
    val notifTracking: Flow<Boolean> = context.dataStore.data.map { it[NOTIF_TRACKING] ?: true }
    val notifCart: Flow<Boolean> = context.dataStore.data.map { it[NOTIF_CART] ?: true }

    // --- SAVE FUNCTIONS (SUSPEND) ---

    suspend fun setLoggedIn(loggedIn: Boolean) {
        context.dataStore.edit { it[IS_LOGGED_IN] = loggedIn }
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { it[AUTH_TOKEN] = token }
    }

    suspend fun saveEmail(email: String) {
        context.dataStore.edit { it[USER_EMAIL] = email }
    }

    // --- SAFE READ (For use inside ViewModel coroutines) ---
    suspend fun getEmail(): String? {
        // Use firstOrNull() to read once without breaking the flow or closing the app.
        return context.dataStore.data.map { it[USER_EMAIL] }.firstOrNull()
    }

    // UI configuration
    suspend fun setThemeMode(mode: String) { context.dataStore.edit { it[THEME_MODE] = mode } }
    suspend fun setAccentColor(color: String) { context.dataStore.edit { it[ACCENT_COLOR] = color } }
    suspend fun setFontScale(scale: Float) { context.dataStore.edit { it[FONT_SCALE] = scale } }

    suspend fun setNotifOffers(enabled: Boolean) { context.dataStore.edit { it[NOTIF_OFFERS] = enabled } }
    suspend fun setNotifTracking(enabled: Boolean) { context.dataStore.edit { it[NOTIF_TRACKING] = enabled } }
    suspend fun setNotifCart(enabled: Boolean) { context.dataStore.edit { it[NOTIF_CART] = enabled } }

    // --- CLEAR DATA (LOGOUT) ---
    suspend fun clear() {
        context.dataStore.edit {
            it.remove(IS_LOGGED_IN)
            it.remove(AUTH_TOKEN)
            it.remove(USER_EMAIL)
            // Keep theme preferences to preserve the experience.
        }
    }
}