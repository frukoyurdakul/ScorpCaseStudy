package com.scorp.casestudy.furkanyurdakul.ui

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.scorp.casestudy.furkanyurdakul.R
import com.scorp.casestudy.furkanyurdakul.databinding.ActivityMainBinding
import com.scorp.casestudy.furkanyurdakul.ui.base.BaseActivity
import com.scorp.casestudy.furkanyurdakul.ui.base.BaseFragment
import com.scorp.casestudy.furkanyurdakul.util.asInstance
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: BaseActivity<ActivityMainBinding>()
{
    override val layoutId = R.layout.activity_main

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    override suspend fun setupUi()
    {
        with(binding)
        {
            navHostFragment = supportFragmentManager.findFragmentById(binding.navHost.id) as NavHostFragment
            navController = navHostFragment.navController

            setSupportActionBar(toolbar)

            val appBarConfiguration = AppBarConfiguration.Builder(
                setOf(R.id.homeFragment)
            ).build()

            toolbar.setupWithNavController(navController, appBarConfiguration)

            // Set a custom click listener that can be overridden
            // from BaseFragment specifically.
            toolbar.setNavigationOnClickListener {
                // In the setupWithNavController call, the listener simply calls
                // NavigationUI.navigateUp so we can direct the navigation
                // calls to fragments first to override the behavior,
                // or let the normal behavior handle the call.

                // If the top fragment is a BaseFragment, then try
                // to override, otherwise execute default behavior.
                navHostFragment.childFragmentManager
                    .primaryNavigationFragment
                    .asInstance(BaseFragment::class)?.let {
                        if (!it.onBackPress())
                            NavigationUI.navigateUp(navController, appBarConfiguration)
                    } ?: NavigationUI.navigateUp(navController, appBarConfiguration)
            }
        }
    }
}