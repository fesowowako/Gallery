package app.android.gallery.backend.model

data class Album(
    val id: Any = 0,
    val label: String,
    val thumbnail: Any?,
    val relativePath: String,
    val timestamp: Long,
    var count: Int = 0,
    val selected: Boolean = false,
    val isPinned: Boolean = false
)
