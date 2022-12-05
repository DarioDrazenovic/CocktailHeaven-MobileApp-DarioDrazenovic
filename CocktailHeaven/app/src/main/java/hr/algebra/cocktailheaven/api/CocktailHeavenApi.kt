package hr.algebra.cocktailheaven.api

import retrofit2.Call
import retrofit2.http.GET
import java.util.*

const val API_URL = "https://api.openbrewerydb.org/"
interface CocktailHeavenApi {
    @GET("breweries")
    fun fetchItems() : Call<List<CocktailHeavenItem>>
}