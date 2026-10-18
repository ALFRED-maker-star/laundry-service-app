package com.example.ui.customer

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LaundryRepository
import com.example.model.LaundryOrder
import com.example.model.OrderStatus
import com.example.ui.components.StatusBadge
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CleanWhite
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
fun OrderHistoryScreen(
    onTrackOrder: (String) -> Unit,
    onReorder: (LaundryOrder) -> Unit,
    modifier: Modifier = Modifier
) {
    val orders by LaundryRepository.orders.collectAsState()
    var filterType by remember { mutableStateOf("ALL") }

    val filteredOrders = remember(orders, filterType) {
        when (filterType) {
            "ACTIVE" -> orders.filter { it.status != OrderStatus.DELIVERED }
            "COMPLETED" -> orders.filter { it.status == OrderStatus.DELIVERED }
            else -> orders
        }
    }

    Scaffold(
        modifier = modifier.testTag("order_history_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Order History",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Track current & reorder past laundry",
                            fontSize = 12.sp,
                            color = TextSecondary
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Filter chips
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val filters = listOf(
                        Pair("ALL", "All Orders (${orders.size})"),
                        Pair("ACTIVE", "Active (${orders.count { it.status != OrderStatus.DELIVERED }})"),
                        Pair("COMPLETED", "Completed (${orders.count { it.status == OrderStatus.DELIVERED }})")
                    )

                    items(filters) { (key, label) ->
                        val isSelected = filterType == key
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) SkyPrimary else CleanWhite,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) SkyPrimary else CardBorder
                            ),
                            modifier = Modifier.clickable { filterType = key }
                        ) {
                            Text(
                                text = label,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isSelected) CleanWhite else TextPrimary,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                            )
                        }
                    }
                }
            }

            if (filteredOrders.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CleanWhite)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = SkyLight,
                                modifier = Modifier.size(56.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.History,
                                        contentDescription = null,
                                        tint = SkyPrimary,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }
                            Text(
                                text = "No orders found",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Your past laundry orders will appear here.",
                                fontSize = 13.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            items(filteredOrders) { order ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onTrackOrder(order.id) }
                        .border(1.dp, CardBorder, RoundedCornerShape(18.dp)),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = CleanWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Order #${order.id}",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "${order.pickupDate} • ${order.pickupTimeSlot}",
                                    fontSize = 12.sp,
                                    color = TextMuted
                                )
                            }
                            StatusBadge(status = order.status)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "${order.serviceType.title} • ${order.totalItemCount} garments",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SkyPrimaryDark
                        )

                        // Item preview tags
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            order.items.take(3).forEach { orderItem ->
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = SkyLight
                                ) {
                                    Text(
                                        text = "${orderItem.quantity}x ${orderItem.item.name}",
                                        fontSize = 11.sp,
                                        color = SkyPrimaryDark,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        maxLines = 1
                                    )
                                }
                            }
                            if (order.items.size > 3) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = CardBorder.copy(alpha = 0.5f)
                                ) {
                                    Text(
                                        text = "+${order.items.size - 3} more",
                                        fontSize = 11.sp,
                                        color = TextSecondary,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = String.format(Locale.US, "Total: $%.2f", order.total),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextPrimary
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedButton(
                                    onClick = {
                                        LaundryRepository.loadReorderDraft(order)
                                        onReorder(order)
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = SkyPrimaryDark
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Reorder", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }

                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = SkyLight,
                                    modifier = Modifier.clickable { onTrackOrder(order.id) }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Details",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SkyPrimaryDark
                                        )
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                            contentDescription = null,
                                            tint = SkyPrimaryDark,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
