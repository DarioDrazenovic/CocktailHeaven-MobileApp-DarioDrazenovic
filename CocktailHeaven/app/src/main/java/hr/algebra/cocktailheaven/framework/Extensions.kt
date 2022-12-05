package hr.algebra.cocktailheaven.framework

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.getSystemService
import androidx.preference.PreferenceManager
import hr.algebra.cocktailheaven.COCKTAIL_HEAVEN_PROVIDER_URI
import hr.algebra.cocktailheaven.HostActivity
import hr.algebra.cocktailheaven.R
import hr.algebra.cocktailheaven.model.Item

fun View.startAnimation(animationId: Int)
    = startAnimation(AnimationUtils.loadAnimation(context, animationId))

//napisemo inline i reified(da se mogu koristiti generics) i zadrzat ce do runtimea i snimiti informaciju cime ju mozemo pozvati
inline fun<reified T : Activity> Context.startActivity()
    =startActivity(Intent(this, T::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
})

inline fun<reified T : Activity> Context.startActivity(key: String, value: Int)
        =startActivity(Intent(this, T::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        putExtra(key, value)
})

// reified(da se mogu koristiti generics)
inline fun<reified T: BroadcastReceiver> Context.sendBroadcast()
= sendBroadcast(Intent(this, T::class.java))

//setter
fun Context.setBooleanPreference(key: String, value: Boolean) =
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putBoolean(key, value)
                .apply()

//getter
fun Context.getBooleanProperty(key: String) =
        PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(key, false)

fun Context.isOnline() : Boolean {
        val connectivityManager = getSystemService<ConnectivityManager>()

        // u manifestu dozvolili access, ?. -> moze biti null
        // dovuci sistemski servis, ako postoji i postoji njegov active network iskoristi network da izvuces njegove capabilitie,
        // ako postoje capability onda provjeri da li smo na celular
        connectivityManager?.activeNetwork?.let { network ->
                connectivityManager.getNetworkCapabilities(network)?.let{ networkCapabilities ->
                        return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                                || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                }
        }
        return false
}

// 3 sekunde cekamo prije nego aplikacija preskoci iz 1 activity u drugi
fun callDelayed(delay: Long, function: Runnable) {
        Handler(Looper.getMainLooper()).postDelayed(
                function,
                delay
        )
}

fun Context.fetchItems() : MutableList<Item> {
        val items = mutableListOf<Item>()
        val cursor = contentResolver?.query(COCKTAIL_HEAVEN_PROVIDER_URI,
        null,
        null,
        null,
        null)
        while(cursor != null && cursor.moveToNext()) {
                items.add(Item(
                        cursor.getLong(cursor.getColumnIndexOrThrow(Item::_id.name)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Item::id.name)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Item::name.name)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Item::brewery_type.name)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Item::city.name)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Item::state.name)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Item::postal_code.name)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Item::country.name)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Item::updated_at.name)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Item::created_at.name)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(Item::read.name)) == 1
                ))
        }
        return items
}