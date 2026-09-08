package com.jordiphonedeveloper.digitalportfolio.feature.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jordiphonedeveloper.digitalportfolio.core.model.CredentialItem
import com.jordiphonedeveloper.digitalportfolio.core.model.ExperienceItem
import com.jordiphonedeveloper.digitalportfolio.core.model.OperationPillar
import com.jordiphonedeveloper.digitalportfolio.feature.profile.ProfileIntent
import com.jordiphonedeveloper.digitalportfolio.feature.profile.ProfileUiState

@Composable
fun ExperienceScreen(
    state: ProfileUiState,
    onIntent: (ProfileIntent) -> Unit,
) {
    val profile = state.profile
    if (profile == null) {
        EmptyProfileState(state.isLoading, { onIntent(ProfileIntent.Refresh) })
        return
    }

    LazyColumn(
        contentPadding = PaddingValues(PagePadding, 28.dp, PagePadding, 40.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        item {
            Column(Modifier.fillMaxWidth().widthIn(max = 1040.dp)) {
                PageHeader(
                    eyebrow = "Experiencia",
                    title = "Trayectoria y forma de trabajar",
                    subtitle = "La tecnología crea valor cuando está conectada con objetivos, personas y una operación clara.",
                    state = state,
                    onIntent = onIntent,
                )
                Spacer(Modifier.height(28.dp))
                SectionTitle("Dirección de operaciones")
            }
        }

        profile.operations.forEachIndexed { index, pillar ->
            item { OperationCard(index + 1, pillar) }
        }

        item {
            Column(Modifier.padding(top = 12.dp)) {
                SectionTitle(
                    "Experiencia profesional",
                    "Ejemplo editable: sustituye periodos y logros por tus datos contrastables.",
                )
            }
        }

        profile.experience.forEach { experience ->
            item { ExperienceCard(experience) }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                SectionTitle("Competencias")
                SkillFlow(profile.skills)
            }
        }

        if (profile.credentials.isNotEmpty()) {
            item { SectionTitle("Formación y certificaciones") }
            profile.credentials.forEach { item { CredentialCard(it) } }
        }
    }
}

@Composable
private fun OperationCard(number: Int, pillar: OperationPillar) {
    Card(
        modifier = Modifier.fillMaxWidth().widthIn(max = 1040.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    number.toString().padStart(2, '0'),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(pillar.title, style = MaterialTheme.typography.titleLarge)
                Text(
                    pillar.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun ExperienceCard(experience: ExperienceItem) {
    Card(
        modifier = Modifier.fillMaxWidth().widthIn(max = 1040.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                experience.period,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary,
            )
            Text(experience.role, style = MaterialTheme.typography.headlineSmall)
            Text(
                experience.company,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(experience.summary, style = MaterialTheme.typography.bodyLarge)
            HorizontalDivider(Modifier.padding(vertical = 4.dp))
            experience.achievements.forEach { achievement ->
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.Top) {
                    Icon(
                        Icons.Outlined.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(20.dp),
                    )
                    Text(achievement, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
private fun CredentialCard(credential: CredentialItem) {
    Card(
        modifier = Modifier.fillMaxWidth().widthIn(max = 1040.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(credential.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("${credential.issuer} · ${credential.period}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
