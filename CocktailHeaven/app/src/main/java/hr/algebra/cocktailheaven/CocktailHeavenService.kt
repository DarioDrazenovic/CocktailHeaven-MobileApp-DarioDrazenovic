package hr.algebra.cocktailheaven

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import hr.algebra.cocktailheaven.api.CocktailHeavenFetcher
import hr.algebra.cocktailheaven.framework.sendBroadcast
import hr.algebra.cocktailheaven.framework.setBooleanPreference

private const val JOB_ID = 1
class CocktailHeavenService : JobIntentService() {

    override fun onHandleWork(intent: Intent) {

        CocktailHeavenFetcher(this).fetchItems()

    }

    companion object {
        fun enqueue(context: Context, intent: Intent) {
            enqueueWork(context, CocktailHeavenService::class.java, JOB_ID, intent)
        }
    }
}