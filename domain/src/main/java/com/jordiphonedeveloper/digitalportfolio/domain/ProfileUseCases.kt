package com.jordiphonedeveloper.digitalportfolio.domain

class ObserveProfileUseCase(private val repository: ProfileRepository) {
    operator fun invoke() = repository.observeProfile()
}

class EnsureLocalProfileUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke() = repository.ensureLocalProfile()
}

class RefreshProfileUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke() = repository.refreshProfile()
}
