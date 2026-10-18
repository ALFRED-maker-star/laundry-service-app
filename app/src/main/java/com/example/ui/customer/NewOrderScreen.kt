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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LaundryRepository
import com.example.model.DeliverySpeed
import com.example.model.ServiceType
import com.example.ui.components.ServiceTypeCard
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CleanWhite
import com.example.ui.theme.MintSuccess
import com.example.ui.theme.SkyLight
import com.example.ui.theme.SkyPrimary
import com.example.ui.theme.SkyPrimaryDark
import com.example.ui.theme.SkySoft
import com.example.ui.theme.SkyUltraLight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewOrderScreen(
    onBack: () -> Unit,
    onContinueToItems: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedService by LaundryRepository.draftServiceType.collectAsState()
    val pickupAddress by LaundryRepository.draftPickupAddress.collectAsState()
    val pickupDate by LaundryRepository.draftPickupDate.collectAsState()
    val pickupTimeSlot by LaundryRepository.draftPickupTimeSlot.collectAsState()
    val deliverySpeed by LaundryRepository.draftDeliverySpeed.collectAsState()
    val instructions by LaundryRepository.draftInstructions.collectAsState()

    var isCustomAddressOpen by remember { mutableStateOf(false) }

    val savedAddresses = listOf(
        Pair("Home", "742 Evergreen Terrace, Apt 4B"),
        Pair("Work", "1200 Grand Ave, Suite 300"),
        Pair("Studio", "88 Pinecrest Blvd, Loft 9")
    )

    val dates = listOf("Today", "Tomorrow", "Wed, Oct 21", "Thu, Oct 22")
    val timeSlots = listOf(
        "Morning (8:00 AM - 11:00 AM)",
        "Afternoon (12:00 PM - 3:00 PM)",
        "Evening (5:00 PM - 8:00 PM)"
    )

    Scaffold(
        modifier = modifier.testTag("new_order_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "New Order",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Step 1 of 3: Pickup Details",
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
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    Button(
                        onClick = onContinueToItems,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("continue_to_items_btn"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SkyPrimary,
                            contentColor = CleanWhite
                        )
                    ) {
                        Text(
                            text = "Select Items & Garments",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
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
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // 1. Service Type Selection
            item {
                Column {
                    Text(
                        text = "1. SELECT SERVICE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SkyPrimaryDark,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ServiceTypeCard(
                                serviceType = ServiceType.WASH_AND_FOLD,
                                isSelected = selectedService == ServiceType.WASH_AND_FOLD,
                                onClick = { LaundryRepository.setDraftServiceType(ServiceType.WASH_AND_FOLD) },
                                modifier = Modifier.weight(1f)
                            )
                            ServiceTypeCard(
                                serviceType = ServiceType.DRY_CLEAN,
                                isSelected = selectedService == ServiceType.DRY_CLEAN,
                                onClick = { LaundryRepository.setDraftServiceType(ServiceType.DRY_CLEAN) },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ServiceTypeCard(
                                serviceType = ServiceType.IRONING,
                                isSelected = selectedService == ServiceType.IRONING,
                                onClick = { LaundryRepository.setDraftServiceType(ServiceType.IRONING) },
                                modifier = Modifier.weight(1f)
                            )
                            ServiceTypeCard(
                                serviceType = ServiceType.BULKY_CARE,
                                isSelected = selectedService == ServiceType.BULKY_CARE,
                                onClick = { LaundryRepository.setDraftServiceType(ServiceType.BULKY_CARE) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // 2. Pickup Address
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
                                text = "2. PICKUP & DELIVERY ADDRESS",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = SkyPrimaryDark,
                                letterSpacing = 1.sp
                            )

                            Text(
                                text = if (isCustomAddressOpen) "Hide" else "Custom",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SkyPrimary,
                                modifier = Modifier.clickable { isCustomAddressOpen = !isCustomAddressOpen }
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        savedAddresses.forEach { (label, addr) ->
                            val isSelected = pickupAddress == addr
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) SkyLight else Color.Transparent)
                                    .clickable {
                                        LaundryRepository.setDraftPickupAddress(addr)
                                        LaundryRepository.setDraftDeliveryAddress(addr)
                                    }
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (label == "Home") Icons.Default.Home else if (label == "Work") Icons.Default.Work else Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = if (isSelected) SkyPrimaryDark else TextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = label,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = addr,
                                        fontSize = 12.sp,
                                        color = TextSecondary
                                    )
                                }
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = SkyPrimaryDark,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        if (isCustomAddressOpen) {
                            Spacer(modifier = Modifier.height(10.dp))
                            OutlinedTextField(
                                value = pickupAddress,
                                onValueChange = {
                                    LaundryRepository.setDraftPickupAddress(it)
                                    LaundryRepository.setDraftDeliveryAddress(it)
                                },
                                label = { Text("Enter Pickup Address") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = SkyPrimary,
                                    unfocusedBorderColor = CardBorder
                                )
                            )
                        }
                    }
                }
            }

            // 3. Pickup Date & Time Slot
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CleanWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "3. PICKUP SCHEDULE",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SkyPrimaryDark,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Text(
                            text = "Select Date",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(dates) { d ->
                                val isSelected = pickupDate == d
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) SkyPrimary else SkyLight,
                                    modifier = Modifier.clickable { LaundryRepository.setDraftPickupDate(d) }
                                ) {
                                    Text(
                                        text = d,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isSelected) CleanWhite else SkyPrimaryDark,
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Pickup Time Window",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            timeSlots.forEach { slot ->
                                val isSelected = pickupTimeSlot == slot
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .border(
                                            width = if (isSelected) 1.5.dp else 1.dp,
                                            color = if (isSelected) SkyPrimary else CardBorder,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .background(if (isSelected) SkyUltraLight else CleanWhite)
                                        .clickable { LaundryRepository.setDraftPickupTimeSlot(slot) }
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Schedule,
                                            contentDescription = null,
                                            tint = if (isSelected) SkyPrimaryDark else TextSecondary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = slot,
                                            fontSize = 13.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = TextPrimary
                                        )
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
            }

            // 4. Delivery Speed
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CleanWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "4. TURNAROUND SPEED",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SkyPrimaryDark,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Standard
                            val isStdSelected = deliverySpeed == DeliverySpeed.STANDARD
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { LaundryRepository.setDraftDeliverySpeed(DeliverySpeed.STANDARD) }
                                    .border(
                                        width = if (isStdSelected) 2.dp else 1.dp,
                                        color = if (isStdSelected) SkyPrimary else CardBorder,
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isStdSelected) SkyUltraLight else CleanWhite
                                )
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "Standard (48h)",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "FREE",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MintSuccess,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }
                            }

                            // Express
                            val isExpSelected = deliverySpeed == DeliverySpeed.EXPRESS
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { LaundryRepository.setDraftDeliverySpeed(DeliverySpeed.EXPRESS) }
                                    .border(
                                        width = if (isExpSelected) 2.dp else 1.dp,
                                        color = if (isExpSelected) SkyPrimary else CardBorder,
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isExpSelected) SkyUltraLight else CleanWhite
                                )
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.ElectricBolt,
                                            contentDescription = null,
                                            tint = SkyPrimaryDark,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Express (24h)",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                    }
                                    Text(
                                        text = "+$5.00",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SkyPrimaryDark,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 5. Special Instructions
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CleanWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "5. SPECIAL LAUNDRY INSTRUCTIONS (OPTIONAL)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SkyPrimaryDark,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        OutlinedTextField(
                            value = instructions,
                            onValueChange = { LaundryRepository.setDraftInstructions(it) },
                            placeholder = { Text("E.g. Hypoallergenic wash, light starch on shirts, gate code #401...") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            maxLines = 4,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SkyPrimary,
                                unfocusedBorderColor = CardBorder
                            )
                        )
                    }
                }
            }
        }
    }
}
