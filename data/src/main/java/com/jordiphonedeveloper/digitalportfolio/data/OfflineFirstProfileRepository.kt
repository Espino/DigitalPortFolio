package com.jordiphonedeveloper.digitalportfolio.data

import com.jordiphonedeveloper.digitalportfolio.core.database.ProfileDao
import com.jordiphonedeveloper.digitalportfolio.core.database.ProfileEntity
import com.jordiphonedeveloper.digitalportfolio.core.model.ProfessionalProfile
import com.jordiphonedeveloper.digitalportfolio.core.model.ProfileDataSource
import com.jordiphonedeveloper.digitalportfolio.core.network.ProfileApi
import com.jordiphonedeveloper.digitalportfolio.core.network.dto.ProfessionalProfileDto
import com.jordiphonedeveloper.digitalportfolio.domain.ProfileRefreshResult
import com.jordiphonedeveloper.digitalportfolio.domain.ProfileRepository
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.CancellationException
import java.io.IOException

class OfflineFirstProfileRepository(
    private val api: ProfileApi,
    private val dao: ProfileDao,
    private val assetDataSource: AssetProfileDataSource,
    moshi: Moshi,
) : ProfileRepository {

    private val adapter = moshi.adapter(ProfessionalProfileDto::class.java)

    override fun observeProfile(): Flow<ProfessionalProfile?> =
        dao.observeProfile().map { entity ->
            entity?.let { cached ->
                runCatching { adapter.fromJson(cached.jsonPayload)?.toDomain() }.getOrNull()
            }
        }

    override suspend fun ensureLocalProfile(): Boolean {
        if (dao.getProfile() != null) return false
        return runCatching {
            val seedDto = requireNotNull(adapter.fromJson(assetDataSource.readSeedJson()))
            validate(seedDto)
            persist(seedDto)
            true
        }.getOrDefault(false)
    }

    override suspend fun refreshProfile(): ProfileRefreshResult = try {
        val remote = api.getProfessionalProfile()
        validate(remote)
        persist(remote)
        ProfileRefreshResult.Success(ProfileDataSource.REMOTE)
    } catch (error: Exception) {
        if (error is CancellationException) throw error
        val hasCache = dao.getProfile() != null || ensureLocalProfile()
        if (hasCache) {
            ProfileRefreshResult.Success(
                source = ProfileDataSource.CACHE,
                notice = if (error is IOException) {
                    "Sin conexión: se muestra la copia guardada en el dispositivo."
                } else {
                    "La web no está disponible: se muestra la copia guardada."
                },
            )
        } else {
            ProfileRefreshResult.Failure(
                "No se ha podido cargar el perfil. Revisa la conexión y la URL de GitHub Pages.",
            )
        }
    }

    private suspend fun persist(dto: ProfessionalProfileDto) {
        dao.upsertProfile(
            ProfileEntity(
                jsonPayload = adapter.toJson(dto),
                contentUpdatedAt = dto.updatedAt,
                cachedAtEpochMillis = System.currentTimeMillis(),
            ),
        )
    }

    private fun validate(dto: ProfessionalProfileDto) {
        require(dto.schemaVersion >= 1) { "Unsupported profile schema" }
        require(dto.identity.fullName.isNotBlank()) { "The profile name is required" }
        require(dto.contact.websiteUrl.startsWith("https://")) { "The website must use HTTPS" }
    }
}
