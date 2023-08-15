package com.ilustris.motivcompose.ui.navigation

import android.os.Bundle
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.creat.motiv.features.home.ui.HomeView
import com.creat.motiv.features.post.ui.QuotePostScreen
import com.creat.motiv.features.settings.ui.SettingsView
import com.ilustris.manager.feature.styles.ui.form.ui.NewStyleScreen
import com.ilustris.motiv.base.navigation.AppNavigation
import com.ilustris.motiv.foundation.ui.theme.gradientFill
import com.ilustris.motiv.foundation.ui.theme.grayGradients
import com.ilustris.motiv.foundation.ui.theme.motivGradient
import com.ilustris.motiv.foundation.ui.theme.radioIconModifier
import com.ilustris.motiv.manager.ManagerScreen
import com.ilustris.motivcompose.features.profile.ui.ProfileView
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.glide.GlideRequestType

@Composable
fun MotivNavigationGraph(
    navHostController: NavHostController,
    modifier: Modifier
) {
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = AppNavigation.SETTINGS.route,
    ) {
        AppNavigation.values().forEach { item ->
            val args = item.arguments.map { navArgument(it) { type = NavType.StringType } }
            composable(
                route = item.route,
                arguments = args,
                enterTransition = { fadeIn() },
                exitTransition = { fadeOut() },
                popEnterTransition = { fadeIn() },
                popExitTransition = { fadeOut() }) {
                GetRouteScreen(navigationItem = item, navHostController, it.arguments)
            }
        }
    }
}

@Composable
fun MotivBottomNavigation(navController: NavController, userProfilePic: String? = null) {

    fun navigateToScreen(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination

    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        AppNavigation.values().filter { it.showOnNavigation }.forEach { item ->
            val isSelected = currentRoute?.hierarchy?.any { it.route == item.route } == true

            val selectedBrush = if (isSelected) motivGradient() else grayGradients()

            BottomNavigationItem(
                selected = isSelected,
                onClick = { navigateToScreen(item.route) },
                icon = {
                    if (item == AppNavigation.PROFILE && userProfilePic != null) {
                        GlideImage(
                            imageModel = { userProfilePic },
                            glideRequestType = GlideRequestType.BITMAP,
                            modifier = Modifier
                                .size(32.dp)
                                .radioIconModifier(
                                    0f,
                                    borderWidth = 1.dp,
                                    sizeValue = 24.dp,
                                    brush = selectedBrush
                                ),
                            imageOptions = ImageOptions(
                                Alignment.Center,
                                contentScale = ContentScale.Crop
                            )
                        )
                    } else {
                        Image(
                            painterResource(item.icon),
                            contentDescription = item.title,
                            modifier = Modifier
                                .size(24.dp)
                                .gradientFill(brush = selectedBrush)
                                .clip(CircleShape),

                            )
                    }

                })
        }
    }
}


@Composable
fun GetRouteScreen(
    navigationItem: AppNavigation,
    navController: NavController,
    arguments: Bundle?
) {
    when (navigationItem) {
        AppNavigation.HOME -> {
            HomeView(navController)
        }

        AppNavigation.PROFILE -> {
            val userID = arguments?.getString("userId")
            ProfileView(userID, navController = navController)
        }

        AppNavigation.POST -> {
            val quoteId = arguments?.getString(navigationItem.arguments.first())
            QuotePostScreen(quoteId, navController)
        }

        AppNavigation.SETTINGS -> {
            SettingsView(navController)
        }

        AppNavigation.MANAGER -> {
            ManagerScreen(navController = navController)
        }

        AppNavigation.NEWSTYLE -> {
            NewStyleScreen(navController = navController)
        }
    }
}