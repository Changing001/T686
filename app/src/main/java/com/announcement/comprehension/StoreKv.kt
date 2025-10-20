package com.announcement.comprehension

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


object StoreKv {


    private  val KEY_FILE_LIST = "key_file_list"

    suspend fun getFileList(): MutableSet<String> {
        val set:Set<String> =  getSet(KEY_FILE_LIST)
        return set.toMutableSet();
    }

    suspend fun putFileList(value:MutableSet<String>){
        putSet(KEY_FILE_LIST,value)
    }



    private val Context.dataStore by preferencesDataStore("draw_image_323")

    private lateinit var mAppContext: Context
    fun init(mContext: Context){
        mAppContext = mContext.applicationContext
    }


    private  suspend fun putInt(key: String, value: Int) {
        dataStorePfn().edit { pfn ->
            pfn[intPreferencesKey(key)] = value
        }
    }

    private suspend fun getInt(key: String): Int {
        val flow: Int? = dataStorePfn().data.map { pfn ->
            val vvv = pfn[intPreferencesKey(key)]
            vvv
        }.first()
        return flow ?: 0
    }

    private suspend fun putString(key: String, value: String) {
        dataStorePfn().edit { pfn ->
            pfn[stringPreferencesKey(key)] = value
        }
    }

    private suspend fun getString(key: String): String {
        val flow: String? = dataStorePfn().data.map { pfn ->
            val vvv = pfn[stringPreferencesKey(key)]
            vvv
        }.first()
        return flow ?: ""
    }


    suspend fun putSet(key: String, value: Set<String>) {
        dataStorePfn().edit { pfn ->
            pfn[stringSetPreferencesKey(key)] = value
        }
    }

    private  suspend fun getSet(key: String): Set<String> {
        val flow: Set<String>? = dataStorePfn().data.map { pfn ->
            val vvv = pfn[stringSetPreferencesKey(key)]
            vvv
        }.first()
        return flow ?: setOf()
    }

    private fun dataStorePfn(): DataStore<Preferences> {
        return mAppContext.dataStore
    }

//    fun dataStorePfn(mContext: Context): DataStore<Preferences> {
//        val app = mContext.applicationContext
//        return app.dataStore
//    }




}