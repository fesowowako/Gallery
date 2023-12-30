package app.android.gallery.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Destination(val route: String) {
    object Home : Destination("local_music")
    object MediaPage : Destination("all_media") {
        val routeWithArgs = "$route/{id}"
        val arguments = listOf(navArgument("id") { type = NavType.IntType })
    }

    object AlbumMediaPage : Destination("album_media") {
        val routeWithArgs = "$route/{id}"
        val arguments = listOf(navArgument("id") { type = NavType.IntType })
    }

    object AlbumPage : Destination("album")

}
