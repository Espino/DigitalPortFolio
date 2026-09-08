package com.jordiphonedeveloper.digitalportfolio

import com.jordiphonedeveloper.digitalportfolio.core.database.ProfileDatabase
import com.jordiphonedeveloper.digitalportfolio.core.network.NetworkFactory
import com.jordiphonedeveloper.digitalportfolio.data.AssetProfileDataSource
import com.jordiphonedeveloper.digitalportfolio.data.OfflineFirstProfileRepository
import com.jordiphonedeveloper.digitalportfolio.domain.EnsureLocalProfileUseCase
import com.jordiphonedeveloper.digitalportfolio.domain.ObserveProfileUseCase
import com.jordiphonedeveloper.digitalportfolio.domain.ProfileRepository
import com.jordiphonedeveloper.digitalportfolio.domain.RefreshProfileUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { NetworkFactory.createMoshi() }
    single {
        NetworkFactory.createProfileApi(
            baseUrl = BuildConfig.PROFILE_BASE_URL,
            moshi = get(),
            enableLogging = BuildConfig.DEBUG,
        )
    }
    single { ProfileDatabase.create(androidContext()) }
    single { get<ProfileDatabase>().profileDao() }
    single { AssetProfileDataSource(androidContext()) }
    single<ProfileRepository> {
        OfflineFirstProfileRepository(
            api = get(),
            dao = get(),
            assetDataSource = get(),
            moshi = get(),
        )
    }
    factory { ObserveProfileUseCase(get()) }
    factory { EnsureLocalProfileUseCase(get()) }
    factory { RefreshProfileUseCase(get()) }
}
