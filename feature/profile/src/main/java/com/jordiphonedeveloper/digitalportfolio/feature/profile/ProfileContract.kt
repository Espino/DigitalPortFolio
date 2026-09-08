package com.jordiphonedeveloper.digitalportfolio.feature.profile

import com.jordiphonedeveloper.digitalportfolio.core.model.ContactInfo
import com.jordiphonedeveloper.digitalportfolio.core.model.ProfessionalProfile
import com.jordiphonedeveloper.digitalportfolio.core.model.ProfileDataSource

data class ProfileUiState(
    val profile: ProfessionalProfile? = null,
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val dataSource: ProfileDataSource = ProfileDataSource.BUNDLED,
    val isQrVisible: Boolean = false,
    val notice: String? = null,
)

sealed interface ProfileIntent {
    data object Refresh : ProfileIntent
    data object ShowQr : ProfileIntent
    data object HideQr : ProfileIntent
    data object DismissNotice : ProfileIntent
    data object OpenWebsite : ProfileIntent
    data object OpenEmail : ProfileIntent
    data object OpenPhone : ProfileIntent
    data object OpenWhatsApp : ProfileIntent
    data object OpenLinkedIn : ProfileIntent
    data object OpenGitHub : ProfileIntent
    data object OpenCv : ProfileIntent
    data object ShareProfile : ProfileIntent
    data object SaveContact : ProfileIntent
    data class OpenProject(val url: String) : ProfileIntent
}

sealed interface ProfileEffect {
    data class OpenUri(val uri: String) : ProfileEffect
    data class ShareText(val title: String, val text: String) : ProfileEffect
    data class InsertContact(val name: String, val contact: ContactInfo) : ProfileEffect
    data class ShowToast(val message: String) : ProfileEffect
}
