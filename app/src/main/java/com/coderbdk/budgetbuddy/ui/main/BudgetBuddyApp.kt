package com.coderbdk.budgetbuddy.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.coderbdk.budgetbuddy.ui.theme.BudgetBuddyTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetBuddyApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val currentDestination = navBackStackEntry?.destination


    val bottomNavItems = remember {
        listOf(
            BottomNavItem(Screen.Home, "Home", Icons.Default.Home),
            BottomNavItem(Screen.Budgets, "Budgets", Icons.Default.BusinessCenter),
            BottomNavItem(Screen.Analytics, "Analytics", Icons.Default.Analytics),
            BottomNavItem(Screen.Settings, "Settings", Icons.Default.Settings),
        )
    }
    val rotation by animateFloatAsState(
        if (currentDestination?.hasRoute(Screen.Home::class) != true) 0f else 90f
    )

    val mainViewModel = hiltViewModel<MainViewModel>()
    val fabVisible by mainViewModel.fabVisible.collectAsState()

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                mainViewModel.setFabVisibility(available.y > 0)
                return Offset.Zero
            }
        }
    }

    val title = currentDestination?.getNavDestinationTitle("Null")

    BudgetBuddyTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
                .nestedScroll(nestedScrollConnection),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = if (currentDestination?.hasRoute(Screen.Home::class) == true) "Budget Buddy" else "$title",
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        if (currentDestination?.hasRoute(Screen.Home::class) != true) {
                            IconButton(
                                onClick = { navController.navigateUp() },
                                modifier = Modifier.rotate(rotation)
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    },
                    scrollBehavior = topAppBarScrollBehavior
                )
            },
            bottomBar = {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            },
            floatingActionButton = {
                AnimatedVisibility(
                    visible = /*currentDestination?.hasRoute(AddTransaction::class) != true && currentDestination?.hasRoute(
                        Budget::class
                    ) != true &&*/ fabVisible,
                    enter = slideInVertically() + fadeIn(),
                    exit = slideOutVertically() + fadeOut()
                ) {
                    val baseRoute = currentDestination?.route?.substringBefore("/") ?: ""
                    FloatingActionButtonContent(baseRoute, navController, mainViewModel)
                }
            }) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Home,
                modifier = Modifier.padding(innerPadding),
                enterTransition = { slideInHorizontally(initialOffsetX = { -it }) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() },
                builder = {
                    navRouteBuilder(navController, mainViewModel)
                }
            )
        }
    }
}

@Composable
fun FloatingActionButtonContent(
    baseRoute: String,
    navController: NavController,
    mainViewModel: MainViewModel
) {
    when (baseRoute) {
        Screen.Home::class.qualifiedName -> {
            FloatingActionButton(onClick = {
                navController.navigate(Screen.AddTransaction)
            }) {

                Icon(
                    Icons.Default.MonetizationOn,
                    contentDescription = "Add Transaction"
                )
            }
        }

        Screen.Budgets::class.qualifiedName -> {
            FloatingActionButton(onClick = {
                mainViewModel.performFabAction(FabAction.AddBudget)
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Budget")
            }
        }
    }
}


private data class BottomNavItem<T : Any>(
    val route: T,
    val label: String,
    val icon: ImageVector
)