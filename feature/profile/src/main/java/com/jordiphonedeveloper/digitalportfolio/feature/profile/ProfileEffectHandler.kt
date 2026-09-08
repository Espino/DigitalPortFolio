package com.jordiphonedeveloper.digitalportfolio.feature.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.widget.Toast

internal fun Context.handleProfileEffect(effect: ProfileEffect) {
    when (effect) {
        is ProfileEffect.OpenUri -> launchSafely(Intent(Intent.ACTION_VIEW, Uri.parse(effect.uri)))
        is ProfileEffect.ShareText -> {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, effect.title)
                putExtra(Intent.EXTRA_TEXT, effect.text)
            }
            launchSafely(Intent.createChooser(shareIntent, effect.title))
        }
        is ProfileEffect.InsertContact -> {
            val insertIntent = Intent(ContactsContract.Intents.Insert.ACTION).apply {
                type = ContactsContract.RawContacts.CONTENT_TYPE
                putExtra(ContactsContract.Intents.Insert.NAME, effect.name)
                putExtra(ContactsContract.Intents.Insert.EMAIL, effect.contact.email)
                putExtra(ContactsContract.Intents.Insert.PHONE, effect.contact.phone)
                putExtra(ContactsContract.Intents.Insert.NOTES, effect.contact.websiteUrl)
            }
            launchSafely(insertIntent)
        }
        is ProfileEffect.ShowToast -> toast(effect.message)
    }
}

private fun Context.launchSafely(intent: Intent) {
    runCatching { startActivity(intent) }
        .onFailure { toast("No hay una aplicación disponible para esta acción.") }
}

private fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
