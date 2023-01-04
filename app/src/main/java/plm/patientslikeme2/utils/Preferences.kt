package plm.patientslikeme2.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import plm.patientslikeme2.data.di.MyApplication

object Preferences {

    var sharedpreferences: SharedPreferences? = null
    var editor: Editor? = null

    @SuppressLint("CommitPrefEdits")
    fun setValue(key: String?, value: String?) {
        sharedpreferences = MyApplication.appContext.getSharedPreferences(
            Constants.Preference,
            Context.MODE_PRIVATE
        )
        editor = sharedpreferences?.edit()
        editor?.putString(key, value)
        editor?.apply()
    }

    @SuppressLint("CommitPrefEdits")
    fun setValue(key: String?, value: Int) {
        sharedpreferences = MyApplication.appContext.getSharedPreferences(
            Constants.Preference,
            Context.MODE_PRIVATE
        )
        editor = sharedpreferences?.edit()
        editor?.putInt(key, value)
        editor?.apply()
    }

    @SuppressLint("CommitPrefEdits")
    fun setValue(key: String?, value: Long) {
        sharedpreferences = MyApplication.appContext.getSharedPreferences(
            Constants.Preference,
            Context.MODE_PRIVATE
        )
        editor = sharedpreferences?.edit()
        editor?.putLong(key, value)
        editor?.apply()
    }

    @SuppressLint("CommitPrefEdits")
    fun setValue(key: String?, value: Boolean?) {
        sharedpreferences = MyApplication.appContext.getSharedPreferences(
            Constants.Preference,
            Context.MODE_PRIVATE
        )
        editor = sharedpreferences?.edit()
        editor?.putBoolean(key, value ?: false)
        editor?.apply()
    }

    fun getValue(key: String?, defaultValue: String?): String? {
        sharedpreferences = MyApplication.appContext.getSharedPreferences(
            Constants.Preference,
            Context.MODE_PRIVATE
        )
        return sharedpreferences?.getString(key, defaultValue)
    }

    fun getValue(key: String?, defaultValue: Int): Int {
        sharedpreferences = MyApplication.appContext.getSharedPreferences(
            Constants.Preference,
            Context.MODE_PRIVATE
        )
        return sharedpreferences!!.getInt(key, defaultValue)
    }

    fun getValue(key: String?, defaultValue: Long): Long {
        sharedpreferences = MyApplication.appContext.getSharedPreferences(
            Constants.Preference,
            Context.MODE_PRIVATE
        )
        return sharedpreferences!!.getLong(key, defaultValue)
    }

    fun getValue(key: String?, defaultValue: Boolean): Boolean {
        sharedpreferences = MyApplication.appContext.getSharedPreferences(
            Constants.Preference,
            Context.MODE_PRIVATE
        )
        return sharedpreferences!!.getBoolean(key, defaultValue)
    }

    @SuppressLint("CommitPrefEdits")
    fun clearAll() {
        sharedpreferences = MyApplication.appContext.getSharedPreferences(
            Constants.Preference,
            Context.MODE_PRIVATE
        )
        editor = sharedpreferences?.edit()
        editor?.clear()
        editor?.apply()
    }

    @SuppressLint("CommitPrefEdits")
    fun removeValue(key: String?) {
        sharedpreferences = MyApplication.appContext.getSharedPreferences(
            Constants.Preference,
            Context.MODE_PRIVATE
        )
        editor = sharedpreferences?.edit()
        editor?.remove(key)
        editor?.apply()
    }
}