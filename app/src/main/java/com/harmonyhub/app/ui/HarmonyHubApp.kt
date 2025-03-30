package com.harmonyhub.app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.harmonyhub.app.navigation.HarmonyHubDestination
import com.harmonyhub.app.navigation.harmonyHubScreens
import com.harmonyhub.feature.auth.LoginScreen
import com.harmonyhub.feature.auth.ProfileScreen
import com.harmonyhub.feature.checkin.CheckInScreen
import com.harmonyhub.feature.expense.ExpenseScreen
import com.harmonyhub.feature.messaging.MessagingScreen
import com.harmonyhub.feature.schedule.ScheduleScreen
import com.harmonyhub.feature.storage.StorageScreen

@Composable
fun HarmonyHubApp(
    navController: NavController,
    isUserAuthenticated: Boolean
) {
    val startDestination = if (isUserAuthenticated) {
        HarmonyHubDestination.HOME.route
    } else {
        HarmonyHubDestination.LOGIN.route
    }
    
    Scaffold(
        bottomBar = {
            if (isUserAuthenticated) {
                HarmonyHubBottomBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Auth screens
            composable(HarmonyHubDestination.LOGIN.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(HarmonyHubDestination.HOME.route) {
                            popUpTo(HarmonyHubDestination.LOGIN.route) { inclusive = true }
                        }
                    }
                )
            }
            
            // Main screens
            composable(HarmonyHubDestination.HOME.route) {
                // Placeholder for home screen until we implement it
                ScheduleScreen() // Using schedule as home screen initially
            }
            
            composable(HarmonyHubDestination.SCHEDULE.route) {
                ScheduleScreen()
            }
            
            composable(HarmonyHubDestination.MESSAGES.route) {
                MessagingScreen()
            }
            
            composable(HarmonyHubDestination.EXPENSES.route) {
                ExpenseScreen()
            }
            
            composable(HarmonyHubDestination.STORAGE.route) {
                StorageScreen()
            }
            
            composable(HarmonyHubDestination.CHECKIN.route) {
                CheckInScreen()
            }
            
            composable(HarmonyHubDestination.PROFILE.route) {
                ProfileScreen(
                    onLogout = {
                        navController.navigate(HarmonyHubDestination.LOGIN.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun HarmonyHubBottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    NavigationBar {
        harmonyHubScreens.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = null) },
                label = { Text(stringResource(screen.resourceId)) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}