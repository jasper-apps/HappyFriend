package com.yterletskyi.happyfriend

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.yterletskyi.happyfriend.databinding.ActivityMainBinding
import com.yterletskyi.happyfriend.features.pin.data.PinCodeController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity @Inject constructor(pinCodeController: PinCodeController) : AppCompatActivity() {

    private val bottomTabIds = setOf(
        R.id.friendsScreen,
        R.id.settingsScreen,
    )

    private val pinCodeController_ = pinCodeController

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

        val navGraph = navController.graph

        if (pinCodeController_.pinCode.pin.equals(null)) {
            navGraph.setStartDestination(R.id.setupPinScreen)
            navController.setGraph(navGraph.id)
        } else {
            navGraph.setStartDestination(R.id.pinScreen)
            navController.setGraph(navGraph.id)
        }
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
