package com.jordiphonedeveloper.digitalportfolio.data

import com.jordiphonedeveloper.digitalportfolio.core.model.ContactInfo
import com.jordiphonedeveloper.digitalportfolio.core.model.CredentialItem
import com.jordiphonedeveloper.digitalportfolio.core.model.ExperienceItem
import com.jordiphonedeveloper.digitalportfolio.core.model.OperationPillar
import com.jordiphonedeveloper.digitalportfolio.core.model.ProfessionalProfile
import com.jordiphonedeveloper.digitalportfolio.core.model.ProfessionalService
import com.jordiphonedeveloper.digitalportfolio.core.model.ProfileHighlight
import com.jordiphonedeveloper.digitalportfolio.core.model.ProfileIdentity
import com.jordiphonedeveloper.digitalportfolio.core.model.ProjectItem
import com.jordiphonedeveloper.digitalportfolio.core.network.dto.ProfessionalProfileDto

internal fun ProfessionalProfileDto.toDomain() = ProfessionalProfile(
    schemaVersion = schemaVersion,
    updatedAt = updatedAt,
    identity = ProfileIdentity(
        fullName = identity.fullName,
        initials = identity.initials,
        headline = identity.headline,
        location = identity.location,
        availability = identity.availability,
        photoUrl = identity.photoUrl,
        shortBio = identity.shortBio,
        elevatorPitch = identity.elevatorPitch,
    ),
    contact = ContactInfo(
        email = contact.email,
        phone = contact.phone,
        websiteUrl = contact.websiteUrl,
        linkedInUrl = contact.linkedInUrl,
        githubUrl = contact.githubUrl,
        whatsAppUrl = contact.whatsAppUrl,
        cvUrl = contact.cvUrl,
    ),
    highlights = highlights.map { ProfileHighlight(it.label, it.value) },
    services = services.map { ProfessionalService(it.title, it.description, it.icon) },
    operations = operations.map { OperationPillar(it.title, it.description) },
    experience = experience.map {
        ExperienceItem(it.role, it.company, it.period, it.summary, it.achievements)
    },
    projects = projects.map {
        ProjectItem(it.name, it.summary, it.role, it.technologies, it.url)
    },
    skills = skills,
    credentials = credentials.map { CredentialItem(it.title, it.issuer, it.period) },
)
