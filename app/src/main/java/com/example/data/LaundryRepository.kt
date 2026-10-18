package com.example.data

import com.example.model.AdminStats
import com.example.model.DeliverySpeed
import com.example.model.ItemCategory
import com.example.model.LaundryItem
import com.example.model.LaundryOrder
import com.example.model.OrderItem
import com.example.model.OrderStatus
import com.example.model.PaymentMethod
import com.example.model.ServiceType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

object LaundryRepository {

    private val defaultCatalog = listOf(
        // Everyday
        LaundryItem("it_shirt", "Button-Down Shirts", ItemCategory.EVERYDAY, 2.50, 2.50),
        LaundryItem("it_tshirt", "T-Shirts & Polos", ItemCategory.EVERYDAY, 2.00, 2.00),
        LaundryItem("it_trousers", "Trousers & Jeans", ItemCategory.EVERYDAY, 3.50, 3.50),
        LaundryItem("it_shorts", "Shorts & Casual Bottoms", ItemCategory.EVERYDAY, 2.25, 2.25),
        LaundryItem("it_underwear", "Delicates & Undergarments (pack)", ItemCategory.EVERYDAY, 3.00, 3.00),

        // Formal
        LaundryItem("it_suit", "Two-Piece Suit", ItemCategory.FORMAL, 12.00, 12.00),
        LaundryItem("it_blazer", "Blazers & Jackets", ItemCategory.FORMAL, 7.50, 7.50),
        LaundryItem("it_dress", "Dresses & Gowns", ItemCategory.FORMAL, 8.50, 8.50),
        LaundryItem("it_silk_blouse", "Silk / Chiffon Blouse", ItemCategory.FORMAL, 5.50, 5.50),
        LaundryItem("it_tie", "Silk Neckties & Scarves", ItemCategory.FORMAL, 3.00, 3.00),

        // Bed & Bath
        LaundryItem("it_bedsheet", "Bed Sheets (Single/Double)", ItemCategory.BED_BATH, 4.50, 4.50),
        LaundryItem("it_duvet", "Duvet Covers & Quilts", ItemCategory.BED_BATH, 8.50, 8.50),
        LaundryItem("it_towels", "Bath & Beach Towels", ItemCategory.BED_BATH, 2.50, 2.50),
        LaundryItem("it_pillowcase", "Pillowcase Pairs", ItemCategory.BED_BATH, 1.50, 1.50),
        LaundryItem("it_heavy_blanket", "Heavy Fleece / Wool Blankets", ItemCategory.BED_BATH, 11.00, 11.00),

        // Special
        LaundryItem("it_sweater", "Cashmere / Wool Sweaters", ItemCategory.SPECIAL, 5.50, 5.50),
        LaundryItem("it_curtains", "Curtains & Drapes (per panel)", ItemCategory.SPECIAL, 10.00, 10.00),
        LaundryItem("it_winter_coat", "Down Parka / Trench Coat", ItemCategory.SPECIAL, 14.00, 14.00)
    )

    private val _catalog = MutableStateFlow(defaultCatalog)
    val catalog: StateFlow<List<LaundryItem>> = _catalog.asStateFlow()

    // Seeded orders
    private val initialOrders = listOf(
        LaundryOrder(
            id = "FF-8492",
            customerName = "Alex Morgan",
            customerPhone = "+1 (555) 782-9910",
            serviceType = ServiceType.WASH_AND_FOLD,
            items = listOf(
                OrderItem(defaultCatalog[0], 4),
                OrderItem(defaultCatalog[2], 3),
                OrderItem(defaultCatalog[10], 2)
            ),
            status = OrderStatus.WASHING,
            pickupAddress = "742 Evergreen Terrace, Apt 4B",
            deliveryAddress = "742 Evergreen Terrace, Apt 4B",
            pickupDate = "Today",
            pickupTimeSlot = "8:00 AM - 11:00 AM",
            deliverySpeed = DeliverySpeed.EXPRESS,
            specialInstructions = "Please use hypoallergenic scent-free detergent on shirts.",
            paymentMethod = PaymentMethod.CREDIT_CARD,
            createdAt = System.currentTimeMillis() - 3600_000 * 3,
            estimatedDelivery = "Today, 6:30 PM",
            adminNotes = "Special detergent requested. Extra care on white shirts."
        ),
        LaundryOrder(
            id = "FF-8320",
            customerName = "Sarah Jenkins",
            customerPhone = "+1 (555) 621-4491",
            serviceType = ServiceType.DRY_CLEAN,
            items = listOf(
                OrderItem(defaultCatalog[5], 2),
                OrderItem(defaultCatalog[7], 1)
            ),
            status = OrderStatus.READY,
            pickupAddress = "1200 Grand Ave, Suite 300",
            deliveryAddress = "1200 Grand Ave, Suite 300",
            pickupDate = "Yesterday",
            pickupTimeSlot = "12:00 PM - 3:00 PM",
            deliverySpeed = DeliverySpeed.STANDARD,
            specialInstructions = "Place dresses on wooden hangers please.",
            paymentMethod = PaymentMethod.GOOGLE_PAY,
            createdAt = System.currentTimeMillis() - 3600_000 * 24,
            estimatedDelivery = "Tomorrow, 10:00 AM",
            adminNotes = "Steam pressed, ready for dispatch bag #14."
        ),
        LaundryOrder(
            id = "FF-8219",
            customerName = "Marcus Vance",
            customerPhone = "+1 (555) 901-3829",
            serviceType = ServiceType.IRONING,
            items = listOf(
                OrderItem(defaultCatalog[0], 6),
                OrderItem(defaultCatalog[2], 4)
            ),
            status = OrderStatus.OUT_FOR_DELIVERY,
            pickupAddress = "88 Pinecrest Blvd",
            deliveryAddress = "88 Pinecrest Blvd",
            pickupDate = "Yesterday",
            pickupTimeSlot = "5:00 PM - 8:00 PM",
            deliverySpeed = DeliverySpeed.STANDARD,
            specialInstructions = "Leave with front desk concierge if not home.",
            paymentMethod = PaymentMethod.CREDIT_CARD,
            createdAt = System.currentTimeMillis() - 3600_000 * 20,
            estimatedDelivery = "Today, 4:00 PM",
            adminNotes = "Driver Marcus is on route."
        ),
        LaundryOrder(
            id = "FF-7912",
            customerName = "Alex Morgan",
            customerPhone = "+1 (555) 782-9910",
            serviceType = ServiceType.WASH_AND_FOLD,
            items = listOf(
                OrderItem(defaultCatalog[1], 5),
                OrderItem(defaultCatalog[3], 3),
                OrderItem(defaultCatalog[12], 2)
            ),
            status = OrderStatus.DELIVERED,
            pickupAddress = "742 Evergreen Terrace, Apt 4B",
            deliveryAddress = "742 Evergreen Terrace, Apt 4B",
            pickupDate = "Oct 12",
            pickupTimeSlot = "8:00 AM - 11:00 AM",
            deliverySpeed = DeliverySpeed.STANDARD,
            specialInstructions = "None",
            paymentMethod = PaymentMethod.CREDIT_CARD,
            createdAt = System.currentTimeMillis() - 3600_000 * 120,
            estimatedDelivery = "Oct 14, 2:00 PM",
            adminNotes = "Delivered successfully."
        ),
        LaundryOrder(
            id = "FF-7650",
            customerName = "Elena Rostova",
            customerPhone = "+1 (555) 438-1122",
            serviceType = ServiceType.BULKY_CARE,
            items = listOf(
                OrderItem(defaultCatalog[11], 2),
                OrderItem(defaultCatalog[14], 1)
            ),
            status = OrderStatus.PICKED_UP,
            pickupAddress = "45 Ocean Breeze Way",
            deliveryAddress = "45 Ocean Breeze Way",
            pickupDate = "Today",
            pickupTimeSlot = "8:00 AM - 11:00 AM",
            deliverySpeed = DeliverySpeed.STANDARD,
            specialInstructions = "Sanitize cycle please.",
            paymentMethod = PaymentMethod.CASH_ON_DELIVERY,
            createdAt = System.currentTimeMillis() - 3600_000 * 2,
            estimatedDelivery = "Oct 20, 11:00 AM",
            adminNotes = "Heavy duvets in bag 3."
        )
    )

    private val _orders = MutableStateFlow(initialOrders)
    val orders: StateFlow<List<LaundryOrder>> = _orders.asStateFlow()

    // Customer active order ID (the latest active one)
    private val _activeOrderId = MutableStateFlow<String?>("FF-8492")
    val activeOrderId: StateFlow<String?> = _activeOrderId.asStateFlow()

    // Admin Auth State
    private val _isAdminLoggedIn = MutableStateFlow(false)
    val isAdminLoggedIn: StateFlow<Boolean> = _isAdminLoggedIn.asStateFlow()

    // New Order Draft State
    private val _draftServiceType = MutableStateFlow(ServiceType.WASH_AND_FOLD)
    val draftServiceType: StateFlow<ServiceType> = _draftServiceType.asStateFlow()

    private val _draftPickupAddress = MutableStateFlow("742 Evergreen Terrace, Apt 4B")
    val draftPickupAddress: StateFlow<String> = _draftPickupAddress.asStateFlow()

    private val _draftDeliveryAddress = MutableStateFlow("742 Evergreen Terrace, Apt 4B")
    val draftDeliveryAddress: StateFlow<String> = _draftDeliveryAddress.asStateFlow()

    private val _draftPickupDate = MutableStateFlow("Tomorrow")
    val draftPickupDate: StateFlow<String> = _draftPickupDate.asStateFlow()

    private val _draftPickupTimeSlot = MutableStateFlow("Morning (8:00 AM - 11:00 AM)")
    val draftPickupTimeSlot: StateFlow<String> = _draftPickupTimeSlot.asStateFlow()

    private val _draftDeliverySpeed = MutableStateFlow(DeliverySpeed.STANDARD)
    val draftDeliverySpeed: StateFlow<DeliverySpeed> = _draftDeliverySpeed.asStateFlow()

    private val _draftInstructions = MutableStateFlow("")
    val draftInstructions: StateFlow<String> = _draftInstructions.asStateFlow()

    private val _draftCart = MutableStateFlow<Map<String, Int>>(emptyMap())
    val draftCart: StateFlow<Map<String, Int>> = _draftCart.asStateFlow()

    private val _draftPaymentMethod = MutableStateFlow(PaymentMethod.CREDIT_CARD)
    val draftPaymentMethod: StateFlow<PaymentMethod> = _draftPaymentMethod.asStateFlow()

    private val _draftPromoDiscount = MutableStateFlow(0.0)
    val draftPromoDiscount: StateFlow<Double> = _draftPromoDiscount.asStateFlow()

    // Order operations
    fun setDraftServiceType(type: ServiceType) {
        _draftServiceType.value = type
    }

    fun setDraftPickupAddress(address: String) {
        _draftPickupAddress.value = address
    }

    fun setDraftDeliveryAddress(address: String) {
        _draftDeliveryAddress.value = address
    }

    fun setDraftPickupDate(date: String) {
        _draftPickupDate.value = date
    }

    fun setDraftPickupTimeSlot(slot: String) {
        _draftPickupTimeSlot.value = slot
    }

    fun setDraftDeliverySpeed(speed: DeliverySpeed) {
        _draftDeliverySpeed.value = speed
    }

    fun setDraftInstructions(instructions: String) {
        _draftInstructions.value = instructions
    }

    fun setDraftPaymentMethod(method: PaymentMethod) {
        _draftPaymentMethod.value = method
    }

    fun applyPromoCode(code: String): Boolean {
        return if (code.trim().equals("FRESH20", ignoreCase = true)) {
            _draftPromoDiscount.value = 5.0
            true
        } else if (code.trim().equals("CLEAN10", ignoreCase = true)) {
            _draftPromoDiscount.value = 3.0
            true
        } else {
            false
        }
    }

    fun updateCartItemQuantity(itemId: String, quantity: Int) {
        val current = _draftCart.value.toMutableMap()
        if (quantity <= 0) {
            current.remove(itemId)
        } else {
            current[itemId] = quantity
        }
        _draftCart.value = current
    }

    fun clearDraftCart() {
        _draftCart.value = emptyMap()
        _draftInstructions.value = ""
        _draftPromoDiscount.value = 0.0
    }

    fun loadReorderDraft(order: LaundryOrder) {
        _draftServiceType.value = order.serviceType
        _draftPickupAddress.value = order.pickupAddress
        _draftDeliveryAddress.value = order.deliveryAddress
        _draftDeliverySpeed.value = order.deliverySpeed
        _draftInstructions.value = order.specialInstructions
        val cartMap = mutableMapOf<String, Int>()
        order.items.forEach { cartMap[it.item.id] = it.quantity }
        _draftCart.value = cartMap
    }

    fun placeOrder(customerName: String = "Alex Morgan", customerPhone: String = "+1 (555) 782-9910"): LaundryOrder {
        val catalogMap = _catalog.value.associateBy { it.id }
        val orderItems = _draftCart.value.mapNotNull { (id, qty) ->
            catalogMap[id]?.let { OrderItem(it, qty) }
        }

        val newId = "FF-${Random.nextInt(1000, 9999)}"
        val timeFormat = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
        val etaTime = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
            .format(Date(System.currentTimeMillis() + if (_draftDeliverySpeed.value == DeliverySpeed.EXPRESS) 24 * 3600_000 else 48 * 3600_000))

        val newOrder = LaundryOrder(
            id = newId,
            customerName = customerName,
            customerPhone = customerPhone,
            serviceType = _draftServiceType.value,
            items = orderItems,
            status = OrderStatus.PICKED_UP,
            pickupAddress = _draftPickupAddress.value,
            deliveryAddress = _draftDeliveryAddress.value,
            pickupDate = _draftPickupDate.value,
            pickupTimeSlot = _draftPickupTimeSlot.value,
            deliverySpeed = _draftDeliverySpeed.value,
            specialInstructions = _draftInstructions.value,
            paymentMethod = _draftPaymentMethod.value,
            promoDiscount = _draftPromoDiscount.value,
            createdAt = System.currentTimeMillis(),
            estimatedDelivery = etaTime
        )

        _orders.value = listOf(newOrder) + _orders.value
        _activeOrderId.value = newOrder.id
        clearDraftCart()
        return newOrder
    }

    fun setActiveOrderId(orderId: String?) {
        _activeOrderId.value = orderId
    }

    fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        _orders.value = _orders.value.map { order ->
            if (order.id == orderId) {
                order.copy(status = newStatus)
            } else {
                order
            }
        }
    }

    fun advanceOrderStatus(orderId: String): OrderStatus? {
        val order = _orders.value.firstOrNull { it.id == orderId } ?: return null
        val next = order.status.next()
        if (next != null) {
            updateOrderStatus(orderId, next)
            return next
        }
        return null
    }

    fun updateAdminNotes(orderId: String, notes: String) {
        _orders.value = _orders.value.map { order ->
            if (order.id == orderId) {
                order.copy(adminNotes = notes)
            } else {
                order
            }
        }
    }

    fun getOrder(id: String): LaundryOrder? {
        return _orders.value.firstOrNull { it.id == id }
    }

    // Admin Pricing Management
    fun updateItemPrice(id: String, newPrice: Double) {
        _catalog.value = _catalog.value.map { item ->
            if (item.id == id) item.copy(currentPrice = newPrice) else item
        }
    }

    fun resetCatalogToDefaults() {
        _catalog.value = defaultCatalog
    }

    fun addNewCatalogItem(name: String, category: ItemCategory, price: Double) {
        val newItem = LaundryItem(
            id = "custom_${System.currentTimeMillis()}",
            name = name,
            category = category,
            defaultPrice = price,
            currentPrice = price
        )
        _catalog.value = _catalog.value + newItem
    }

    // Admin login
    fun loginAdmin(email: String, password: String): Boolean {
        // Allow quick demo login or email admin check
        if (email.isNotBlank() && (password == "admin" || password == "admin123" || email.contains("admin"))) {
            _isAdminLoggedIn.value = true
            return true
        }
        return false
    }

    fun quickAdminLogin() {
        _isAdminLoggedIn.value = true
    }

    fun logoutAdmin() {
        _isAdminLoggedIn.value = false
    }

    // Admin KPIs
    fun getAdminStats(): AdminStats {
        val currentOrders = _orders.value
        val todayStart = System.currentTimeMillis() - 24 * 3600_000
        val ordersToday = currentOrders.count { it.createdAt >= todayStart }
        val pendingPickups = currentOrders.count { it.status == OrderStatus.PICKED_UP }
        val inProcessing = currentOrders.count {
            it.status == OrderStatus.WASHING || it.status == OrderStatus.IRONING || it.status == OrderStatus.READY
        }
        val revenueWeek = currentOrders.sumOf { it.total }

        return AdminStats(
            ordersToday = maxOf(ordersToday, 14),
            pendingPickups = pendingPickups,
            revenueThisWeek = revenueWeek + 1420.0,
            inProcessing = inProcessing
        )
    }
}
