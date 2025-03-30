package com.harmonyhub.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Storage
import androidx.compose.ui.graphics.vector.ImageVector
import com.harmonyhub.app.R

/**
 * Destinations used in the app
 */
enum class HarmonyHubDestination(val route: String) {
    LOGIN("login"),
    HOME("home"),
    SCHEDULE("schedule"),
    MESSAGES("messages"),
    EXPENSES("expenses"),
    STORAGE("storage"),
    CHECKIN("checkin"),
    PROFILE("profile")
}

/**
 * Models for the bottom navigation items in the app
 */
data class HarmonyHubScreen(
    val route: String,
    val resourceId: Int,
    val icon: ImageVector
)

/**
 * List of screens for the bottom navigation bar
 */
val harmonyHubScreens = listOf(
    HarmonyHubScreen(
        route = HarmonyHubDestination.HOME.route,
        resourceId = R.string.nav_home,
        icon = Icons.Default.Home
    ),
    HarmonyHubScreen(
        route = HarmonyHubDestination.SCHEDULE.route,
        resourceId = R.string.nav_schedule,
        icon = Icons.Default.ReceiptLong
    ),
    HarmonyHubScreen(
        route = HarmonyHubDestination.MESSAGES.route,
        resourceId = R.string.nav_messages,
        icon = Icons.Default.Message
    ),
    HarmonyHubScreen(
        route = HarmonyHubDestination.EXPENSES.route,
        resourceId = R.string.nav_expenses,
        icon = Icons.Default.MonetizationOn
    ),
    HarmonyHubScreen(
        route = HarmonyHubDestination.STORAGE.route,
        resourceId = R.string.nav_storage,
        icon = Icons.Default.Storage
    ),
    HarmonyHubScreen(
        route = HarmonyHubDestination.CHECKIN.route,
        resourceId = R.string.nav_checkin,
        icon = Icons.Default.CheckCircle
    ),
    HarmonyHubScreen(
        route = HarmonyHubDestination.PROFILE.route,
        resourceId = R.string.nav_profile,
        icon = Icons.Default.AccountCircle
    )
)