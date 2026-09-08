package com.jordiphonedeveloper.digitalportfolio.feature.profile

import androidx.lifecycle.viewModelScope
import com.jordiphonedeveloper.digitalportfolio.core.common.BaseViewModel
import com.jordiphonedeveloper.digitalportfolio.core.model.ProfessionalProfile
import com.jordiphonedeveloper.digitalportfolio.core.model.ProfileDataSource
import com.jordiphonedeveloper.digitalportfolio.domain.EnsureLocalProfileUseCase
import com.jordiphonedeveloper.digitalportfolio.domain.ObserveProfileUseCase
import com.jordiphonedeveloper.digitalportfolio.domain.ProfileRefreshResult
import com.jordiphonedeveloper.digitalportfolio.domain.RefreshProfileUseCase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val observeProfile: ObserveProfileUseCase,
    private val ensureLocalProfile: EnsureLocalProfileUseCase,
    private val refreshProfile: RefreshProfileUseCase,
) : BaseViewModel<ProfileIntent, ProfileUiState, ProfileEffect>(ProfileUiState()) {

    init {
        observeStoredProfile()
        viewModelScope.launch {
            val seeded = ensureLocalProfile()
            if (seeded) updateState { copy(dataSource = ProfileDataSource.BUNDLED) }
            refresh(showProgress = false)
        }
    }

    override fun onIntent(intent: ProfileIntent) {
        when (intent) {
            ProfileIntent.Refresh -> viewModelScope.launch { refresh(showProgress = true) }
            ProfileIntent.ShowQr -> updateState { copy(isQrVisible = true) }
            ProfileIntent.HideQr -> updateState { copy(isQrVisible = false) }
            ProfileIntent.DismissNotice -> updateState { copy(notice = null) }
            ProfileIntent.OpenWebsite -> withProfile { openUrl(it.contact.websiteUrl) }
            ProfileIntent.OpenEmail -> withProfile { openUrl("mailto:${it.contact.email}") }
            ProfileIntent.OpenPhone -> withProfile {
                openUrl("tel:${it.contact.phone.filterNot(Char::isWhitespace)}")
            }
            ProfileIntent.OpenWhatsApp -> withProfile { openUrl(it.contact.whatsAppUrl) }
            ProfileIntent.OpenLinkedIn -> withProfile { openUrl(it.contact.linkedInUrl) }
            ProfileIntent.OpenGitHub -> withProfile { openUrl(it.contact.githubUrl) }
            ProfileIntent.OpenCv -> withProfile { openUrl(it.contact.cvUrl) }
            ProfileIntent.ShareProfile -> withProfile(::share)
            ProfileIntent.SaveContact -> withProfile {
                sendEffect(ProfileEffect.InsertContact(it.identity.fullName, it.contact))
            }
            is ProfileIntent.OpenProject -> openUrl(intent.url)
        }
    }

    private fun observeStoredProfile() {
        viewModelScope.launch {
            observeProfile().collectLatest { profile ->
                updateState {
                    copy(
                        profile = profile,
                        isLoading = profile == null && isLoading,
                    )
                }
            }
        }
    }

    private suspend fun refresh(showProgress: Boolean) {
        updateState { copy(isRefreshing = showProgress, notice = null) }
        when (val result = refreshProfile()) {
            is ProfileRefreshResult.Success -> updateState {
                copy(
                    isLoading = false,
                    isRefreshing = false,
                    dataSource = result.source,
                    notice = result.notice,
                )
            }
            is ProfileRefreshResult.Failure -> updateState {
                copy(
                    isLoading = false,
                    isRefreshing = false,
                    notice = result.message,
                )
            }
        }
    }

    private fun withProfile(block: (ProfessionalProfile) -> Unit) {
        state.value.profile?.let(block)
    }

    private fun openUrl(url: String) {
        if (url.isBlank()) {
            sendEffect(ProfileEffect.ShowToast("Completa este enlace en profile.json."))
        } else {
            sendEffect(ProfileEffect.OpenUri(url))
        }
    }

    private fun share(profile: ProfessionalProfile) {
        val text = buildString {
            appendLine(profile.identity.fullName)
            appendLine(profile.identity.headline)
            appendLine(profile.identity.elevatorPitch)
            append(profile.contact.websiteUrl)
        }
        sendEffect(
            ProfileEffect.ShareText(
                title = "Perfil profesional de ${profile.identity.fullName}",
                text = text,
            ),
        )
    }
}
