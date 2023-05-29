package com.example.graduationproject

import android.content.ClipData.Item
import android.os.Bundle
import android.text.Layout
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.core.view.MenuItemCompat
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.graduationproject.databinding.ActivityTrenerAreaBinding
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView

class TrenerArea : AppCompatActivity() {

    private lateinit var binding: ActivityTrenerAreaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTrenerAreaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_trener_area)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.fragmentWorkoutPlanDay1, R.id.fragmentWorkoutPlanDay2, R.id.fragmentWorkoutPlanDay3
            )
        )

        val secondDay: BottomNavigationItemView = findViewById(R.id.fragmentWorkoutPlanDay2)
        secondDay.visibility = View.INVISIBLE
        val threeDay: BottomNavigationItemView = findViewById(R.id.fragmentWorkoutPlanDay3)
        threeDay.visibility = View.INVISIBLE

        binding.buttonAddDay.setOnClickListener {
            when(navView.selectedItemId) {
                R.id.fragmentWorkoutPlanDay1 -> secondDay.visibility = View.VISIBLE
                R.id.fragmentWorkoutPlanDay2 -> threeDay.visibility = View.VISIBLE
            }
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}