package com.coderbdk.budgetbuddy.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.BusinessCenter
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.coderbdk.budgetbuddy.ui.home.HomePreview
import com.coderbdk.budgetbuddy.ui.settings.SettingsViewModel
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
            BottomNavItem(Screen.Home, "Home", Icons.Filled.Home, Icons.Outlined.Home),
            BottomNavItem(Screen.Budgets, "Budgets", Icons.Filled.BusinessCenter, Icons.Outlined.BusinessCenter),
            BottomNavItem(Screen.Analytics, "Analytics", Icons.Filled.Analytics, Icons.Outlined.Analytics),
            BottomNavItem(Screen.Settings, "Settings", Icons.Filled.Settings, Icons.Outlined.Settings),
        )
    }
    val rotation by animateFloatAsState(
        if (currentDestination?.hasRoute(Screen.Home::class) != true) 0f else 90f
    )

    val mainViewModel = hiltViewModel<MainViewModel>()
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
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

    BudgetBuddyTheme(
        darkTheme = settingsViewModel.isDarkTheme
    ) {
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
                BudgetBuddyBottomNavigation(
                    currentDestination,
                    bottomNavItems,
                    onNavigate = {
                        navController.navigate(it.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                /*NavigationBar {
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
                }*/
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
                    navRouteBuilder(navController, mainViewModel, settingsViewModel)
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
                    Icons.Default.AddCircle,
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
    val iconFilled: ImageVector,
    val iconOutlined: ImageVector
)


@Composable
private fun BudgetBuddyBottomNavigation(
    currentDestination: NavDestination?,
    bottomNavItems: List<BottomNavItem<out Screen>>,
    onNavigate: (BottomNavItem<out Screen>) -> Unit
) {
        NavigationBar(
            containerColor = Color.Transparent
        ) {
            bottomNavItems.forEach { item ->
                val isSelected =
                    currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true
                NavigationBarItem(
                    icon = {
                        Icon(
                            if(isSelected)item.iconFilled else item.iconOutlined,
                            contentDescription = item.label,
                            modifier = Modifier.size(if (isSelected) 24.dp else 22.dp),
                        )
                    },
                    label = { Text(item.label) },
                    selected = isSelected,
                    onClick = {
                        onNavigate(item)
                    },
                )
            }
        }
}

@Preview(showBackground = true)
@Composable
private fun AppPreview() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavItems = remember {
        listOf(
            BottomNavItem(Screen.Home, "Home", Icons.Filled.Home, Icons.Outlined.Home),
            BottomNavItem(Screen.Budgets, "Budgets", Icons.Filled.BusinessCenter, Icons.Outlined.BusinessCenter),
            BottomNavItem(Screen.Analytics, "Analytics", Icons.Filled.Analytics, Icons.Outlined.Analytics),
            BottomNavItem(Screen.Settings, "Settings", Icons.Filled.Settings, Icons.Outlined.Settings),
        )
    }
    BudgetBuddyTheme {

        Scaffold(
            bottomBar = {
                BudgetBuddyBottomNavigation(
                    currentDestination = currentDestination,
                    bottomNavItems
                ) { }
            }
        ) { innerPadding ->
            Box(Modifier.padding(innerPadding)) {
                HomePreview()
            }
        }

    }
}

