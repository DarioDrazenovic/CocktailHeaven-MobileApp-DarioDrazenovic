package hr.algebra.cocktailheaven.api

import android.content.ContentValues
import android.content.Context
import android.util.Log
import hr.algebra.cocktailheaven.COCKTAIL_HEAVEN_PROVIDER_URI
import hr.algebra.cocktailheaven.CocktailHeavenReceiver
import hr.algebra.cocktailheaven.DATA_IMPORTED
import hr.algebra.cocktailheaven.framework.sendBroadcast
import hr.algebra.cocktailheaven.framework.setBooleanPreference
import hr.algebra.cocktailheaven.model.Item
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CocktailHeavenFetcher(private val context: Context) {

    private var cocktailHeavenApi: CocktailHeavenApi
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        cocktailHeavenApi = retrofit.create(CocktailHeavenApi::class.java)
    }

    fun fetchItems(){
        val request = cocktailHeavenApi.fetchItems()

        request.enqueue(object: Callback<List<CocktailHeavenItem>> {
            override fun onResponse(
                call: Call<List<CocktailHeavenItem>>,
                response: Response<List<CocktailHeavenItem>>
            ) {
                response.body()?.let{
                    populateItems(it)
                }
            }

            override fun onFailure(call: Call<List<CocktailHeavenItem>>, t: Throwable) {
                Log.d(javaClass.name, t.message, t)
            }

        })
    }

    private fun populateItems(cocktailHeavenItems: List<CocktailHeavenItem>) {

        GlobalScope.launch {
            cocktailHeavenItems.forEach{

                val values = ContentValues().apply {
                    put(Item::id.name, it.id)
                    put(Item::name.name, it.name)
                    put(Item::brewery_type.name, it.brewery_type)
                    put(Item::city.name, it.city)
                    put(Item::state.name, it.state)
                    put(Item::postal_code.name, it.postal_code)
                    put(Item::country.name, it.country)
                    put(Item::updated_at.name, it.updated_at)
                    put(Item::created_at.name, it.created_at)
                    put(Item::read.name, false)
                }
                context.contentResolver.insert(COCKTAIL_HEAVEN_PROVIDER_URI, values)

            }

            context.setBooleanPreference(DATA_IMPORTED, true)
            context.sendBroadcast<CocktailHeavenReceiver>()
        }
    }


}
