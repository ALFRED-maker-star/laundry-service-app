package com.example.ui.admin

import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LaundryRepository
import com.example.model.OrderStatus
import com.example.ui.components.StatusBadge
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CleanWhite
import com.example.ui.theme.MintSuccess
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
fun AdminOrderDetailScreen(
    orderId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val orders by LaundryRepository.orders.collectAsState()
    val order = orders.firstOrNull { it.id == orderId } ?: orders.firstOrNull()

    if (order == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Order not found", color = TextSecondary)
        }
        return
    }

    var isDropdownExpanded by remember { mutableStateOf(false) }
    var notesText by remember(order.adminNotes) { mutableStateOf(order.adminNotes) }

    Scaffold(
        modifier = modifier.testTag("admin_order_detail_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Order #${order.id}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Admin Management & Status Dispatch",
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
            // 1. Status Update Control Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = CleanWhite),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, SkyPrimary),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "STATUS DISPATCH CONTROL",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SkyPrimaryDark,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        // Dropdown status selector
                        ExposedDropdownMenuBox(
                            expanded = isDropdownExpanded,
                            onExpandedChange = { isDropdownExpanded = !isDropdownExpanded }
                        ) {
                            OutlinedTextField(
                                value = order.status.title,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Current Status Stage") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = SkyPrimary,
                                    unfocusedBorderColor = CardBorder
                                )
                            )

                            ExposedDropdownMenu(
                                expanded = isDropdownExpanded,
                                onDismissRequest = { isDropdownExpanded = false }
                            ) {
                                OrderStatus.entries.forEach { statusOption ->
                                    DropdownMenuItem(
                                        text = {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "${statusOption.stepIndex + 1}. ${statusOption.title}",
                                                    fontWeight = if (statusOption == order.status) FontWeight.Bold else FontWeight.Normal
                                                )
                                                StatusBadge(status = statusOption)
                                            }
                                        },
                                        onClick = {
                                            LaundryRepository.updateOrderStatus(order.id, statusOption)
                                            isDropdownExpanded = false
                                            Toast.makeText(context, "Status set to ${statusOption.title}", Toast.LENGTH_SHORT).show()
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Quick 1-tap advance button
                        val nextStatus = order.status.next()
                        Button(
                            onClick = {
                                val next = LaundryRepository.advanceOrderStatus(order.id)
                                if (next != null) {
                                    Toast.makeText(context, "Advanced to ${next.title}", Toast.LENGTH_SHORT).show()
                                }
                            },
                            enabled = nextStatus != null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SkyPrimary,
                                contentColor = CleanWhite
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.FastForward,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (nextStatus != null) "Advance to ${nextStatus.title}" else "Order Completed (Delivered)",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // 2. Customer Information Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = CleanWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "CUSTOMER CONTACT INFO",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SkyPrimaryDark,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
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
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = order.customerName,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = order.customerPhone,
                                        fontSize = 13.sp,
                                        color = TextSecondary
                                    )
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
                                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${order.customerPhone}"))
                                                context.startActivity(intent)
                                            } catch (e: Exception) {
                                                Toast.makeText(context, "Dialing ${order.customerPhone}", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Call,
                                            contentDescription = "Call",
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
                                                val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:${order.customerPhone}"))
                                                context.startActivity(intent)
                                            } catch (e: Exception) {
                                                Toast.makeText(context, "SMS to ${order.customerPhone}", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Message,
                                            contentDescription = "Message",
                                            tint = SkyPrimaryDark,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = SkyPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Address: ${order.deliveryAddress}",
                                fontSize = 13.sp,
                                color = TextSecondary
                            )
                        }

                        if (order.specialInstructions.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Customer Note: \"${order.specialInstructions}\"",
                                fontSize = 12.sp,
                                color = TextPrimary,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                                    .background(SkyLight.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                    .padding(8.dp)
                            )
                        }
                    }
                }
            }

            // 3. Item Breakdown Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = CleanWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "ITEM BREAKDOWN (${order.totalItemCount} ITEMS)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = SkyPrimaryDark,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = order.serviceType.title,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = SkyPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        order.items.forEach { orderItem ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${orderItem.quantity}x ${orderItem.item.name}",
                                    fontSize = 14.sp,
                                    color = TextPrimary
                                )
                                Text(
                                    text = String.format(Locale.US, "$%.2f", orderItem.subtotal),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = CardBorder)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Order Total (${order.paymentMethod.title})",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = String.format(Locale.US, "$%.2f", order.total),
                                fontSize = 17.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = SkyPrimaryDark
                            )
                        }
                    }
                }
            }

            // 4. Staff Internal Notes Editor
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = CleanWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.NoteAlt,
                                    contentDescription = null,
                                    tint = SkyPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "INTERNAL STAFF NOTES",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SkyPrimaryDark,
                                    letterSpacing = 1.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = notesText,
                            onValueChange = { notesText = it },
                            placeholder = { Text("E.g. Extra starch on collars, delicates in wash bag #4...") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            maxLines = 4,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SkyPrimary,
                                unfocusedBorderColor = CardBorder
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                LaundryRepository.updateAdminNotes(order.id, notesText)
                                Toast.makeText(context, "Staff notes saved!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.align(Alignment.End),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SkyLight,
                                contentColor = SkyPrimaryDark
                            )
                        ) {
                            Icon(imageVector = Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Save Notes", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
