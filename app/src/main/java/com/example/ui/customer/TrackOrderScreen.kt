package com.example.ui.customer

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LaundryRepository
import com.example.model.OrderStatus
import com.example.ui.components.StatusBadge
import com.example.ui.components.StatusStepper
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CleanWhite
import com.example.ui.theme.MintSuccess
import com.example.ui.theme.MintSuccessLight
import com.example.ui.theme.SkyLight
import com.example.ui.theme.SkyPrimary
import com.example.ui.theme.SkyPrimaryDark
import com.example.ui.theme.SkySoft
import com.example.ui.theme.SkyUltraLight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackOrderScreen(
    orderId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val orders by LaundryRepository.orders.collectAsState()
    val order = orders.firstOrNull { it.id == orderId } ?: orders.firstOrNull()

    var showItemsList by remember { mutableStateOf(false) }

    if (order == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No order found", color = TextSecondary)
        }
        return
    }

    Scaffold(
        modifier = modifier.testTag("track_order_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Track Order #${order.id}",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "${order.serviceType.title} • Live Status",
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
                actions = {
                    StatusBadge(status = order.status, modifier = Modifier.padding(end = 12.dp))
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
            // 1. Prominent ETA & Status Header Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CleanWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = if (order.status == OrderStatus.DELIVERED) "Delivered at Doorstep" else "Estimated Delivery",
                                    fontSize = 12.sp,
                                    color = TextMuted,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = order.estimatedDelivery,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (order.status == OrderStatus.DELIVERED) MintSuccess else SkyPrimaryDark
                                )
                            }

                            Surface(
                                shape = CircleShape,
                                color = if (order.status == OrderStatus.DELIVERED) MintSuccessLight else SkyLight,
                                modifier = Modifier.size(48.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = if (order.status == OrderStatus.DELIVERED) Icons.Default.CheckCircle else Icons.Default.Schedule,
                                        contentDescription = null,
                                        tint = if (order.status == OrderStatus.DELIVERED) MintSuccess else SkyPrimary,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = SkyPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = order.deliveryAddress,
                                fontSize = 13.sp,
                                color = TextSecondary,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            // 2. Status Stepper
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CleanWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "ORDER PROGRESS",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SkyPrimaryDark,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        StatusStepper(currentStatus = order.status)
                    }
                }
            }

            // 3. Valet / Driver Specialist Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CleanWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "ASSIGNED VALET",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SkyPrimaryDark,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = SkyLight,
                                    modifier = Modifier.size(46.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = null,
                                            tint = SkyPrimaryDark,
                                            modifier = Modifier.size(26.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = order.driverName,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            tint = Color(0xFFFFD166),
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Text(
                                            text = "${order.driverRating} • Laundry Valet",
                                            fontSize = 12.sp,
                                            color = TextSecondary
                                        )
                                    }
                                }
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Surface(
                                    shape = CircleShape,
                                    color = SkyLight,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .clickable {
                                            try {
                                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${order.driverPhone}"))
                                                context.startActivity(intent)
                                            } catch (e: Exception) {
                                                Toast.makeText(context, "Calling ${order.driverName}...", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Call,
                                            contentDescription = "Call driver",
                                            tint = SkyPrimaryDark,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                Surface(
                                    shape = CircleShape,
                                    color = SkyLight,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .clickable {
                                            try {
                                                val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:${order.driverPhone}"))
                                                context.startActivity(intent)
                                            } catch (e: Exception) {
                                                Toast.makeText(context, "Messaging ${order.driverName}...", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Message,
                                            contentDescription = "Message driver",
                                            tint = SkyPrimaryDark,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 4. Live Simulation / Quick Advance Tool (For User Testing!)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = SkyLight.copy(alpha = 0.5f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SkySoft)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FastForward,
                                contentDescription = null,
                                tint = SkyPrimaryDark,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "LIVE SIMULATION CONTROLS",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = SkyPrimaryDark
                            )
                        }

                        Text(
                            text = "Advance order to next step in real-time or test full workflow.",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(top = 4.dp, bottom = 10.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    val next = LaundryRepository.advanceOrderStatus(order.id)
                                    if (next != null) {
                                        Toast.makeText(context, "Order advanced to ${next.title}!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Order is already Delivered!", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                enabled = order.status != OrderStatus.DELIVERED,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = SkyPrimary,
                                    contentColor = CleanWhite
                                )
                            ) {
                                Text("Advance Next Step")
                            }

                            OutlinedButton(
                                onClick = {
                                    LaundryRepository.updateOrderStatus(order.id, OrderStatus.PICKED_UP)
                                    Toast.makeText(context, "Reset to Picked Up", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Reset")
                            }
                        }
                    }
                }
            }

            // 5. Expandable Receipt Breakdown
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = CleanWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showItemsList = !showItemsList },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Receipt,
                                    contentDescription = null,
                                    tint = SkyPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Order Receipt (${order.totalItemCount} items)",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                            Text(
                                text = if (showItemsList) "Hide Details" else "View Details",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SkyPrimary
                            )
                        }

                        AnimatedVisibility(visible = showItemsList) {
                            Column(
                                modifier = Modifier.padding(top = 12.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                order.items.forEach { orderItem ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "${orderItem.quantity}x ${orderItem.item.name}",
                                            fontSize = 13.sp,
                                            color = TextSecondary
                                        )
                                        Text(
                                            text = String.format(Locale.US, "$%.2f", orderItem.subtotal),
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = TextPrimary
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Total Charged",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = String.format(Locale.US, "$%.2f", order.total),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = SkyPrimaryDark
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
