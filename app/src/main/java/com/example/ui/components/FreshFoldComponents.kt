package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LaundryItem
import com.example.model.OrderStatus
import com.example.model.ServiceType
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.AmberWarningLight
import com.example.ui.theme.BubbleCyan
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CleanWhite
import com.example.ui.theme.MintSuccess
import com.example.ui.theme.MintSuccessLight
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.PurpleAccentLight
import com.example.ui.theme.SkyLight
import com.example.ui.theme.SkyPrimary
import com.example.ui.theme.SkyPrimaryDark
import com.example.ui.theme.SkySoft
import com.example.ui.theme.SkyUltraLight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

@Composable
fun DecorativeBubbleCanvas(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "bubble_motion")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 40f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "waveOffset"
    )

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        // Bubble 1
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(CleanWhite.copy(alpha = 0.55f), SkyPrimary.copy(alpha = 0.15f)),
                center = Offset(width * 0.15f, height * 0.35f + waveOffset * 0.3f),
                radius = 28.dp.toPx()
            ),
            radius = 28.dp.toPx(),
            center = Offset(width * 0.15f, height * 0.35f + waveOffset * 0.3f)
        )
        drawCircle(
            color = CleanWhite.copy(alpha = 0.6f),
            radius = 28.dp.toPx(),
            center = Offset(width * 0.15f, height * 0.35f + waveOffset * 0.3f),
            style = Stroke(width = 1.5.dp.toPx())
        )

        // Bubble 2
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(BubbleCyan.copy(alpha = 0.45f), SkyPrimary.copy(alpha = 0.12f)),
                center = Offset(width * 0.85f, height * 0.25f - waveOffset * 0.25f),
                radius = 36.dp.toPx()
            ),
            radius = 36.dp.toPx(),
            center = Offset(width * 0.85f, height * 0.25f - waveOffset * 0.25f)
        )
        drawCircle(
            color = CleanWhite.copy(alpha = 0.5f),
            radius = 36.dp.toPx(),
            center = Offset(width * 0.85f, height * 0.25f - waveOffset * 0.25f),
            style = Stroke(width = 1.2.dp.toPx())
        )

        // Bubble 3 small
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(CleanWhite.copy(alpha = 0.6f), Color.Transparent),
                center = Offset(width * 0.72f, height * 0.65f + waveOffset * 0.4f),
                radius = 16.dp.toPx()
            ),
            radius = 16.dp.toPx(),
            center = Offset(width * 0.72f, height * 0.65f + waveOffset * 0.4f)
        )

        // Wave path at the bottom
        val wavePath = Path().apply {
            moveTo(0f, height * 0.88f)
            cubicTo(
                width * 0.25f, height * (0.84f + (waveOffset * 0.001f)),
                width * 0.65f, height * (0.92f - (waveOffset * 0.001f)),
                width, height * 0.86f
            )
            lineTo(width, height)
            lineTo(0f, height)
            close()
        }
        drawPath(
            path = wavePath,
            brush = Brush.verticalGradient(
                colors = listOf(CleanWhite.copy(alpha = 0.25f), CleanWhite.copy(alpha = 0.05f))
            )
        )
    }
}

@Composable
fun StatusBadge(status: OrderStatus, modifier: Modifier = Modifier) {
    val (bg, text, borderCol) = when (status) {
        OrderStatus.PICKED_UP -> Triple(SkyLight, SkyPrimaryDark, SkySoft)
        OrderStatus.WASHING -> Triple(BubbleCyan.copy(alpha = 0.15f), SkyPrimaryDark, BubbleCyan.copy(alpha = 0.4f))
        OrderStatus.IRONING -> Triple(PurpleAccentLight, PurpleAccent, PurpleAccent.copy(alpha = 0.3f))
        OrderStatus.READY -> Triple(AmberWarningLight, AmberWarning, AmberWarning.copy(alpha = 0.4f))
        OrderStatus.OUT_FOR_DELIVERY -> Triple(SkyLight, SkyPrimaryDark, SkyPrimary)
        OrderStatus.DELIVERED -> Triple(MintSuccessLight, MintSuccess, MintSuccess.copy(alpha = 0.3f))
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = bg,
        border = androidx.compose.foundation.BorderStroke(1.dp, borderCol)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(text)
            )
            Text(
                text = status.title,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = text
            )
        }
    }
}

@Composable
fun StatusStepper(
    currentStatus: OrderStatus,
    modifier: Modifier = Modifier
) {
    val statuses = OrderStatus.entries

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        statuses.forEachIndexed { index, status ->
            val isCompleted = status.stepIndex < currentStatus.stepIndex
            val isCurrent = status == currentStatus
            val isPending = status.stepIndex > currentStatus.stepIndex

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Stepper column with icon and connecting line
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(36.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isCompleted -> MintSuccess
                                    isCurrent -> SkyPrimary
                                    else -> CardBorder
                                }
                            )
                            .border(
                                width = if (isCurrent) 3.dp else 1.dp,
                                color = if (isCurrent) SkyLight else Color.Transparent,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Completed",
                                tint = CleanWhite,
                                modifier = Modifier.size(16.dp)
                            )
                        } else if (isCurrent) {
                            Icon(
                                imageVector = Icons.Default.WaterDrop,
                                contentDescription = "Active",
                                tint = CleanWhite,
                                modifier = Modifier.size(16.dp)
                            )
                        } else {
                            Text(
                                text = "${index + 1}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMuted
                            )
                        }
                    }

                    // Connecting vertical line if not last
                    if (index < statuses.lastIndex) {
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(38.dp)
                                .background(
                                    if (isCompleted) MintSuccess else CardBorder
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Status details
                Column(
                    modifier = Modifier
                        .padding(bottom = if (index < statuses.lastIndex) 16.dp else 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = status.title,
                            fontSize = 15.sp,
                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.SemiBold,
                            color = when {
                                isCurrent -> SkyPrimaryDark
                                isCompleted -> TextPrimary
                                else -> TextMuted
                            }
                        )

                        if (isCurrent) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = SkyLight
                            ) {
                                Text(
                                    text = "In Progress",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SkyPrimaryDark,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Text(
                        text = status.description,
                        fontSize = 12.sp,
                        color = if (isCurrent) TextSecondary else TextMuted,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ServiceTypeCard(
    serviceType: ServiceType,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { onClick() }
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) SkyPrimary else CardBorder,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) SkyUltraLight else CleanWhite
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) SkyPrimary else SkyLight,
                    modifier = Modifier.size(42.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.LocalLaundryService,
                            contentDescription = serviceType.title,
                            tint = if (isSelected) CleanWhite else SkyPrimaryDark,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSelected) SkyPrimaryDark.copy(alpha = 0.1f) else CardBorder.copy(alpha = 0.4f)
                ) {
                    Text(
                        text = "${serviceType.turnaroundHours}h turn",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isSelected) SkyPrimaryDark else TextSecondary,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            Text(
                text = serviceType.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Text(
                text = serviceType.subtitle,
                fontSize = 12.sp,
                color = TextSecondary,
                lineHeight = 16.sp,
                maxLines = 2
            )
        }
    }
}

@Composable
fun ItemQuantityStepper(
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(SkyLight, RoundedCornerShape(24.dp))
            .padding(horizontal = 4.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        IconButton(
            onClick = { if (quantity > 0) onQuantityChange(quantity - 1) },
            modifier = Modifier.size(32.dp),
            enabled = quantity > 0
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = "Decrease quantity",
                tint = if (quantity > 0) SkyPrimaryDark else TextMuted,
                modifier = Modifier.size(16.dp)
            )
        }

        Text(
            text = "$quantity",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = if (quantity > 0) SkyPrimaryDark else TextSecondary,
            modifier = Modifier.width(20.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        IconButton(
            onClick = { onQuantityChange(quantity + 1) },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Increase quantity",
                tint = SkyPrimaryDark,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun LaundryItemCard(
    item: LaundryItem,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = if (quantity > 0) SkyPrimary.copy(alpha = 0.5f) else CardBorder,
                shape = RoundedCornerShape(14.dp)
            ),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (quantity > 0) SkyUltraLight else CleanWhite
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Text(
                        text = String.format(Locale.US, "$%.2f", item.currentPrice),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = SkyPrimaryDark
                    )
                    Text(
                        text = item.unit,
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }
            }

            ItemQuantityStepper(
                quantity = quantity,
                onQuantityChange = onQuantityChange
            )
        }
    }
}
