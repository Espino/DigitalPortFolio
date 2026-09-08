package com.jordiphonedeveloper.digitalportfolio.feature.profile.di

import com.jordiphonedeveloper.digitalportfolio.feature.profile.ProfileViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val profileFeatureModule = module {
    viewModelOf(::ProfileViewModel)
}
