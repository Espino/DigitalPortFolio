package com.jordiphonedeveloper.digitalportfolio.feature.profile.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowOutward
import androidx.compose.material.icons.outlined.RocketLaunch
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jordiphonedeveloper.digitalportfolio.core.model.ProjectItem
import com.jordiphonedeveloper.digitalportfolio.feature.profile.ProfileIntent
import com.jordiphonedeveloper.digitalportfolio.feature.profile.ProfileUiState

@Composable
fun ProjectsScreen(
    state: ProfileUiState,
    onIntent: (ProfileIntent) -> Unit,
) {
    val profile = state.profile
    if (profile == null) {
        EmptyProfileState(state.isLoading, { onIntent(ProfileIntent.Refresh) })
        return
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 300.dp),
        contentPadding = PaddingValues(PagePadding, 28.dp, PagePadding, 40.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column {
                PageHeader(
                    eyebrow = "Portfolio",
                    title = "Proyectos destacados",
                    subtitle = "Una selección que combina producto, arquitectura móvil, negocio y operación.",
                    state = state,
                    onIntent = onIntent,
                )
                Spacer(Modifier.height(12.dp))
            }
        }
        items(profile.projects, key = { it.name }) { project ->
            ProjectCard(project, onIntent)
        }
    }
}

@Composable
private fun ProjectCard(project: ProjectItem, onIntent: (ProfileIntent) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                ) {
                    Icon(
                        Icons.Outlined.RocketLaunch,
                        contentDescription = null,
                        modifier = Modifier.padding(10.dp).size(24.dp),
                    )
                }
                if (project.url.isNotBlank()) {
                    TextButton(onClick = { onIntent(ProfileIntent.OpenProject(project.url)) }) {
                        Text("Ver")
                        Icon(Icons.Outlined.ArrowOutward, contentDescription = null)
                    }
                }
            }
            Text(project.name, style = MaterialTheme.typography.headlineSmall)
            Text(
                project.role,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary,
            )
            Text(
                project.summary,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            SkillFlow(project.technologies)
        }
    }
}
