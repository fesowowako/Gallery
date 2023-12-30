package app.android.gallery.util

import android.content.Context
import android.content.Intent
import androidx.core.app.ShareCompat
import app.android.gallery.R
import app.android.gallery.backend.model.Media

fun Context.launchEditIntent(media: Media) {
    val intent = Intent(Intent.ACTION_EDIT).apply {
        addCategory(Intent.CATEGORY_DEFAULT)
        setDataAndType(media.uri, media.mimeType)
        putExtra("mimeType", media.mimeType)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    startActivity(Intent.createChooser(intent, getString(R.string.edit)))
}

fun Context.launchUseAsIntent(media: Media) {
    val intent = Intent(Intent.ACTION_ATTACH_DATA).apply {
        addCategory(Intent.CATEGORY_DEFAULT)
        setDataAndType(media.uri, media.mimeType)
        putExtra("mimeType", media.mimeType)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    startActivity(Intent.createChooser(intent, getString(R.string.set_as)))
}

fun Context.launchOpenWithIntent(media: Media) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        addCategory(Intent.CATEGORY_DEFAULT)
        setDataAndType(media.uri, media.mimeType)
        putExtra("mimeType", media.mimeType)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    startActivity(Intent.createChooser(intent, getString(R.string.open_with)))
}

fun Context.shareMedia(media: Media) {
    ShareCompat
        .IntentBuilder(this)
        .setType(media.mimeType)
        .addStream(media.uri)
        .startChooser()
}
