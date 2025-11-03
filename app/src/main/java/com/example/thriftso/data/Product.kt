data class Product(
    val id: String = System.currentTimeMillis().toString(),
    val name: String,
    val price: Double,
    val originalPrice: Double? = null,
    val description: String,
    val imageUrl: String, // Ganti dari imageRes ke imageUrl
    val category: String = "Fashion",
    val isFeatured: Boolean = false,
    val rating: Float = 0f,
    val reviewCount: Int = 0,
    val size: List<String> = listOf("S", "M", "L", "XL"),
    val condition: String = "Good",
    val seller: String = "Retro Thrift Store"
)

data class CartItem(
    val product: Product,
    var quantity: Int = 1,
    var selectedSize: String = "M"
) {
    fun getTotalPrice(): Double = product.price * quantity

    fun hasDiscount(): Boolean = product.originalPrice != null
    fun getDiscountPercentage(): Int {
        return if (hasDiscount()) {
            (((product.originalPrice!! - product.price) / product.originalPrice) * 100).toInt()
        } else 0
    }
}