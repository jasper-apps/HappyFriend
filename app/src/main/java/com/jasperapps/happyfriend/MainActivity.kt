package com.jasperapps.happyfriend

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.jasperapps.happyfriend.databinding.ActivityMainBinding
import com.jasperapps.happyfriend.features.pin.data.PinCodeController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val bottomTabIds = setOf(
        R.id.friendsScreen,
        R.id.settingsScreen,
    )

    @Inject
    lateinit var pinCodeController: PinCodeController

    private lateinit var onDestinationChangeListener: NavController.OnDestinationChangedListener

    private val navController: NavController by lazy {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = inflateView()

        onDestinationChangeListener =
            NavController.OnDestinationChangedListener { _, destination, _ ->
                view.navBar.isVisible = destination.id in bottomTabIds
            }
        navController.addOnDestinationChangedListener(onDestinationChangeListener)
        view.navBar.onItemClickListener = { index ->
            val options = NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setRestoreState(true)
                .setPopUpTo(
                    navController.graph.id,
                    inclusive = true,
                    saveState = true
                )
                .build()

            when (index) {
                0 -> navController.navigate(R.id.friendsScreen, null, options)
                1 -> navController.navigate(R.id.settingsScreen, null, options)
                else -> throw IllegalArgumentException("unsupported view index: $index")
            }
        }

        // inflateGraph()
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

    private fun inflateGraph() {
        val graph = navController.navInflater.inflate(R.navigation.mobile_navigation)
        val startDestination = pinCodeController.getPinCode()
            ?.let { R.id.pinScreen }
            ?: R.id.setupPinScreen

        graph.setStartDestination(startDestination)
        navController.setGraph(graph, null)
    }
}
