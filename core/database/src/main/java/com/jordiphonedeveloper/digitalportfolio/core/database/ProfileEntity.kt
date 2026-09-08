package com.jordiphonedeveloper.digitalportfolio.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "professional_profile")
data class ProfileEntity(
    @PrimaryKey val id: Int = PROFILE_ID,
    val jsonPayload: String,
    val contentUpdatedAt: String,
    val cachedAtEpochMillis: Long,
) {
    companion object {
        const val PROFILE_ID = 1
    }
}
