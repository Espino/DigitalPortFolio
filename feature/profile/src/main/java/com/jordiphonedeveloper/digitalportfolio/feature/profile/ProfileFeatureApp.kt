package com.jordiphonedeveloper.digitalportfolio.feature.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.RocketLaunch
import androidx.compose.material.icons.outlined.WorkHistory
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jordiphonedeveloper.digitalportfolio.feature.profile.ui.ContactScreen
import com.jordiphonedeveloper.digitalportfolio.feature.profile.ui.ExperienceScreen
import com.jordiphonedeveloper.digitalportfolio.feature.profile.ui.HomeScreen
import com.jordiphonedeveloper.digitalportfolio.feature.profile.ui.ProjectsScreen
import com.jordiphonedeveloper.digitalportfolio.feature.profile.ui.QrCodeDialog
import org.koin.androidx.compose.koinViewModel

private data class Destination(
    val route: String,
    val label: String,
    val icon: ImageVector,
)

private val destinations = listOf(
    Destination("home", "Perfil", Icons.Outlined.Home),
    Destination("experience", "Trayectoria", Icons.Outlined.WorkHistory),
    Destination("projects", "Proyectos", Icons.Outlined.RocketLaunch),
    Destination("contact", "Contacto", Icons.Outlined.AlternateEmail),
)

@Composable
fun ProfileFeatureApp(viewModel: ProfileViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.effects.collect(context::handleProfileEffect)
    }

    LaunchedEffect(state.notice) {
        state.notice?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onIntent(ProfileIntent.DismissNotice)
        }
    }

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val useRail = maxWidth >= 840.dp
        if (useRail) {
            Row(Modifier.fillMaxSize()) {
                PortfolioNavigationRail(navController)
                Box(Modifier.weight(1f)) {
                    ProfileNavHost(navController, state, viewModel::onIntent)
                    SnackbarHost(
                        hostState = snackbarHostState,
                        modifier = Modifier.padding(24.dp),
                    )
                }
            }
        } else {
            Scaffold(
                containerColor = MaterialTheme.colorScheme.background,
                snackbarHost = { SnackbarHost(snackbarHostState) },
                bottomBar = { PortfolioNavigationBar(navController) },
            ) { innerPadding ->
                ProfileNavHost(
                    navController = navController,
                    state = state,
                    onIntent = viewModel::onIntent,
                    modifier = Modifier.padding(innerPadding),
                )
            }
        }
    }

    val profile = state.profile
    if (state.isQrVisible && profile != null) {
        QrCodeDialog(
            name = profile.identity.fullName,
            websiteUrl = profile.contact.websiteUrl,
            onDismiss = { viewModel.onIntent(ProfileIntent.HideQr) },
            onOpenWebsite = { viewModel.onIntent(ProfileIntent.OpenWebsite) },
        )
    }
}

@Composable
private fun ProfileNavHost(
    navController: NavHostController,
    state: ProfileUiState,
    onIntent: (ProfileIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = destinations.first().route,
        modifier = modifier.fillMaxSize(),
    ) {
        composable("home") { HomeScreen(state, onIntent) }
        composable("experience") { ExperienceScreen(state, onIntent) }
        composable("projects") { ProjectsScreen(state, onIntent) }
        composable("contact") { ContactScreen(state, onIntent) }
    }
}

@Composable
private fun PortfolioNavigationBar(navController: NavHostController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        destinations.forEach { destination ->
            NavigationBarItem(
                selected = currentRoute == destination.route,
                onClick = { navController.navigateSingleTop(destination.route) },
                icon = { Icon(destination.icon, contentDescription = destination.label) },
                label = { androidx.compose.material3.Text(destination.label) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
            )
        }
    }
}

@Composable
private fun PortfolioNavigationRail(navController: NavHostController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    NavigationRail(containerColor = MaterialTheme.colorScheme.surface) {
        destinations.forEach { destination ->
            NavigationRailItem(
                selected = currentRoute == destination.route,
                onClick = { navController.navigateSingleTop(destination.route) },
                icon = { Icon(destination.icon, contentDescription = destination.label) },
                label = { androidx.compose.material3.Text(destination.label) },
            )
        }
    }
}

private fun NavHostController.navigateSingleTop(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
