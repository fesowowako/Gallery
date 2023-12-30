package app.android.gallery.backend.model

import android.net.Uri

interface Media {
    val id: Long
    val label: String
    val uri: Uri
    val path: String
    val relativePath: String
    val albumID: Long
    val albumLabel: String
    val timestamp: Long
    val takenTimestamp: Long?
    val fullDate: String
    val mimeType: String
    val orientation: Int
    val favorite: Int
    val trashed: Int
}
