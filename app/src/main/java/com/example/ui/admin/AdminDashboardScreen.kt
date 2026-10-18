package com.example.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PriceChange
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LaundryRepository
import com.example.model.OrderStatus
import com.example.ui.components.StatusBadge
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.AmberWarningLight
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CleanWhite
import com.example.ui.theme.MintSuccess
import com.example.ui.theme.MintSuccessLight
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.PurpleAccentLight
import com.example.ui.theme.SkyLight
import com.example.ui.theme.SkyPrimary
import com.example.ui.theme.SkyPrimaryDark
import com.example.ui.theme.SkyUltraLight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    onNavigateOrdersList: () -> Unit,
    onNavigateOrderDetail: (String) -> Unit,
    onNavigatePricing: () -> Unit,
    onSwitchToCustomer: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val orders by LaundryRepository.orders.collectAsState()
    val stats = remember(orders) { LaundryRepository.getAdminStats() }

    // Orders that require staff status updates (e.g. PICKED_UP, WASHING, IRONING)
    val ordersNeedingUpdate = remember(orders) {
        orders.filter { it.status == OrderStatus.PICKED_UP || it.status == OrderStatus.WASHING || it.status == OrderStatus.IRONING }
    }

    Scaffold(
        modifier = modifier.testTag("admin_dashboard_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "FreshFold Operations",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Admin Hub & Facility Overview",
                            fontSize = 12.sp,
                            color = SkyPrimaryDark
                        )
                    }
                },
                actions = {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SkyLight,
                        modifier = Modifier
                            .clickable { onSwitchToCustomer() }
                            .padding(end = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapHoriz,
                                contentDescription = null,
                                tint = SkyPrimaryDark,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Customer App",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = SkyPrimaryDark
                            )
                        }
                    }

                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Logout",
                            tint = TextSecondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CleanWhite)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(SkyUltraLight)
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Stat Cards 2x2 Grid
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        KpiStatCard(
                            label = "Orders Today",
                            value = "${stats.ordersToday}",
                            trend = "+18% vs yesterday",
                            icon = Icons.Default.ShoppingBag,
                            color = SkyPrimary,
                            bgColor = SkyLight,
                            modifier = Modifier.weight(1f)
                        )

                        KpiStatCard(
                            label = "Pending Pickups",
                            value = "${stats.pendingPickups}",
                            trend = "Needs dispatch",
                            icon = Icons.Default.LocalLaundryService,
                            color = AmberWarning,
                            bgColor = AmberWarningLight,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        KpiStatCard(
                            label = "Revenue This Week",
                            value = String.format(Locale.US, "$%.0f", stats.revenueThisWeek),
                            trend = "+12% growth",
                            icon = Icons.Default.AttachMoney,
                            color = MintSuccess,
                            bgColor = MintSuccessLight,
                            modifier = Modifier.weight(1f)
                        )

                        KpiStatCard(
                            label = "In Processing",
                            value = "${stats.inProcessing}",
                            trend = "Wash & Press stages",
                            icon = Icons.Default.TrendingUp,
                            color = PurpleAccent,
                            bgColor = PurpleAccentLight,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Quick Operations Shortcuts
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CleanWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onNavigateOrdersList,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SkyPrimary,
                                contentColor = CleanWhite
                            )
                        ) {
                            Text("All Orders (${orders.size})", fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = onNavigatePricing,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SkyPrimaryDark),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = SkyPrimaryDark
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.PriceChange,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Pricing Setup", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Needs Attention Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = AmberWarningLight,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.NotificationsActive,
                                    contentDescription = null,
                                    tint = AmberWarning,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                        Text(
                            text = "ORDERS NEEDING STATUS UPDATE",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = AmberWarning,
                            letterSpacing = 1.sp
                        )
                    }

                    Text(
                        text = "${ordersNeedingUpdate.size} pending",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondary
                    )
                }
            }

            items(ordersNeedingUpdate) { order ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateOrderDetail(order.id) }
                        .border(1.5.dp, AmberWarning.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CleanWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Order #${order.id} • ${order.customerName}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "${order.serviceType.title} • ${order.totalItemCount} garments",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }

                            StatusBadge(status = order.status)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = String.format(Locale.US, "$%.2f", order.total),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = SkyPrimaryDark
                            )

                            Button(
                                onClick = {
                                    LaundryRepository.advanceOrderStatus(order.id)
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = SkyLight,
                                    contentColor = SkyPrimaryDark
                                ),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "Advance to ${order.status.next()?.title ?: "Next"}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun KpiStatCard(
    label: String,
    value: String,
    trend: String,
    icon: ImageVector,
    color: Color,
    bgColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.border(1.dp, CardBorder, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CleanWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = bgColor,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = color,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = value,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary
            )

            Text(
                text = trend,
                fontSize = 11.sp,
                color = color,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}
