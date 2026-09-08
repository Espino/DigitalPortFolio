package com.jordiphonedeveloper.digitalportfolio.core.network

import com.jordiphonedeveloper.digitalportfolio.core.network.dto.ProfessionalProfileDto
import retrofit2.http.GET
import retrofit2.http.Headers

interface ProfileApi {

    @Headers("Accept: application/json")
    @GET("profile.json")
    suspend fun getProfessionalProfile(): ProfessionalProfileDto
}
