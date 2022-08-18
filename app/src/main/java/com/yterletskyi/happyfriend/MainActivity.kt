package com.yterletskyi.happyfriend

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.yterletskyi.happyfriend.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val bottomTabIds = listOf(
        R.id.contactsScreen,
        R.id.friendsScreen,
        R.id.settingsScreen
    )

    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
            .navController
    }

    private lateinit var onDestinationChangeListener: NavController.OnDestinationChangedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = inflateView()

        onDestinationChangeListener =
            NavController.OnDestinationChangedListener { _, destination, _ ->
                view.navBar.isVisible = destination.id in bottomTabIds
            }
        navController.addOnDestinationChangedListener(onDestinationChangeListener)
        view.navBar.setupWithNavController(navController)
    }

    override fun onDestroy() {
        super.onDestroy()
        navController.removeOnDestinationChangedListener(onDestinationChangeListener)
    }

    private fun inflateView(): ActivityMainBinding {
        val inflater = LayoutInflater.from(this)
        val binding = ActivityMainBinding.inflate(inflater)
        setContentView(binding.root)
        return binding
    }
}
