package hr.algebra.cocktailheaven

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import hr.algebra.cocktailheaven.framework.startActivity

class CocktailHeavenReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        context.startActivity<HostActivity>()
    }
}