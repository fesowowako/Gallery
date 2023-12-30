package app.android.gallery.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.android.gallery.backend.viewmodel.AlbumDetailsViewModel
import app.android.gallery.backend.viewmodel.PhotoViewModel
import app.android.gallery.ui.components.albumview.AlbumView
import app.android.gallery.ui.components.mediaview.MediaPageView
import app.android.gallery.ui.screens.HomeScreen

@Composable
fun AppNavHost(modifier: Modifier = Modifier, navHostController: NavHostController) {
    val viewModelStoreOwner = LocalViewModelStoreOwner.current!!
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = Destination.Home.route
    ) {
        composable(route = Destination.Home.route,
            enterTransition = {
                when (initialState.destination.route) {
                    Destination.AlbumPage.route -> slideInVertically(initialOffsetY = {
                        -it / 2
                    }) + fadeIn()

                    else -> EnterTransition.None
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    Destination.AlbumPage.route -> slideOutVertically(targetOffsetY = {
                        -it / 2
                    }) + fadeOut()

                    else ->
                        ExitTransition.None
                }
            }) {
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                HomeScreen(onClickItem = {
                    navHostController.navigateTo(Destination.MediaPage.append(it))
                }, onNavigate = {
                    navHostController.navigateTo(it.route)
                })
            }
        }
        composable(
            route = Destination.MediaPage.routeWithArgs,
            arguments = Destination.MediaPage.arguments,
            enterTransition = { scaleIn(initialScale = 0.5f) + fadeIn() },
            exitTransition = { scaleOut(targetScale = 0.5f) + fadeOut() }
        ) {
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                val id = it.arguments?.getInt("id")
                if (id != null) {
                    val photoViewModel: PhotoViewModel = viewModel(factory = PhotoViewModel.Factory)
                    val photos by photoViewModel.photos.collectAsState()
                    MediaPageView(initialPage = id, photos)
                }
            }
        }

        composable(
            route = Destination.AlbumMediaPage.routeWithArgs,
            arguments = Destination.AlbumMediaPage.arguments,
            enterTransition = { scaleIn(initialScale = 0.5f) + fadeIn() },
            exitTransition = { scaleOut(targetScale = 0.5f) + fadeOut() }
        ) {
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                val id = it.arguments?.getInt("id")
                if (id != null) {
                    val albumDetailsViewModel: AlbumDetailsViewModel =
                        viewModel(factory = AlbumDetailsViewModel.Factory)
                    val albumDetails by albumDetailsViewModel.albumDetails.collectAsState()
                    MediaPageView(initialPage = id, albumDetails.media)
                }
            }
        }

        composable(
            route = Destination.AlbumPage.route,
            enterTransition = {
                when (initialState.destination.route) {
                    Destination.Home.route -> slideInVertically(initialOffsetY = {
                        it / 2
                    }) + fadeIn()

                    else -> EnterTransition.None
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    Destination.Home.route -> slideOutVertically(targetOffsetY = {
                        it / 2
                    }) + fadeOut()

                    else -> ExitTransition.None
                }
            }) {
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                AlbumView(onClickItem = {
                    navHostController.navigateTo(Destination.AlbumMediaPage.append(it))
                })
            }
        }
    }
}

fun NavHostController.navigateTo(route: String) = this.navigate(route) {
    launchSingleTop = true
    restoreState = true
}

fun Destination.append(arg: Any): String {
    return "$route/$arg"
}
