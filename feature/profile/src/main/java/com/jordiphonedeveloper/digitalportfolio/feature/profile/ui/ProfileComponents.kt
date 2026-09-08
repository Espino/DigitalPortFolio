package com.jordiphonedeveloper.digitalportfolio.feature.profile.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudDone
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.OfflineBolt
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.QrCode2
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.jordiphonedeveloper.digitalportfolio.core.designsystem.ColorTokens
import com.jordiphonedeveloper.digitalportfolio.core.model.ProfessionalProfile
import com.jordiphonedeveloper.digitalportfolio.core.model.ProfileDataSource
import com.jordiphonedeveloper.digitalportfolio.feature.profile.ProfileIntent
import com.jordiphonedeveloper.digitalportfolio.feature.profile.ProfileUiState

internal val PagePadding = 20.dp

@Composable
internal fun PageHeader(
    eyebrow: String,
    title: String,
    subtitle: String,
    state: ProfileUiState,
    onIntent: (ProfileIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = eyebrow.uppercase(),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary,
            )
            Spacer(Modifier.height(6.dp))
            Text(text = title, style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(6.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            SourceBadge(state.dataSource)
            IconButton(
                onClick = { onIntent(ProfileIntent.Refresh) },
                enabled = !state.isRefreshing,
            ) {
                if (state.isRefreshing) {
                    CircularProgressIndicator(Modifier.size(22.dp), strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Outlined.Refresh, contentDescription = "Actualizar perfil")
                }
            }
        }
    }
}

@Composable
private fun SourceBadge(source: ProfileDataSource) {
    val isRemote = source == ProfileDataSource.REMOTE
    Surface(
        shape = RoundedCornerShape(50),
        color = if (isRemote) {
            MaterialTheme.colorScheme.secondaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        },
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Icon(
                imageVector = if (isRemote) Icons.Outlined.CloudDone else Icons.Outlined.OfflineBolt,
                contentDescription = null,
                modifier = Modifier.size(15.dp),
            )
            Text(
                text = if (isRemote) "Actualizado" else "Offline",
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

@Composable
internal fun TemplateNotice() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        ),
        shape = RoundedCornerShape(18.dp),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Perfil de ejemplo", fontWeight = FontWeight.Bold)
            Text(
                "Sustituye los campos marcados en docs/profile.json antes del evento y sincroniza el contenido local.",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
internal fun BusinessCard(
    profile: ProfessionalProfile,
    onIntent: (ProfileIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .shadow(20.dp, RoundedCornerShape(30.dp))
            .clip(RoundedCornerShape(30.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(ColorTokens.GradientStart, ColorTokens.GradientEnd),
                ),
            )
            .padding(24.dp),
    ) {
        val wide = maxWidth >= 680.dp
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            if (wide) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    ProfileAvatar(profile, 132.dp)
                    IdentityBlock(profile, Modifier.weight(1f))
                    QrPreview(
                        content = profile.contact.websiteUrl,
                        size = 132.dp,
                        onClick = { onIntent(ProfileIntent.ShowQr) },
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        ProfileAvatar(profile, 104.dp)
                        QrPreview(
                            content = profile.contact.websiteUrl,
                            size = 104.dp,
                            onClick = { onIntent(ProfileIntent.ShowQr) },
                        )
                    }
                    IdentityBlock(profile)
                }
            }
            HorizontalDivider(color = Color.White.copy(alpha = 0.18f))
            CardActions(onIntent)
        }
    }
}

@Composable
private fun IdentityBlock(profile: ProfessionalProfile, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = profile.identity.fullName,
            style = MaterialTheme.typography.displaySmall,
            color = Color.White,
        )
        Text(
            text = profile.identity.headline,
            style = MaterialTheme.typography.titleLarge,
            color = ColorTokens.Accent,
        )
        Text(
            text = profile.identity.location,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White.copy(alpha = 0.78f),
        )
        Text(
            text = profile.identity.shortBio,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White.copy(alpha = 0.92f),
        )
    }
}

@Composable
internal fun BusinessCardActions(onIntent: (ProfileIntent) -> Unit) {
    CardActions(onIntent)
}

@Composable
private fun CardActions(onIntent: (ProfileIntent) -> Unit) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Button(
            onClick = { onIntent(ProfileIntent.OpenWebsite) },
            colors = ButtonDefaults.buttonColors(
                containerColor = ColorTokens.Accent,
                contentColor = ColorTokens.GradientStart,
            ),
        ) {
            Icon(Icons.Outlined.Language, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Ver web")
        }
        OutlinedButton(
            onClick = { onIntent(ProfileIntent.SaveContact) },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.5f)),
        ) {
            Icon(Icons.Outlined.PersonAdd, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Guardar")
        }
        OutlinedButton(
            onClick = { onIntent(ProfileIntent.ShareProfile) },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.5f)),
        ) {
            Icon(Icons.Outlined.ContentCopy, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Compartir")
        }
    }
}

@Composable
internal fun ProfileAvatar(profile: ProfessionalProfile, size: Dp) {
    var imageFailed by remember(profile.identity.photoUrl) { mutableStateOf(false) }
    val modifier = Modifier
        .size(size)
        .clip(CircleShape)
        .border(3.dp, Color.White.copy(alpha = 0.75f), CircleShape)

    if (profile.identity.photoUrl.isBlank() || imageFailed) {
        Box(
            modifier = modifier.background(
                Brush.linearGradient(listOf(ColorTokens.Accent, MaterialTheme.colorScheme.tertiary)),
            ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = profile.identity.initials,
                style = MaterialTheme.typography.headlineMedium,
                color = ColorTokens.GradientStart,
                fontWeight = FontWeight.ExtraBold,
            )
        }
    } else {
        AsyncImage(
            model = profile.identity.photoUrl,
            contentDescription = "Fotografía de ${profile.identity.fullName}",
            modifier = modifier,
            contentScale = ContentScale.Crop,
            onError = { imageFailed = true },
        )
    }
}

@Composable
internal fun QrPreview(
    content: String,
    size: Dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .size(size)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Box(Modifier.fillMaxSize().padding(9.dp), contentAlignment = Alignment.Center) {
            QrCodeImage(content = content, modifier = Modifier.fillMaxSize())
            Surface(
                modifier = Modifier.align(Alignment.Center),
                shape = RoundedCornerShape(7.dp),
                color = ColorTokens.GradientStart,
            ) {
                Icon(
                    Icons.Outlined.QrCode2,
                    contentDescription = null,
                    tint = ColorTokens.Accent,
                    modifier = Modifier.padding(4.dp).size(16.dp),
                )
            }
        }
    }
}

@Composable
internal fun QrCodeImage(content: String, modifier: Modifier = Modifier) {
    val image = remember(content) { createQrBitmap(content, 720).asImageBitmap() }
    Image(
        bitmap = image,
        contentDescription = "Código QR para abrir el perfil web",
        modifier = modifier.aspectRatio(1f),
        contentScale = ContentScale.Fit,
    )
}

private fun createQrBitmap(content: String, size: Int): Bitmap {
    val safeContent = content.ifBlank { "https://example.com" }
    val matrix = QRCodeWriter().encode(
        safeContent,
        BarcodeFormat.QR_CODE,
        size,
        size,
        mapOf(
            EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.M,
            EncodeHintType.MARGIN to 1,
            EncodeHintType.CHARACTER_SET to "UTF-8",
        ),
    )
    val pixels = IntArray(size * size)
    for (y in 0 until size) {
        for (x in 0 until size) {
            pixels[y * size + x] = if (matrix[x, y]) 0xFF071521.toInt() else 0xFFFFFFFF.toInt()
        }
    }
    return Bitmap.createBitmap(pixels, size, size, Bitmap.Config.ARGB_8888)
}

@Composable
internal fun QrCodeDialog(
    name: String,
    websiteUrl: String,
    onDismiss: () -> Unit,
    onOpenWebsite: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Outlined.QrCode2, contentDescription = null) },
        title = { Text("Escanea mi perfil") },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(22.dp),
                ) {
                    QrCodeImage(
                        content = websiteUrl,
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                    )
                }
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = websiteUrl,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onOpenWebsite) { Text("Abrir web") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cerrar") }
        },
    )
}

@Composable
internal fun SectionTitle(title: String, subtitle: String? = null) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(title, style = MaterialTheme.typography.headlineSmall)
        subtitle?.let {
            Text(
                it,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
internal fun SkillFlow(skills: List<String>) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        skills.forEach { skill ->
            AssistChip(onClick = {}, label = { Text(skill) })
        }
    }
}

@Composable
internal fun EmptyProfileState(
    isLoading: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("No se ha podido cargar el perfil.", textAlign = TextAlign.Center)
                Spacer(Modifier.height(12.dp))
                Button(onClick = onRefresh) { Text("Reintentar") }
            }
        }
    }
}

@Composable
internal fun PageWidth(content: @Composable () -> Unit) {
    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
        Column(
            modifier = Modifier.fillMaxWidth().widthIn(max = 1120.dp),
        ) { content() }
    }
}
