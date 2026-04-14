package com.shashank.extrack.ui.settings

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class AutoDeletePrefs(private val context: Context) {

    private val KEY_POLICY = stringPreferencesKey("auto_delete_policy")

    val policyFlow: Flow<AutoDeletePolicy> = context.dataStore.data.map { prefs ->
        val value = prefs[KEY_POLICY] ?: AutoDeletePolicy.NEVER.name
        runCatching { AutoDeletePolicy.valueOf(value) }.getOrDefault(AutoDeletePolicy.NEVER)
    }

    suspend fun setPolicy(policy: AutoDeletePolicy) {
        context.dataStore.edit { prefs ->
            prefs[KEY_POLICY] = policy.name
        }
    }
}