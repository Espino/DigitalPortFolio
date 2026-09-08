package com.jordiphonedeveloper.digitalportfolio.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AssetProfileDataSource(private val context: Context) {

    suspend fun readSeedJson(): String = withContext(Dispatchers.IO) {
        context.assets.open(PROFILE_ASSET).bufferedReader().use { it.readText() }
    }

    private companion object {
        const val PROFILE_ASSET = "profile.json"
    }
}
