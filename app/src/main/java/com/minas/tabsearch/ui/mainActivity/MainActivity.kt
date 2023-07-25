package com.minas.tabsearch.ui.mainActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.minas.tabsearch.databinding.ActivityMainBinding
import com.minas.tabsearch.ui.tabsActivity.TabsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.followingBtn.setOnClickListener {
            startActivity(TabsActivity.newInstance(this, 0))
        }
        binding.followersBtn.setOnClickListener {
            startActivity(TabsActivity.newInstance(this, 1))
        }
    }
}