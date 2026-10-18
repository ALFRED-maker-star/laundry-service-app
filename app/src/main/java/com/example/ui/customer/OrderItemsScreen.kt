package com.example.ui.customer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.model.ItemCategory
import com.example.ui.components.LaundryItemCard
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CleanWhite
import com.example.ui.theme.CoralError
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
fun OrderItemsScreen(
    onBack: () -> Unit,
    onContinueToSummary: () -> Unit,
    modifier: Modifier = Modifier
) {
    val catalog by LaundryRepository.catalog.collectAsState()
    val draftCart by LaundryRepository.draftCart.collectAsState()
    val serviceType by LaundryRepository.draftServiceType.collectAsState()

    var selectedCategory by remember { mutableStateOf<ItemCategory?>(null) }

    val filteredItems = remember(catalog, selectedCategory) {
        if (selectedCategory == null) catalog else catalog.filter { it.category == selectedCategory }
    }

    val totalItemCount = remember(draftCart) {
        draftCart.values.sum()
    }

    val runningSubtotal = remember(draftCart, catalog) {
        val catMap = catalog.associateBy { it.id }
        draftCart.entries.sumOf { (id, qty) ->
            (catMap[id]?.currentPrice ?: 0.0) * qty
        }
    }

    Scaffold(
        modifier = modifier.testTag("order_items_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Add Clothing Items",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "${serviceType.title} • Step 2 of 3",
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
                    if (totalItemCount > 0) {
                        IconButton(onClick = { LaundryRepository.clearDraftCart() }) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Clear items",
                                tint = CoralError
                            )
                        }
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
                                text = "Running Subtotal",
                                fontSize = 12.sp,
                                color = TextMuted
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = String.format(Locale.US, "$%.2f", runningSubtotal),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = SkyPrimaryDark
                                )
                                Text(
                                    text = "($totalItemCount items)",
                                    fontSize = 13.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Button(
                            onClick = onContinueToSummary,
                            enabled = totalItemCount > 0,
                            modifier = Modifier
                                .height(48.dp)
                                .testTag("continue_to_summary_btn"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SkyPrimary,
                                contentColor = CleanWhite,
                                disabledContainerColor = CardBorder,
                                disabledContentColor = TextMuted
                            )
                        ) {
                            Text(
                                text = "Review Order",
                                fontSize = 15.sp,
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
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(SkyUltraLight)
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Category filter chips
            item {
                Column(modifier = Modifier.padding(bottom = 6.dp)) {
                    Text(
                        text = "FILTER BY CATEGORY",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SkyPrimaryDark,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            val isSelected = selectedCategory == null
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) SkyPrimary else CleanWhite,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) SkyPrimary else CardBorder
                                ),
                                modifier = Modifier.clickable { selectedCategory = null }
                            ) {
                                Text(
                                    text = "All Items",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isSelected) CleanWhite else TextPrimary,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                                )
                            }
                        }

                        items(ItemCategory.entries.toTypedArray()) { category ->
                            val isSelected = selectedCategory == category
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) SkyPrimary else CleanWhite,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) SkyPrimary else CardBorder
                                ),
                                modifier = Modifier.clickable { selectedCategory = category }
                            ) {
                                Text(
                                    text = category.label,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isSelected) CleanWhite else TextPrimary,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Quick reminder info pill
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SkyLight,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = CleanWhite,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingBag,
                                    contentDescription = null,
                                    tint = SkyPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Add approximate quantities. Our team re-verifies and tallies upon valet pickup.",
                            fontSize = 12.sp,
                            color = SkyPrimaryDark,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            // Garment items list
            items(filteredItems) { item ->
                val quantity = draftCart[item.id] ?: 0
                LaundryItemCard(
                    item = item,
                    quantity = quantity,
                    onQuantityChange = { newQty ->
                        LaundryRepository.updateCartItemQuantity(item.id, newQty)
                    }
                )
            }
        }
    }
}
