package com.example.ui.customer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.LaundryRepository
import com.example.model.OrderStatus
import com.example.model.ServiceType
import com.example.ui.components.DecorativeBubbleCanvas
import com.example.ui.components.ServiceTypeCard
import com.example.ui.components.StatusBadge
import com.example.ui.theme.BubbleCyan
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

@Composable
fun CustomerHomeScreen(
    onSchedulePickupClick: () -> Unit,
    onServiceSelected: (ServiceType) -> Unit,
    onTrackOrderClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val orders by LaundryRepository.orders.collectAsState()
    val activeOrderId by LaundryRepository.activeOrderId.collectAsState()
    val activeOrder = orders.firstOrNull { it.id == activeOrderId && it.status != OrderStatus.DELIVERED }
        ?: orders.firstOrNull { it.status != OrderStatus.DELIVERED }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SkyUltraLight)
            .testTag("customer_home_screen"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Top App Bar / Hero Section
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(SkyPrimary, SkyPrimaryDark)
                        )
                    )
            ) {
                DecorativeBubbleCanvas(
                    modifier = Modifier
                        .matchParentSize()
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = CleanWhite.copy(alpha = 0.2f),
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.WaterDrop,
                                        contentDescription = "FreshFold Logo",
                                        tint = CleanWhite,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "FreshFold",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = CleanWhite
                                )
                                Text(
                                    text = "Premium Laundry & Dry Clean",
                                    fontSize = 12.sp,
                                    color = SkyLight
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = CleanWhite.copy(alpha = 0.2f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Rating",
                                    tint = Color(0xFFFFD166),
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "4.92 ★",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CleanWhite
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Hero Card / Value Prop
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = CleanWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Column {
                            // Hero Image Banner
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(130.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_freshfold_hero),
                                    contentDescription = "Fresh Clean Laundry",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(
                                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.45f))
                                            )
                                        )
                                )
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MintSuccess,
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .padding(12.dp)
                                ) {
                                    Text(
                                        text = "FREE PICKUP & DELIVERY",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = CleanWhite,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Column(modifier = Modifier.padding(18.dp)) {
                                Text(
                                    text = "Laundry Day, Handled.",
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Scheduled door-to-door pickup, eco-wash, crisp fold & wrinkle-free return in 24 hours.",
                                    fontSize = 13.sp,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp),
                                    lineHeight = 18.sp
                                )

                                Button(
                                    onClick = onSchedulePickupClick,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp)
                                        .testTag("schedule_pickup_cta"),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = SkyPrimary,
                                        contentColor = CleanWhite
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Schedule a Pickup",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Active Order Status Banner (if active)
        if (activeOrder != null) {
            item {
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + slideInVertically()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 14.dp)
                    ) {
                        Text(
                            text = "CURRENT ORDER",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SkyPrimaryDark,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onTrackOrderClick(activeOrder.id) }
                                .border(1.5.dp, SkySoft, RoundedCornerShape(18.dp))
                                .testTag("active_order_banner"),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = CleanWhite),
                            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Surface(
                                            shape = CircleShape,
                                            color = SkyLight,
                                            modifier = Modifier.size(36.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = Icons.Default.LocalLaundryService,
                                                    contentDescription = null,
                                                    tint = SkyPrimary,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        }

                                        Column {
                                            Text(
                                                text = "Order #${activeOrder.id}",
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextPrimary
                                            )
                                            Text(
                                                text = "${activeOrder.serviceType.title} • ${activeOrder.totalItemCount} items",
                                                fontSize = 12.sp,
                                                color = TextSecondary
                                            )
                                        }
                                    }

                                    StatusBadge(status = activeOrder.status)
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Progress Indicator
                                val progress = (activeOrder.status.stepIndex + 1) / 6f
                                LinearProgressIndicator(
                                    progress = { progress },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = SkyPrimary,
                                    trackColor = SkyLight
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Schedule,
                                            contentDescription = null,
                                            tint = TextMuted,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "ETA: ${activeOrder.estimatedDelivery}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = TextSecondary
                                        )
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                                    ) {
                                        Text(
                                            text = "Track Live",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SkyPrimaryDark
                                        )
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                            contentDescription = null,
                                            tint = SkyPrimaryDark,
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

        // Service Type Selection Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "OUR SERVICES",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SkyPrimaryDark,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Tap to select",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 2x2 Grid of services
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ServiceTypeCard(
                            serviceType = ServiceType.WASH_AND_FOLD,
                            isSelected = false,
                            onClick = { onServiceSelected(ServiceType.WASH_AND_FOLD) },
                            modifier = Modifier.weight(1f)
                        )
                        ServiceTypeCard(
                            serviceType = ServiceType.DRY_CLEAN,
                            isSelected = false,
                            onClick = { onServiceSelected(ServiceType.DRY_CLEAN) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ServiceTypeCard(
                            serviceType = ServiceType.IRONING,
                            isSelected = false,
                            onClick = { onServiceSelected(ServiceType.IRONING) },
                            modifier = Modifier.weight(1f)
                        )
                        ServiceTypeCard(
                            serviceType = ServiceType.BULKY_CARE,
                            isSelected = false,
                            onClick = { onServiceSelected(ServiceType.BULKY_CARE) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // FreshFold Guarantees
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "THE FRESHFOLD STANDARD",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = SkyPrimaryDark,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CleanWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        GuaranteeRow(
                            icon = Icons.Default.Eco,
                            iconColor = MintSuccess,
                            iconBg = MintSuccessLight,
                            title = "Plant-Based Detergents",
                            subtitle = "Hypoallergenic, dye-free formulas gentle on fabrics and skin."
                        )
                        GuaranteeRow(
                            icon = Icons.Default.ElectricBolt,
                            iconColor = SkyPrimary,
                            iconBg = SkyLight,
                            title = "24-Hour Express Turnaround",
                            subtitle = "From pickup to fresh delivery in as fast as one business day."
                        )
                        GuaranteeRow(
                            icon = Icons.Default.Shield,
                            iconColor = Color(0xFF6366F1),
                            iconBg = Color(0xFFEEF2FF),
                            title = "Fabric Care Guarantee",
                            subtitle = "Every garment inspected, treated, and insured against damage."
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GuaranteeRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    iconBg: Color,
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = iconBg,
            modifier = Modifier.size(38.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Column {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = TextSecondary,
                lineHeight = 16.sp
            )
        }
    }
}
