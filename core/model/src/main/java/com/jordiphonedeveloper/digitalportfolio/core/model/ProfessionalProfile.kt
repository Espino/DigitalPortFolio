package com.jordiphonedeveloper.digitalportfolio.core.model

data class ProfessionalProfile(
    val schemaVersion: Int,
    val updatedAt: String,
    val identity: ProfileIdentity,
    val contact: ContactInfo,
    val highlights: List<ProfileHighlight>,
    val services: List<ProfessionalService>,
    val operations: List<OperationPillar>,
    val experience: List<ExperienceItem>,
    val projects: List<ProjectItem>,
    val skills: List<String>,
    val credentials: List<CredentialItem>,
) {
    val isTemplate: Boolean
        get() = contact.websiteUrl.contains("TU_USUARIO", ignoreCase = true) ||
            contact.email.startsWith("tu-", ignoreCase = true)
}

data class ProfileIdentity(
    val fullName: String,
    val initials: String,
    val headline: String,
    val location: String,
    val availability: String,
    val photoUrl: String,
    val shortBio: String,
    val elevatorPitch: String,
)

data class ContactInfo(
    val email: String,
    val phone: String,
    val websiteUrl: String,
    val linkedInUrl: String,
    val githubUrl: String,
    val whatsAppUrl: String,
    val cvUrl: String,
)

data class ProfileHighlight(
    val label: String,
    val value: String,
)

data class ProfessionalService(
    val title: String,
    val description: String,
    val icon: String,
)

data class OperationPillar(
    val title: String,
    val description: String,
)

data class ExperienceItem(
    val role: String,
    val company: String,
    val period: String,
    val summary: String,
    val achievements: List<String>,
)

data class ProjectItem(
    val name: String,
    val summary: String,
    val role: String,
    val technologies: List<String>,
    val url: String,
)

data class CredentialItem(
    val title: String,
    val issuer: String,
    val period: String,
)

enum class ProfileDataSource {
    REMOTE,
    CACHE,
    BUNDLED,
}
