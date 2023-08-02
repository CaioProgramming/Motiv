package com.ilustris.motiv.base.navigation

import ai.atick.material.MaterialColor
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.creat.motiv.base.R


private const val HOME_ROUTE = "home"
private const val NEW_POST_ROUTE = "new_post"
private const val PROFILE_ROUTE = "profile"

enum class BottomNavItem(
    val title: String,
    var icon: Int = R.drawable.ic_saturn_and_other_planets_primary,
    val route: String,
    val showOnNavigation: Boolean = true,
    val showBottomNav: Boolean = true,
    val showStatusBar: Boolean = true
) {
    HOME(title = "Home", route = HOME_ROUTE),
    NEW_POST(title = "Publicar", route = NEW_POST_ROUTE),
    PROFILE(title = "Eu", route = PROFILE_ROUTE, showStatusBar = false),
}

@Composable
fun getComposeForItem(item: BottomNavItem, navController: NavHostController) {
    when (item) {
        BottomNavItem.HOME -> Text(text = "Home")
        BottomNavItem.NEW_POST -> Text("New post")
        BottomNavItem.PROFILE -> Text("Perfil")
    }
}

@Composable
fun NavigationGraph(navController: NavHostController, bottomPadding: Dp) {

    NavHost(
        navController = navController,
        startDestination = HOME_ROUTE,
        modifier = Modifier.padding(bottom = bottomPadding)
    ) {
        BottomNavItem.values().forEach { item ->
            composable(route = item.route) {
                getComposeForItem(item, navController)
            }
        }
    }
}

@Composable
fun BottomNavigationMenu(navController: NavController) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        val routes = BottomNavItem.values().filter { it.showOnNavigation }
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination
        routes.forEach { item ->
            val isSelected = currentRoute?.hierarchy?.any { it.route == item.route } == true
            val itemColor =
                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground.copy(
                    alpha = 0.5f
                )
            BottomNavigationItem(
                selected = isSelected,
                label = {
                    androidx.compose.material3.Text(
                        text = item.title,
                        style = MaterialTheme.typography.labelSmall,
                        color = itemColor
                    )
                },
                icon = {
                    Image(
                        painterResource(item.icon),
                        contentDescription = item.title,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape),
                        colorFilter = ColorFilter.tint(itemColor)
                    )
                },
                selectedContentColor = MaterialColor.White,
                unselectedContentColor = MaterialColor.White.copy(alpha = 0.3f),
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
        }
    }
}