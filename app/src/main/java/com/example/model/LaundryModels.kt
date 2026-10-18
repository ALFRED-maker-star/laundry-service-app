package com.example.model

enum class ServiceType(
    val title: String,
    val subtitle: String,
    val turnaroundHours: Int
) {
    WASH_AND_FOLD("Wash & Fold", "Daily laundry, tumble dried & crisp folded", 24),
    DRY_CLEAN("Dry Clean", "Gentle solvent care for suits, coats & silks", 48),
    IRONING("Steam Ironing", "Wrinkle-free crisp press on wooden hangers", 24),
    BULKY_CARE("Duvets & Bedding", "Sanitizing wash for comforters & blankets", 48)
}

enum class ItemCategory(val label: String) {
    EVERYDAY("Tops & Bottoms"),
    FORMAL("Formal & Dresses"),
    BED_BATH("Bed & Bath"),
    SPECIAL("Delicates & Specialty")
}

data class LaundryItem(
    val id: String,
    val name: String,
    val category: ItemCategory,
    val defaultPrice: Double,
    val currentPrice: Double,
    val unit: String = "per item"
)

data class OrderItem(
    val item: LaundryItem,
    val quantity: Int
) {
    val subtotal: Double get() = item.currentPrice * quantity
}

enum class OrderStatus(
    val title: String,
    val description: String,
    val stepIndex: Int
) {
    PICKED_UP("Picked Up", "Driver picked up your laundry bag", 0),
    WASHING("Washing", "In cycle with eco-friendly detergent", 1),
    IRONING("Ironing", "Steam pressed & wrinkle treatment", 2),
    READY("Ready", "Quality checked, folded & packaged", 3),
    OUT_FOR_DELIVERY("Out for Delivery", "Valet is en route to your address", 4),
    DELIVERED("Delivered", "Delivered fresh to your doorstep", 5);

    fun next(): OrderStatus? {
        val nextIndex = this.stepIndex + 1
        return entries.firstOrNull { it.stepIndex == nextIndex }
    }
}

enum class DeliverySpeed(val title: String, val fee: Double) {
    STANDARD("Standard (48 hrs)", 0.0),
    EXPRESS("Express (24 hrs)", 5.0)
}

enum class PaymentMethod(val title: String, val detail: String) {
    CREDIT_CARD("Credit Card", "Visa ending in •••• 4242"),
    GOOGLE_PAY("Google Pay", "Fast, secure mobile checkout"),
    CASH_ON_DELIVERY("Cash on Pickup", "Pay cash or card to driver")
}

data class LaundryOrder(
    val id: String,
    val customerName: String,
    val customerPhone: String,
    val serviceType: ServiceType,
    val items: List<OrderItem>,
    val status: OrderStatus,
    val pickupAddress: String,
    val deliveryAddress: String,
    val pickupDate: String,
    val pickupTimeSlot: String,
    val deliverySpeed: DeliverySpeed = DeliverySpeed.STANDARD,
    val specialInstructions: String = "",
    val paymentMethod: PaymentMethod = PaymentMethod.CREDIT_CARD,
    val promoDiscount: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis(),
    val estimatedDelivery: String = "Tomorrow, 6:00 PM",
    val driverName: String = "Marcus Davies",
    val driverRating: Double = 4.92,
    val driverPhone: String = "+1 (555) 349-2104",
    val adminNotes: String = ""
) {
    val subtotal: Double get() = items.sumOf { it.subtotal }
    val expressFee: Double get() = deliverySpeed.fee
    val ecoFee: Double get() = if (items.isNotEmpty()) 1.50 else 0.0
    val tax: Double get() = ((subtotal + expressFee) * 0.08)
    val total: Double get() = maxOf(0.0, (subtotal + expressFee + ecoFee + tax) - promoDiscount)
    val totalItemCount: Int get() = items.sumOf { it.quantity }
}

data class AdminStats(
    val ordersToday: Int,
    val pendingPickups: Int,
    val revenueThisWeek: Double,
    val inProcessing: Int
)
