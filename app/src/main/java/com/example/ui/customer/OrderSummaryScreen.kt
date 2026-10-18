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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LaundryRepository
import com.example.model.DeliverySpeed
import com.example.model.PaymentMethod
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CleanWhite
import com.example.ui.theme.CoralError
import com.example.ui.theme.MintSuccess
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
fun OrderSummaryScreen(
    onBack: () -> Unit,
    onEditSchedule: () -> Unit,
    onEditItems: () -> Unit,
    onOrderPlaced: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val catalog by LaundryRepository.catalog.collectAsState()
    val draftCart by LaundryRepository.draftCart.collectAsState()
    val serviceType by LaundryRepository.draftServiceType.collectAsState()
    val pickupAddress by LaundryRepository.draftPickupAddress.collectAsState()
    val pickupDate by LaundryRepository.draftPickupDate.collectAsState()
    val pickupTimeSlot by LaundryRepository.draftPickupTimeSlot.collectAsState()
    val deliverySpeed by LaundryRepository.draftDeliverySpeed.collectAsState()
    val instructions by LaundryRepository.draftInstructions.collectAsState()
    val selectedPayment by LaundryRepository.draftPaymentMethod.collectAsState()
    val promoDiscount by LaundryRepository.draftPromoDiscount.collectAsState()

    var promoInput by remember { mutableStateOf("") }
    var promoMessage by remember { mutableStateOf<String?>(null) }
    var isPlacingOrder by remember { mutableStateOf(false) }

    val catalogMap = remember(catalog) { catalog.associateBy { it.id } }
    val cartItems = remember(draftCart, catalogMap) {
        draftCart.mapNotNull { (id, qty) ->
            catalogMap[id]?.let { Pair(it, qty) }
        }
    }

    val subtotal = remember(cartItems) {
        cartItems.sumOf { (item, qty) -> item.currentPrice * qty }
    }
    val expressFee = if (deliverySpeed == DeliverySpeed.EXPRESS) 5.00 else 0.00
    val ecoFee = if (cartItems.isNotEmpty()) 1.50 else 0.00
    val tax = (subtotal + expressFee) * 0.08
    val total = maxOf(0.0, (subtotal + expressFee + ecoFee + tax) - promoDiscount)

    Scaffold(
        modifier = modifier.testTag("order_summary_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Order Summary",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Final Step: Review & Confirm",
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
        },
        bottomBar = {
            Surface(
                color = CleanWhite,
                shadowElevation = 12.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Total Amount",
                                fontSize = 12.sp,
                                color = TextMuted
                            )
                            Text(
                                text = String.format(Locale.US, "$%.2f", total),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = SkyPrimaryDark
                            )
                        }

                        Button(
                            onClick = {
                                if (!isPlacingOrder) {
                                    isPlacingOrder = true
                                    val newOrder = LaundryRepository.placeOrder()
                                    onOrderPlaced(newOrder.id)
                                }
                            },
                            enabled = cartItems.isNotEmpty() && !isPlacingOrder,
                            modifier = Modifier
                                .height(52.dp)
                                .testTag("place_order_btn"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SkyPrimary,
                                contentColor = CleanWhite
                            )
                        ) {
                            if (isPlacingOrder) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = CleanWhite,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    text = "Place Order",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
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
            // 1. Pickup & Delivery Summary Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CleanWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "PICKUP & DELIVERY",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = SkyPrimaryDark,
                                letterSpacing = 1.sp
                            )
                            Row(
                                modifier = Modifier.clickable { onEditSchedule() },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit schedule",
                                    tint = SkyPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Edit",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SkyPrimary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = SkyPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = pickupAddress,
                                fontSize = 13.sp,
                                color = TextPrimary,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = SkyPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "$pickupDate • $pickupTimeSlot",
                                fontSize = 13.sp,
                                color = TextPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = SkyLight,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Text(
                                text = "Service: ${serviceType.title} (${deliverySpeed.title})",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SkyPrimaryDark,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        if (instructions.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Notes: \"$instructions\"",
                                fontSize = 12.sp,
                                color = TextSecondary,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )
                        }
                    }
                }
            }

            // 2. Itemized Breakdown Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CleanWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "ITEMS BREAKDOWN (${cartItems.sumOf { it.second }})",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = SkyPrimaryDark,
                                letterSpacing = 1.sp
                            )
                            Row(
                                modifier = Modifier.clickable { onEditItems() },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit items",
                                    tint = SkyPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Edit Items",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SkyPrimary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        cartItems.forEach { (item, qty) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = SkyLight,
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = "${qty}x",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = SkyPrimaryDark
                                            )
                                        }
                                    }
                                    Text(
                                        text = item.name,
                                        fontSize = 14.sp,
                                        color = TextPrimary
                                    )
                                }

                                Text(
                                    text = String.format(Locale.US, "$%.2f", item.currentPrice * qty),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                }
            }

            // 3. Promo Code Input Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CleanWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "PROMO CODE",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SkyPrimaryDark,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = promoInput,
                                onValueChange = { promoInput = it },
                                placeholder = { Text("Try: FRESH20") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = SkyPrimary,
                                    unfocusedBorderColor = CardBorder
                                )
                            )

                            Button(
                                onClick = {
                                    val success = LaundryRepository.applyPromoCode(promoInput)
                                    promoMessage = if (success) "Promo applied! -$5.00 discount" else "Invalid code. Try 'FRESH20'"
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = SkyLight,
                                    contentColor = SkyPrimaryDark
                                )
                            ) {
                                Text("Apply", fontWeight = FontWeight.Bold)
                            }
                        }

                        if (promoMessage != null) {
                            Text(
                                text = promoMessage ?: "",
                                fontSize = 12.sp,
                                color = if (promoDiscount > 0) MintSuccess else CoralError,
                                modifier = Modifier.padding(top = 6.dp)
                            )
                        }
                    }
                }
            }

            // 4. Payment Method Selection
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CleanWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "PAYMENT METHOD",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SkyPrimaryDark,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        PaymentMethod.entries.forEach { method ->
                            val isSelected = selectedPayment == method
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(
                                        width = if (isSelected) 1.5.dp else 1.dp,
                                        color = if (isSelected) SkyPrimary else CardBorder,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .background(if (isSelected) SkyUltraLight else CleanWhite)
                                    .clickable { LaundryRepository.setDraftPaymentMethod(method) }
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = when (method) {
                                            PaymentMethod.CREDIT_CARD -> Icons.Default.CreditCard
                                            PaymentMethod.GOOGLE_PAY -> Icons.Default.PhoneAndroid
                                            PaymentMethod.CASH_ON_DELIVERY -> Icons.Default.LocalAtm
                                        },
                                        contentDescription = null,
                                        tint = if (isSelected) SkyPrimaryDark else TextSecondary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = method.title,
                                            fontSize = 13.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = method.detail,
                                            fontSize = 11.sp,
                                            color = TextSecondary
                                        )
                                    }
                                }

                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clip(CircleShape)
                                            .background(SkyPrimary),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = CleanWhite,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 5. Total Cost Calculation
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CleanWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "PRICE BREAKDOWN",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SkyPrimaryDark,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        SummaryFeeRow(label = "Garments Subtotal", amount = subtotal)
                        SummaryFeeRow(
                            label = "Pickup & Delivery",
                            amount = 0.0,
                            customDisplay = "FREE"
                        )
                        if (expressFee > 0) {
                            SummaryFeeRow(label = "Express 24h Turnaround", amount = expressFee)
                        }
                        SummaryFeeRow(label = "Eco Detergent & Sanitization", amount = ecoFee)
                        SummaryFeeRow(label = "Estimated Taxes (8%)", amount = tax)

                        if (promoDiscount > 0) {
                            SummaryFeeRow(
                                label = "Promo Discount",
                                amount = -promoDiscount,
                                isDiscount = true
                            )
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 6.dp),
                            color = CardBorder
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Estimated Total",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = String.format(Locale.US, "$%.2f", total),
                                fontSize = 20.sp,
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

@Composable
private fun SummaryFeeRow(
    label: String,
    amount: Double,
    customDisplay: String? = null,
    isDiscount: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = TextSecondary
        )
        Text(
            text = customDisplay ?: if (isDiscount) {
                String.format(Locale.US, "-$%.2f", kotlin.math.abs(amount))
            } else {
                String.format(Locale.US, "$%.2f", amount)
            },
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isDiscount || customDisplay == "FREE") MintSuccess else TextPrimary
        )
    }
}
