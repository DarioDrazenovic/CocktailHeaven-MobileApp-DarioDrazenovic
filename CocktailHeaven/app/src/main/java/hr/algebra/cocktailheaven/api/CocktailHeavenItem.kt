package hr.algebra.cocktailheaven.api

import com.google.gson.annotations.SerializedName

data class CocktailHeavenItem(

    @SerializedName("id") val id : String,
    @SerializedName("name") val name : String,
    @SerializedName("brewery_type") val brewery_type : String,
    @SerializedName("city") val city : String,
    @SerializedName("state") val state : String,
    @SerializedName("postal_code") val postal_code : String,
    @SerializedName("country") val country : String,
    @SerializedName("updated_at") val updated_at : String,
    @SerializedName("created_at") val created_at : String
)
