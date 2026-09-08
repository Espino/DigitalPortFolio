package com.jordiphonedeveloper.digitalportfolio.core.network.dto

data class ProfessionalProfileDto(
    val schemaVersion: Int,
    val updatedAt: String,
    val identity: ProfileIdentityDto,
    val contact: ContactInfoDto,
    val highlights: List<ProfileHighlightDto> = emptyList(),
    val services: List<ProfessionalServiceDto> = emptyList(),
    val operations: List<OperationPillarDto> = emptyList(),
    val experience: List<ExperienceItemDto> = emptyList(),
    val projects: List<ProjectItemDto> = emptyList(),
    val skills: List<String> = emptyList(),
    val credentials: List<CredentialItemDto> = emptyList(),
)

data class ProfileIdentityDto(
    val fullName: String,
    val initials: String,
    val headline: String,
    val location: String,
    val availability: String,
    val photoUrl: String = "",
    val shortBio: String,
    val elevatorPitch: String,
)

data class ContactInfoDto(
    val email: String,
    val phone: String,
    val websiteUrl: String,
    val linkedInUrl: String = "",
    val githubUrl: String = "",
    val whatsAppUrl: String = "",
    val cvUrl: String = "",
)

data class ProfileHighlightDto(val label: String, val value: String)

data class ProfessionalServiceDto(
    val title: String,
    val description: String,
    val icon: String = "code",
)

data class OperationPillarDto(val title: String, val description: String)

data class ExperienceItemDto(
    val role: String,
    val company: String,
    val period: String,
    val summary: String,
    val achievements: List<String> = emptyList(),
)

data class ProjectItemDto(
    val name: String,
    val summary: String,
    val role: String,
    val technologies: List<String> = emptyList(),
    val url: String = "",
)

data class CredentialItemDto(
    val title: String,
    val issuer: String,
    val period: String,
)
