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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LaundryRepository
import com.example.model.OrderStatus
import com.example.ui.components.StatusBadge
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CleanWhite
import com.example.ui.theme.SkyPrimary
import com.example.ui.theme.SkyPrimaryDark
import com.example.ui.theme.SkyUltraLight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminOrdersListScreen(
    onBack: () -> Unit,
    onSelectOrder: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val orders by LaundryRepository.orders.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf<OrderStatus?>(null) }

    val filteredOrders = remember(orders, searchQuery, selectedStatus) {
        orders.filter { order ->
            val matchesQuery = searchQuery.isBlank() ||
                    order.id.contains(searchQuery, ignoreCase = true) ||
                    order.customerName.contains(searchQuery, ignoreCase = true) ||
                    order.customerPhone.contains(searchQuery, ignoreCase = true)

            val matchesStatus = selectedStatus == null || order.status == selectedStatus

            matchesQuery && matchesStatus
        }
    }

    Scaffold(
        modifier = modifier.testTag("admin_orders_list_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "All Orders Database",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "${filteredOrders.size} of ${orders.size} orders showing",
                            fontSize = 12.sp,
                            color = SkyPrimaryDark
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Search field
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search by Order #, Customer Name, Phone...") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = SkyPrimary)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear search", tint = TextMuted)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SkyPrimary,
                        unfocusedBorderColor = CardBorder,
                        focusedContainerColor = CleanWhite,
                        unfocusedContainerColor = CleanWhite
                    )
                )
            }

            // Status filter chips
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        val isSelected = selectedStatus == null
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) SkyPrimary else CleanWhite,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) SkyPrimary else CardBorder
                            ),
                            modifier = Modifier.clickable { selectedStatus = null }
                        ) {
                            Text(
                                text = "All (${orders.size})",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isSelected) CleanWhite else TextPrimary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }

                    items(OrderStatus.entries.toTypedArray()) { status ->
                        val isSelected = selectedStatus == status
                        val count = orders.count { it.status == status }
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) SkyPrimary else CleanWhite,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) SkyPrimary else CardBorder
                            ),
                            modifier = Modifier.clickable { selectedStatus = status }
                        ) {
                            Text(
                                text = "${status.title} ($count)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isSelected) CleanWhite else TextPrimary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            // Orders list items
            if (filteredOrders.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No matching orders found",
                            fontSize = 14.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            items(filteredOrders) { order ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectOrder(order.id) }
                        .border(1.dp, CardBorder, RoundedCornerShape(16.dp)),
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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "#${order.id}",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SkyPrimaryDark
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = order.customerName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                            }
                            StatusBadge(status = order.status)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "${order.serviceType.title} • ${order.totalItemCount} garments",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                                Text(
                                    text = "Phone: ${order.customerPhone}",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = String.format(Locale.US, "$%.2f", order.total),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = TextMuted,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
