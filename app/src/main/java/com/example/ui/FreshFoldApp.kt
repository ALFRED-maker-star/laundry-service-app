package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.PriceChange
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LaundryRepository
import com.example.ui.admin.AdminDashboardScreen
import com.example.ui.admin.AdminLoginScreen
import com.example.ui.admin.AdminOrderDetailScreen
import com.example.ui.admin.AdminOrdersListScreen
import com.example.ui.admin.AdminPricingScreen
import com.example.ui.customer.CustomerHomeScreen
import com.example.ui.customer.NewOrderScreen
import com.example.ui.customer.OrderHistoryScreen
import com.example.ui.customer.OrderItemsScreen
import com.example.ui.customer.OrderSummaryScreen
import com.example.ui.customer.TrackOrderScreen
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CleanWhite
import com.example.ui.theme.SkyLight
import com.example.ui.theme.SkyPrimary
import com.example.ui.theme.SkyPrimaryDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary

sealed class FreshFoldScreen {
    // Customer Screens
    data object CustomerHome : FreshFoldScreen()
    data object NewOrder : FreshFoldScreen()
    data object OrderItems : FreshFoldScreen()
    data object OrderSummary : FreshFoldScreen()
    data class TrackOrder(val orderId: String) : FreshFoldScreen()
    data object OrderHistory : FreshFoldScreen()

    // Admin Screens
    data object AdminLogin : FreshFoldScreen()
    data object AdminDashboard : FreshFoldScreen()
    data object AdminOrdersList : FreshFoldScreen()
    data class AdminOrderDetail(val orderId: String) : FreshFoldScreen()
    data object AdminPricing : FreshFoldScreen()
}

@Composable
fun FreshFoldApp(modifier: Modifier = Modifier) {
    val navStack = remember { mutableStateListOf<FreshFoldScreen>(FreshFoldScreen.CustomerHome) }
    val currentScreen = navStack.lastOrNull() ?: FreshFoldScreen.CustomerHome

    val isAdminLoggedIn by LaundryRepository.isAdminLoggedIn.collectAsState()

    fun navigateTo(screen: FreshFoldScreen) {
        navStack.add(screen)
    }

    fun navigateBack() {
        if (navStack.size > 1) {
            navStack.removeAt(navStack.lastIndex)
        }
    }

    fun replaceTop(screen: FreshFoldScreen) {
        if (navStack.isNotEmpty()) {
            navStack.removeAt(navStack.lastIndex)
        }
        navStack.add(screen)
    }

    fun resetTo(screen: FreshFoldScreen) {
        navStack.clear()
        navStack.add(screen)
    }

    // Handle Android system back button
    BackHandler(enabled = navStack.size > 1) {
        navigateBack()
    }

    val isCustomerSection = currentScreen is FreshFoldScreen.CustomerHome ||
            currentScreen is FreshFoldScreen.NewOrder ||
            currentScreen is FreshFoldScreen.OrderItems ||
            currentScreen is FreshFoldScreen.OrderSummary ||
            currentScreen is FreshFoldScreen.TrackOrder ||
            currentScreen is FreshFoldScreen.OrderHistory

    val isAdminSection = currentScreen is FreshFoldScreen.AdminDashboard ||
            currentScreen is FreshFoldScreen.AdminOrdersList ||
            currentScreen is FreshFoldScreen.AdminOrderDetail ||
            currentScreen is FreshFoldScreen.AdminPricing

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            if (isCustomerSection) {
                Surface(
                    color = CleanWhite,
                    shadowElevation = 8.dp,
                    border = androidx.compose.foundation.BorderStroke(0.5.dp, CardBorder)
                ) {
                    NavigationBar(
                        containerColor = CleanWhite,
                        contentColor = TextSecondary,
                        modifier = Modifier
                            .windowInsetsPadding(WindowInsets.navigationBars)
                            .testTag("customer_bottom_nav")
                    ) {
                        NavigationBarItem(
                            selected = currentScreen is FreshFoldScreen.CustomerHome,
                            onClick = { resetTo(FreshFoldScreen.CustomerHome) },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = "Home"
                                )
                            },
                            label = { Text("Home", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = SkyPrimaryDark,
                                selectedTextColor = SkyPrimaryDark,
                                indicatorColor = SkyLight
                            )
                        )

                        NavigationBarItem(
                            selected = currentScreen is FreshFoldScreen.NewOrder || currentScreen is FreshFoldScreen.OrderItems || currentScreen is FreshFoldScreen.OrderSummary,
                            onClick = {
                                if (currentScreen !is FreshFoldScreen.NewOrder) {
                                    navigateTo(FreshFoldScreen.NewOrder)
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.AddCircle,
                                    contentDescription = "Pickup"
                                )
                            },
                            label = { Text("New Order", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = SkyPrimaryDark,
                                selectedTextColor = SkyPrimaryDark,
                                indicatorColor = SkyLight
                            )
                        )

                        NavigationBarItem(
                            selected = currentScreen is FreshFoldScreen.OrderHistory,
                            onClick = {
                                if (currentScreen !is FreshFoldScreen.OrderHistory) {
                                    navigateTo(FreshFoldScreen.OrderHistory)
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.History,
                                    contentDescription = "History"
                                )
                            },
                            label = { Text("Orders", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = SkyPrimaryDark,
                                selectedTextColor = SkyPrimaryDark,
                                indicatorColor = SkyLight
                            )
                        )

                        NavigationBarItem(
                            selected = false,
                            onClick = {
                                if (isAdminLoggedIn) {
                                    resetTo(FreshFoldScreen.AdminDashboard)
                                } else {
                                    navigateTo(FreshFoldScreen.AdminLogin)
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.AdminPanelSettings,
                                    contentDescription = "Admin Portal"
                                )
                            },
                            label = { Text("Admin", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = SkyPrimaryDark,
                                selectedTextColor = SkyPrimaryDark,
                                indicatorColor = SkyLight
                            )
                        )
                    }
                }
            } else if (isAdminSection) {
                Surface(
                    color = CleanWhite,
                    shadowElevation = 8.dp,
                    border = androidx.compose.foundation.BorderStroke(0.5.dp, CardBorder)
                ) {
                    NavigationBar(
                        containerColor = CleanWhite,
                        contentColor = TextSecondary,
                        modifier = Modifier
                            .windowInsetsPadding(WindowInsets.navigationBars)
                            .testTag("admin_bottom_nav")
                    ) {
                        NavigationBarItem(
                            selected = currentScreen is FreshFoldScreen.AdminDashboard,
                            onClick = { resetTo(FreshFoldScreen.AdminDashboard) },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Dashboard,
                                    contentDescription = "Dashboard"
                                )
                            },
                            label = { Text("Dashboard", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = SkyPrimaryDark,
                                selectedTextColor = SkyPrimaryDark,
                                indicatorColor = SkyLight
                            )
                        )

                        NavigationBarItem(
                            selected = currentScreen is FreshFoldScreen.AdminOrdersList,
                            onClick = {
                                if (currentScreen !is FreshFoldScreen.AdminOrdersList) {
                                    navigateTo(FreshFoldScreen.AdminOrdersList)
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.ListAlt,
                                    contentDescription = "Orders"
                                )
                            },
                            label = { Text("All Orders", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = SkyPrimaryDark,
                                selectedTextColor = SkyPrimaryDark,
                                indicatorColor = SkyLight
                            )
                        )

                        NavigationBarItem(
                            selected = currentScreen is FreshFoldScreen.AdminPricing,
                            onClick = {
                                if (currentScreen !is FreshFoldScreen.AdminPricing) {
                                    navigateTo(FreshFoldScreen.AdminPricing)
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.PriceChange,
                                    contentDescription = "Pricing"
                                )
                            },
                            label = { Text("Pricing", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = SkyPrimaryDark,
                                selectedTextColor = SkyPrimaryDark,
                                indicatorColor = SkyLight
                            )
                        )

                        NavigationBarItem(
                            selected = false,
                            onClick = { resetTo(FreshFoldScreen.CustomerHome) },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.SwapHoriz,
                                    contentDescription = "Exit to Customer"
                                )
                            },
                            label = { Text("Customer", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = SkyPrimaryDark,
                                selectedTextColor = SkyPrimaryDark,
                                indicatorColor = SkyLight
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Crossfade(targetState = currentScreen, label = "screen_transition") { screen ->
                when (screen) {
                    is FreshFoldScreen.CustomerHome -> {
                        CustomerHomeScreen(
                            onSchedulePickupClick = { navigateTo(FreshFoldScreen.NewOrder) },
                            onServiceSelected = { service ->
                                LaundryRepository.setDraftServiceType(service)
                                navigateTo(FreshFoldScreen.NewOrder)
                            },
                            onTrackOrderClick = { orderId ->
                                navigateTo(FreshFoldScreen.TrackOrder(orderId))
                            }
                        )
                    }

                    is FreshFoldScreen.NewOrder -> {
                        NewOrderScreen(
                            onBack = { navigateBack() },
                            onContinueToItems = { navigateTo(FreshFoldScreen.OrderItems) }
                        )
                    }

                    is FreshFoldScreen.OrderItems -> {
                        OrderItemsScreen(
                            onBack = { navigateBack() },
                            onContinueToSummary = { navigateTo(FreshFoldScreen.OrderSummary) }
                        )
                    }

                    is FreshFoldScreen.OrderSummary -> {
                        OrderSummaryScreen(
                            onBack = { navigateBack() },
                            onEditSchedule = { navigateBack(); navigateBack() },
                            onEditItems = { navigateBack() },
                            onOrderPlaced = { orderId ->
                                replaceTop(FreshFoldScreen.TrackOrder(orderId))
                            }
                        )
                    }

                    is FreshFoldScreen.TrackOrder -> {
                        TrackOrderScreen(
                            orderId = screen.orderId,
                            onBack = {
                                if (navStack.size > 1) navigateBack() else resetTo(FreshFoldScreen.CustomerHome)
                            }
                        )
                    }

                    is FreshFoldScreen.OrderHistory -> {
                        OrderHistoryScreen(
                            onTrackOrder = { orderId ->
                                navigateTo(FreshFoldScreen.TrackOrder(orderId))
                            },
                            onReorder = { order ->
                                navigateTo(FreshFoldScreen.NewOrder)
                            }
                        )
                    }

                    is FreshFoldScreen.AdminLogin -> {
                        AdminLoginScreen(
                            onBackToCustomer = { resetTo(FreshFoldScreen.CustomerHome) },
                            onLoginSuccess = { resetTo(FreshFoldScreen.AdminDashboard) }
                        )
                    }

                    is FreshFoldScreen.AdminDashboard -> {
                        AdminDashboardScreen(
                            onNavigateOrdersList = { navigateTo(FreshFoldScreen.AdminOrdersList) },
                            onNavigateOrderDetail = { orderId ->
                                navigateTo(FreshFoldScreen.AdminOrderDetail(orderId))
                            },
                            onNavigatePricing = { navigateTo(FreshFoldScreen.AdminPricing) },
                            onSwitchToCustomer = { resetTo(FreshFoldScreen.CustomerHome) },
                            onLogout = {
                                LaundryRepository.logoutAdmin()
                                resetTo(FreshFoldScreen.CustomerHome)
                            }
                        )
                    }

                    is FreshFoldScreen.AdminOrdersList -> {
                        AdminOrdersListScreen(
                            onBack = { navigateBack() },
                            onSelectOrder = { orderId ->
                                navigateTo(FreshFoldScreen.AdminOrderDetail(orderId))
                            }
                        )
                    }

                    is FreshFoldScreen.AdminOrderDetail -> {
                        AdminOrderDetailScreen(
                            orderId = screen.orderId,
                            onBack = { navigateBack() }
                        )
                    }

                    is FreshFoldScreen.AdminPricing -> {
                        AdminPricingScreen(
                            onBack = { navigateBack() }
                        )
                    }
                }
            }
        }
    }
}
