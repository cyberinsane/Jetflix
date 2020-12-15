package com.cyberinsane.jetflix.ui

import androidx.annotation.StringRes
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.*
import com.cyberinsane.jetflix.R


@Composable
fun JetflixApp(mainViewModel: MainViewModel) {

    val items = listOf(
        Screen.Home,
        Screen.Search,
        Screen.Downloads,
        Screen.MyProfile
    )

    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)
                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = { Icon(screen.icon) },
                        label = { Text(stringResource(screen.resourceId)) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            // This is the equivalent to popUpTo the start destination
                            navController.popBackStack(navController.graph.startDestination, false)

                            // This if check gives us a "singleTop" behavior where we do not create a
                            // second instance of the composable if we are already on that destination
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route)
                            }
                        }
                    )
                }
            }
        }
    ) {
        NavHost(navController, startDestination = Screen.Home.route) {
            composable(Screen.Home.route) { Home(mainViewModel, navController) }
            composable(Screen.Search.route) { Dummy(navController) }
            composable(Screen.Downloads.route) { Dummy(navController) }
            composable(Screen.MyProfile.route) { Dummy(navController) }
        }
    }
}

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Home : Screen("home", R.string.home, Icons.Filled.Home)
    object Search : Screen("search", R.string.Search, Icons.Filled.Search)
    object Downloads : Screen("downloads", R.string.downloads, Icons.Filled.CloudDownload)
    object MyProfile : Screen("profile", R.string.profile, Icons.Filled.AccountCircle)
}