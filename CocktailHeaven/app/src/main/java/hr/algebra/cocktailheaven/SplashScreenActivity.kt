package hr.algebra.cocktailheaven

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import hr.algebra.cocktailheaven.databinding.ActivitySplashScreenBinding
import hr.algebra.cocktailheaven.framework.*

private const val DELAY = 3000L
const val DATA_IMPORTED = "hr.algebra.cocktailheaven.data_imported"
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startAnimations()
        redirect()

        //sakrivam navigation bar i buttone (deprecated)
        this.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    private fun startAnimations() {
        //u extensions napravljen startAnimation da skratimo kod
        binding.ivSplash.startAnimation(R.anim.scale)
        binding.tvSplash.startAnimation(R.anim.blink)
    }

    // BITNO!!!
    private fun redirect() {
        // ako su podaci importani, redirect:
        if (getBooleanProperty(DATA_IMPORTED)) {
                callDelayed(DELAY) {startActivity<HostActivity>()}
            // u suprotnom
        } else {
            if (isOnline()) {
                // 1. ako si online pokreni servis za skidanje podataka
                Intent(this, CocktailHeavenService::class.java).apply {
                    CocktailHeavenService.enqueue(
                        this@SplashScreenActivity,
                        this
                    )
                }
            } else {
                // 2. u suprotnom ispisi poruku i izađi
                binding.tvSplash.text = getString(R.string.no_internet)

                callDelayed(DELAY) {finish()}
            }
        }
    }
}