package app.android.gallery.backend.model

import android.net.Uri

data class Photo(
    override val id: Long = 0,
    override val label: String,
    override val uri: Uri,
    override val path: String,
    override val relativePath: String,
    override val albumID: Long,
    override val albumLabel: String,
    override val timestamp: Long,
    override val takenTimestamp: Long? = null,
    override val fullDate: String,
    override val mimeType: String,
    override val orientation: Int,
    override val favorite: Int,
    override val trashed: Int
) : Media