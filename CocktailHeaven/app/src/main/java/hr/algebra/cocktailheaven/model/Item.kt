package hr.algebra.cocktailheaven.model

data class Item(
    var _id: Long?,
    val id: String,
    val name: String,
    val brewery_type: String,
    val city: String,
    val state: String,
    val postal_code : String,
    val country : String,
    val updated_at : String,
    val created_at : String,
    var read: Boolean
)
