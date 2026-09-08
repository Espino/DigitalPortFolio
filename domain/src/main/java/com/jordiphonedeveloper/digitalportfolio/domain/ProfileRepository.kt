package com.jordiphonedeveloper.digitalportfolio.domain

import com.jordiphonedeveloper.digitalportfolio.core.model.ProfessionalProfile
import com.jordiphonedeveloper.digitalportfolio.core.model.ProfileDataSource
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun observeProfile(): Flow<ProfessionalProfile?>
    suspend fun ensureLocalProfile(): Boolean
    suspend fun refreshProfile(): ProfileRefreshResult
}

sealed interface ProfileRefreshResult {
    data class Success(
        val source: ProfileDataSource,
        val notice: String? = null,
    ) : ProfileRefreshResult

    data class Failure(val message: String) : ProfileRefreshResult
}
