package com.jordiphonedeveloper.digitalportfolio.feature.profile.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Android
import androidx.compose.material.icons.outlined.Architecture
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.Hub
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jordiphonedeveloper.digitalportfolio.core.model.ProfessionalService
import com.jordiphonedeveloper.digitalportfolio.feature.profile.ProfileIntent
import com.jordiphonedeveloper.digitalportfolio.feature.profile.ProfileUiState

@Composable
fun HomeScreen(
    state: ProfileUiState,
    onIntent: (ProfileIntent) -> Unit,
) {
    val profile = state.profile
    if (profile == null) {
        EmptyProfileState(state.isLoading, { onIntent(ProfileIntent.Refresh) })
        return
    }

    LazyColumn(
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            start = PagePadding,
            top = 28.dp,
            end = PagePadding,
            bottom = 32.dp,
        ),
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth().widthIn(max = 1120.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                PageHeader(
                    eyebrow = "Tarjeta profesional",
                    title = "Perfil",
                    subtitle = "Mi identidad digital",
                    state = state,
                    onIntent = onIntent,
                )
                if (profile.isTemplate) TemplateNotice()
                BusinessCard(profile, onIntent)
                ElevatorPitchCard(profile.identity.elevatorPitch)
                Highlights(profile.highlights)
                SectionTitle(
                    title = "Cómo puedo aportar",
                    subtitle = "Desarrollo senior, visión de producto y capacidad operativa en una misma conversación.",
                )
                ServicesGrid(profile.services)
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun ElevatorPitchCard(pitch: String) {
    Card(
        shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                "ELEVATOR PITCH",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary,
            )
            Text(
                text = pitch,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}

@Composable
private fun Highlights(items: List<com.jordiphonedeveloper.digitalportfolio.core.model.ProfileHighlight>) {
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val itemWidth = if (maxWidth >= 720.dp) (maxWidth - 24.dp) / 3 else maxWidth
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items.forEach { item ->
                Card(
                    modifier = Modifier.width(itemWidth),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                ) {
                    Column(Modifier.padding(18.dp)) {
                        Text(
                            item.label,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            item.value,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ServicesGrid(services: List<ProfessionalService>) {
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val itemWidth = if (maxWidth >= 760.dp) (maxWidth - 16.dp) / 2 else maxWidth
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            services.forEach { service ->
                Card(
                    modifier = Modifier.width(itemWidth),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.Top,
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            ),
                        ) {
                            Icon(
                                imageVector = serviceIcon(service.icon),
                                contentDescription = null,
                                modifier = Modifier.padding(10.dp),
                                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            )
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(service.title, style = MaterialTheme.typography.titleLarge)
                            Text(
                                service.description,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun serviceIcon(icon: String): ImageVector = when (icon) {
    "android" -> Icons.Outlined.Android
    "architecture" -> Icons.Outlined.Architecture
    "operations" -> Icons.Outlined.Hub
    else -> Icons.Outlined.Devices
}
