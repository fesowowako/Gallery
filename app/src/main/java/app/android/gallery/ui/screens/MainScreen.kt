package app.android.gallery.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import app.android.gallery.navigation.AppNavHost

@Composable
fun MainScreen() {
    val navHostController = rememberNavController()
    AppNavHost(navHostController = navHostController)
}
