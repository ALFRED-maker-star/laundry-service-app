package com.example.ui.admin

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LaundryRepository
import com.example.model.ItemCategory
import com.example.model.LaundryItem
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
fun AdminPricingScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val catalog by LaundryRepository.catalog.collectAsState()
    var selectedCategory by remember { mutableStateOf<ItemCategory?>(null) }

    // Dialog state for editing price
    var editingItem by remember { mutableStateOf<LaundryItem?>(null) }
    var editPriceText by remember { mutableStateOf("") }

    // Dialog state for adding new item
    var isAddDialogOpen by remember { mutableStateOf(false) }
    var newItemName by remember { mutableStateOf("") }
    var newItemPriceText by remember { mutableStateOf("") }
    var newItemCategory by remember { mutableStateOf(ItemCategory.EVERYDAY) }
    var isCategoryDropdownOpen by remember { mutableStateOf(false) }

    val filteredItems = remember(catalog, selectedCategory) {
        if (selectedCategory == null) catalog else catalog.filter { it.category == selectedCategory }
    }

    Scaffold(
        modifier = modifier.testTag("admin_pricing_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Pricing Setup",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Item rates & service catalog management",
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
                    IconButton(onClick = {
                        LaundryRepository.resetCatalogToDefaults()
                        Toast.makeText(context, "Reset all rates to factory defaults", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(
                            imageVector = Icons.Default.RestartAlt,
                            contentDescription = "Reset rates to default",
                            tint = TextSecondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CleanWhite)
            )
        },
        floatingActionButton = {
            Button(
                onClick = { isAddDialogOpen = true },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SkyPrimary,
                    contentColor = CleanWhite
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Add Item Rate", fontWeight = FontWeight.Bold)
            }
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
            // Category filter chips
            item {
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
                                text = "All Items (${catalog.size})",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isSelected) CleanWhite else TextPrimary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }

                    items(ItemCategory.entries.toTypedArray()) { category ->
                        val isSelected = selectedCategory == category
                        val count = catalog.count { it.category == category }
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
                                text = "${category.label} ($count)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isSelected) CleanWhite else TextPrimary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            // Info hint
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SkyLight,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Any price changes made here instantly apply to customer new order calculation.",
                        fontSize = 12.sp,
                        color = SkyPrimaryDark,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            // Items list
            items(filteredItems) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CardBorder, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CleanWhite),
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
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = SkyLight
                                ) {
                                    Text(
                                        text = item.category.label,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SkyPrimaryDark,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }

                                if (item.currentPrice != item.defaultPrice) {
                                    Text(
                                        text = String.format(Locale.US, "Default: $%.2f", item.defaultPrice),
                                        fontSize = 11.sp,
                                        color = TextMuted,
                                        textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                                    )
                                }
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = String.format(Locale.US, "$%.2f", item.currentPrice),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = SkyPrimaryDark
                            )

                            Button(
                                onClick = {
                                    editingItem = item
                                    editPriceText = String.format(Locale.US, "%.2f", item.currentPrice)
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = SkyLight,
                                    contentColor = SkyPrimaryDark
                                ),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Edit", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }

    // Edit Item Price Dialog
    editingItem?.let { targetItem ->
        AlertDialog(
            onDismissRequest = { editingItem = null },
            title = { Text("Edit Price for ${targetItem.name}") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Current price: ${String.format(Locale.US, "$%.2f", targetItem.currentPrice)}",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                    OutlinedTextField(
                        value = editPriceText,
                        onValueChange = { editPriceText = it },
                        label = { Text("New Price ($)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val parsed = editPriceText.toDoubleOrNull()
                        if (parsed != null && parsed >= 0) {
                            LaundryRepository.updateItemPrice(targetItem.id, parsed)
                            Toast.makeText(context, "${targetItem.name} price updated to $${String.format(Locale.US, "%.2f", parsed)}", Toast.LENGTH_SHORT).show()
                            editingItem = null
                        } else {
                            Toast.makeText(context, "Enter a valid positive price", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SkyPrimary)
                ) {
                    Text("Save Price")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingItem = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Add New Item Dialog
    if (isAddDialogOpen) {
        AlertDialog(
            onDismissRequest = { isAddDialogOpen = false },
            title = { Text("Add New Laundry Item") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = newItemName,
                        onValueChange = { newItemName = it },
                        label = { Text("Garment / Item Name") },
                        placeholder = { Text("E.g. Wool Scarf, Tablecloth") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = newItemPriceText,
                        onValueChange = { newItemPriceText = it },
                        label = { Text("Unit Price ($)") },
                        placeholder = { Text("E.g. 4.50") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    ExposedDropdownMenuBox(
                        expanded = isCategoryDropdownOpen,
                        onExpandedChange = { isCategoryDropdownOpen = !isCategoryDropdownOpen }
                    ) {
                        OutlinedTextField(
                            value = newItemCategory.label,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Category") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryDropdownOpen) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = isCategoryDropdownOpen,
                            onDismissRequest = { isCategoryDropdownOpen = false }
                        ) {
                            ItemCategory.entries.forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat.label) },
                                    onClick = {
                                        newItemCategory = cat
                                        isCategoryDropdownOpen = false
                                    }
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val parsed = newItemPriceText.toDoubleOrNull()
                        if (newItemName.isNotBlank() && parsed != null && parsed >= 0) {
                            LaundryRepository.addNewCatalogItem(newItemName, newItemCategory, parsed)
                            Toast.makeText(context, "$newItemName added at $${String.format(Locale.US, "%.2f", parsed)}", Toast.LENGTH_SHORT).show()
                            isAddDialogOpen = false
                            newItemName = ""
                            newItemPriceText = ""
                        } else {
                            Toast.makeText(context, "Please enter valid item name and price", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SkyPrimary)
                ) {
                    Text("Add Item")
                }
            },
            dismissButton = {
                TextButton(onClick = { isAddDialogOpen = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
