package dev.vadzimv.paraphrase

import android.content.Context
import android.content.SharedPreferences

interface KeyValueStorage {
    fun save(key: String, value: String)
    fun read(key: String): String?
}

class SharedPreferenceKeyValueStorage(context: Context) : KeyValueStorage {

    private val preference: SharedPreferences = context.getSharedPreferences("app-settings", Context.MODE_PRIVATE)


    override fun save(key: String, value: String) {
        preference.edit().putString(key, value).apply()
    }

    override fun read(key: String): String? {
        return preference.getString(key, null)
    }

}