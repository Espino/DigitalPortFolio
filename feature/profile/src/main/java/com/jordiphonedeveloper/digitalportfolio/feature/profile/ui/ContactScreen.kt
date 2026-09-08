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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jordiphonedeveloper.digitalportfolio.feature.profile.ProfileIntent
import com.jordiphonedeveloper.digitalportfolio.feature.profile.ProfileUiState

@Composable
fun ContactScreen(
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
            Column(Modifier.fillMaxWidth().widthIn(max = 920.dp)) {
                PageHeader(
                    eyebrow = "Networking",
                    title = "Proyectos Digitales",
                    subtitle = profile.identity.availability,
                    state = state,
                    onIntent = onIntent,
                )
                Spacer(Modifier.height(24.dp))
                ContactHero(profile.identity.fullName, profile.contact.websiteUrl, onIntent)
            }
        }

        item { ContactAction(Icons.Outlined.AlternateEmail, "Email", profile.contact.email) { onIntent(ProfileIntent.OpenEmail) } }
        item { ContactAction(Icons.Outlined.Phone, "Teléfono", profile.contact.phone) { onIntent(ProfileIntent.OpenPhone) } }
        item { ContactAction(Icons.Outlined.Language, "Web profesional", profile.contact.websiteUrl) { onIntent(ProfileIntent.OpenWebsite) } }
        if (profile.contact.linkedInUrl.isNotBlank()) {
            item { ContactAction(Icons.Outlined.Work, "LinkedIn", profile.contact.linkedInUrl) { onIntent(ProfileIntent.OpenLinkedIn) } }
        }
        if (profile.contact.githubUrl.isNotBlank()) {
            item { ContactAction(Icons.Outlined.Code, "GitHub", profile.contact.githubUrl) { onIntent(ProfileIntent.OpenGitHub) } }
        }
        if (profile.contact.cvUrl.isNotBlank()) {
            item { ContactAction(Icons.Outlined.Description, "Currículum web", profile.contact.cvUrl) { onIntent(ProfileIntent.OpenCv) } }
        }
    }
}

@Composable
private fun ContactHero(
    name: String,
    websiteUrl: String,
    onIntent: (ProfileIntent) -> Unit,
) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            QrPreview(
                content = websiteUrl,
                size = 220.dp,
                onClick = { onIntent(ProfileIntent.ShowQr) },
            )
            Text(name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(
                "Escanea el QR o guarda mis datos en tu agenda.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(onClick = { onIntent(ProfileIntent.SaveContact) }) {
                    Icon(Icons.Outlined.PersonAdd, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Guardar")
                }
                OutlinedButton(onClick = { onIntent(ProfileIntent.ShareProfile) }) {
                    Icon(Icons.Outlined.Share, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Compartir")
                }
            }
        }
    }
}

@Composable
private fun ContactAction(
    icon: ImageVector,
    title: String,
    value: String,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().widthIn(max = 920.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.labelLarge)
                Text(
                    value,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Icon(Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = "Abrir $title")
        }
    }
}
